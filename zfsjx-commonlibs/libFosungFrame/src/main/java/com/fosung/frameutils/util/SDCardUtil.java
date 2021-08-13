/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 下午4:41
 **********************************************************/

package com.fosung.frameutils.util;

import android.os.Environment;
import android.os.StatFs;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * SD卡相关的工具类
 */
public class SDCardUtil {
    private SDCardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState()
                          .equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Utils.getApp().getExternalFilesDir(null)
                          .getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocksLong();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory()
                                  .getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
        return stat.getBlockSizeLong() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory()
                          .getAbsolutePath() + File.separator;
    }
}