package com.xinyiandroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyi.xycameraview.CameraManager;
import com.xinyi.xycameraview.ui.XYCameraOverlapFragment;
import com.xinyiandroid.utils.camerautils.CompressHelper;
import com.xinyiandroid.utils.camerautils.FileUtil;
import com.xinyiandroid.utils.camerautils.PictureUtils;
import com.xinyiandroid.utils.camerautils.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import xinyi.com.xinyiandroid.R;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener,
        Camera.PictureCallback {
    private static final int GET_PHOTO = 101;

    TextView tv_select_photo;
    RelativeLayout rl_change;
    ImageView iv_selected, iv_delete;
    ImageView iv_photo, iv_result, iv_take_photo;

    private XYCameraOverlapFragment camera_fragment;
    public int currentCamera = CameraManager.FRONT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        initView();
    }

    private void initView() {
        iv_photo = findViewById(R.id.iv_take_photo);
        tv_select_photo = findViewById(R.id.tv_select_photo);
        rl_change = findViewById(R.id.rl_change_camera);
        iv_result = findViewById(R.id.iv_resultImage);
        iv_selected = findViewById(R.id.iv_yes_photo);
        iv_delete = findViewById(R.id.iv_no_photo);
        iv_take_photo = findViewById(R.id.iv_take_photo);

        iv_photo.setOnClickListener(this);
        tv_select_photo.setOnClickListener(this);
        rl_change.setOnClickListener(this);
        iv_selected.setOnClickListener(this);
        iv_delete.setOnClickListener(this);

        //初始化自定义camera
        camera_fragment = new XYCameraOverlapFragment();
        float rate = ScreenUtils.getScreenWidth(this) * 1.0f / ScreenUtils.getScreenHeight(this);
        currentCamera = PictureUtils.hasFrontCamera();
        Bundle bundle = new Bundle();
        bundle.putInt("currentCamera", currentCamera);
        bundle.putFloat("rate", rate);
        camera_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_camera_Layout, camera_fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera_fragment.releaseCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_photo:
                //选择照片
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, GET_PHOTO);
                break;
            case R.id.iv_take_photo:
                //拍照
                if (camera_fragment.isPreviewing()) {
                    camera_fragment.tackPicture(this);
                }
                break;
            case R.id.rl_change_camera:
                //摄像头切换
                changeCamera();
                break;
            case R.id.iv_no_photo:
                hideOrShowView(false);
                camera_fragment.startPreview();
                break;
            case R.id.iv_yes_photo:
                // 给据业务做相应处理-------

                finish();
                break;

        }
    }

    //自定义摄像头拍照回调
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap rotateBitmap = PictureUtils.rotateBitmap(this, bitmap, currentCamera);
        //未压缩的---------------------------
        hideOrShowView(true);
        iv_result.setImageBitmap(rotateBitmap);
        checkedFace(rotateBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();
            if (selectedImage != null) {
                try {
                    File oldFile = FileUtil.getTempFile(this, selectedImage);
                    String oldSize = String.format("Size : %s", getReadableFileSize(oldFile.length()));

                    File newFile = CompressHelper.getDefault(getApplicationContext()).compressToFile(oldFile);
                    Bitmap rotate = BitmapFactory.decodeFile(newFile.getAbsolutePath());

                    String newSize = String.format("Size : %s", getReadableFileSize(newFile.length()));
                    Toast.makeText(this, new StringBuilder()
                                    .append("压缩前：").append(oldSize)
                                    .append("    压缩后：").append(newSize).toString(),
                                    Toast.LENGTH_LONG).show();

                    //自定义压缩属性
//                    File newFile = new CompressHelper.Builder(this)
//                            .setMaxWidth(720)  // 默认最大宽度为720
//                            .setMaxHeight(960) // 默认最大高度为960
//                            .setQuality(80)    // 默认压缩质量为80
//                            .setFileName(yourFileName) // 设置你需要修改的文件名
//                            .setCompressFormat(CompressFormat.JPEG) // 设置默认压缩为jpg格式
//                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                            .build()
//                            .compressToFile(oldFile);

                    hideOrShowView(true);
                    iv_result.setImageBitmap(rotate);
                    checkedFace(rotate);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "选择照片发生了错误或选择了多张照片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //用系统自带人脸检测
    private void checkedFace(Bitmap bitmap) {
        int faceCount = PictureUtils.findFace(bitmap);
        if (faceCount == 0) {
            Toast.makeText(this, "未检测到人脸,请选择正确的人脸照片", Toast.LENGTH_SHORT).show();
            return;
        } else if (faceCount > 1) {
            Toast.makeText(this, "检测到多个人脸,请选择单个人脸照片", Toast.LENGTH_SHORT).show();
            return;
        } else if (faceCount == 1) {
            //给据业务做相应处理-------
            Toast.makeText(this, "检测到一个人脸", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 切换摄像头
     */
    private void changeCamera() {
        if (currentCamera == CameraManager.BACK)
            currentCamera = CameraManager.FRONT;
        else
            currentCamera = CameraManager.BACK;
        camera_fragment.changeCamera(currentCamera);
    }

    private void hideOrShowView(boolean isHide) {
        tv_select_photo.setVisibility(isHide ? View.GONE : View.VISIBLE);
        rl_change.setVisibility(isHide ? View.GONE : View.VISIBLE);
        iv_take_photo.setVisibility(isHide ? View.INVISIBLE : View.VISIBLE);
        iv_result.setVisibility(isHide ? View.INVISIBLE : View.VISIBLE);

        iv_delete.setVisibility(isHide ? View.VISIBLE : View.GONE);
        iv_selected.setVisibility(isHide ? View.VISIBLE : View.GONE);
        iv_result.setVisibility(isHide ? View.VISIBLE : View.GONE);
    }

    //计算资源文件占用大小--------可删----
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
