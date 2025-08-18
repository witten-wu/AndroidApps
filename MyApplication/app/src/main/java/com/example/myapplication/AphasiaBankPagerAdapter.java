package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AphasiaBankPagerAdapter extends RecyclerView.Adapter<AphasiaBankPagerAdapter.ViewHolder> {

    private Context context;
    private List<ImagePageData> pages;

    public AphasiaBankPagerAdapter(Context context, List<ImagePageData> pages) {
        this.context = context;
        this.pages = pages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ImagePageData.PageType.SINGLE_IMAGE.ordinal()) {
            view = LayoutInflater.from(context).inflate(R.layout.aphasia_single_image, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.aphasia_multiple_images, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImagePageData pageData = pages.get(position);
        holder.bind(pageData);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return pages.get(position).getPageType().ordinal();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView singleImageView;
        private GridLayout multipleImagesGrid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            singleImageView = itemView.findViewById(R.id.singleImageView);
            multipleImagesGrid = itemView.findViewById(R.id.multipleImagesGrid);
        }

        public void bind(ImagePageData pageData) {
            if (pageData.getPageType() == ImagePageData.PageType.SINGLE_IMAGE) {
                // 显示单张图片
                if (singleImageView != null) {
                    singleImageView.setVisibility(View.VISIBLE);
                    loadAssetImage(singleImageView, pageData.getImagePaths().get(0));
                }
                if (multipleImagesGrid != null) {
                    multipleImagesGrid.setVisibility(View.GONE);
                }
            } else {
                // 显示多张图片
                if (singleImageView != null) {
                    singleImageView.setVisibility(View.GONE);
                }
                if (multipleImagesGrid != null) {
                    multipleImagesGrid.setVisibility(View.VISIBLE);
                    multipleImagesGrid.removeAllViews();
                    setupMultipleImages(pageData.getImagePaths());
                }
            }
        }

        private void setupMultipleImages(List<String> imagePaths) {
            int imageCount = imagePaths.size();
            Log.d("AphasiaBankAdapter", "Setting up multiple images, count: " + imageCount);

            // 设置网格布局：固定为2行3列
            multipleImagesGrid.setRowCount(2);
            multipleImagesGrid.setColumnCount(3);

            int imageWidthDp = 350;
            float density = context.getResources().getDisplayMetrics().density;
            int imageWidth = (int) (imageWidthDp * density);

            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView imageView = new ImageView(context);

                int column, row;

                if (i < 3) {
                    // 第一行：0, 1, 2
                    row = 0;
                    column = i;
                } else {
                    // 第二行：0, 1
                    row = 1;
                    column = i - 3; // 0 或 1
                }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = imageWidth;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(column);
                params.setMargins(5, 50, 5, 50);

                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setAdjustViewBounds(true);

                loadAssetImage(imageView, imagePaths.get(i));
                multipleImagesGrid.addView(imageView);
            }

            Log.d("AphasiaBankAdapter", "Added " + multipleImagesGrid.getChildCount() + " views to grid");
        }

        private void loadAssetImage(ImageView imageView, String assetPath) {
            try {
                Log.d("AphasiaBankAdapter", "Loading image: " + assetPath);
                InputStream inputStream = context.getAssets().open(assetPath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    Log.d("AphasiaBankAdapter", "Successfully loaded image: " + assetPath);
                } else {
                    Log.e("AphasiaBankAdapter", "Bitmap is null for: " + assetPath);
                }
                inputStream.close();
            } catch (IOException e) {
                Log.e("AphasiaBankAdapter", "Error loading image: " + assetPath, e);
            }
        }
    }
}