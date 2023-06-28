package com.example.FinalProject_Nhom_3.asl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Pair;
import android.util.TypedValue;

import java.util.LinkedList;
import java.util.List;

import com.example.FinalProject_Nhom_3.asl.BorderedText;
import com.example.FinalProject_Nhom_3.asl.ImageUtils;
import org.tensorflow.lite.examples.detection.tflite.Detector.Recognition;

public class BoxTracker {
    private static class TrackedRecognition {
        RectF location;
        String title;
    }
    private static final float TEXT_SIZE_DIP = 18;
    private static final float MIN_SIZE = 16.0f;
    final List<Pair<Float, RectF>> screenRects = new LinkedList<>();
    private final List<TrackedRecognition> trackedObjects = new LinkedList<>();
    private final Paint boxPaint = new Paint();
    private final BorderedText borderedText;
    private Matrix frameToCanvasMatrix;
    private int frameWidth, frameHeight, sensorOrientation;

    public BoxTracker(final Context context) {
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Style.STROKE);
        boxPaint.setStrokeWidth(10.0f);
        boxPaint.setStrokeCap(Cap.ROUND);
        boxPaint.setStrokeJoin(Join.ROUND);
        boxPaint.setStrokeMiter(100);

        float textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
    }

    public synchronized void setFrameConfiguration(
            final int width, final int height, final int sensorOrientation) {
        frameWidth = width;
        frameHeight = height;
        this.sensorOrientation = sensorOrientation;
    }

    public synchronized void trackResults(final List<Recognition> results) {
        processResults(results);
    }

    private Matrix getFrameToCanvasMatrix() {
        return frameToCanvasMatrix;
    }

    public synchronized void draw(final Canvas canvas) {
        final boolean rotated = sensorOrientation % 180 == 90;
        final float multiplier = Math.min(
                canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
                canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
        frameToCanvasMatrix = ImageUtils.getTransformationMatrix(
                frameWidth,
                frameHeight,
                (int) (multiplier * (rotated ? frameHeight : frameWidth)),
                (int) (multiplier * (rotated ? frameWidth : frameHeight)),
                sensorOrientation,
                false);
        for (final TrackedRecognition recognition : trackedObjects) {
            final RectF trackedPos = new RectF(recognition.location);

            getFrameToCanvasMatrix().mapRect(trackedPos);
            boxPaint.setColor(Color.GREEN);

            float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
            canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint);

            borderedText.drawText(canvas, trackedPos.left + cornerSize, trackedPos.top, recognition.title , boxPaint);
        }
    }

    private void processResults(final List<Recognition> results) {
        final List<Pair<Float, Recognition>> rectsToTrack = new LinkedList<>();
        final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

        screenRects.clear();
        for (final Recognition result : results) {
            if (result.getLocation() == null) {
                continue;
            }
            final RectF detectionFrameRect = new RectF(result.getLocation());
            final RectF detectionScreenRect = new RectF();

            rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);
            screenRects.add(new Pair<>(result.getConfidence(), detectionScreenRect));

            if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
                continue;
            }

            rectsToTrack.add(new Pair<>(result.getConfidence(), result));
        }

        trackedObjects.clear();

        if (rectsToTrack.isEmpty()) {
            return;
        }

        for (final Pair<Float, Recognition> potential : rectsToTrack) {
            final TrackedRecognition trackedRecognition = new TrackedRecognition();
            trackedRecognition.location = new RectF(potential.second.getLocation());
            trackedRecognition.title = potential.second.getTitle();
            trackedObjects.add(trackedRecognition);
        }
    }
}
