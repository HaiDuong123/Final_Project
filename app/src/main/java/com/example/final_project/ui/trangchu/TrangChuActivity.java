package com.example.final_project.ui.trangchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.ghiam.BatDauGhiAmActivity;
import com.example.final_project.ui.tracnghiem.BatDauTracNghiemActivity;
import com.example.final_project.ui.hinhanh.BatDauHinhAnhActivity;
import com.example.final_project.util.DataManager; // Import kho dữ liệu dùng chung

public class TrangChuActivity extends AppCompatActivity {

    private ImageView btnTracNghiem, btnGhiAm, btnHinhAnh;
    private TextView txtHello, txtFinalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);


        btnTracNghiem = findViewById(R.id.btn_tracnghiem);
        btnGhiAm = findViewById(R.id.btn_ghiam);
        btnHinhAnh = findViewById(R.id.btn_hinhanh);
        txtHello = findViewById(R.id.txtHello);


        txtFinalResult = findViewById(R.id.txtFinalResult);


        String username = getIntent().getStringExtra("username");
        if (username != null) {
            txtHello.setText("Chào, " + username);
        }


        setupNavigation();


        calculateAndShowFinalScore();
    }


    @Override
    protected void onResume() {
        super.onResume();
        calculateAndShowFinalScore();
    }

    private void calculateAndShowFinalScore() {
        SharedPreferences prefs = DataManager.getPrefs(this);


        int scorePHQ9 = prefs.getInt("S_QUIZ", 0);    // Điểm Quiz [cite: 7, 8]
        int scoreText = prefs.getInt("S_TEXT", 0);    // Điểm Text [cite: 31, 32]
        int labelVoice = prefs.getInt("S_VOICE", 0);  // Nhãn Voice (0 hoặc 1) [cite: 48, 49]
        boolean isFaceDep = prefs.getBoolean("S_FACE", false); // Nhãn Face [cite: 68, 70]


        double sQuiz = scorePHQ9 / 24.0;
        double sText = scoreText / 24.0;
        double sVoice = (labelVoice == 1) ? 1.0 : 0.0;
        double sFace = isFaceDep ? 1.0 : 0.0;


        double sFinal = (0.40 * sQuiz) + (0.30 * sText) + (0.20 * sVoice) + (0.10 * sFace);


        int finalScore = (int) Math.round(sFinal * 24);


        String level;
        if (finalScore <= 4) level = "Bình thường";
        else if (finalScore <= 9) level = "Nhẹ";
        else if (finalScore <= 14) level = "Vừa";
        else if (finalScore <= 19) level = "Nặng vừa";
        else level = "Nặng";


        txtFinalResult.setText(finalScore + " điểm - " + level);
    }

    private void setupNavigation() {
        btnTracNghiem.setOnClickListener(v -> startActivity(new Intent(this, BatDauTracNghiemActivity.class)));
        btnGhiAm.setOnClickListener(v -> startActivity(new Intent(this, BatDauGhiAmActivity.class)));
        btnHinhAnh.setOnClickListener(v -> startActivity(new Intent(this, BatDauHinhAnhActivity.class)));
    }
}