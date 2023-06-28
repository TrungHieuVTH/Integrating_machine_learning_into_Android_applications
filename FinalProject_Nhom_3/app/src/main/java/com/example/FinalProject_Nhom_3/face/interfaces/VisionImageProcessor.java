package com.example.FinalProject_Nhom_3.face.interfaces;

import android.graphics.Bitmap;

import com.google.firebase.ml.common.FirebaseMLException;

import java.nio.ByteBuffer;

import com.example.FinalProject_Nhom_3.face.common.FrameMetadata;
import com.example.FinalProject_Nhom_3.face.common.GraphicOverlay;

public interface VisionImageProcessor {

  void process(ByteBuffer data, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay)
      throws FirebaseMLException;

  void stop();
}
