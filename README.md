# PermissionUtils
Android 运行时权限工具类
# 使用：

```
 // 相机权限、多个权限
    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR};

    // 打开相机请求Code，多个权限请求Code
    private final int REQUEST_CODE_CAMERA = 1,REQUEST_CODE_PERMISSIONS=2;
```

###  申请一个权限：

```
    PermissionUtils.checkAndRequestPermission(mContext, PERMISSION_CAMERA, REQUEST_CODE_CAMERA,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
                // 权限已被授予
                toCamera();
            }
        });
```
### 申请多个权限

```
    PermissionUtils.checkAndRequestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
                // 权限已被授予
                toCamera();
            }
        });
```
### 自定义申请权限

```
PermissionUtils.checkPermission(mContext, PERMISSION_CAMERA,
                new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                // 已授予权限
                toCamera();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                // 上一次申请权限被拒绝，可用于向用户说明权限原因
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                // 第一次申请权限或被禁止申请权限，建议直接调用申请权限方法。
            }
        });
```
###   onRequestPermissionsResult：

```
 PermissionUtils.onRequestPermissionResult(mContext, PERMISSION_CAMERA, grantResults, new PermissionUtils.PermissionCheckCallBack() {
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
                        // 前往应用权限设置界面
                        showToAppSettingDialog();
                    }
                });
```
**详细使用建议clone代码看一下，第一次封装工具类，如果问题，请指教，谢谢**





