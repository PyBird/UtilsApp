package com.onepeak.utilsapp.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by OnePeak on 2018/1/25.
 */

public class SizeUtils {

    private final static String TAG = SizeUtils.class.getSimpleName();

    private SizeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 各种单位转换
     * <p>该方法存在于 TypedValue</p>
     *
     * @param unit    单位
     * @param value   值
     * @param metrics DisplayMetrics
     * @return 转换结果
     */
    public static float applyDimension(final int unit,
                                       final float value,
                                       final DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    /**
     * 在 onCreate 中获取视图的尺寸
     * <p>需回调 onGetSizeListener 接口，在 onGetSize 中获取 view 宽高</p>
     * <p>用法示例如下所示</p>
     * <pre>
     * SizeUtils.forceGetViewSize(view, new SizeUtils.onGetSizeListener() {
     *     Override
     *     public void onGetSize(final View view) {
     *         view.getWidth();
     *     }
     * });
     * </pre>
     *
     * @param view     视图
     * @param listener 监听器
     */
    public static void forceGetViewSize(final View view, final onGetSizeListener listener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onGetSize(view);
                }
            }
        });
    }

    /**
     * 获取到 View 尺寸的监听
     */
    public interface onGetSizeListener {
        void onGetSize(View view);
    }

    /**
     * 测量视图尺寸
     *
     * @param view 视图
     * @return arr[0]: 视图宽度, arr[1]: 视图高度
     */
    public static int[] measureView(final View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    /**
     * 获取测量视图宽度
     *
     * @param view 视图
     * @return 视图宽度
     */
    public static int getMeasuredWidth(final View view) {
        return measureView(view)[0];
    }

    /**
     * 获取测量视图高度
     *
     * @param view 视图
     * @return 视图高度
     */
    public static int getMeasuredHeight(final View view) {
        return measureView(view)[1];
    }

    public static Bitmap preprocessBitmap(Bitmap bmp, int angle, int dw, int dh){
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        // cut
        int scaled_size =  Math.min(w, h);
        bmp = Bitmap.createScaledBitmap(bmp, scaled_size,scaled_size,true);

        // resize and rotate
        float scaleW = ((float)dw) / scaled_size;
        float scaleH = ((float)dh) / scaled_size;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW,scaleH);
        matrix.postRotate(angle);
//        Bitmap res = Bitmap.createBitmap(bmp,0 ,0,scaled_size, scaled_size,matrix,true);
        bmp = Bitmap.createBitmap(bmp,0, 0, scaled_size, scaled_size, matrix, true);
        return bmp;
    }


    /**
     *  获取页面宽高 int[宽，高]
     * @param activity
     * @return
     */
    public static int[] getScreenWidthHeight(Activity activity) {
        WindowManager wm = (WindowManager) activity.getApplication().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            int x = activity.getApplication().getResources().getDisplayMetrics().widthPixels;
            int y = activity.getApplication().getResources().getDisplayMetrics().heightPixels;
            return new int[]{x,y};
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return new int[]{point.x,point.y};
    }

    /**
     *  获取页面宽高 int[宽，高]
     * @param app
     * @return
     */
    public static int[] getScreenWidthHeight(Application app) {
        WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            int x = app.getResources().getDisplayMetrics().widthPixels;
            int y = app.getResources().getDisplayMetrics().heightPixels;
            return new int[]{x,y};
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return new int[]{point.x,point.y};
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int clamp(int x, int min, int max)
    {
        if (x < min)
        {
            return min;
        } else if (x > max)
        {
            return max;
        } else
        {
            return x;
        }
    }

}
