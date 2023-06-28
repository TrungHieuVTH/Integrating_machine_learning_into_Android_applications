package com.example.FinalProject_Nhom_3.face.interfaces;

import android.graphics.Bitmap;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;

import com.example.FinalProject_Nhom_3.face.common.FrameMetadata;
import com.example.FinalProject_Nhom_3.face.common.GraphicOverlay;

public interface FrameReturn {
    void onFrame(Bitmap image, FirebaseVisionFace face, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay);
}