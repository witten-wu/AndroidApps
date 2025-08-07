package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AssetImagePagerAdapter extends RecyclerView.Adapter<AssetImagePagerAdapter.ViewHolder> {

    private static final String TAG = "AssetImagePagerAdapter";
    private Context context;
    private List<String> imagePaths;

    private LruCache<String, Bitmap> memoryCache;

    public AssetImagePagerAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;

        // 初始化内存缓存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ns_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        loadImageFromAssets(holder.imageView, imagePath);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    /**
     * 从assets加载图片
     */
    private void loadImageFromAssets(ImageView imageView, String imagePath) {
        // 先从缓存中获取
        Bitmap cachedBitmap = memoryCache.get(imagePath);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
            return;
        }

        // 异步加载
        new Thread(() -> {
            AssetManager assetManager = context.getAssets();
            try (InputStream inputStream = assetManager.open(imagePath)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    // 添加到缓存
                    memoryCache.put(imagePath, bitmap);

                    // 在主线程更新UI
                    ((Activity) context).runOnUiThread(() ->
                            imageView.setImageBitmap(bitmap)
                    );
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading image: " + imagePath, e);
                ((Activity) context).runOnUiThread(() ->
                        imageView.setImageResource(R.drawable.ic_launcher_foreground)
                );
            }
        }).start();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}