package com.cpsc.cpsc_pgsip.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述:
 * <p>
 * Created by allens on 2018/1/29.
 */

public class SerializableUtil {

    private static String fileName = "CallSIP.txt";

    /**
     * 写入本地文件
     *
     * @param context
     * @param obj
     */
    public static void write(Context context, Object obj) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(bout);
            oout.writeObject(obj);
            oout.flush();
            oout.close();
            bout.close();
            byte[] b = bout.toByteArray();
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从本地文件读取
     *
     * @param context
     * @return
     */
    public static Object read(Context context) {
        // 拿出持久化数据
        Object obj = null;
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            obj = oin.readObject();
            in.close();
            oin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
