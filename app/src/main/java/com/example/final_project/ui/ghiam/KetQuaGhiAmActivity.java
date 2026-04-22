package com.example.final_project.ui.ghiam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.final_project.R;
import com.example.final_project.ui.trangchu.TrangChuActivity;
import com.example.final_project.util.DataManager;

public class KetQuaGhiAmActivity extends AppCompatActivity {

    private TextView txtMoTa, txtKetQuaTongHop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua_ghiam);

        txtMoTa = findViewById(R.id.txtMoTa);
        txtKetQuaTongHop = findViewById(R.id.txtKetQuaCuoiCung);

        Intent intent = getIntent();
        int labelAudio = intent.getIntExtra("label_voice", 0);

        Log.d("FINAL_DEBUG", "labelAudio nhận được = " + labelAudio);

        displayVoiceResult(labelAudio);

        findViewById(R.id.btnFinish).setOnClickListener(v -> {
            Intent i = new Intent(KetQuaGhiAmActivity.this, TrangChuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }

    private void displayVoiceResult(int labelAudio) {
        String resultTitle;
        String advice;
        int color;

        if (labelAudio == 1) {
            resultTitle = "CÓ DẤU HIỆU TRẦM CẢM";
            color = android.R.color.holo_red_dark;
            advice = "Phân tích giọng nói cho thấy các chỉ số liên quan đến trầm cảm. Bạn hãy thực hiện thêm bài trắc nghiệm để có kết quả chính xác nhất.";
        } else {
            resultTitle = "TÂM TRẠNG BÌNH THƯỜNG";
            color = android.R.color.holo_green_dark;
            advice = "Giọng nói của bạn hiện tại không cho thấy dấu hiệu trầm cảm. Hãy tiếp tục duy trì trạng thái tích cực này nhé!";
        }

        txtKetQuaTongHop.setText(resultTitle);
        txtKetQuaTongHop.setTextColor(ContextCompat.getColor(this, color));
        txtMoTa.setText(advice);

        DataManager.saveVoiceResult(this, labelAudio);
    }
}