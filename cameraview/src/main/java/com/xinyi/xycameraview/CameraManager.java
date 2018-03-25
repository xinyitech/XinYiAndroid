package com.xinyi.xycameraview;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CameraManager {
    private CameraConfigurationManager configManager;
    private Camera camera;
    private boolean previewing;
    private Parameters parameter;
    public static final int FRONT = 1;//前置摄像头标记
    public static final int BACK = 0;//后置摄像头标记

    public CameraManager(Context context) {
        this.configManager = new CameraConfigurationManager(context);
    }

    /**
     * 开启设备
     *
     * @param holder
     * @throws IOException
     */
    public void openDriver(SurfaceHolder holder) throws IOException {
        openDriver(holder, Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * 开启设备
     *
     * @param holder
     * @throws IOException
     */
    public void openDriver(SurfaceHolder holder, int type) throws IOException {

        if (camera == null) {
            camera = openCamera(type);
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);
        }
    }


    public CameraConfigurationManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(CameraConfigurationManager configManager) {
        this.configManager = configManager;
    }


    public boolean isPreviewing() {
        return previewing;
    }

    public void setPreviewing(boolean previewing) {
        this.previewing = previewing;
    }

    /**
     * 释放资源
     */
    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    /**
     * 开启预览
     */
    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }


    /**
     * 关闭预览
     */
    public void stopPreview() {
        if (camera != null && previewing) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            previewing = false;
        }
    }


    private Camera openCamera(int type) {
        int frontIndex = -1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backIndex = cameraIndex;
            }
        }
        try {
            if (type == Camera.CameraInfo.CAMERA_FACING_FRONT && frontIndex != -1) {
                return Camera.open(frontIndex);
            } else if (type == Camera.CameraInfo.CAMERA_FACING_BACK && backIndex != -1) {
                return Camera.open(backIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 请求聚焦
     */
    public void requestFocus() {
        if (camera != null && previewing) {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                    }
                }
            });
        }
    }


    /**
     * 开闪光灯
     */
    public void openLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        }
    }

    /**
     * 光闪光灯
     */
    public void offLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
        }
    }

    public void tackPicture(Camera.PictureCallback callback) {
        if (camera != null) {
            previewing = false;
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {

                }
            }, null, callback);
        }
    }


    public void setPreviewCallback(Camera.PreviewCallback callback) {
        if (camera != null) {
            camera.setPreviewCallback(callback);
        }
    }

    public void setUseOneShotPreviewCallback(Camera.PreviewCallback callback) {
        if (camera != null) {
            camera.setOneShotPreviewCallback(callback);
        }
    }

    public Camera.Size getPreviewSize() {
        Camera.Size previewSize = null;
        if (camera != null) {
            Parameters parameters = camera.getParameters();
            previewSize = parameters.getPreviewSize();
        }
        return previewSize;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public boolean isZoomSupported() {
        return camera == null ? false : camera.getParameters().isZoomSupported();
    }

    public void setZoom(float scaleFactor) {
        configManager.setZoom(camera, scaleFactor);
    }

    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    public boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
        if (camera == null) {
            return false;
        }
        Parameters parameters = null;
        try {
            parameters = camera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if (Build.VERSION.SDK_INT >= 14) {
            if (parameters.getMaxNumFocusAreas() <= 0) {
                return focus(callback);
            }
            //定点对焦
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            int left = point.x - 300;
            int top = point.y - 300;
            int right = point.x + 300;
            int bottom = point.y + 300;
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
            parameters.setFocusAreas(areas);
            try {
                //本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
                //目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
                camera.setParameters(parameters);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }


        return focus(callback);
    }

    private boolean focus(Camera.AutoFocusCallback callback) {
        try {
            camera.autoFocus(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
