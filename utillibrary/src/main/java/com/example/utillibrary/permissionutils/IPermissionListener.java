package com.example.utillibrary.permissionutils;

import java.util.List;

public interface IPermissionListener {
    void onPermissionGranted(int reqCode);
    void onPermissionDenied(int reqCode, List<String> deniedPer);
}
