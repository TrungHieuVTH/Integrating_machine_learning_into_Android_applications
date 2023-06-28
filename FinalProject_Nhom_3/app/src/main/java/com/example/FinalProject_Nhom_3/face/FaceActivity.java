package com.example.FinalProject_Nhom_3.face;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.io.IOException;

import com.example.FinalProject_Nhom_3.R;
import com.example.FinalProject_Nhom_3.face.common.CameraSource;
import com.example.FinalProject_Nhom_3.face.common.CameraSourcePreview;
import com.example.FinalProject_Nhom_3.face.interfaces.FaceDetectStatus;
import com.example.FinalProject_Nhom_3.face.common.FrameMetadata;
import com.example.FinalProject_Nhom_3.face.interfaces.FrameReturn;
import com.example.FinalProject_Nhom_3.face.common.GraphicOverlay;
import com.example.FinalProject_Nhom_3.face.base.PublicMethods;
import com.example.FinalProject_Nhom_3.face.models.RectModel;
import com.example.FinalProject_Nhom_3.face.visions.FaceDetectionProcessor;

@KeepName
public class FaceActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback, FrameReturn, FaceDetectStatus {
    private static final String FACE_DETECTION = "Face Detection";
    private static final String TAG = "MLKitTAG";
    Bitmap originalImage = null;
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private SmileRating smile_rating;
    private Bitmap croppedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_face_main);
        preview = findViewById(R.id.firePreview);
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        smile_rating = findViewById(R.id.smile_rating);

        if (PublicMethods.allPermissionsGranted(this)) {
            createCameraSource();
        } else {
            PublicMethods.getRuntimePermissions(this);
        }

    }

    private void createCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }
        try {
            FaceDetectionProcessor processor = new FaceDetectionProcessor(getResources());

            processor.frameHandler = this;
            processor.faceDetectStatus = this;

            cameraSource.setMachineLearningFrameProcessor(processor);

        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + FACE_DETECTION, e);
            Toast.makeText(getApplicationContext(), "Can not create image processor: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PublicMethods.allPermissionsGranted(this)) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFrame(Bitmap image, FirebaseVisionFace face, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay) {
        originalImage = image;

        int smile = 0;

        if (face.getSmilingProbability() > .8) {
            smile = BaseRating.GREAT;
        } else if (face.getSmilingProbability() <= .8 && face.getSmilingProbability() > .6) {
            smile = BaseRating.GOOD;
        } else if (face.getSmilingProbability() <= .6 && face.getSmilingProbability() > .4) {
            smile = BaseRating.OKAY;
        } else if (face.getSmilingProbability() <= .4 && face.getSmilingProbability() > .2) {
            smile = BaseRating.BAD;
        }

        smile_rating.setSelectedSmile(smile, true);
    }

    @Override
    public void onFaceLocated(RectModel rectModel) {}

    @Override
    public void onFaceNotLocated() {}
}
