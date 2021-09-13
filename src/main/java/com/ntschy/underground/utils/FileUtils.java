/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: FileUtils.java
 * Author: 陈佳
 * Date: 2021/9/13 上午10:25
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.utils;

import java.io.*;

public class FileUtils {

    public static File makeFileDir(String fileDir) {
        File dir = new File(fileDir);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new RuntimeException("无法创建shape文件夹");
            }
        } else {
            if (!dir.mkdir()) {
                throw new RuntimeException("无法创建shape文件夹");
            }
        }
        return dir;
    }

    public static boolean createFile(String filePath, String fileContent) {
        boolean ok = false;
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                ok = true;
                writeFileContent(filePath, fileContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    public static boolean writeFileContent(String filePath, String newStr) throws IOException {
        boolean ok = false;
        String fileIn = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filePath);
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(fileIn);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            ok = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return ok;
    }
}
