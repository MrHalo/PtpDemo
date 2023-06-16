package com.remoteyourcam.usb.ptp.commands;

import android.util.Log;

import com.remoteyourcam.usb.AppConfig;
import com.remoteyourcam.usb.ptp.PtpCamera;
import com.remoteyourcam.usb.ptp.PtpConstants;

import java.nio.ByteBuffer;

/**
 * send removeObject command to camera.
 *
 */
public class RemoveObjectCommand extends Command{

    private final int outObjectHandle;
    public RemoveObjectCommand(PtpCamera camera, int objectHandle) {
        super(camera);
        this.outObjectHandle = objectHandle;
    }

    @Override
    public void exec(PtpCamera.IO io) {
        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {

        encodeCommand(b, PtpConstants.Operation.DeleteObject, outObjectHandle);
    }
}
