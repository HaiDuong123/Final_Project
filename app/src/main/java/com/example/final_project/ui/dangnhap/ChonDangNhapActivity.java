package com.example.final_project.ui.dangnhap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;

public class ChonDangNhapActivity extends AppCompatActivity {

    private LinearLayout btnDangNhap;
    private LinearLayout btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        // ánh xạ đúng kiểu
        btnDangNhap = findViewById(R.id.btn_dangnhap);
        btnDangKy = findViewById(R.id.btn_dangky);

        // click đăng ký
        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ChonDangNhapActivity.this,
                    DangKyActivity.class
            );
            startActivity(intent);
        });

        // click đăng nhập
        btnDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ChonDangNhapActivity.this,
                    DangNhapActivity.class
            );
            startActivity(intent);
        });
    }
}