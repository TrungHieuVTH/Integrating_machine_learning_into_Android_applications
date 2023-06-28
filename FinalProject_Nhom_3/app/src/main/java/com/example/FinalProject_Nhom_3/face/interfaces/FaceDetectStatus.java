package com.example.FinalProject_Nhom_3.face.interfaces;

import com.example.FinalProject_Nhom_3.face.models.RectModel;

public interface FaceDetectStatus {
    void onFaceLocated(RectModel rectModel);
    void onFaceNotLocated();
}
