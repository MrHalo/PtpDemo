package com.gosun.demo2.ptp.listener;

import com.gosun.demo2.Utils;
import com.gosun.demo2.ptp.Camera;
import com.gosun.demo2.ptp.PtpCamera;

import java.util.ArrayList;
import java.util.List;

/**
 * create by chenjie
 *
 */

public class StorageInfoListenerImpl implements Camera.StorageInfoListener{

    private static final String TAG = StorageInfoListenerImpl.class.getSimpleName();
    public static List<Integer> storageIds = new ArrayList<>();

    private List<Integer> originHandles = new ArrayList<>();

    private List<Integer> newHandles = new ArrayList<>();


    private boolean isFirst = true;
    @Override
    public void onStorageFound(int handle, String label) {

        if (!storageIds.contains(handle)) {
            Utils.logD(TAG, "add storageId: " + handle);
            storageIds.add(handle);
        }
    }

    @Override
    public void onAllStoragesFound() {

    }

    @Override
    public void onImageHandlesRetrieved(PtpCamera camera, int[] handles) {

        if (isFirst) {
            isFirst = false;
            if (handles.length > 0) {
                for (int handle : handles) {
                    Utils.logD(TAG, "StorageInfo handle: " + handle);
                    originHandles.add(handle);
                }
            }
        } else {
            if (handles.length > 0) {
                for (int handle : handles) {

                    // 如果原有集合不包含该handle，则认为是用户新增
                    if (!originHandles.contains(handle) && !newHandles.contains(handle)) {
                        newHandles.add(handle);
                        Utils.logD(TAG, "new handle: " + handle);
                        camera.queueCopyNewImage(handle);
                    }
                }
            }
        }
    }
}
