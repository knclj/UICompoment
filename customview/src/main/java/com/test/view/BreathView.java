package com.test.view;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import com.test.customview.R;

import java.util.logging.Handler;

public class BreathView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private static final TimeInterpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * 扩散圆圈颜色
     */
    private int mDiffusionColor = Color.parseColor("#303F9F");

    /**
     *
     */
    private int mCoreColor = Color.parseColor("#FFFFFF");

    private float mCoreRadius = 56F;

    private float mMaxWidth = 30F;

    /**
     * 初始透明度
     */
    private int color = (int)(255*04F);

    /**
     * 渲染线程
     */
    private Thread mRenderThread;
    /**
     * 获取Canvas holder
     */
    private SurfaceHolder surfaceHolder;

    private Paint mPaint;
    private float mFraction;
    private int HEART_BEAT_RATE = 3000;

    private float circleX;
    private float circleY;
    private int duration = HEART_BEAT_RATE;
    private volatile boolean isStop = false;

    public BreathView(Context context) {
        super(context);
        init(context,null);
    }

    public BreathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BreathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BreathView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        setFocusable(true);
        if(surfaceHolder == null){
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mCoreRadius = 56/3f +3.5f;
        mMaxWidth = 10;
        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BreathView);
            if(null != array){
                duration = array.getInt(R.styleable.BreathView_duration,HEART_BEAT_RATE);
                array.recycle();
            }
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);


    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isStop = false;
        mRenderThread = new Thread(this::run,"BreathRender");
        mRenderThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isStop = true;

        try {
            mRenderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private long runTime  = 0;
    @Override
    public void run() {
        if(surfaceHolder == null){
            return;
        }
        long runDuration = System.currentTimeMillis() - runTime;
        while (runDuration >= duration){
            runTime = System.currentTimeMillis();
            mFraction = sDefaultInterpolator.getInterpolation(1);
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas == null){
                return;
            }
            mPaint.setColor(mDiffusionColor);
            mPaint.setAlpha((int) (color - color*mFraction));
            canvas.drawCircle(circleX,circleY,mCoreRadius+mMaxWidth*mFraction,mPaint);

            mPaint.setAntiAlias(true);
            mPaint.setAlpha(0);
            mPaint.setColor(mCoreColor);
            canvas.drawCircle(circleX,circleY,mCoreRadius,mPaint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleX = w/2f;
        circleY = h/2f;
    }
}
