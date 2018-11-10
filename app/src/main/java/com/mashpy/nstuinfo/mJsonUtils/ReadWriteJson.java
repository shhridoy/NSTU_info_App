package com.mashpy.nstuinfo.mJsonUtils;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by whoami on 10/24/2018.
 */

public class ReadWriteJson {

    public static final String FILE = "file1.txt";
    public static final String CHECKING_FILE = "file2.txt";

    public static void saveFile (Context context, String text) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String readFile(Context context) {
        String text = "";
        try {
            FileInputStream fis = context.openFileInput(FILE);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void saveInitialJsonFile (Context context, String text) {
        try {
            FileOutputStream fos = context.openFileOutput(CHECKING_FILE, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String readInitialJsonFile(Context context) {
        String text = "";
        try {
            FileInputStream fis = context.openFileInput(CHECKING_FILE);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String readOfflineJsonFromAssets(Context context) {
        String text = "";
        try {
            InputStream is = context.getAssets().open("offline_json.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

}
