package com.example.FinalProject_Nhom_3;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.FinalProject_Nhom_3.asl.AslActivity;
import com.example.FinalProject_Nhom_3.asl.CameraActivity;
import com.example.FinalProject_Nhom_3.digit.DigitActivity;
import com.example.FinalProject_Nhom_3.face.FaceActivity;
import com.example.FinalProject_Nhom_3.face.base.PublicMethods;


public class MainActivity  extends TabActivity {
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        if (!PublicMethods.allPermissionsGranted(this)){
            PublicMethods.getRuntimePermissions(this);
        }

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec;
        Intent intent;

        // Tạo Tab Digit
        spec = tabHost.newTabSpec("Digit");
        spec.setIndicator("Digit", ContextCompat.getDrawable(this, R.drawable.ic_123));
        intent = new Intent(this, DigitActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);
        setupActivity();

        //  Tạo Tab ASL
        spec = tabHost.newTabSpec("ASL");
        spec.setIndicator("ASL", ContextCompat.getDrawable(this, R.drawable.ic_hand));
        intent = new Intent(this, AslActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);


        // Tạo Tab Face
        spec = tabHost.newTabSpec("Face");
        spec.setIndicator("Face", ContextCompat.getDrawable(this, R.drawable.ic_face));
        intent = new Intent(this, FaceActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);

        // Hiển thị tab music đầu tiên
        tabHost.setCurrentTab(0);


        // Gắn sự kiện cho các tab
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActivity(){
        Toast.makeText(getApplicationContext(), "ASL", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Face", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Digit", Toast.LENGTH_SHORT).show();
    }

    private void requestPermission() {
        if (!PublicMethods.allPermissionsGranted(this)) {
            PublicMethods.getRuntimePermissions(this);
        }

        if (checkSelfPermission(PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(MainActivity.this,"Camera permission is required for this app",Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
        }

    }
}