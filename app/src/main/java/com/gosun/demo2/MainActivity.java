package com.gosun.demo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import com.gosun.demo2.ptp.Camera;
import com.gosun.demo2.ptp.PtpService;
import com.gosun.demo2.ptp.model.LiveViewData;
import com.gosun.demo2.util.FileUtils;

public class MainActivity extends AppCompatActivity implements Camera.CameraListener {

    /**
     * 步骤
     * 1.mainfest中声明权限
     * 2.
     *
     *
     *
     *
     * @param savedInstanceState
     */


    private static final String TAG = "PTPEventReceiver";
    private static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private PTPEventReceiver mPTPEventReceiver;

    private PtpService ptpService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ptpService = PtpService.Singleton.getInstance(this);
        ptpService.setCameraListener(this);

        // 注册PTP事件接收器
        mPTPEventReceiver = new PTPEventReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mPTPEventReceiver, filter);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有获取权限，则向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }



    }

    @Override
    public void onCameraStarted(Camera camera) {

    }

    @Override
    public void onCameraStopped(Camera camera) {

    }

    @Override
    public void onNoCameraFound() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onPropertyChanged(int property, int value) {

    }

    @Override
    public void onPropertyStateChanged(int property, boolean enabled) {

    }

    @Override
    public void onPropertyDescChanged(int property, int[] values) {

    }

    @Override
    public void onLiveViewStarted() {

    }

    @Override
    public void onLiveViewData(LiveViewData data) {

    }

    @Override
    public void onLiveViewStopped() {

    }

    @Override
    public void onCapturedPictureReceived(int objectHandle, String filename, Bitmap thumbnail, Bitmap bitmap) {

    }

    @Override
    public void onBulbStarted() {

    }

    @Override
    public void onBulbExposureTime(int seconds) {

    }

    @Override
    public void onBulbStopped() {

    }

    @Override
    public void onFocusStarted() {

    }

    @Override
    public void onFocusEnded(boolean hasFocused) {

    }

    @Override
    public void onFocusPointsChanged() {

    }

    @Override
    public void onObjectAdded(int handle, int format) {
        Log.i(TAG, String.format("handle = %d, format = %d ", handle, format));
//        if (camera() == null) {
//            return;
//        }
//        if (format == PtpConstants.ObjectFormat.EXIF_JPEG) {
//            if (isPro && liveViewToggle.isChecked() && showCapturedPictureNever) {
//                camera().retrieveImageInfo(this, handle);
//                handler.post(liveViewRestarterRunner);
//            } else {
//                camera().retrievePicture(handle);
//            }
//        }
    }


    private class PTPEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
//                // 获取连接的USB设备
//                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//
//                // 检查设备是否为PTP相机设备
//                if (device != null && isPTPCamera(device)) {
//                    // 连接到PTP相机设备，执行相应操作
//                    connectToPTPCamera(device);
//                }


                PtpService.Singleton.getInstance(context).initialize(context, intent);

            }
        }
    }
}