// Feature.java
package com.example.myapplication;

public class Feature {
    private String wordEn;
    private String wordZh;
    private String featureZh;
    private boolean hasFeature;

    public Feature(String wordEn, String wordZh, String featureZh, boolean hasFeature) {
        this.wordEn = wordEn;
        this.wordZh = wordZh;
        this.featureZh = featureZh;
        this.hasFeature = hasFeature;
    }

    public Feature() {
    }

    // Getters
    public String getWordEn() { return wordEn; }
    public String getWordZh() { return wordZh; }
    public String getFeatureZh() { return featureZh; }
    public boolean hasFeature() { return hasFeature; }
    public void setWordEn(String wordEn) { this.wordEn = wordEn; }
    public void setWordZh(String wordZh) { this.wordZh = wordZh; }
    public void setFeatureZh(String featureZh) { this.featureZh = featureZh; }
    public void setHasFeature(boolean hasFeature) { this.hasFeature = hasFeature; }
}