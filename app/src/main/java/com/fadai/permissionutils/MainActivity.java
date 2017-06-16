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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnRequestOnePermission,mBtnRequestPermissions;

    private Context mContext;

    // 相机权限
    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR};

    // 打开相机请求Code，多个权限请求Code
    private final int REQUEST_CODE_CAMERA = 1,REQUEST_CODE_PERMISSIONS=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnRequestOnePermission = (Button) findViewById(R.id.btn_main_request_one_permission);
        mBtnRequestPermissions=(Button)findViewById(R.id.btn_main_request_permissions);

        mBtnRequestOnePermission.setOnClickListener(this);
        mBtnRequestPermissions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_request_one_permission:
                requestOnePermission();
                break;
            case R.id.btn_main_request_permissions:
                requestPermissions();
                break;

        }
    }

    private void requestOnePermission(){
        PermissionUtils.checkPermission(mContext, PERMISSION_CAMERA, new PermissionUtils.CheckPermissionCallBack() {
            @Override
            public void onHasPermission() {
                toCamera();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                showExplainDialog();
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(mContext, PERMISSION_CAMERA, REQUEST_CODE_CAMERA);
            }
        });
    }

    private void requestPermissions(){
        PermissionUtils.checkAndRequestPermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS, new PermissionUtils.RequestPermissionsCallBack() {
            @Override
            public void onHasPermission() {
                toCamera();
            }
        });
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
                        PermissionUtils.requestPermission(mContext, PERMISSION_CAMERA, REQUEST_CODE_CAMERA);
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
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                PermissionUtils.onRequestPermissionResult(mContext, PERMISSION_CAMERA, grantResults, new PermissionUtils.CheckPermissionCallBack() {
                    @Override
                    public void onHasPermission() {
                        toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                        showToAppSettingDialog();
                    }
                });
                break;
            case REQUEST_CODE_PERMISSIONS:
                PermissionUtils.onRequestPermissionsResult(mContext, PERMISSIONS, new PermissionUtils.CheckPermissionCallBack() {
                    @Override
                    public void onHasPermission() {
                        toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要"+ Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                        showToAppSettingDialog();
                    }
                });


        }
    }


}
