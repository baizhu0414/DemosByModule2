package com.example.utillibrary.permissionutils;

public interface IPermissionListener {
    void onPermissionGranted(int reqCode);
    void onPermissionDenied(int reqCode);
}
