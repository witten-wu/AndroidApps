package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private static final String NOUN_FOLDER = "Noun";
    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".bmp", ".webp"};

    /**
     * 从assets/Noun/imageName目录下加载唯一的图片到ImageView
     * @param context 上下文
     * @param imageView 要设置图片的ImageView
     * @param imageName 图片文件夹名称
     * @return 是否成功加载图片
     */
    public static boolean loadImageFromAssets(Context context, ImageView imageView, String imageName) {
        try {
            String folderPath = NOUN_FOLDER + "/" + imageName;

            // 获取文件夹下的所有文件
            String[] files = context.getAssets().list(folderPath);
            if (files == null || files.length == 0) {
                return false;
            }

            // 找到第一个图片文件
            String imageFile = null;
            for (String file : files) {
                if (isImageFile(file)) {
                    imageFile = file;
                    break; // 只有一张图片，找到就退出
                }
            }

            if (imageFile == null) {
                return false;
            }

            // 加载图片
            String fullPath = folderPath + "/" + imageFile;
            InputStream inputStream = context.getAssets().open(fullPath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            inputStream.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 预加载图片为Drawable对象
     * @param context 上下文
     * @param imageName 图片文件夹名称
     * @return Drawable对象，失败返回null
     */
    public static Drawable loadDrawableFromAssets(Context context, String imageName) {
        try {
            String folderPath = NOUN_FOLDER + "/" + imageName;
            String[] files = context.getAssets().list(folderPath);

            if (files == null || files.length == 0) {
                return null;
            }

            // 找到第一个图片文件
            String imageFile = null;
            for (String file : files) {
                if (isImageFile(file)) {
                    imageFile = file;
                    break;
                }
            }

            if (imageFile == null) {
                return null;
            }

            String fullPath = folderPath + "/" + imageFile;
            InputStream inputStream = context.getAssets().open(fullPath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            inputStream.close();

            return drawable;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查指定的图片文件夹是否存在且包含图片文件
     * @param context 上下文
     * @param imageName 图片文件夹名称
     * @return 文件夹是否存在且包含图片
     */
    public static boolean isImageFolderValid(Context context, String imageName) {
        try {
            String folderPath = NOUN_FOLDER + "/" + imageName;
            String[] files = context.getAssets().list(folderPath);

            if (files == null || files.length == 0) {
                return false;
            }

            // 检查是否包含至少一个图片文件
            for (String file : files) {
                if (isImageFile(file)) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 检查文件是否为图片文件
     * @param fileName 文件名
     * @return 是否为图片文件
     */
    private static boolean isImageFile(String fileName) {
        if (fileName == null) return false;

        String lowerCaseFileName = fileName.toLowerCase();
        for (String extension : IMAGE_EXTENSIONS) {
            if (lowerCaseFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}