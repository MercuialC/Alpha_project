package com.example.administrator.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    protected void onPause() {
        super.onPause();
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
            parameters.setPictureSize(size.width, size.height);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
       // parameters.setPreviewFrameRate(20);
        //设置相机预览方向
        mCamera.setDisplayOrientation(90);
        //mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        mCamera.startPreview();
        mCamera.cancelAutoFocus();
        if (!mIsTimerRunning) {
            mIsTimerRunning = true;
            mHandler.post(timerRunnable);
        }
    }

    private void stopPreview() {
        //释放Camera对象
        if (mCamera != null) {
            try {
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
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
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
            Toast.makeText(diy.this,"计时中！",Toast.LENGTH_SHORT).show();
            if (mCurrentTimer > 0) {
                mTvCountDown.setText(mCurrentTimer + "");
                mCurrentTimer--;
                mHandler.postDelayed(timerRunnable, 300);
            } else {
                mTvCountDown.setText("");

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

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            File file =  new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(),generateFileName());
            FileOutputStream out = new FileOutputStream(file.getAbsoluteFile());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mCamera.startPreview();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
}
