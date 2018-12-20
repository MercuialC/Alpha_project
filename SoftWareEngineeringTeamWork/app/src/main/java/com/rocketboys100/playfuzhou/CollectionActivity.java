package com.rocketboys100.playfuzhou;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back2;
    private Button btn_clear2;
    private ListView lv_collection;
    private List<CollectionItem> lists;
    private CollectionDBOpenHelper dbOpenHelper;

    private void initUI(){
        btn_back2 = findViewById(R.id.btn_back2);
        btn_clear2 = findViewById(R.id.btn_clear2);
        lv_collection = findViewById(R.id.lv_collection);

        btn_back2.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);

        lists = new ArrayList<CollectionItem>();
        lv_collection.setAdapter(new CollectionListViewAdapter());
        dbOpenHelper = new CollectionDBOpenHelper(getApplicationContext());
        dbOpenHelper.getReadableDatabase();
        readDataFromDB();

//        addItemToDB("111","111");
//        lists.add(new CollectionItem("111", "111"));
//        addItemToDB("111","111");
//        lists.add(new CollectionItem("111", "111"));
//        addItemToDB("111","111");
//        lists.add(new CollectionItem("111", "111"));
    }

    private void readDataFromDB() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select picPath,shopName from collection", null);
        if(cursor!=null && cursor.getCount()>0) {
            while(cursor.moveToNext()){
                String picPath = cursor.getString(0);
                String shopName = cursor.getString(1);
                lists.add(new CollectionItem(shopName, picPath));
            }
        }
        db.close();
    }

    private void clearDB()
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from collection");
        db.close();
    }

    private void addItemToDB(String shopName, String picPath){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("insert into collection(picPath,shopName) values(?,?)",new Object[]{picPath,shopName});
        db.close();
    }

    private void deleteItemFromDB(String shopName, String picPath) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from collection where picPath=? and shopName=?",new Object[]{picPath,shopName});
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        getSupportActionBar().hide();

        initUI();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back2:
                finish();
                break;
            case R.id.btn_clear2:
                clearDB();
                lists.clear();
                lv_collection.setAdapter(new CollectionListViewAdapter());
                break;
        }
    }

    class CollectionListViewAdapter extends BaseAdapter {

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
            tv.setText(lists.get(position).getShopName());

            String picPath = lists.get(position).getPicUrl();
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
