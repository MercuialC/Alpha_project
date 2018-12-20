package com.rocketboys100.playfuzhou;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.rocketboys100.playfuzhou.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.rocketboys100.playfuzhou.costom_layouts.Httpun.postPicture;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements View.OnClickListener {

    private Button btn_camera;
    private MainActivity mainActivity;
    private Button btn_his;
    private List<String> scanResult;

    public static final int TAKE_PHOTO = 0;
    public static final int TAKE_AR = 1;
    public static final int TAKE_GALLERY = 2;
    public String mCurrentPhotoPath;

    public void gallery() {
            // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 // 开启一个带有返回值的Activity，请求码为TAKE_GALLERY
                startActivityForResult(intent, TAKE_GALLERY);
          }

    private void takeCamera(int num) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {   //判断是否有相机应用
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }
        startActivityForResult(takePictureIntent, num);//跳转界面传回拍照所得数据
    }
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

    private void showPopueWindow(){
        View popView = View.inflate(getActivity(),R.layout.popue_window,null);
        Button bt_camera = popView.findViewById(R.id.btn_pop_camera);
        Button bt_AR = popView.findViewById(R.id.btn_pop_AR);
        Button btn_gallery = popView.findViewById(R.id.btn_pop_gallery);
        Button bt_cancle = popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*9/23;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //takeCamera(RESULT_CAMERA_IMAGE);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        &&ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE )!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    //拍照上传
                    takeCamera(TAKE_PHOTO);
                }

                popupWindow.dismiss();
            }
        });
        bt_AR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivityForResult(i,RESULT_LOAD_IMAGE);
//                downloadPic("QQ.png");
//                downloadPic("wechat.png");
//                downloadPic("pineapple.png");
//                downloadPic("test.jpg");
                Intent intent = new Intent(getActivity(),diy.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);
    }
    public CameraFragment() {
    }
    public static CameraFragment newInstance(MainActivity mainActivity) {
        CameraFragment fragment = new CameraFragment();
        fragment.mainActivity = mainActivity;
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {                         //返回结果为拍照上传
                Toast.makeText(getActivity(),"拍照完成",Toast.LENGTH_SHORT).show();
                //getActivity().sendBroadcast(data);
                uploadPic();

            }
            else if(requestCode == TAKE_AR)                            //返回结果为AR扫描
            {

            }
            else if(requestCode == TAKE_GALLERY)
            {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();

                    Cursor cursor = getActivity().getContentResolver().query(uri, null,null,null,null );
                    int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    mCurrentPhotoPath = cursor.getString(colum_index);


//                    ContentResolver resolver = getActivity().getContentResolver();
//                    Uri uri = data.getData();
//                    try {
//                        MediaStore.Images.Media.getBitmap(resolver,uri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    mCurrentPhotoPath = MediaStore.Images.Media.DATA;
//                    Cursor cursor = managedQuery(uri,mCurrentPhotoPath,null,null,null);

                   // mCurrentPhotoPath=uri.getPath();
                    Toast.makeText(getActivity(),"选取本地图片\n" + mCurrentPhotoPath,Toast.LENGTH_SHORT).show();
                    uploadPic();
                }
            }
        }
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private void uploadPic()
    {
        new Thread(){
            @Override
            public void run() {

                File flie_img = new File(mCurrentPhotoPath);
                postPicture(MainActivity.serverURL +"OkHttpTest/uploadFile.do", flie_img, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                downloadCalcResult();
                            }
                        });
                    }
                });
            }}.start();

    }



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

                        System.out.println("3");
                        Thread.sleep(200);
                        if(connection.getResponseCode() == 200)
                        {
                            InputStream is = connection.getInputStream();
//                            char c;
//                            String thisLine = "";
//                            while((c = (char) is.read())>=0)
//                            {
//                                thisLine += c;
//                            }
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String thisLine;
                            if ((thisLine = br.readLine()) != null) {
                                scanResult.add(thisLine);
                            }
                            br.close();

                            final String tmp = thisLine;

                            is.close();
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "下载结果完成" + tmp, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //--------------------------------------------------------------
//                            downloadOK();
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

//    private void downloadOK() {
//        final String fileName = Environment.getExternalStorageDirectory().getPath() + "tmp_picture.jpg";
////        AssetManager.open("tmp.jpg",)
//        new Thread(){
//            @Override
//            public void run() {
//
//                final File flie_img = new File(fileName);
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Toast.makeText(MainActivity.context, (int) flie_img.getTotalSpace(), Toast.LENGTH_SHORT).show();
//                        }catch(Exception err){
//                            err.printStackTrace();
//                        }
//                    }
//                });
//                postPicture(MainActivity.serverURL +"OkHttpTest/uploadFile.do", flie_img, new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.context, "fail", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.context, "本次识别完成", Toast.LENGTH_SHORT).show();
//                                //downloadCalcResult();
//                            }
//                        });
//                    }
//                });
//            }}.start();
//    }

//    private void downloadPic(final String fileName) {
//        new Thread(){
//            @Override
//            public void run() {
//                try {
////                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//                    URL url = new URL("http://47.107.236.72:8080/" + fileName);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setReadTimeout(20000);
//                    InputStream inStream = conn.getInputStream();
//                    //得到图片的二进制数据，以二进制封装得到数据，具有通用性
//                    byte[] data = readInputStream(inStream);
//                    //new一个文件对象用来保存图片，默认保存当前工程根目录
//                    File imageFile = new File(Environment.getExternalStorageDirectory().getPath(), fileName);
//                    //创建输出流
//                    FileOutputStream outStream = new FileOutputStream(imageFile);
//                    //写入数据
//                    outStream.write(data);
//                    //关闭输出流
//                    outStream.close();
//
//                    mainActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(MainActivity.context, "下载完成", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } catch (FileNotFoundException e1) {
//                    e1.printStackTrace();
//                } catch (MalformedURLException e1) {
//                    e1.printStackTrace();
//                } catch (ProtocolException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }}.start();
//    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        btn_his = view.findViewById(R.id.btn_his);
        btn_his.setOnClickListener(this);
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath(),"buf.txt");
            FileOutputStream ofs = null;
            ofs = new FileOutputStream(file);
            ofs.write(Integer.parseInt("0"));
            ofs.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_camera=getActivity().findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopueWindow();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_his:
                Intent intent = new Intent(mainActivity, HistoryPage.class);
                startActivity(intent);

                break;
        }
    }





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

            InputStream inputStream = getActivity().getAssets().open("ansen.txt");
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
            Log.i("MainActivity","onFailure");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            //从response从获取服务器返回的数据，转成字符串处理
            String str = new String(response.body().bytes(),"utf-8");
            Log.i("MainActivity","onResponse:"+str);
        }
    };



}
