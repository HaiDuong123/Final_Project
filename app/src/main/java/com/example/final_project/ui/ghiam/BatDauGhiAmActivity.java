package com.example.final_project.ui.ghiam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.trangchu.TrangChuActivity;

public class BatDauGhiAmActivity extends AppCompatActivity {

    private LinearLayout btnBatDau;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batdau_ghiam);

        // ánh xạ nút bắt đầu
        btnBatDau = findViewById(R.id.btn_batdauthuchienghiam);

        // ánh xạ nút back
        btnBack = findViewById(R.id.btn_back);

        // xử lý click bắt đầu
        btnBatDau.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BatDauGhiAmActivity.this,
                    TrangDauTienGhiAmActivity.class
            );
            startActivity(intent);
        });

        // xử lý click back
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BatDauGhiAmActivity.this,
                    TrangChuActivity.class
            );
            startActivity(intent);
            finish(); // đóng màn hiện tại
        });
    }
}