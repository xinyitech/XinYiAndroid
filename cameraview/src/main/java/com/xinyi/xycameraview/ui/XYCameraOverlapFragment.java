package com.xinyi.xycameraview.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xinyi.xycameraview.CameraConfigurationManager;
import com.xinyi.xycameraview.CameraManager;
import com.xinyi.xycameraview.Constants;
import com.xinyi.xycameraview.R;
import com.xinyi.xycameraview.listener.CameraTouchListener;
import com.xinyi.xycameraview.ui.view.FocusImageView;
import com.xinyi.xycameraview.util.SNLog;

import java.io.IOException;



public class XYCameraOverlapFragment extends Fragment implements SurfaceHolder.Callback {

    private static final String TAG = "XYCameraOverlapFragment";

    private SurfaceView surfaceView;
    private FocusImageView focusImageView;

    private FrameLayout mView;

    public int currentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceHolder surfaceHolder;
    private CameraManager mCameraManager;
    private boolean hasSurface;
    private float camera_preview_rate;
    public final static int open_camera = 1010;
    private boolean isPause;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.sn_fragment_camera_overlap, null, false);
        surfaceView = (SurfaceView) mView.findViewById(R.id.surfaceView);
        focusImageView = (FocusImageView) mView.findViewById(R.id.focusImageView);
        mCameraManager = new CameraManager(getActivity());
        surfaceHolder = surfaceView.getHolder();
        hasSurface = false;
        int screen_width = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int screen_height = getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight();
        surfaceView.setOnTouchListener(new CameraTouchListener(
                mCameraManager, surfaceView,
                true, focusImageView,
                screen_width, screen_height));
    }


    public void addFaceView(View faceView, FrameLayout.LayoutParams layoutParams) {
        if (mView != null) {
            ((FrameLayout) mView).addView(faceView, 2, layoutParams);
            faceView.bringToFront();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        currentCamera = bundle.getInt("currentCamera", Camera.CameraInfo.CAMERA_FACING_BACK);
        camera_preview_rate = bundle.getFloat("rate", 0.75f);
        return mView;
    }

    public void setPreviewCallback(Camera.PreviewCallback callback) {
        if (mCameraManager != null) {
            mCameraManager.setPreviewCallback(callback);
        }
    }


    public void startCamera() {
        if (surfaceHolder != null) {
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }
    }

    private SurfaceHolder.Callback surfaceCallback;

    public void addSurfaceCallback(SurfaceHolder.Callback callback) {
        this.surfaceCallback = callback;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        Log.d("XYCameraOverlapFragment","start camera");
        startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        Log.d("XYCameraOverlapFragment","relese camera");
        if (mCameraManager != null) {
            releaseCamera();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(open_camera);
            handler = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface&&!isPause) {
            Log.i("CAMERA1","surfaceCreated");
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceCallback != null) {
            surfaceCallback.surfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("DEBUG", "surfaceDestroyed");
        hasSurface = false;
    }


    private void initCamera(final SurfaceHolder Holder) {
        releaseCamera();
        openCamera(Holder);
    }


    public Camera.Size getPreviewSize() {
        if (mCameraManager != null) {
            return mCameraManager.getPreviewSize();
        }
        return null;
    }

    public void releaseCamera() {
        Log.d("CAMERA1","RELESE CAMERA");
        mCameraManager.stopPreview();
        mCameraManager.closeDriver();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case open_camera:
                    boolean flag = !mCameraManager.isPreviewing();
                    boolean flag1 = hasSurface;
                    if (flag && flag1) {
                        mCameraManager.startPreview();
                        if (cameraInitCallback != null) {
                            cameraInitCallback.initCallback(true);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 切换摄像头
     */
    public void changeCamera(int cameraId) {
        currentCamera = cameraId;
        mCameraManager.stopPreview();
        mCameraManager.closeDriver();

        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    private void openCamera(final SurfaceHolder Holder) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("CAMERA1","OPEN CAMERA");
                    mCameraManager.openDriver(Holder, currentCamera);
                    CameraConfigurationManager configManager = mCameraManager.getConfigManager();
                    configManager.setSuitedPreviewSizeByRate(mCameraManager.getCamera(), Constants.CAMERA_PREVIEW_MIN_WIDTH, camera_preview_rate);
                    configManager.setSuitedPictureSizeByRate(mCameraManager.getCamera(), Constants.CAMERA_PICTURE_MAX_WIDTH, camera_preview_rate);
                    configManager.setCameraDisplayOrientation(mCameraManager.getCamera(), getActivity(), currentCamera);
                    handler.sendEmptyMessage(open_camera);
                } catch (IOException e) {
                    e.printStackTrace();
                    SNLog.e(getString(R.string.camera_open_fail));
                }
            }
        });
    }

    public boolean isPreviewing() {
        if (mCameraManager != null) {
            return mCameraManager.isPreviewing();
        }
        return false;
    }

    public void tackPicture(Camera.PictureCallback pictureCallback) {
        if (mCameraManager != null && pictureCallback != null) {
            mCameraManager.tackPicture(pictureCallback);
        }
    }

    public void startPreview() {
        if (mCameraManager != null) {
            mCameraManager.startPreview();
        }
    }

    private CameraInitCallback cameraInitCallback;

    public void setCameraInitCallback(CameraInitCallback cameraInitCallback) {
        this.cameraInitCallback = cameraInitCallback;
    }


    public interface CameraInitCallback {
        void initCallback(boolean state);
    }

}
