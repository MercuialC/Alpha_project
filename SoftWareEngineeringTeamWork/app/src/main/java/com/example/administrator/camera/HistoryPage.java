package com.example.administrator.camera;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back;
    private Button btn_clear;
    private List<History> lists;
    private ListView lv_his;
    private HistoryDBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        lists = new ArrayList<History>();

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        lv_his = findViewById(R.id.lv_his);
        lv_his.setAdapter(new HistoryListViewAdapter());

        dbOpenHelper = new HistoryDBOpenHelper(getApplicationContext());
        dbOpenHelper.getReadableDatabase();
        readDataFromDB();
    }

    private void readDataFromDB(){
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select picPath,text from history", null);
        if(cursor!=null && cursor.getCount()>0) {
            while(cursor.moveToNext()){
                String picPath = cursor.getString(0);
                String text = cursor.getString(1);
                lists.add(new History(picPath, text));
            }
        }
        db.close();
    }

    private void clearDB()
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from history");
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clear:
                Toast.makeText(this, "清空历史", Toast.LENGTH_SHORT).show();
                clearDB();
                lists.clear();
                lv_his.setAdapter(new HistoryListViewAdapter());
                break;
        }
    }

    class HistoryListViewAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.history_item, null);
            } else {
                view = (View)convertView;
            }

            TextView tv = view.findViewById(R.id.tv_his);
            tv.setText(lists.get(position).getText());

            String picPath = lists.get(position).getPicPath();
 //           File file = new File(picPath);
//            用于测试
            File file = new File(Environment.getExternalStorageDirectory().getPath(),"bee.png");
            if(file.exists()) {
                ImageView iv = view.findViewById(R.id.iv_his);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                iv.setImageBitmap(bitmap);
            }

            return view;
        }
    }

}
