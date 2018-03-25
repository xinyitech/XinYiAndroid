package com.xinyi.xycameraview;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.xinyi.xycameraview.util.CameraUtils;
import com.xinyi.xycameraview.util.SNLog;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

final public class CameraConfigurationManager {

    private static final String TAG = CameraConfigurationManager.class
            .getSimpleName();

    private static final int TEN_DESIRED_ZOOM = 27;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private final Context context;
    private CameraSizeComparator sizeComparator;

    public CameraConfigurationManager(Context context) {
        this.context = context;
        sizeComparator = new CameraSizeComparator();
    }


    /**
     * @param camera
     * @param minPreviewWidth 预览尺寸的最小宽度
     * @param rate
     */
    public void setSuitedPreviewSizeByRate(Camera camera,int minPreviewWidth,float rate) {
        if(camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        //设置合适的预览尺寸
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = CameraUtils.getInstance().getPreviewSize(supportedPreviewSizes,minPreviewWidth,rate);
        SNLog.i("w = "+previewSize.width+"  h = "+previewSize.height);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
    }

    public void setSuitedPictureSizeByRate(Camera camera,int maxPictureWidth,float rate) {
        if(camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        //设置合适的图片尺寸
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        Camera.Size pictureSize = CameraUtils.getInstance().getPictureSize(supportedPictureSizes,maxPictureWidth,rate);
        SNLog.i("pictureSize w = "+pictureSize.width+" pictureSize h = "+pictureSize.height);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        camera.setParameters(parameters);
    }

    /**
     * 设置预览方向，使得能够看到一个正确的画面
     * @param camera
     * @param activity
     * @param camera_type
     */
    public void setCameraDisplayOrientation(Camera camera, Activity activity, int camera_type) {
        if(camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        int cameraDisplayOrientation = getCameraDisplayOrientation(camera_type, activity);
        camera.setDisplayOrientation(cameraDisplayOrientation);
    }





    /**
     * 根据相机本身的旋转角度获取需要旋转的角度
     *
     * @param cameraId 0:后置摄像头
     * @return
     */
    public int getCameraDisplayOrientation(
            int cameraId) {
        return getCameraDisplayOrientation(cameraId, null);
    }

    /**
     * 根据相机本身的旋转角度获取需要旋转的角度
     *
     * @param cameraId 0:后置摄像头
     * @return
     */
    public int getCameraDisplayOrientation(
            int cameraId, Activity activity) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = getPreviewDegree(activity);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public int getPreviewDegree(Activity activity) {
        if (activity == null) {
            return 0;
        } else {// 获得手机的方向
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
    }



    /**
     * 获取最佳缩放距离
     *
     * @param stringValues
     * @param tenDesiredZoom
     * @return
     */
    private static int findBestMotZoomValue(CharSequence stringValues,
                                            int tenDesiredZoom) {
        int tenBestValue = 0;
        for (String stringValue : COMMA_PATTERN.split(stringValues)) {
            stringValue = stringValue.trim();
            double value;
            try {
                value = Double.parseDouble(stringValue);
            } catch (NumberFormatException nfe) {
                return tenDesiredZoom;
            }
            int tenValue = (int) (10.0 * value);
            if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
                    - tenBestValue)) {
                tenBestValue = tenValue;
            }
        }
        return tenBestValue;
    }

    private void setFlash(Camera.Parameters parameters) {
        if (Build.MODEL.contains("Behold II") && Build.VERSION.SDK_INT == 3) { // 3
            parameters.set("flash-value", 1);
        } else {
            parameters.set("flash-value", 2);
        }
        parameters.set("flash-mode", "off");
    }

    private void setZoom(Camera.Parameters parameters) {
        String zoomSupportedString = parameters.get("zoom-supported");
        if (zoomSupportedString != null
                && !Boolean.parseBoolean(zoomSupportedString)) {
            return;
        }

        int tenDesiredZoom = TEN_DESIRED_ZOOM;

        String maxZoomString = parameters.get("max-zoom");
        if (maxZoomString != null) {
            try {
                int tenMaxZoom = (int) (10.0 * Double
                        .parseDouble(maxZoomString));
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Bad max-zoom: " + maxZoomString);
            }
        }

        String takingPictureZoomMaxString = parameters
                .get("taking-picture-zoom-max");
        if (takingPictureZoomMaxString != null) {
            try {
                int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Bad taking-picture-zoom-max: "
                        + takingPictureZoomMaxString);
            }
        }

        String motZoomValuesString = parameters.get("mot-zoom-values");
        if (motZoomValuesString != null) {
            tenDesiredZoom = findBestMotZoomValue(motZoomValuesString,
                    tenDesiredZoom);
        }

        String motZoomStepString = parameters.get("mot-zoom-step");
        if (motZoomStepString != null) {
            try {
                double motZoomStep = Double.parseDouble(motZoomStepString
                        .trim());
                int tenZoomStep = (int) (10.0 * motZoomStep);
                if (tenZoomStep > 1) {
                    tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
                }
            } catch (NumberFormatException nfe) {
                // continue
            }
        }

        // Set zoom. This helps encourage the user to pull back.
        // Some devices like the Behold have a zoom parameter
        if (maxZoomString != null || motZoomValuesString != null) {
            parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
        }

        // Most devices, like the Hero, appear to expose this zoom parameter.
        // It takes on values like "27" which appears to mean 2.7x zoom
        if (takingPictureZoomMaxString != null) {
            parameters.set("taking-picture-zoom", tenDesiredZoom);
        }
    }

    public void setZoom(Camera camera, float scaleFactor) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            int maxZoom = parameters.getMaxZoom();
            Log.i(TAG, "setZoom: maxZoom = "+maxZoom);
            int minZoom = 0;
            int zoom = parameters.getZoom();
            if(scaleFactor > 1){
                zoom+=2;
                if(zoom > maxZoom)
                    zoom = maxZoom;
            }else{
                zoom -= 2;
                if(zoom < 2)
                    zoom = 0;
            }
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        }
    }


    public class CameraSizeComparator implements Comparator<Camera.Size> {
        //按升序排列
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
