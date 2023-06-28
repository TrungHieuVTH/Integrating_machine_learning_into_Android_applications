package com.example.FinalProject_Nhom_3.face.visions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;

import com.example.FinalProject_Nhom_3.face.common.GraphicOverlay;
import com.example.FinalProject_Nhom_3.face.interfaces.FaceDetectStatus;
import com.example.FinalProject_Nhom_3.face.models.RectModel;

public class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float ID_TEXT_SIZE = 30.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private int facing;
    private final Paint facePositionPaint;
    private final Paint idPaint;
    private final Paint boxPaint;
    private volatile FirebaseVisionFace firebaseVisionFace;
    private final Bitmap overlayBitmap;
    FaceDetectStatus faceDetectStatus = null;

    FaceGraphic(GraphicOverlay overlay, FirebaseVisionFace face, int facing, Bitmap overlayBitmap) {
        super(overlay);

        firebaseVisionFace = face;
        this.facing = facing;
        this.overlayBitmap = overlayBitmap;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(Color.GREEN);

        idPaint = new Paint();
        idPaint.setColor(Color.GREEN);
        idPaint.setTextSize(ID_TEXT_SIZE);

        boxPaint = new Paint();
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    @Override
    public void draw(Canvas canvas) {
        FirebaseVisionFace face = firebaseVisionFace;
        if (face == null) {
            return;
        }

        float x = translateX(face.getBoundingBox().centerX());
        float y = translateY(face.getBoundingBox().centerY());
        float xOffset = scaleX(face.getBoundingBox().width() / 2.0f);
        float yOffset = scaleY(face.getBoundingBox().height() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, boxPaint);

        if (left < 190 && top < 450 && right > 850 && bottom > 1050) {
            if (faceDetectStatus != null)
                faceDetectStatus.onFaceLocated(new RectModel(left, top, right, bottom));
        } else {
            if (faceDetectStatus != null) faceDetectStatus.onFaceNotLocated();
        }
    }

}
