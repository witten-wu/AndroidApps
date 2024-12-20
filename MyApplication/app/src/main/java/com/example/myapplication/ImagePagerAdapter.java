package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {

    private final Context context;
    private final int[] images;

    public ImagePagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载单个页面的布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.rs_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 设置图片资源
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        // 返回图片数组的长度
        return images.length;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 绑定布局中的 ImageView
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}