package com.rocketboys100.playfuzhou;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.rocketboys100.playfuzhou.costom_layouts.Httpun.postPicture;

public class diy extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnClickListener, Camera.PictureCallback {
    private SurfaceView mSurfaceView;
    private ImageView mIvStart;
    private TextView mTvCountDown;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Handler mHandler = new Handler();
    private int mCurrentTimer = 3;
    private boolean mIsSurfaceCreated = false;
    private boolean mIsTimerRunning = false;
    private static final int CAMERA_ID = 0; //后置摄像头
    private static final String TAG = MainActivity.class.getSimpleName();
    private String mCurrentPhotoPath;
    private boolean hasExit = false;
    private boolean waitFlag = false;

    Paint paint;      //画笔
    int currentAlpha=0;  //当前的不透明值
    int sleepSpan=60;      //动画的时延ms
    Bitmap currentLogo,logos;  //当前logo图片引用
    int currentX=40;      //图片位置
    int currentY=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);
        getSupportActionBar().hide();
        if (ContextCompat.checkSelfPermission(diy.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(diy.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(diy.this, new String[]{Manifest.permission.CAMERA,}, 1);
        }
        else
        {
            initView();
            initEvent();
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        System.out.println("onBack");
        stopPreview();
        hasExit = true;
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
        stopPreview();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        //mIvStart = (ImageView) findViewById(R.id.start);
        mTvCountDown = (TextView) findViewById(R.id.count_down);
    }

    private void initEvent() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        //mIvStart.setOnClickListener(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
//        new Thread() {
//            public void run() {
//                currentLogo = logos;//当前图片的引用
//                currentLogo = BitmapFactory.decodeFile("logo.png");
//                System.out.print(currentLogo.getHeight());
//                for (int i = 255; i > -10; i = i - 10) {//动态更改图片的透明度值并不断重绘
//                    currentAlpha = i;
//                    if (currentAlpha < 0)//如果当前不透明度小于零
//                    {
//                        currentAlpha = 0;//将不透明度置为零
//                    }
//                    Canvas canvas = holderfinal.lockCanvas();//获取画布
//                    try {
//                        synchronized (holderfinal)//同步
//                        {
//                            paint.setColor(Color.BLACK);//设置画笔颜色
//                            paint.setAlpha(255);//设置不透明度为255
//                            canvas.drawRect(0, 0, 720, 1080, paint);
//                            //进行平面贴图
//                            if(currentLogo==nullshop)return;
//                            paint.setAlpha(currentAlpha);
//                            canvas.drawBitmap(currentLogo, currentX, currentY, paint);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (canvas != nullshop)//如果当前画布不为空
//                        {
//                            holderfinal.unlockCanvasAndPost(canvas);//解锁画布
//                        }
//                    }
//                    try {
//                        if (i == 255)//若是新图片，多等待一会
//                        {
//                            Thread.sleep(10);
//                        }
//                        Thread.sleep(60);
//                    } catch (Exception e)//抛出异常
//                    {
//                        e.printStackTrace();
//                    }
//                }
//                        takePic();
//            }
//        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsSurfaceCreated = false;
    }

    private void startPreview() {
        if (mCamera != null || !mIsSurfaceCreated) {
            Log.d(TAG, "startPreview will return");
            return;
        }
        mCamera = Camera.open(CAMERA_ID);
        Camera.Parameters parameters = mCamera.getParameters();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Camera.Size size = getBestPreviewSize(width, height, parameters);

        if (size != null) {
            //设置预览分辨率
            parameters.setPreviewSize(size.width, size.height);

            //设置保存图片的大小
            //parameters.setPictureSize(size.width, size.height);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        // parameters.setPreviewFrameRate(20);
        //设置相机预览方向
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        mCamera.startPreview();
        mCamera.cancelAutoFocus();
       takePic();
//        new Thread(){
//            @Override
//            public void run() {
//                while (true) {
//                    if (!mIsTimerRunning) {
//                        mIsTimerRunning = true;
//                        mHandler.post(timerRunnable);
//                    }
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
////            }}.start();

//        if (!mIsTimerRunning) {
//            mIsTimerRunning = true;
//            mHandler.post(timerRunnable);
//        }
    }

    private void takePic()
    {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);
                    while(!hasExit) {
//                        System.out.println("1");
                        Thread.sleep(500);
                        if(hasExit){
                            break;
                        }
                        if(waitFlag==true){
//                            System.out.println("2");
                            continue;
                        }else{
//                            System.out.println("3");
                            waitFlag = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"开始识别，请稍候..." , Toast.LENGTH_SHORT).show();
                                }
                            });
                            mCamera.takePicture(null, null, null, diy.this);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();
    }
    private void stopPreview() {
        //释放Camera对象
        if (mCamera != null) {
            try {
                System.out.println("stopPreview");
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            result=size;
        }
        return result;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (!mIsTimerRunning) {
                    mIsTimerRunning = true;
                    mHandler.post(timerRunnable);
                }
                break;
        }
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCurrentTimer > 0) {
                mTvCountDown.setText(mCurrentTimer + "");
                mCurrentTimer--;
                mHandler.postDelayed(timerRunnable, 1000);
            } else {
                mTvCountDown.setText("666");
                mCamera.takePicture(null, null,null, diy.this);
                mIsTimerRunning = false;
                mCurrentTimer = 3;
            }
        }
    };

    private File createImageFile() {
        File image = null;
        image = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(),generateFileName());

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String generateFileName() {
        String imageFileName ="rocket.jpg";
        return imageFileName;
    }
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCamera.startPreview();
        final byte[] datatmp = data;
        new Thread(){
            @Override
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(datatmp, 0, datatmp.length);
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath(), generateFileName());

                    Matrix matrix = new Matrix();
                    matrix.postScale(1f, 1f);
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);



                    FileOutputStream out = new FileOutputStream(file.getAbsoluteFile());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    mCamera.startPreview();
                    Thread.sleep(200);

                    mCurrentPhotoPath = file.getAbsolutePath();
                    postPicture(MainActivity.serverURL +"OkHttpTest/uploadFile.do", file, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "上传服务器失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "上传服务器成功", Toast.LENGTH_SHORT).show();
                                    downloadCalcResult();
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();
    }

    /*
        private void uploadPic()
        {
            new Thread(){
                @Override
                public void run() {
                    File flie_img = new File(mCurrentPhotoPath);
                    System.out.println(flie_img.getPath() + "+++++");
                    postPicture(MainActivity.serverURL +"OkHttpTest/uploadFile.do", flie_img, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
    //                                waitFlag = false;
                                    downloadCalcResult();
                                }
                            });
                        }
                    });
                }}.start();
        }
    */
    private List<String> scanResult;
    private void downloadCalcResult(){
        if(scanResult == null)
            scanResult = new ArrayList<String>();
        else
            scanResult.clear();
        new Thread(){
            @Override
            public void run() {
                try {
                    while (true)
                    {
                        URL url = new URL(MainActivity.serverURL + "result.txt");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setReadTimeout(20000);

                        Thread.sleep(200);
                        if(connection.getResponseCode() == 200)
                        {
                            InputStream is = connection.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String thisLine;
                            while ((thisLine = br.readLine()) != null) {
                                scanResult.add(thisLine);
                            }
                            br.close();

                            final String tmp = scanResult.get(0);

                            is.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "识别结果:" + tmp, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //--------------------------------------------------------------
                            downloadOK2();
                            //--------------------------------------------------------------
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();

    }

    private final OkHttpClient client = new OkHttpClient();
    private void downloadOK2() throws IOException {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("username", "ansen");//表单参数
        builder.addFormDataPart("password", "123456");//表单参数

        builder.setType(MultipartBody.FORM);
        MediaType mediaType = MediaType.parse("application/octet-stream");
        //MediaType Image = MediaType.parse("image/jpeg; charset=utf-8");
        byte[] bytes=getUploadFileBytes();//获取文件内容存入byte数组
        //上传文件 参数1:name 参数2:文件名称 参数3:文件byte数组
        //builder.addFormDataPart("upload_file", "ansen.txt",RequestBody.create(Image,bytes));
        builder.addFormDataPart("upload_file", "ansen.txt",RequestBody.create(mediaType,bytes));
        RequestBody requestBody = builder.build();
        Request.Builder requestBuider = new Request.Builder();
        requestBuider.url(MainActivity.serverURL + "OkHttpTest/uploadFile.do");
        requestBuider.post(requestBody);
        execute(requestBuider);
    }

    private byte[] getUploadFileBytes(){
        byte[] bytes=null;
        try {

            InputStream inputStream = getAssets().open("ansen.txt");
            Log.i("ansen","文件长度:"+inputStream.available());
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private void execute(Request.Builder builder){
        Call call = client.newCall(builder.build());
        call.enqueue(callback);//加入调度队列
    }

    private Callback callback=new Callback(){
        @Override
        public void onFailure(Call call, IOException e) {
//            Log.i("onFailure","onFailure");
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "覆盖文件出错", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            //从response从获取服务器返回的数据，转成字符串处理
//            String str = new String(response.body().bytes(),"utf-8");
//            Log.i("onResponse","onResponse:"+str);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "本次识别完成", Toast.LENGTH_SHORT).show();
                    if(scanResult.size()>2) {
                        stopPreview();
                        hasExit = true;
                        Intent intent = new Intent(getApplicationContext(), ScanResult.class);
                        intent.putExtra("scanResult", (Serializable) scanResult);
                        startActivity(intent);
                        finish();
                    }else{
                        waitFlag = false;
                    }
                    waitFlag = false;
                }
            });


        }
    };

}
