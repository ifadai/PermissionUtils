package com.fadai.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/13
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class PermissionUtils {

    /**
     * 检测权限
     *
     * @describe
     */
    public static boolean checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 请求权限
     *
     * @describe
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }

    /**
     * 判断是否已拒绝权限
     *
     * @return 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true;
     * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     * @describe
     */
    public static boolean judgePermission(Context context, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
            return true;
        else
            return false;
    }

    /**
     * 检测权限并请求权限：如果没有权限，则请求权限
     *
     * @describe
     */
    public static void checkAndRequestPermission(Context context, String permission, int requestCode) {
        if (!checkPermission(context, permission)) {
            requestPermission(context, permission, requestCode);
        }
    }

    /**
     *  检测并申请权限
     *  通过回调方法，实现：已允许权限回调、用户之前已拒绝权限回调、用户之前已拒绝并勾选了不在询问选项的回调。
     *  @describe
     */
    public static void checkPermission(Context context, String permission,  RequestPermissionCallBack callBack){
        if(checkPermission(context,permission)){
            callBack.onHasPermission();
        } else{
            if(judgePermission(context,permission))
                callBack.onUserHasAlreadyTurnedDown();
            else
                callBack.onUserHasAlreadyTurnedDownAndDontAsk();
        }
    }

    /**
     *  判断权限是否申请成功
     *  @describe
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults){
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     *  用户申请权限返回
     *  @describe
     */
    public static void onRequestPermissionResult(Context context,String permission,int[] grantResults,RequestPermissionCallBack callback){
        if(PermissionUtils.isPermissionRequestSuccess(grantResults)){
            callback.onHasPermission();
        } else {
            if(PermissionUtils.judgePermission(context,permission)){
                callback.onUserHasAlreadyTurnedDown();
            } else {
                callback.onUserHasAlreadyTurnedDownAndDontAsk();
            }
        }

    }


    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }




    public interface RequestPermissionCallBack{
        // 已拥有权限的回调
         void onHasPermission();
        // 用户已拒绝过权限的回调
         void onUserHasAlreadyTurnedDown();
        //　用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限
         void onUserHasAlreadyTurnedDownAndDontAsk();
    }



}
