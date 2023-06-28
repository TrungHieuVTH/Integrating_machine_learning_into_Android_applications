package com.example.FinalProject_Nhom_3.digit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FinalProject_Nhom_3.R;
import com.nex3z.fingerpaintview.FingerPaintView;

import java.io.IOException;

public class DigitActivity extends AppCompatActivity {
    FingerPaintView mFpvPaint;
    TextView mTvPrediction;
    TextView mTvProbability;
    TextView mTvTimeCost;

    private Classifier mClassifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digit_main);

        try {
            mClassifier = new Classifier(this);
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }


        mFpvPaint = findViewById(R.id.fpv_paint);
        mTvPrediction = findViewById(R.id.prediction);
        mTvProbability = findViewById(R.id.probability);
        mTvTimeCost = findViewById(R.id.timecost);

        Button detect = findViewById(R.id.btn_detect);
        Button clear = findViewById(R.id.btn_clear);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = mFpvPaint.exportToBitmap(Classifier.IMG_WIDTH,Classifier.IMG_HEIGHT);
                Result res = mClassifier.classify(bitmap);
                mTvProbability.setText("Probability: "+res.getProbability()+"");
                mTvPrediction.setText("Prediction: "+res.getNumber()+"");
                mTvTimeCost.setText("TimeCost: "+res.getTimeCost()+"");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFpvPaint.clear();
                mTvPrediction.setText("");
                mTvProbability.setText("");
                mTvTimeCost.setText("");
            }
        });
    }
}
