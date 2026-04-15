package com.example.final_project.ui.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.ghiam.BatDauGhiAmActivity;
import com.example.final_project.ui.trangchu.TrangChuActivity;

public class BatDauTracNghiemActivity extends AppCompatActivity {

    private LinearLayout btnBatDau;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batdau_tracnghiem);


        btnBatDau = findViewById(R.id.btn_batdautracnghiem);
        btnBack = findViewById(R.id.btn_back);


        btnBatDau.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BatDauTracNghiemActivity.this,
                    ChonTracNghiemActivity.class
            );
            startActivity(intent);
        });
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BatDauTracNghiemActivity.this,
                    TrangChuActivity.class
            );
            startActivity(intent);
            finish(); // đóng màn hiện tại
        });
    }
}
