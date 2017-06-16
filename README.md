# PermissionUtils
Android 运行时权限工具类
## 简洁版申请权限

####  申请一个权限：
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
#### 然后在onRequestPermissionsResult中：

```
if(PermissionUtils.isPermissionRequestSuccess(grantResults))
                {
                    // 权限申请成功
                    toCamera();
                }
```

#### 什么？要同时申请多个权限？
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
#### 当然上面这些都不是申请权限的正确姿势，理想的姿势应该是：
- 第一次申请权限：按照正常流程走；
- 如果用户第一次拒绝了权限申请，第二次申请时应向用户解释权限用途；
- 如果用户勾选了“不再询问”选项，应引导用户去设置页手动开启权限。
- 
于是，引申出了复杂版的权限申请方法：
## 自定义权限申请：
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
                // 上一次申请权限被拒绝，可用于向用户说明权限原因，然后调用权限申请方法。
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                // 第一次申请权限或被禁止申请权限，建议直接调用申请权限方法。
            }
        });
```
#### 然后在onRequestPermissionsResult中：

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
                        // 显示前往设置页的dialog
                        showToAppSettingDialog();
                    }
                });
```
**详细使用建议clone代码看一下，第一次封装工具类，如果问题，请指教，谢谢**





