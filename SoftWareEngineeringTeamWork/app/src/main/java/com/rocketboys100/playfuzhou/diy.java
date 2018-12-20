package com.rocketboys100.playfuzhou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocketboys100.playfuzhou.R;

import java.io.File;
import java.io.FileOutputStream;

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
                while(true) {
                    mCamera.takePicture(null, null, null, diy.this);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(hasExit) {
                        break;
                    }
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
//            if (size.width <= width && size.height <= height) {
//                if (result == null) {
//                    result = size;
//                } else {
//                    int resultArea = result.width * result.height;
//                    int newArea = size.width * size.height;
//                    if (newArea > resultArea) {
//                        result = size;
//                    }
//                }
//            }
        result=size;
       }
//        System.out.println("height:" + result.height);
//        System.out.println("width:" + result.width);

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

//    HandlerThread handlerThread = new HandlerThread("takePic");
//    Handler hand = new Handler(handlerThread.getLooper()){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            mCamera.takePicture(null, null,null, diy.this);
//        }
//    };

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            //Toast.makeText(diy.this,"计时中！",Toast.LENGTH_SHORT).show();
            if (mCurrentTimer > 0) {
                mTvCountDown.setText(mCurrentTimer + "");
                mCurrentTimer--;
                mHandler.postDelayed(timerRunnable, 1000);
            } else {
                mTvCountDown.setText("666");
//                hand.sendEmptyMessage(1);
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
        String imageFileName ="tem_picture.jpg";
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
                    FileOutputStream out = new FileOutputStream(file.getAbsoluteFile());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    mCamera.startPreview();

                } catch (Exception e) {
                    System.out.print(e.toString());
                }
            }}.start();
    }
}
