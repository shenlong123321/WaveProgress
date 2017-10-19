package com.example.wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by zhangyunlong on 2017/10/18.
 * 自定义波浪进度条
 */

public class Wave extends View {
    private Paint paint_wave,paint_container;
    private Path path,circlePath;
    private int waveSpeedLevel,waveSpeed;                     //海浪横向移动的速度等级
    private int containerShape;                //外部容器的形状，0圆形，1矩形
    private int waveLength,halfWaveLength;     //每个海浪长度，半个海浪长度
    private int dx=0;                            //每次偏移量
    private int waveHeight;                    //海浪高度
    private int waveColor,containerColor;                     //海浪颜色
    private int containerHeight,containerLength;//容器宽高
    private float waveHeightPercent,waveLengthPercent; //海浪宽高百分比
    public Wave(Context context) {
        super(context);
        init();
    }

    public Wave(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttributes(context,attrs);
    }

    public Wave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        paint_wave=new Paint();
        paint_wave.setStyle(Paint.Style.FILL);
        paint_container=new Paint();
        paint_container.setStyle(Paint.Style.FILL_AND_STROKE);
        path=new Path();
        circlePath=new Path();
    }
    public void initAttributes(Context context,AttributeSet attrs){
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.Wave);
        containerShape=ta.getInt(R.styleable.Wave_containerShape,0);
        waveSpeedLevel=ta.getInt(R.styleable.Wave_waveSpeed,1);
        waveColor =ta.getColor(R.styleable.Wave_waveColor, Color.BLUE);
        containerColor=ta.getColor(R.styleable.Wave_containerColor,Color.LTGRAY);
        waveHeightPercent=ta.getFloat(R.styleable.Wave_waveHeight,0.5f);
        waveLengthPercent=ta.getFloat(R.styleable.Wave_waveLength,0.8f);
        containerHeight=(int)ta.getDimension(R.styleable.Wave_containerHeight,200);
        containerLength=(int)ta.getDimension(R.styleable.Wave_containerLength,200);
        waveLength=(int)(containerLength*waveLengthPercent);
        halfWaveLength=waveLength/2;
        waveHeight=(int)(containerHeight*waveHeightPercent);
        paint_wave.setColor(waveColor);
        paint_container.setColor(containerColor);
        circlePath.addCircle(containerLength/2,containerHeight/2,containerLength/2, Path.Direction.CCW);
        setSpeed(waveSpeedLevel);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(-waveLength+dx,waveHeight);
        for(int i=-waveLength;i<containerLength+waveLength;i+=waveLength){
            int quadHeight=waveLength/8;
            path.rQuadTo(halfWaveLength/2,-quadHeight,halfWaveLength,0);
            path.rQuadTo(halfWaveLength/2,quadHeight,halfWaveLength,0);
        }
        path.lineTo(containerLength,containerHeight);
        path.lineTo(0,containerHeight);
        path.close();
        if(containerShape==0){
            canvas.clipPath(circlePath);
            canvas.drawCircle(containerHeight/2,containerHeight/2,containerLength/2,paint_container);
        }else{
            canvas.drawRect(0,0,containerLength,containerHeight,paint_container);
        }
        canvas.drawPath(path,paint_wave);
    }
    private void setSpeed(int waveSpeedLevel ){
        switch (waveSpeedLevel){
            case 0:
                waveSpeed=1000;
                break;
            case 1:
                waveSpeed=2000;
                break;
            case 2:
                waveSpeed=3000;
                break;
            default:
                break;
        }
    }
    public void startToWave(){
        ValueAnimator animator=ValueAnimator.ofInt(0,waveLength);
        animator.setDuration(waveSpeed);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx=(int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
    public void setProgress(float percent){
        waveHeight=(int)(containerHeight*percent);
    }
}
