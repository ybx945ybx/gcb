package com.gocashback.lib_common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @Author 55HAITAO
 * @Date 2019-06-13 15:39
 */
public class FileUtils {

    public static void deleteDir(String path, boolean isDeleteSelf) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath(), false); // 递规的方式删除文件夹
        }
        if (dir.isDirectory() && isDeleteSelf) {
            dir.delete();// 删除目录本身
        }


        path.startsWith("/store");
    }

    /**
     * 只删除相关的文件，不做其它操作
     *
     * @param path
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath()); // 递规的方式删除文件夹
        }
    }

    public static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }


    public static        String CATCH_PATH = Environment.getExternalStorageDirectory().getPath() + "/.gcb/image/";
    // 图片路径
    public static        String PIC_PATH   = "";
    private static final String TAG        = "FileUtil";

    public static String initPath() {
        File f = new File(CATCH_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        return CATCH_PATH;
    }

    public static void initPicPath(Context context) {
        File f = new File(context.getExternalCacheDir() + PIC_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        PIC_PATH = f.getAbsolutePath();
    }

    public static String getPicPath(Context context) {
        if (TextUtils.isEmpty(PIC_PATH)) {
            initPicPath(context);
        }
        return PIC_PATH;
    }

    public static File initDir() {
        File f = new File(CATCH_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }


    /**
     * 将bitmap保存到相册里
     *
     * @param bitmap
     */
    public static void saveBitmapToPicture(Context mContext, Bitmap bitmap) {
        File             parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Date             date       = new Date();
        SimpleDateFormat format     = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String           fileName   = format.format(date) + ".jpg";

        File file = new File(parentpath, fileName);
        file.getParentFile().mkdirs();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
    }


    public static void downloadPicAndSaveToDCIM(Context mContext, String path, String picName) {
        String parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/55海淘/";

        File pic = new File(parentpath, picName);
        if (!pic.getParentFile().exists())
            pic.getParentFile().mkdirs();

        if (pic.exists())
            pic.delete();
        // 从网络上获取图片
        URL url;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream      is     = conn.getInputStream();
                FileOutputStream fos    = new FileOutputStream(pic);
                byte[]           buffer = new byte[1024];
                int              len    = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
            }
            MediaScannerConnection.scanFile(mContext, new String[]{pic.toString()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (!"".equals(path)) {
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                boolean delete = file.delete();
//                Logger.d("删除成功 path = " + path);

            }
        }
    }

    public static String getFileName(String imageUrl) {
        return String.valueOf(imageUrl.hashCode()) + ".png";
    }


    public static String saveBitmap(Bitmap b, String fileName) {
        String path     = initPath();
        String filePath = path + fileName;
//        Logger.d("saveBitmap:jpegName = " + filePath);
        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
//            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
//            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    public static String saveBitmap(Context context, Bitmap b, String fileName) {
        if (b == null) return null;
        String path     = getPicPath(context);
        String filePath = path + fileName;
//        Logger.d("saveBitmap:jpegName = " + filePath);

        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
//            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
//            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param filePath 文件地址
     * @return 文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }


    public static final int SIZETYPE_B  = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file      = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file      = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            size = file.length();
            //			FileInputStream fis = null;
            //			fis = new FileInputStream(file);
            //			size = fis.available();
        } else {
            //file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size    = 0;
        File flist[] = f.listFiles();
        if (flist == null)
            return 0L;
        for (File aFlist : flist) {
            if (aFlist.isDirectory()) {
                size = size + getFileSizes(aFlist);
            } else {
                size = size + getFileSize(aFlist);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df             = new DecimalFormat("#.00");
        String        fileSizeString = "";
        String        wrongSize      = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df           = new DecimalFormat("#.00");
        double        fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }




    public static String saveBitmap(Bitmap b) {

        String path     = initPath();
        long   dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
//        Logger.d("saveBitmap:jpegName = " + jpegName);
        File dir1 = new File(path);
        if (!dir1.exists()) {
            dir1.mkdirs();
        }
        try {
            FileOutputStream     fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            //            FLog.d(TAG, "saveBitmapSuccess");
            return jpegName;
        } catch (IOException e) {
            //            FLog.e(TAG, "saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFileToSD(Context context, String filename, Bitmap bitmap) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/gcb";
            String filePath     = initPath();

            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }

//            filename = filePath + "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filePath + "/" + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
            //关闭输出流
//            Toast.makeText(context, "图片已成功保存到" + filePath, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "图片已成功保存至相册", Toast.LENGTH_SHORT).show();
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        filePath + "/" + filename, filename, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(filename))));
        } else
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }
}
