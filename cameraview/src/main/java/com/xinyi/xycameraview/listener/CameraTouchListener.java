package com.xinyi.xycameraview.listener;

import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.xinyi.xycameraview.CameraManager;
import com.xinyi.xycameraview.ui.view.FocusImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 自定义拍照手势缩放监听以及单击聚焦
 */

public class CameraTouchListener implements View.OnTouchListener {

    public static final String TAG = "CameraTouchListener";
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private FocusImageView mFocusImageView;
    private boolean isMain;
    private float oldDist = 1.0f;
    private boolean isClick = true;
    private static final int MODE_INIT = 0;
    private int mode = MODE_INIT;
    private int screen_width, screen_height;
    private int touchParam = 400;

    /**
     * @param mCameraManager
     * @param surfaceView     手势缩放控件
     * @param isMain          注：为解决不同界面事件存在的冲突问题，true表示处理当前事件
     * @param mFocusImageView 自定义聚焦view
     * @param screen_width    当前设备宽度
     * @param screen_height   当前设备高度
     */
    public CameraTouchListener(CameraManager mCameraManager, SurfaceView surfaceView, boolean isMain,
                               FocusImageView mFocusImageView, int screen_width, int screen_height) {
        this.mCameraManager = mCameraManager;
        this.mSurfaceView = surfaceView;
        this.isMain = isMain;
        this.mFocusImageView = mFocusImageView;
        this.screen_width = screen_width;
        this.screen_height = screen_height;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getPointerCount() == 2) {
            isClick = false;
            //当为两个手指时，通知所有父控件不要拦截该事件，自行处理
            mSurfaceView.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist) {
                        handleZoom(true, mCameraManager.getCamera());
                    } else if (newDist < oldDist) {
                        handleZoom(false, mCameraManager.getCamera());
                    }
                    oldDist = newDist;
                    return false;
            }
        } else if (event.getPointerCount() == 1) {
            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                if (isClick) {
                    mode = MODE_INIT;
                    Point point = new Point((int) event.getX(), (int) event.getY());
                    Log.i(TAG, "x = " + point.x + " , y = " + point.y + "");
                    if (point.y < screen_height / 2 + touchParam + 100 && point.y > screen_height / 2 - touchParam - 100 &&
                            point.x < screen_width / 2 + touchParam && point.x > screen_width / 2 - touchParam) {
                        onFocus(point, autoFocusCallback);
                        mFocusImageView.startFocus(point);
                    } else {
                        return false;
                    }
                } else {
                    isClick = true;
                }
            }
        }
        return isMain;
    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom = zoom + 1 > maxZoom ? maxZoom : zoom + 1;

            } else if (zoom > 0) {
                zoom = zoom - 1 < 0 ? 0 : zoom - 1;
            }
            if(maxZoom != zoom){
                params.setZoom(zoom);
                camera.setParameters(params);
            }
        } else {
            Log.i(TAG, "不支持缩放");
        }
    }

    protected void onFocus(Point point, Camera.AutoFocusCallback callback) {
        Camera mCamera = mCameraManager.getCamera();
        if (mCamera == null) {
            return;
        }
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters != null) {
                if (parameters.getMaxNumFocusAreas() <= 0) {
                    mCamera.autoFocus(callback);
                    return;
                }
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
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

                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);


            }
            mCamera.autoFocus(callback);
        } catch (Exception e) {
            Log.e(TAG, "点击聚焦错误:" + e.getMessage());
        }
    }

    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                mFocusImageView.onFocusSuccess();
            } else {
                mFocusImageView.onFocusFailed();
            }
        }
    };
}
