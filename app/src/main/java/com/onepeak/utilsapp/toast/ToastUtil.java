package com.onepeak.utilsapp.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 *
 * Created by OnePeak on 2018/4/20
 *
 */
public class ToastUtil {

    private static Toast longToast, shortToast;

    /**
     * 长时间提醒
     *
     * @param resId
     */
    public static synchronized void showLongToast(Context context, int resId) {
        showLongToast(context, context.getString(resId));
    }

    public static synchronized void showLongToast(Context context, String tips) {
        if (longToast == null) {
            longToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }

        longToast.setText(tips);
        longToast.show();
    }

    /**
     * 短时间提醒
     *
     * @param tips
     */
    public static synchronized void showShortToast(Context context, String tips) {
//        if (shortToast == null) {
//            shortToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
//            //居中显示
//            shortToast.setGravity(Gravity.CENTER,0,0);
//        }
//        shortToast.setText(tips);
//        shortToast.show();
        showShortToast(context,tips,0);
    }
    public static synchronized void showShortToast(Context context, String tips,int angle) {
//        if (shortToast == null) {
//            shortToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
//            shortToast.setGravity(Gravity.CENTER,0,0);
//        }
//        shortToast.setText(tips);
//        shortToast.show();

        if (shortToast == null) {
            shortToast = new Toast(context);
            View view = new RotateToastView(context);
            shortToast.setView(view);
            shortToast.setGravity(Gravity.CENTER,0,0);
        }
        RotateToastView view = (RotateToastView)shortToast.getView();
        if(null != view){
            view.setText(tips);
            view.setAngle(angle);
//            RotateToastView.Type type;
//            if(angle==0){
//                type = RotateToastView.Type.ORITATION;
//            }
//            else if(angle==90){
//                type = RotateToastView.Type.ORITATION_90;
//            }
//            else if(angle==180){
//                type = RotateToastView.Type.ORITATION_180;
//            }
//            else{
//                type = RotateToastView.Type.ORITATION_270;
//            }
//            view.setOrientation(type);
        }
        shortToast.show();
    }

    /**
     * 短时间提醒
     *
     * @param tipsResId
     */
    public static synchronized void showShortToast(Context context, int tipsResId) {
        showShortToast(context, context.getString(tipsResId));
    }
    public static synchronized void showShortToast(Context context, int tipsResId,int angle) {
        showShortToast(context, context.getString(tipsResId), angle);
    }
    /**
     * 设置旋转角度
     * @param angle
     */
    public static synchronized void setShortToastAngle(float angle) {
        if (null != shortToast) {
            RotateToastView view = (RotateToastView)shortToast.getView();
            if(null != view){
                view.setAngle((int)angle);
            }
        }
    }

    public static void cancelToast(){
        if(null != shortToast){
            shortToast.cancel();
        }
        if(null != longToast){
            longToast.cancel();
        }
    }
}
