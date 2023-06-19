package com.gosun.demo2.util;

import static com.gosun.demo2.ptp.PtpConstants.ObjectFormat.EXIF_JPEG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.gosun.demo2.MyApplication;
import com.gosun.demo2.Utils;
import com.gosun.demo2.ptp.model.ObjectInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    private static String externalStoragePath = MyApplication.getContext().getExternalFilesDir(null).getPath() ;
    /**
     * copy picture to phone
     * 使用注意，创建文件时，需保证文件父文件夹已创建成功
     * @param b 字节缓冲区
     * @param length
     * @param objectHandle
     * @param objectInfo
     */
    public static void writeBytesToFile(ByteBuffer b, int length, int objectHandle, ObjectInfo objectInfo){


        b.position(0);
        FileOutputStream outputStream = null;
        String file_name = "";

        try {
            if (objectInfo.objectFormat == EXIF_JPEG) {
                createDir(externalStoragePath + "/JPG/");
                file_name = externalStoragePath + "/JPG/" + objectHandle + ".jpg";
            } else {
                String filename = objectInfo.filename;

                String name_suffix ="";
                if (!TextUtils.isEmpty(objectInfo.filename)) {
                    String[] split = filename.split("\\.");
                    name_suffix = split[split.length -1];
                }

                createDir(externalStoragePath + "/RAW/");
                file_name = externalStoragePath + "/RAW/" + objectHandle + "." + name_suffix;
            }

            File file = new File(file_name);
            if (!file.exists()) {
                boolean success = file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            outputStream.write(b.array(), 12, length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDir(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static boolean rawTOJpg(String path){
        boolean isSuccess = false;
        InputStream rawInputStream = null;
        FileOutputStream outputStream = null;
        try {
            rawInputStream = new FileInputStream(path);
            Bitmap rawBitmap = BitmapFactory.decodeStream(rawInputStream);
            File outputFile = new File(externalStoragePath, "output.jpg");
            outputStream = new FileOutputStream(outputFile);
            isSuccess = rawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
            Utils.logE(TAG, "raw to Jpg error: " + e.getMessage());
        } finally {

            try {
                if (rawInputStream != null) {
                    rawInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }
}
