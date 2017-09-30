package com.ksdc.brokenline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ksdc.brokenline.R;

/**
 * Created by liangkun on 2017/9/27 0027.
 * 自定义心电波形
 */

public class EcgWaveform extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder; //控制surfaceview的对象
    //    private Canvas canvas;
    private boolean isRunning;
    private int centerY;
    private int ecgPerCount = 50; // 每次画心电数据的个数，心电每秒有500个数据包
    private float waveSpeed;
    private Rect rect;
    private float ecgXOffset = 0; // 每次X坐标偏移的像素
    private int sleepTime = 100; // 每次锁屏的时间间距，单位ms
    private float lockWidth; // 每次锁屏需要画的宽度，单位像素
    private int blankLineWidth = 0; // 右侧缺口的宽度，单位像素
    private int waveWidgetHeight;
    private Paint wavePaint;
    private float startX = 0;
    private float stopY = 0;

    /**
     * 这个是new对象出来
     * @param context 上下文
     */
    public EcgWaveform(Context context) {
        super(context);
    }

    /***
     * xml配置
     * @param context 上下文
     * @param attrs 属性
     */
    public EcgWaveform(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置Windows上面
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //设置回调
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        initAttributes();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //得到控件宽高
        final int width = getWidth();
        int height = getHeight();
        //在控件中间画波形
        centerY = height / 2;
        DrawBackground();
        //开始画波形
        startDrawWave();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawWave();
    }

    private void DrawBackground() {
        final int width = getWidth();
        final int height = getHeight();

        final Drawable drawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                canvas.drawColor(Color.BLACK);
                drawEcgGrid(canvas);

            }
            @Override
            public void setAlpha(int alpha) {
            }
            @Override
            public void setColorFilter(ColorFilter cf) {
            }
            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
        this.setBackground(drawable);
    }

    /**
     * //开启画波形
     */
    private void startDrawWave() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    int count = 1;
    //波形子线程
    public Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                long startTime = System.currentTimeMillis();
                drawWave();

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < sleepTime) {
                    try {
                        count++;
                        Thread.sleep(sleepTime - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 初始化属性
     */
    private void initAttributes() {
        // 每毫米有多少像素。1280屏幕横向像素数；216.96为屏幕横向宽度，单位mm
        final float pxPerMm = 1280f / 216.96f;
        float pxPerSecond; //每秒画多少像素
        waveWidgetHeight = getHeight() / 3;

        // 初始化矩形波形绘制区域
        rect = new Rect();
        // 初始化其他波形绘制相关的参数
        waveSpeed = getWaveSpeedConfig();
        pxPerSecond = waveSpeed * pxPerMm; // 计算每秒画多少像素
        lockWidth = pxPerSecond * (sleepTime / 1000f); // 每次锁屏所需画的宽度
        ecgXOffset = lockWidth / ecgPerCount;
        blankLineWidth = 20;
        //波形画笔
        //波形画笔颜色
        wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setColor(Color.parseColor("#7CFC00"));
        wavePaint.setAntiAlias(true); // 设置抗锯齿
    }

    /**
     * 获取波形速率配置
     * @return 波形速率
     */
    private float getWaveSpeedConfig() {
        return 5f;
    }

    /**
     * 绘制波形
     */
    private void drawWave() {
        //设置画布的绘制区域
        rect.set(0, 0, getWidth() - blankLineWidth, getHeight());// 3宽度倍数
        Canvas canvas = surfaceHolder.lockCanvas(rect);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < ecgPerCount; i++) {
            stopY = count * ecgXOffset * (i + 1);
            canvas.drawLine(startX, centerY, stopY, centerY, wavePaint);
            startX = stopY;
            if (i == ecgPerCount - 1) {
                startX = ecgXOffset * (i + 1);
            }

        }
        if (startX == 757.4041) {

            startX = 0;
            count = 1;
            wavePaint.setColor(Color.RED);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 当surfaceview销毁，停止画波形
     */
    private void stopDrawWave() {
        isRunning = false;
    }

    /**
     * 画心电测量界面的网格
     * @param canvas
     */
    private void drawEcgGrid(Canvas canvas) {
        Log.e("kkk","drawEcgGrid@@@@@@@@@@@@@@@@@");
        //得到画布对
        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.tab_color));
        paint.setTextSize(14);
        paint.setAlpha(150);
        paint.setAntiAlias(true);

        // 单位:像素/mm,1280为屏幕横向像素数，216.96为屏幕横向长度（单位mm）
        double pixelPerMm = 1280 / 216.96;
        int width = getWidth();
        int height = getHeight();
        int widthLen = (int) (width / pixelPerMm); //转换为mm长度
        int heightLen = (int) (height / pixelPerMm); //转换为mm长度

        float startX;
        float startY;
        float stopX;
        float stopY;

        // 画X轴,每隔5mm画一条线
        paint.setStrokeWidth(2.0f);
        startX = 0;
        stopX = width;
        for (int i = 0; i < heightLen; i++) {
            startY = (float) (i * pixelPerMm * 5);
            stopY = (float) (i * pixelPerMm * 5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // 画Y轴,每隔5mm画一条线
        paint.setStrokeWidth(2.0f);
        startY = 0;
        stopY = height;
        for (int i = 0; i < widthLen; i++) {
            startX = (float) (i * pixelPerMm * 5);
            stopX = (float) (i * pixelPerMm * 5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }
}
