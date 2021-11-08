package com.wuwl.mynote.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardUtil {
    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;//File.separator相当于'/'
    public static String APP_NAME = "noteTest";


    /**
     * 获得文章图片保存路径
     */
    public static String getPictureDir() {
        String imageCacheUrl = SDCardRoot + APP_NAME + File.separator;
        File file = new File(imageCacheUrl);
        if (!file.exists()) {
            file.mkdir();  //如果不存在则创建
        }
        return imageCacheUrl;
    }

    public static String saveMyBitmap(Bitmap bmp, String bitName) throws IOException {
        File dirFile = new File(getPictureDir());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(getPictureDir() + bitName + ".png");
//        boolean newFile = file.createNewFile();
//        System.out.println(newFile);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);//图像压缩成PNG，高质量
            fOut.flush();//清空缓冲区，使得数据完全输出
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                fOut.close();//关闭读写流
            }
        }
        return file.getAbsolutePath();
    }

}
