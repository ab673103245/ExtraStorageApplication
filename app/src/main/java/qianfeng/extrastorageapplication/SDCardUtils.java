package org.lenve.a3_3externalstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 王松 on 2016/9/7.
 */
public class SDCardUtils {
    /**
     * SD卡是否挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        //获取SD卡的状态
//        Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getSDCardRootDirectory() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SD卡总大小，返回MB
     *
     * @return
     */
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            //参数表示即将获取大小的路径
            StatFs statFs = new StatFs(getSDCardRootDirectory());
            long blockCountLong = 0;
            long blockSizeLong = 0;
            //如果Android版本号大于17，使用新的API，否则使用旧的API
            if (Build.VERSION.SDK_INT > 17) {
                blockCountLong = statFs.getBlockCountLong();
                blockSizeLong = statFs.getBlockSizeLong();

            } else {
                blockCountLong = statFs.getBlockCount();
                blockSizeLong = statFs.getBlockSize();
            }
            return blockSizeLong * blockCountLong / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 返回SD卡的剩余空间，返回MB
     *
     * @return
     */
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            StatFs statFs = new StatFs(getSDCardRootDirectory());
            long count = 0;
            long size = 0;
            if (Build.VERSION.SDK_INT > 17) {
                count = statFs.getFreeBlocksLong();
                size = statFs.getBlockSizeLong();
            } else {
                count = statFs.getFreeBlocks();
                size = statFs.getBlockSize();
            }
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡的可用空间大小，返回MB
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            StatFs statFs = new StatFs(getSDCardRootDirectory());
            long count = 0;
            long size = 0;
            if (Build.VERSION.SDK_INT > 17) {
                count = statFs.getAvailableBlocksLong();
                size = statFs.getBlockSizeLong();
            } else {
                count = statFs.getAvailableBlocks();
                size = statFs.getBlockSize();
            }
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 往SD卡的公有目录下保存文件
     *
     * @param type     公有目录名称
     * @param fileName 文件名
     * @param buf      文件
     */
    public static void saveData2PublicDirectory(String type, String fileName, byte[] buf) {
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            File file1 = new File(file, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file1);
                fos.write(buf, 0, buf.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 往SD卡的自定义目录下保存文件
     *
     * @param filePath 自定义目录名称
     * @param fileName 文件名
     * @param buf      文件
     */
    public static void saveData2CustomDirectory(String filePath, String fileName, byte[] buf) {
        if (isSDCardMounted()) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filePath);
            //如果文件夹不存在则创建新的文件夹
            if (!file.exists()) {
                file.mkdirs();//递归创建新目录
            }
            File file1 = new File(file, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file1);
                fos.write(buf, 0, buf.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 往SD卡的私有Files目录下保存文件
     *
     * @param context  当前应用的上下文
     * @param type     Files中的目录
     * @param fileName 文件名
     * @param data     数据
     */
    public static void saveData2SDCardPrivateFiles(Context context, String type, String fileName, byte[] data) {
        if (isSDCardMounted()) {
            File externalFilesDir = context.getExternalFilesDir(type);
            File file1 = new File(externalFilesDir, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file1);
                fos.write(data, 0, data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 往SD卡的私有Cache目录下保存文件
     *
     * @param context  当前应用的上下文
     * @param fileName 文件名
     * @param data     数据
     */
    public static void saveData2SDCardPrivateCache(Context context, String fileName, byte[] data) {
        if (isSDCardMounted()) {
            File externalCacheDir = context.getExternalCacheDir();
            File file1 = new File(externalCacheDir, fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file1);
                fos.write(data, 0, data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 保存bitmap图片到SDCard的私有Cache目录
     *
     * @param context  上下文
     * @param bitmap   要保存的Bitmap对象
     * @param fileName Bitmap文件名
     */
    public static void saveBitmap2Cache(Context context, Bitmap bitmap, String fileName) {
        if (isSDCardMounted()) {
            File externalCacheDir = context.getExternalCacheDir();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(externalCacheDir, fileName));
                if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
                    //1.图片的格式
                    //2.表示图片质量，取值为0～100，值越大，图片质量越高，但是PNG为无损压缩，如果第一个参数为PNG，则第二个参数无效
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 从SD卡获取文件
     * @param filePath  文件路径
     * @return
     */
    public static byte[] getDataFromSDCard(String filePath) {
        if (isSDCardMounted()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream is = null;
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    return null;
                }
                is = new FileInputStream(file);
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                return baos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取SD卡上的一张图片
     * @param filePath  图片地址
     * @return
     */
    public static Bitmap getBitmapFromSDCard(String filePath) {
        if (isSDCardMounted()) {
//            byte[] dataFromSDCard = getDataFromSDCard(filePath);
//            Bitmap bitmap1 = BitmapFactory.decodeByteArray(dataFromSDCard, 0, dataFromSDCard.length);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return bitmap;
        }
        return null;
    }

    /**
     * 获取SD卡公有目录的路径
     * @param type
     * @return
     */
    public static String getPublicDirectory(String type) {
        if (isSDCardMounted()) {
            return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
        }
        return null;
    }


    /**
     * 获取SD卡私有Files目录的路径
     * @param context
     * @param type
     * @return
     */
    public static String getPrivateFilesDir(Context context, String type) {
        if (isSDCardMounted()) {
            return context.getExternalFilesDir(type).getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SD卡私有Cache目录的路径
     * @param context
     * @return
     */
    public static String getPrivateCacheDir(Context context) {
        if (isSDCardMounted()) {
            return context.getExternalCacheDir().getAbsolutePath();
        }
        return null;
    }

    /**
     * 删除文件
     * @param filePath 要删除文件的路径
     */
    public static void deleteFile(String filePath) {
        if (isSDCardMounted()) {
            File file = new File(filePath);
            deleteFile(file);
        }
    }

    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    deleteFile(file1);
                }else{
                    file1.delete();
                }
            }
            file.delete();
        }else{
            file.delete();
        }
    }
}
