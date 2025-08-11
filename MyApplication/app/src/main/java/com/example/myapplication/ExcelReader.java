// ExcelReader.java
package com.example.myapplication;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<Feature> readFeaturesFromAssets(Context context, String fileName) {
        List<Feature> features = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // 跳过标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // 处理CSV格式，考虑可能包含逗号的字段
                String[] columns = parseCSVLine(line);
                if (columns.length >= 4) {
                    String wordEn = columns[0].trim();
                    String wordZh = columns[1].trim();
                    String featureZh = columns[2].trim();
                    boolean hasFeature = "1".equals(columns[3].trim());

                    features.add(new Feature(wordEn, wordZh, featureZh, hasFeature));
                }
            }

            reader.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return features;
    }

    /**
     * 解析CSV行，处理可能包含逗号的引号字段
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // 添加最后一个字段
        result.add(currentField.toString());

        return result.toArray(new String[0]);
    }

    public static List<Feature> getFeaturesForWord(Context context, String fileName, String wordEn) {
        List<Feature> allFeatures = readFeaturesFromAssets(context, fileName);
        List<Feature> wordFeatures = new ArrayList<>();

        for (Feature feature : allFeatures) {
            if (feature.getWordEn().equals(wordEn)) {
                wordFeatures.add(feature);
            }
        }

        return wordFeatures;
    }

    public static List<Feature> selectFeatures(List<Feature> features, int correctCount, int incorrectCount) {
        List<Feature> correctFeatures = new ArrayList<>();
        List<Feature> incorrectFeatures = new ArrayList<>();

        // 分离正确和错误的特征
        for (Feature feature : features) {
            if (feature.hasFeature()) {
                correctFeatures.add(feature);
            } else {
                incorrectFeatures.add(feature);
            }
        }

        List<Feature> selectedFeatures = new ArrayList<>();

        // 选择正确的特征
        int actualCorrectCount = Math.min(correctCount, correctFeatures.size());
        for (int i = 0; i < actualCorrectCount; i++) {
            selectedFeatures.add(correctFeatures.get(i));
        }

        // 选择错误的特征
        int actualIncorrectCount = Math.min(incorrectCount, incorrectFeatures.size());
        for (int i = 0; i < actualIncorrectCount; i++) {
            selectedFeatures.add(incorrectFeatures.get(i));
        }

        // 打乱顺序
        java.util.Collections.shuffle(selectedFeatures);

        return selectedFeatures;
    }
}