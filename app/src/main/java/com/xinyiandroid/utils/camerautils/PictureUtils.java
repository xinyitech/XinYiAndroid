package com.xinyiandroid.utils.camerautils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;


public class PictureUtils {

    private PictureUtils() {
        throw new Error("不需要实例化");
    }


    /**
     * 根据相机本身的旋转角度获取需要旋转的角度
     *
     * @param activity
     * @param cameraId
     * @return
     */
    public static int getCameraDisplayOrientation(Activity activity, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = getPreviewDegree(activity);
        int result;

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360; // compensate the mirror
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;


    }


    /**
     * 判断是否支持前置摄像头，不支持换后置摄像头
     *
     * @return 前后置摄像头参数
     */
    public static int hasFrontCamera() {
        int cameraID = 0;
        boolean hasFront = PictureUtils.hasFrontFacingCamera();//是否有前置摄像头
        cameraID = hasFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        return cameraID;
    }


    /**
     * 判断是否支持前置摄像头
     *
     * @return
     */
    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static Bitmap rotateBitmap(Activity activity, Bitmap bitmap, int cameraId) {
        int cameraDisplayOrientation = PictureUtils.getCameraDisplayOrientation(activity, cameraId);
        return rotateBitmap(bitmap, cameraDisplayOrientation);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Bitmap bmp;
        //旋转图片(相机有个旋转角度)
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //对图像进行旋转
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        if (!bitmap.isRecycled() && bitmap != rotateBitmap) {
            bitmap.recycle();
            bitmap = null;
            return rotateBitmap;
        } else {
            return bitmap;
        }
    }

    public static int findFace(Bitmap sourceBitmap) {

        long startTime = System.currentTimeMillis();

        //保证 bitmap 的宽度为偶数
        if ((1 == (sourceBitmap.getWidth() % 2))) {
            sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap,
                    sourceBitmap.getWidth() + 1, sourceBitmap.getHeight(), false);
        }
        //必须保证是565格式
        if (sourceBitmap.getConfig() != Bitmap.Config.RGB_565) {
            sourceBitmap = sourceBitmap.copy(Bitmap.Config.RGB_565, true);
        }
        //设置最多检测多少个 Face
        int maxFace = 3;
        FaceDetector mFaceDetector = new FaceDetector(sourceBitmap.getWidth(),
                sourceBitmap.getHeight(), maxFace);
        FaceDetector.Face[] mFace = new FaceDetector.Face[maxFace];
        // 实际检测到的脸数
        int faceCount = mFaceDetector.findFaces(sourceBitmap, mFace);
        long endTime = System.currentTimeMillis();
        Log.i("FACE", "耗时=" + (endTime - startTime) + "人脸数=" + faceCount + "-----" + sourceBitmap.getWidth() + "-----" + sourceBitmap.getHeight());
        return faceCount;
    }


    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    public static Uri createImagePathUri(Context context, String path) {
        Uri imgUri = null;
        long time = System.currentTimeMillis();
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.Images.Media.DATA, path);

        imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return imgUri;
    }
}
