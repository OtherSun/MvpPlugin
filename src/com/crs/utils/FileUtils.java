package com.crs.utils;

import com.crs.actions.MvpPlugin;
import com.crs.uis.MvpDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.spellchecker.FileLoader;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FileUtils {

    public static String readFile(AnAction action, String filename) {
        InputStream in = null;
        in = MvpPlugin.class.getClassLoader().getResourceAsStream(filename);
        if (in == null)
            return "";
        String content = "";
        try {
            byte[] bytes = readStream(in);
            content = new String(bytes,"UTF-8");
            System.out.println(new String(bytes,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writetoFile(String content, String filepath, String filename) {
        try {
            File floder = new File(filepath);
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                boolean mkdirs = floder.mkdirs();
                if (!mkdirs)
                    return;
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile)
                    return;
            }

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer,"UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outSteam.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outSteam.toByteArray();
    }
}
