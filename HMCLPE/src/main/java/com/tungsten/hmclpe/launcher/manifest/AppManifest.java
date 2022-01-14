package com.tungsten.hmclpe.launcher.manifest;

import android.content.Context;
import android.os.Environment;

import com.tungsten.hmclpe.utils.file.FileUtils;

public class AppManifest {

    public static String LAUNCHER_DIR = Environment.getExternalStorageDirectory() + "/HMCLPE";
    public static String DEFAULT_GAME_DIR = LAUNCHER_DIR + "/.minecraft";

    public static String INNER_DIR;
    public static String INNER_FILE_DIR;

    public static String EXTERNAL_DIR;

    public static String ACCOUNT_DIR;
    public static String GAME_FILE_DIRECTORY_DIR;
    public static String SETTING_DIR;
    public static String DEBUG_DIR;
    public static String INNER_GAME_DIR;

    public static String DEFAULT_CACHE_DIR;
    public static String DEFAULT_RUNTIME_DIR;

    public static String BOAT_JAVA_DIR;
    public static String BOAT_LIB_DIR;
    public static String POJAV_JAVA_DIR;
    public static String POJAV_LIB_DIR;

    public static void initializeManifest (Context context){
        INNER_DIR = context.getFilesDir().getParent();
        INNER_FILE_DIR = context.getFilesDir().getAbsolutePath();

        EXTERNAL_DIR = context.getExternalCacheDir().getParent();

        ACCOUNT_DIR = INNER_FILE_DIR + "/accounts";
        GAME_FILE_DIRECTORY_DIR = INNER_FILE_DIR + "/paths";
        SETTING_DIR = INNER_FILE_DIR + "/settings";
        DEBUG_DIR = context.getExternalFilesDir("debug").getAbsolutePath();
        INNER_GAME_DIR = context.getExternalFilesDir(".minecraft").getAbsolutePath();

        DEFAULT_CACHE_DIR = context.getCacheDir().getAbsolutePath();
        DEFAULT_RUNTIME_DIR = context.getDir("runtime",0).getAbsolutePath();

        BOAT_JAVA_DIR = DEFAULT_RUNTIME_DIR + "/boat/java";
        BOAT_LIB_DIR = DEFAULT_RUNTIME_DIR + "/boat/lib";
        POJAV_JAVA_DIR = DEFAULT_RUNTIME_DIR + "/pojav/java";
        POJAV_LIB_DIR = DEFAULT_RUNTIME_DIR + "/pojav/lib";

        FileUtils.createDirectory(LAUNCHER_DIR);
        FileUtils.createDirectory(DEFAULT_GAME_DIR);

        FileUtils.createDirectory(INNER_DIR);
        FileUtils.createDirectory(INNER_FILE_DIR);

        FileUtils.createDirectory(EXTERNAL_DIR);

        FileUtils.createDirectory(ACCOUNT_DIR);
        FileUtils.createDirectory(GAME_FILE_DIRECTORY_DIR);
        FileUtils.createDirectory(SETTING_DIR);
        FileUtils.createDirectory(DEBUG_DIR);
        FileUtils.createDirectory(INNER_GAME_DIR);

        FileUtils.createDirectory(DEFAULT_CACHE_DIR);
        FileUtils.createDirectory(DEFAULT_RUNTIME_DIR);

        FileUtils.createDirectory(BOAT_JAVA_DIR);
        FileUtils.createDirectory(BOAT_LIB_DIR);
        FileUtils.createDirectory(POJAV_JAVA_DIR);
        FileUtils.createDirectory(POJAV_LIB_DIR);
    }

}
