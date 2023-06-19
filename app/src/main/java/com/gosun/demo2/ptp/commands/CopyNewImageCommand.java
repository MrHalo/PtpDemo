package com.gosun.demo2.ptp.commands;

import android.text.TextUtils;

import com.gosun.demo2.Utils;
import com.gosun.demo2.ptp.PtpCamera;
import com.gosun.demo2.ptp.PtpConstants;
import com.gosun.demo2.ptp.model.ObjectInfo;
import com.gosun.demo2.util.FileUtils;

import java.nio.ByteBuffer;

/**
 * create by chenjie
 */
public class CopyNewImageCommand extends Command {

    public static final String TAG = CopyNewImageCommand.class.getSimpleName();
    private final PtpCamera camera;
    private final int objectHandle;

    private static boolean test = true;

    private ObjectInfo objectInfo;

    public CopyNewImageCommand(PtpCamera camera, int objectHandle) {
        super(camera);
        this.camera = camera;
        this.objectHandle = objectHandle;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        GetObjectInfoCommand getInfo = new GetObjectInfoCommand(camera, objectHandle);
        io.handleCommand(getInfo);

        if (getInfo.getResponseCode() != PtpConstants.Response.Ok) {
            return;
        }

        objectInfo = getInfo.getObjectInfo();
        if (objectInfo == null) {
            return;
        }

        Utils.logD(TAG, String.format("objectHandle: %d, objectFormat %s", objectHandle, PtpConstants.objectFormatToString(objectInfo.objectFormat)));
        Utils.logD(TAG, String.format("objectHandle: %d, objectFormat 10 %s", objectHandle, objectInfo.objectFormat));
        Utils.logD(TAG, "objectInfo: %s" + objectInfo );

        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetObject, objectHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {

        FileUtils.writeBytesToFile(b, length, objectHandle, objectInfo);
        // copy 到手机后，删除图片
        camera.onEventObjectRemoved(objectHandle);
    }
}
