package qianfeng.extrastorageapplication;

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
 * Created by Administrator on 2016/9/7 0007.
 */
public class SDCardUtil {
    public static boolean isSDCardMount()
    {
//        StatFs statFs = new StatFs(Environment.getExternalStorageState()); // 获取SD卡根目录
//        return statFs.
        String externalStorageState = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(externalStorageState);

    }

    // 获取SD卡的根目录
    public static String getSDCardDirectory()
    {
        if(isSDCardMount())
        {
           return Environment.getExternalStorageDirectory().getAbsolutePath();

        }else
        {
            return null;
        }
    }

    // 获取SD卡的总大小
    public static  long getSDCardSize()
    {
        if(isSDCardMount())
        {
            StatFs statFs = new StatFs(getSDCardDirectory());
            long blockCountLong = 0;
            long blockSizeLong = 0;
            if(Build.VERSION.SDK_INT > 17)
          {
              blockCountLong = statFs.getBlockCountLong();
              blockSizeLong  = statFs.getBlockSizeLong();

          }else
          {
              blockCountLong = statFs.getBlockCount();
              blockSizeLong = statFs.getBlockSize();
          }
            return blockCountLong * blockSizeLong / 1024 / 1024;
        }


            return 0;

    }

    // 获取SDCard的剩余空间
    public static long getSDCardFreeSize()
    {
        if(isSDCardMount())
        {

            StatFs statFs = new StatFs(getSDCardDirectory());
            long blockCountLong = 0;
            long blockSizeLong = 0;

            if(Build.VERSION.SDK_INT > 17)
            {
                blockCountLong = statFs.getFreeBlocksLong();
                blockSizeLong = statFs.getBlockSizeLong();
            }else
            {
                blockCountLong = statFs.getFreeBlocks();
                blockSizeLong = statFs.getBlockSize();
            }

            return blockCountLong * blockSizeLong / 1024 / 1024;
        }

        return 0;

    }


    // 获取SD卡的可用空间（  8GB 不一定是 8GB， 有可能只是 6.5 GB，因为操作系统要往里面写东西）
    public static long getSDCardAvailableSize()
    {
        if(isSDCardMount())
        {
            StatFs statFs = new StatFs(getSDCardDirectory());
            long blockCountLong = 0;
            long blockSizeLong = 0;

            if(Build.VERSION.SDK_INT > 17)
            {
                blockCountLong = statFs.getAvailableBlocksLong();
                blockSizeLong = statFs.getBlockSizeLong();
            }else
            {
                blockCountLong = statFs.getAvailableBlocks();
                blockSizeLong = statFs.getBlockSize();
            }

            return blockCountLong * blockSizeLong / 1024 / 1024;
        }
        return 0;
    }

    // 往SD卡的公有目录下保存文件
    public static void save2PublicDirectory(String type,String filename, byte[] buf)
    {

        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(type); // 这是系统提供的获取type类型的根目录，一般type为Environment.DOWNLOAD等值
        File file = new File(externalStoragePublicDirectory, filename);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(buf,0,buf.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 往SD卡私有的目录下写文件
    public static void save2CustomDirectory(String filePath, String filename, byte[] buf)
    {
        File file = new File(filePath);
        if(file.exists()) // 没有自定义的目录，那就创建一个
        {
            file.mkdirs();
        }else
        {
            File file1 = new File(file, filename);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file1);
                fos.write(buf,0,buf.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally
            {
                if(fos!=null)
                {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }


    // 往SD卡的私有File目录下保存文件                        // type 是指定存储到Files还是 Cache
                                                            // type: Environment.DIRECTORY_DCIM
    public static void saveData2SDCardPrivateFiles(Context context, String type,String filename, byte[] data)
    {
        File externalFilesDir = context.getExternalFilesDir(type);
        File file = new File(externalFilesDir, filename);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(data,0,data.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 往 SDk卡的私有Cache目录下保存文件
                                                    // 注意这个Cache的getExternalCacheDir(), 是不需要type的
                            //Cache:电脑高速缓冲存储器
    public static void saveData2SDCardPrivateCache(Context context,String filename, byte[] data) // 注意这个Cache不像File一样，它是不需要type的
    {
        File externalCacheDir = context.getExternalCacheDir();
        File file = new File(externalCacheDir, filename);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


   // 保存bitmap图片到SDCard的私有Cache目录
    public static void saveBitmap2Cache(Context context, Bitmap bitmap, String filename)
    {

        File externalCacheDir = context.getExternalCacheDir();// Cache:电脑高速缓冲存储器
        File file = new File(externalCacheDir, filename); 

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            if(filename.toLowerCase().endsWith(".png")) // 如果是PNG格式的，无损压缩
            {
                // 如果第一个参数是PNG,无损压缩，那么第二个参数没有用。
                bitmap.compress(Bitmap.CompressFormat.PNG,0,fos);
            }else   // 如果不是无损压缩格式的
            {
                // 如果是有损压缩，即使你图片的质量设置为100，那么也是有损的，不可能达到png质量
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // 从SD卡中获取文件
    public static byte[] getDataFromSDCard(String filePath)
    {
        if(isSDCardMount())
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileInputStream is = null;

            try {
                File file = new File(filePath);
                if(file.exists())
                {
                    is = new FileInputStream(file);

                    int len = 0;
                    byte[] b = new byte[1024];

                    while((len = is.read(b))!=-1)
                    {
                        byteArrayOutputStream.write(b,0,len);
                        byteArrayOutputStream.flush();
                    }
                    return byteArrayOutputStream.toByteArray();

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if( is != null)
                {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        return null;
    }


    // 获取SD卡上的一张图片
    public static Bitmap getBitmapFromSDCard(String filePath)
    {

        if(isSDCardMount())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return bitmap;
        }
        return null;
    }

    //  获取SD卡公有目录的路径(获取共有目录的东西，不需要Context，获取共有目录里面的私有目录，才需要Context)
    public static String getPublicDirectory(String type)
    {
        if(isSDCardMount())
        {
             return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
        }


        return null;

    }

    // 获取SD卡私有Files目录的路径( Files ---> 需要type)
    // Files需要传Tpye是因为里面有很多种系统帮我们写好的文件夹，它们是用固定的名字的
    // Files/DCIM
    // Files/ALARMS
    // 等等很多的文件名字,type是调用系统会自动帮我们创建的文件夹，你也可以不传入Type，但是需要你自己手动判断该文件存在不存在

    public static String getPrivateFilesDir(Context context,String type)
    {

        if (isSDCardMount())
        {
            return context.getExternalFilesDir(type).getAbsolutePath();
        }
        return null;
    }


    // 获取SD卡私有Cache目录的路径
    public static String getPrivateCacheDir(Context context)
    {
        if(isSDCardMount())
        {
            return context.getExternalCacheDir().getAbsolutePath();
        }
        return null;
    }

    // 删除文件
    public static void deleteFile(String filePath)
    {
        if(isSDCardMount())
        {
            File file = new File(filePath);
            deleteFile(file);
            
        }

    }

    private static void deleteFile(File filePath)
    {
        if(filePath.isDirectory())
        {
            File[] files = filePath.listFiles();
            for (File f : files)
            {
                deleteFile(f);
            }

            filePath.delete();

        }else
        {
            filePath.delete();
        }
    }


}
