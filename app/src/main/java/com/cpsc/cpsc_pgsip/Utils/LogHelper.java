package com.cpsc.cpsc_pgsip.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stf on 2018-07-17.
 */

public class LogHelper {
    public static final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "/SIPLog/";

    public static Boolean write(String tag, String context) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        try {
            Date date = new Date();
            String pathName = LOG_PATH + new SimpleDateFormat("yy_MM_dd", Locale.CHINA).format(date) + "/";
            String fileName = tag + "_log.txt";
            context =  context + "\r\n";
//            context = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date) + ":" + context
//                    + "\r\n-------------------------\r\n";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    return false;
                }
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return false;
                }
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(context.getBytes("GBK"));
//            raf.write(context.getBytes("UTF-8"));
            raf.close();
            return true;
        } catch (Exception e) {
            Log.d("log", e.getMessage());
            return false;
        }

    }


    public static Boolean writeLog(String tag, String context) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        try {
            Date date = new Date();
            String pathName = LOG_PATH + new SimpleDateFormat("yy_MM_dd", Locale.CHINA).format(date) + "/";
            String fileName = tag + "_log.txt";
//            context =  context + "\r\n";
            context = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date) + ":" + context
                    + "\r\n-------------------------\r\n";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    return false;
                }
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return false;
                }
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
//            raf.write(context.getBytes("GBK"));
            raf.write(context.getBytes("UTF-8"));
            raf.close();
            return true;
        } catch (Exception e) {
            Log.d("log", e.getMessage());
            return false;
        }

    }


    public void delete(int day) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            File path = new File(LOG_PATH);
            if (path.exists()) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_YEAR, -day);
                int remain = Integer.parseInt(new SimpleDateFormat("yy_MM_dd", Locale.CHINA).format(c.getTime()));
                File[] files = path.listFiles();
                for (File dir : files) {
                    if (dir.isDirectory()) {
                        int date = Integer.parseInt(dir.getName().replace("_", ""));
                        if (date <= remain) {
                            for (File f : dir.listFiles()) {
                                f.delete();
                            }
                            dir.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("log", e.getMessage());
        }
    }
}
