package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineView extends View {

    private Paint paint; // 用于绘制线条的 Paint
    private Path path; // 当前正在绘制的路径
    private float startX, startY, endX, endY; // 起点和终点坐标
    private boolean isDrawing; // 是否正在绘制动态线条

    private List<float[]> testPoints = new ArrayList<>(); // 存储测试点坐标

    // 添加测试点
    public void addTestPoint(float x, float y) {
        testPoints.add(new float[]{x, y});
        invalidate(); // 重新绘制
    }

    // 存储固定线条
    private static class Line {
        float startX, startY, endX, endY;
        int color;

        Line(float startX, float startY, float endX, float endY, int color) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.color = color;
        }
    }

    private final List<Line> lines = new ArrayList<>(); // 存储所有绘制的固定线条

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(8); // 设置线条宽度
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // 抗锯齿
        paint.setColor(0xFF000000); // 默认颜色为黑色

        path = new Path();
        isDrawing = false;
    }

    // 开始绘制动态线条
    public void startDrawing(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        isDrawing = true;
        this.endX = startX; // 初始化终点，防止出现跑到屏幕外的问题
        this.endY = startY;
        invalidate();
    }

    // 更新动态线条终点
    public void updateLine(float endX, float endY) {
        if (isDrawing) { // 确保处于绘制状态
            this.endX = endX;
            this.endY = endY;
            invalidate();
        }
    }

    // 停止动态绘制
    public void stopDrawing() {
        isDrawing = false;
        invalidate();
    }

    // 添加固定线条（默认颜色）
    public void addLine(float startX, float startY, float endX, float endY) {
        addLine(startX, startY, endX, endY, 0xFF000000); // 默认黑色
    }

    // 添加固定线条（指定颜色）
    public void addLine(float startX, float startY, float endX, float endY, int color) {
        lines.add(new Line(startX, startY, endX, endY, color));
        invalidate();
    }

    // 清空所有固定线条
    public void clearLines() {
        lines.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制所有固定线条
        for (Line line : lines) {
            paint.setColor(line.color);
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }

        // 绘制动态线条
        if (isDrawing) {
            paint.setColor(0xFF000000); // 动态线条颜色为黑色
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }
}