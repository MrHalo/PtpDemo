package com.gosun.demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.mtp.MtpConstants;
import android.mtp.MtpDevice;
import android.mtp.MtpEvent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import com.remoteyourcam.usb.ptp.Camera;
import com.remoteyourcam.usb.ptp.PtpConstants;
import com.remoteyourcam.usb.ptp.PtpService;
import com.remoteyourcam.usb.ptp.model.LiveViewData;

import java.io.IOException;

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
    private static UsbManager mUsbManager;
    private PTPEventReceiver mPTPEventReceiver;

    private PtpService ptpService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ptpService = PtpService.Singleton.getInstance(this);
        ptpService.setCameraListener(this);
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // 注册PTP事件接收器
        mPTPEventReceiver = new PTPEventReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mPTPEventReceiver, filter);

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

        private boolean isPTPCamera(UsbDevice device) {
            // 检查设备是否为PTP相机设备
            // 根据设备的VID和PID等信息进行判断
            // ...
            return true;  // 假设设备是PTP相机设备
        }



        public void connectToPTPCamera(UsbDevice device) {
            if (device.getInterfaceCount() > 0) {

                int interfaceCount = device.getInterfaceCount();

                for (int i = 0; i < interfaceCount; i++) {
                    UsbInterface usbInterface = device.getInterface(i);
                    //MtpDevice --》相机
                    if (usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_STILL_IMAGE) {

                        if (mUsbManager.hasPermission(device)) {

                            readMtpDevice(device);
                            break;

                        } else {
                            Toast.makeText(MainActivity.this, "没有设备权限，请求设备权限", Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "pending start");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
                            mUsbManager.requestPermission(device, pendingIntent); //该代码执行后，系统弹出一个对话框，
                            Log.i(TAG, "pending end");

                            try {
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            readMtpDevice(device);

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "不是相机类型", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        }

        private void readMtpDevice(UsbDevice device) {
            CancellationSignal signal = new CancellationSignal();
            UsbDeviceConnection connection = mUsbManager.openDevice(device);
            MtpDevice mtpDevice = new MtpDevice(device);

            if (connection == null || !mtpDevice.open(connection)) {
                Log.i(TAG, connection == null?"connection 为null":"connection不为null.");
                Toast.makeText(MainActivity.this, "无法打开设备", Toast.LENGTH_SHORT).show();
                return;
            }



            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!signal.isCanceled()) {
                        try {
                            Log.d(TAG,"start read event");
                            MtpEvent mtpEvent = mtpDevice.readEvent(signal);
                            Log.d(TAG,"--eventCode:"+mtpEvent.getEventCode());

                            switch (mtpEvent.getEventCode()){
                                case MtpEvent.EVENT_OBJECT_ADDED:
                                    Log.i(TAG, "EVENT_OBJECT_ADDED" + mtpEvent.hashCode());
                                    // Handle the object added event
                                    break;
                                case MtpEvent.EVENT_OBJECT_REMOVED:
                                    // Handle the object removed event
                                    Log.i(TAG, "EVENT_OBJECT_REMOVED" + mtpEvent.hashCode());
                                    break;
                                case MtpEvent.EVENT_DEVICE_INFO_CHANGED:
                                    // Handle the device info changed event
                                    Log.i(TAG, "EVENT_DEVICE_INFO_CHANGED" + mtpEvent.hashCode());
                                    break;
                                // Handle other event types as needed
                            }



                        } catch (Exception e) {
                            Log.e(TAG, "some error occurs: " + e.getMessage());
                            break;
                        }
                    }
                }
            }).start();
        }


        private String getPhotoFormatFromCamera() {
            // 在这里获取照片格式
            // 使用PTP协议相关的命令和响应进行通信
            // ...
            return "JPG";  // 假设照片格式为JPG
        }

        private void handleJPGPhoto() {
            // 处理JPG格式的照片
            // ...


        }

        private void handleRAWPhoto() {
            // 处理RAW格式的照片
            // ...
        }

        private void handleJPGRAWPhoto() {
            // 处理JPG+RAW格式的照片
            // ...
        }
    }
}