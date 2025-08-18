package com.example.myapplication;

import java.util.List;

public class ImagePageData {
    public enum PageType {
        SINGLE_IMAGE,
        MULTIPLE_IMAGES
    }

    private PageType pageType;
    private List<String> imagePaths;

    public ImagePageData(PageType pageType, List<String> imagePaths) {
        this.pageType = pageType;
        this.imagePaths = imagePaths;
    }

    // Getters
    public PageType getPageType() { return pageType; }
    public List<String> getImagePaths() { return imagePaths; }
}
