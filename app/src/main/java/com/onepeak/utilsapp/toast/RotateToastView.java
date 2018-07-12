package com.onepeak.utilsapp.toast;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.onepeak.utilsapp.utils.SizeUtils;

import java.security.InvalidAlgorithmParameterException;

public class RotateToastView extends View{

    private static final int MVISIABLE = 0x0;

    private Paint mPaint = null;
    private Paint mPaintBg = null;

    private int mW = 0;
    private int mH = 0;

    private String mContentText = "";

    private int mFlag = 1;

    private Type mOrientation = Type.ORITATION;

    private Duration mDuration = Duration.SHORT;

    private ObjectAnimator anim;

    private int padding = 0;
    private int angle = 0;
    private int angleOld = 0;
    private boolean useAngle = true;
    private boolean isDraw = false;

    public RotateToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public RotateToastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateToastView(Context context) {
        this(context, null);
    }

    private void initData(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setTextSize(SizeUtils.sp2px(16));
        mPaint.setStyle(Paint.Style.STROKE);

        mPaintBg = new Paint();
        mPaintBg.setColor(Color.DKGRAY);
        mPaintBg.setAntiAlias(true);
        mPaintBg.setDither(true);

        padding = SizeUtils.dpToPx(6);

        initAnim();
    }

    private void initAnim(){
        int duration = mDuration.ordinal() == 0 ? 1000 : 2000;
        anim = ObjectAnimator.ofFloat(this, "alpha", 1.0F, 0.0F).setDuration(duration);
        anim.addUpdateListener(new TimerAnimListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = w;
        mH = h;
    }

    /**
     * toast渐变效果
     */
    public void startAnim(){
        if(anim.isRunning()){
            anim.cancel();
        }
        anim.start();
    }


    public void setText(String text) {
        if(null == text){
            new IllegalArgumentException("Invalid args && draw text no null");
            return ;
        }
        this.mContentText = text;
    }

    public void setOrientation(Type type){
        useAngle = false;
        this.mOrientation = type;
    }

    public void setAngle(int angle){
        useAngle=true;
        this.angle = angle;
        if(isShown() &&  isDraw &&  angle != angleOld){
            invalidate();
        }
    }

    public void setTextSize(int size){
        if(size < 36){
            new InvalidAlgorithmParameterException("Android not know < 12sp textSize");
        }
        mPaint.setTextSize(size);
    }

    public void setDuration(Duration mDuration){
        this.mDuration = mDuration;
    }

    public void setVisiable(boolean isVisiable){
        if(isVisiable){
            mFlag |= MVISIABLE;
        }else{
            mFlag &= ~MVISIABLE;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        isDraw = true;
        if((mFlag & MVISIABLE) != MVISIABLE){ return; }

        canvas.save();
        int ori = 0;
        if(useAngle){
            ori = angle;
        }
        else {
            switch (mOrientation.ordinal()) {
                case 1:
                    ori = 90;
                    break;
                case 2:
                    ori = 180;
                    break;
                case 3:
                    ori = 270;
                    break;
                default:
                    ori = 0;
                    break;
            }
        }
        angleOld = ori;
        int mRotatePointX = mW/2;
        int mRotatePointY = mH/2;

        canvas.rotate(ori, mRotatePointX, mRotatePointY);

        int textW = (int) mPaint.measureText(mContentText);

//        canvas.drawText(mContentText, mRotatePointX - textW/2, mRotatePointY, mPaint);

        //--绘制外边框
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        //padding效果
        int left = mRotatePointX - textW/2 -padding*2;
        int top = (int) (mRotatePointY + fontMetrics.ascent) -padding*2;
        int right = mRotatePointX + textW/2 +padding*2;
        int bottom = (int) (mRotatePointY + fontMetrics.descent) +padding*2;

//        canvas.drawRoundRect(left, top, right, bottom, mRotatePointX - textW/2, mRotatePointY, mPaint);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            canvas.drawRoundRect(left, top, right, bottom, 10, 10, mPaintBg);
        }else{
            canvas.drawRect(left, top, right, bottom, mPaintBg);
        }
        canvas.drawText(mContentText, mRotatePointX - textW/2, mRotatePointY, mPaint);

        canvas.restore();
    }

    public enum Type{
        ORITATION,ORITATION_90,ORITATION_180,ORITATION_270;
    }

    public enum Duration{
        SHORT,LONG;
    }

    private class TimerAnimListener implements AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
            Log.d("TAG","mDuration = " + animation.getCurrentPlayTime());
            float mTotaltime = mDuration == Duration.SHORT ? 2000f : 3000f;
            float currentPlayTime = animation.getCurrentPlayTime();
            float alpha = ((mTotaltime - currentPlayTime)/mTotaltime * 255);
            Log.d("TAG","alpha = " + ((mTotaltime - currentPlayTime)/mTotaltime * 255));
            if(alpha <= 0) {
                alpha = 0;
            }
            mPaint.setAlpha((int)alpha);
            invalidate();
        }

    }
}
