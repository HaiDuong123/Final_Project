package com.example.final_project.ui.trangchu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.tracnghiem.BatDauTracNghiemActivity;
import com.example.final_project.ui.hinhanh.BatDauHinhAnhActivity;
import com.example.final_project.util.DataManager;

public class TrangChuActivity extends AppCompatActivity {

    // ✅ Đã xóa btnGhiAm vì nút này không còn ở màn hình chính
    private ImageView btnTracNghiem, btnHinhAnh;
    private TextView txtHello, txtFinalResult, txtStatusNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        initViews();

        String username = getIntent().getStringExtra("username");
        if (username != null) {
            txtHello.setText("Chào, " + username);
        }

        setupNavigation();
        calculateAndShowFinalScore();
    }

    private void initViews() {
        btnTracNghiem = findViewById(R.id.btn_tracnghiem);
        btnHinhAnh = findViewById(R.id.btn_hinhanh);
        txtHello = findViewById(R.id.txtHello);
        txtFinalResult = findViewById(R.id.txtFinalResult);
        txtStatusNote = findViewById(R.id.txtStatusNote);
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateAndShowFinalScore();
    }

    private void calculateAndShowFinalScore() {
        // 1. Lấy dữ liệu từ DataManager
        double sQuiz = DataManager.getQuizScore(this) / 24.0;
        double sVoice = (DataManager.getVoiceResult(this) == 1) ? 1.0 : 0.0;
        double sFace = DataManager.getFaceResult(this) ? 1.0 : 0.0;

        // 2. Trọng số: Quiz (50%) - Voice (30%) - Face (20%)
        double sFinal = (0.50 * sQuiz) + (0.30 * sVoice) + (0.20 * sFace);
        int finalScore = (int) Math.round(sFinal * 24);

        // 3. Phân loại mức độ
        String level;
        if (finalScore <= 4) level = "Bình thường";
        else if (finalScore <= 9) level = "Nhẹ";
        else if (finalScore <= 14) level = "Vừa";
        else if (finalScore <= 19) level = "Nặng vừa";
        else level = "Nặng";

        txtFinalResult.setText(finalScore + " điểm - " + level);

        // 4. Thông báo nhắc nhở bài test còn thiếu
        StringBuilder missingTests = new StringBuilder();

        if (!DataManager.isQuizCompleted(this)) missingTests.append("Trắc nghiệm, ");
        if (DataManager.getVoiceResult(this) == -1) missingTests.append("Giọng nói, ");
        if (!DataManager.isFaceCompleted(this)) missingTests.append("Khuôn mặt, ");

        if (missingTests.length() > 0) {
            String msg = missingTests.substring(0, missingTests.length() - 2);
            txtStatusNote.setText("✨ Để chính xác hơn, bạn hãy làm thêm bài: " + msg + " nhé!");
            txtStatusNote.setTextColor(Color.WHITE);
            txtStatusNote.setVisibility(View.VISIBLE);
        } else {
            txtStatusNote.setText("🌿 Tuyệt vời! Bạn đã hoàn thành tất cả bài kiểm tra.");
            txtStatusNote.setTextColor(Color.WHITE);
            txtStatusNote.setVisibility(View.VISIBLE);
        }
    }

    private void setupNavigation() {
        btnTracNghiem.setOnClickListener(v -> startActivity(new Intent(this, BatDauTracNghiemActivity.class)));

        btnHinhAnh.setOnClickListener(v -> startActivity(new Intent(this, BatDauHinhAnhActivity.class)));

    }
}