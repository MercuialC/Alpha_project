package com.rocketboys100.playfuzhou;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.rocketboys100.playfuzhou.CameraFragment.readInputStream;

public class ScanResult extends AppCompatActivity {

    public class SendDanmu {
        private TranslateAnimation translateAnimation;
        //我用的是TextView.这个看自己喜好。这里的boolean量是我用来实现两种弹幕特效
        public SendDanmu(TextView textView, RelativeLayout relativeLayout){
            int length=relativeLayout.getBottom()-relativeLayout.getTop()-100;      //获取relativelayout的长度
            int y=relativeLayout.getTop()+(int)(Math.random()*length)+100;       //设置弹幕随机产生的y坐标
//          //天女散花型
//                translateAnimation=new TranslateAnimation(relativeLayout.getLeft(),relativeLayout.getRight(),
//                        relativeLayout.getRight(),y);
            //水平弹幕
            translateAnimation=new TranslateAnimation(relativeLayout.getRight(),-720f,
                    y,y);
            translateAnimation.setDuration(5000);
            textView.setAnimation(translateAnimation);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.start();
        }
    }

    private Button bBack3;
    private RelativeLayout root;
    private TextView tv_shopName;
    private TextView tv_introction;
    private ImageView iv_shopIcon;
    private Random random = new Random(System.currentTimeMillis());
    int i=0;
    ArrayList<String> scanResult;
    private HistoryDBOpenHelper dbOpenHelper;
    private CollectionDBOpenHelper collectionDBOpenHelper;
    private String shopname;
    private String picpath;
    private Button btn_collect;
    private boolean collected = false;
    private ListView lv_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        getSupportActionBar().hide();

        dbOpenHelper = new HistoryDBOpenHelper(getApplicationContext());
        collectionDBOpenHelper = new CollectionDBOpenHelper(getApplicationContext());

        btn_collect = findViewById(R.id.btn_collect);
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                if(collected) {
                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    collected = false;
                    btn_collect.setBackgroundResource(R.drawable.star_empty);
                    SQLiteDatabase db = collectionDBOpenHelper.getWritableDatabase();
                    db.execSQL("delete from collection where picPath=? and shopName=?",new Object[]{picpath,shopname});
                    db.close();
                } else {
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    collected = true;
                    btn_collect.setBackgroundResource(R.drawable.star_full);
                    SQLiteDatabase db = collectionDBOpenHelper.getWritableDatabase();
                    db.execSQL("insert into collection(picPath,shopName) values(?,?)",new Object[]{picpath,shopname});
                    db.close();
                }
            }
        });


        bBack3 = findViewById(R.id.btn_back3);
        bBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scanResult = new ArrayList<String>();
        Intent intent=getIntent();
        ArrayList<String> tmp = (ArrayList<String>)intent.getSerializableExtra("scanResult");
        for(int i = 4;i<tmp.size();i++) {
            scanResult.add(tmp.get(i));
        }

        tv_shopName = findViewById(R.id.tv_shopName);
        shopname = tmp.get(0);
        tv_shopName.setText(shopname);

        tv_introction = findViewById(R.id.tv_introduction);
        tv_introction.setText(tmp.get(1));

        lv_comments = findViewById(R.id.lv_comments);
        lv_comments.setAdapter(new CommentsListViewAdapter());

        iv_shopIcon = findViewById(R.id.iv_shopIcon);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.nullpic);
        iv_shopIcon.setImageBitmap(bitmap);
        File t = new File(Environment.getExternalStorageDirectory().getPath(),shopname + ".jpg");
        picpath = t.getPath();
        getLogo(tmp.get(2));

        root = findViewById(R.id.re);
        root.getBackground().setAlpha(0);
        isrun();

    }

    void getLogo(final String picUrl) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(picUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(20000);
                    InputStream inStream = conn.getInputStream();
                    byte[] data = readInputStream(inStream);
                    final File imageFile = new File(Environment.getExternalStorageDirectory().getPath(), shopname+".jpg");
                    FileOutputStream outStream = new FileOutputStream(imageFile);
                    outStream.write(data);
                    outStream.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
                            iv_shopIcon.setImageBitmap(bitmap);

                            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                            db.execSQL("insert into history(picPath,text) values(?,?)",new Object[]{imageFile.getPath(),shopname});
                            db.close();
                        }
                    });
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }}.start();
    }

    public void adddamu(boolean text) {
        try{
        int[] color = {R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark};//{R.color.red, R.color.blue, R.color.white,R.color.yellow};

        TextView tv = new TextView(this);
        tv.setTextSize(18);
        tv.setTextColor(Color.rgb(random.nextInt(256),random.nextInt(256), random.nextInt(256)));
        int index = (int)(Math.random() * scanResult.size());
        tv.setText(scanResult.get(index));

        root.addView(tv);
        new SendDanmu(tv, root);
        }catch(Exception err){
            err.printStackTrace();
        }

    }

    //使用计时器+handle持续产生弹幕。并清除界面产生的控件
    public void isrun() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                i++;
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, new Date(), 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (i == 3) {
                //清除产生的控件
                root.removeAllViews();
                i = 0;
            }
            adddamu(false);
            super.handleMessage(msg);
        }
    };


    class CommentsListViewAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return scanResult.size();
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
                view = View.inflate(ScanResult.this, R.layout.comment_item, null);
            } else {
                view = (View)convertView;
            }
            TextView tv = view.findViewById(R.id.tv_com_item);
            tv.setText(scanResult.get(position));
            return view;
        }
    }

}
