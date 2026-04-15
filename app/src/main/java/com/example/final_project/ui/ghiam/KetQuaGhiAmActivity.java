package com.example.final_project.ui.ghiam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.final_project.R;
import com.example.final_project.ui.trangchu.TrangChuActivity;
import com.example.final_project.util.DataManager;

public class KetQuaGhiAmActivity extends AppCompatActivity {

    private TextView txtMoTa, txtKetQuaTongHop;
    private View layoutVoice, layoutText, lblVoice, lblText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua_ghiam);

        txtKetQuaTongHop = findViewById(R.id.txtKetQuaCuoiCung);
        txtMoTa = findViewById(R.id.txtMoTa);

        txtKetQuaTongHop = findViewById(R.id.txtKetQuaCuoiCung);

        hideDetailViews();

        Intent intent = getIntent();

        int labelAudio = intent.getIntExtra("label_voice", 0);
        int scoreText = intent.getIntExtra("score_text", 2);

        Log.d("FINAL_DEBUG", "Nhận labelAudio: " + labelAudio + " | scoreText: " + scoreText);

        calculateAndDisplay(labelAudio, scoreText);

        findViewById(R.id.btnFinish).setOnClickListener(v -> {
            Intent i = new Intent(KetQuaGhiAmActivity.this, TrangChuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }

    private void hideDetailViews() {
        try {
            View v1 = findViewById(R.id.txtKetQua1);
            if (v1 != null && v1.getParent() instanceof View) {
                ((View) v1.getParent()).setVisibility(View.GONE);
            }

            View v2 = findViewById(R.id.txtKetQua);
            if (v2 != null && v2.getParent() instanceof View) {
                ((View) v2.getParent()).setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.e("UI_ERROR", "Không thể ẩn các view chi tiết: " + e.getMessage());
        }
    }

    private void calculateAndDisplay(int labelAudio, int scoreText) {
        int scoreAudioEquivalent = (labelAudio == 1) ? 22 : 2;

        int finalScore = (scoreText + scoreAudioEquivalent) / 2;

        String levelFinal;
        int colorRes;
        String advice;

        if (finalScore <= 4) {
            levelFinal = "Bình thường";
            colorRes = android.R.color.holo_green_dark;
            advice = "Tâm trạng ổn định. Hãy duy trì lối sống lành mạnh và tích cực.";
        } else if (finalScore <= 9) {
            levelFinal = "Trầm cảm nhẹ";
            colorRes = android.R.color.holo_blue_dark;
            advice = "Bạn có dấu hiệu nhẹ. Hãy dành thời gian nghỉ ngơi và thư giãn nhiều hơn.";
        } else if (finalScore <= 14) {
            levelFinal = "Trầm cảm vừa phải";
            colorRes = android.R.color.holo_orange_dark;
            advice = "Bạn nên chú ý đến sức khỏe tâm thần và chia sẻ với người thân.";
        } else if (finalScore <= 19) {
            levelFinal = "Trầm cảm nghiêm trọng vừa phải";
            colorRes = android.R.color.holo_red_light;
            advice = "Bạn nên cân nhắc gặp chuyên gia tư vấn để được hỗ trợ kịp thời.";
        } else {
            levelFinal = "Trầm cảm nặng";
            colorRes = android.R.color.holo_red_dark;
            advice = "Cảnh báo mức độ nghiêm trọng. Bạn cần hỗ trợ y tế chuyên nghiệp ngay lập tức.";
        }

        txtKetQuaTongHop.setText("Kết quả chẩn đoán: " + levelFinal);
        txtKetQuaTongHop.setTextColor(ContextCompat.getColor(this, colorRes));

        txtMoTa.setText(advice);

        DataManager.saveTextScore(this, finalScore);
    }
}