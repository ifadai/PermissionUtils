package com.fadai.permissionutils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fadai.library.PermissionUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnToCamare;

    private Context mContext;



    // 相机权限
    private final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    // 打开相机请求Code
    private final int REQUEST_CODE_CAMERA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mBtnToCamare = (Button) findViewById(R.id.btn_main_camera);
        mBtnToCamare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_camera:
                PermissionUtils.checkPermission(mContext, CAMERA_PERMISSION, new PermissionUtils.RequestPermissionCallBack() {
                    @Override
                    public void onHasPermission() {
                        toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown() {
                        showExplainDialog();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk() {
                        PermissionUtils.requestPermission(mContext,CAMERA_PERMISSION,REQUEST_CODE_CAMERA);
                    }
                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                PermissionUtils.onRequestPermissionResult(mContext, CAMERA_PERMISSION, grantResults, new PermissionUtils.RequestPermissionCallBack() {
                    @Override
                    public void onHasPermission() {
                        toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown() {
                        Toast.makeText(mContext, "我们需要相机权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk() {
                        showToAppSettingDialog();
                    }
                });

        }
    }

    private void toCamera() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        startActivity(intent);
    }

    /**
     * 解释权限的dialog
     *
     * @describe
     */
    private void showExplainDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("申请相机权限")
                .setMessage("我们需要相机权限，才能实现拍照功能")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.requestPermission(mContext, CAMERA_PERMISSION, REQUEST_CODE_CAMERA);
                    }
                }).show();
    }

    /**
     * 显示前往应用设置Dialog
     *
     * @describe
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要相机权限")
                .setMessage("我们需要相机权限，才能实现拍照功能，点击前往，将转到应用的设置界面，请开启应用的相机权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }


}
