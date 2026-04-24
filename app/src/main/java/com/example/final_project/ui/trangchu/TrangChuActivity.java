package com.example.final_project.ui.trangchu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.tracnghiem.BatDauTracNghiemActivity;
import com.example.final_project.ui.hinhanh.BatDauHinhAnhActivity;
import com.example.final_project.util.DataManager;

// ⭐ THÊM
import com.example.final_project.data.repository.AccountRepository;
import com.example.final_project.data.model.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuActivity extends AppCompatActivity {

    private ImageView btnTracNghiem, btnHinhAnh;
    private TextView txtHello, txtFinalResult;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        initViews();

        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "User";
        }

        txtHello.setText("Chào, " + username);

        setupNavigation();
        calculateAndShowFinalScore();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        String newUsername = intent.getStringExtra("username");
        if (newUsername != null && !newUsername.isEmpty()) {
            username = newUsername;
            txtHello.setText("Chào, " + username);
        }
    }

    private void initViews() {
        btnTracNghiem = findViewById(R.id.btn_tracnghiem);
        btnHinhAnh = findViewById(R.id.btn_hinhanh);
        txtHello = findViewById(R.id.txtHello);
        txtFinalResult = findViewById(R.id.txtFinalResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateAndShowFinalScore();
    }

    private void calculateAndShowFinalScore() {
        boolean completedAll = DataManager.isQuizCompleted(this) &&
                DataManager.isVoiceCompleted(this) &&
                DataManager.isFaceCompleted(this);

        if (completedAll) {
            double sQuiz = DataManager.getQuizScore(this) / 24.0;
            double sVoice = (DataManager.getVoiceResult(this) == -1) ? 0.0 :
                    (DataManager.getVoiceResult(this) == 1 ? 1.0 : 0.0);
            double sFace = DataManager.getFaceResult(this) ? 1.0 : 0.0;

            double sFinal = (0.50 * sQuiz) + (0.30 * sVoice) + (0.20 * sFace);
            int finalScore = (int) Math.round(sFinal * 24);

            String level;
            if (finalScore <= 4) level = "Bình thường";
            else if (finalScore <= 9) level = "Nhẹ";
            else if (finalScore <= 14) level = "Vừa";
            else if (finalScore <= 19) level = "Nặng vừa";
            else level = "Nặng";

            txtFinalResult.setText(finalScore + " điểm - " + level);
            txtFinalResult.setTextColor(Color.WHITE);
            txtFinalResult.setTextSize(24);
            txtFinalResult.setTypeface(null, Typeface.BOLD);
            txtFinalResult.setAlpha(1.0f);

            // ⭐⭐ THÊM ĐOẠN NÀY: GỌI API LƯU KẾT QUẢ ⭐⭐
            saveResultToServer(finalScore, level);

        } else {
            String title = "Đang cập nhật...\n";
            String subTitle = "Hoàn thành đủ 3 bài test để xem đánh giá";

            SpannableStringBuilder builder = new SpannableStringBuilder();

            int startTitle = builder.length();
            builder.append(title);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCC00")), startTitle, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), startTitle, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            int startSub = builder.length();
            builder.append(subTitle);
            builder.setSpan(new ForegroundColorSpan(Color.WHITE), startSub, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new RelativeSizeSpan(0.8f), startSub, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            txtFinalResult.setText(builder);
            txtFinalResult.setTextSize(18);
            txtFinalResult.setLineSpacing(12f, 1.2f);
            txtFinalResult.setAlpha(1.0f);
        }
    }

    // ⭐ HÀM GỬI API
    private void saveResultToServer(int finalScore, String level) {
        AccountRepository repo = new AccountRepository();

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("finalScore", finalScore);
        body.put("level", level);

        repo.updateResult(body).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lưu thành công
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // lỗi mạng bỏ qua
            }
        });
    }

    private void setupNavigation() {
        btnTracNghiem.setOnClickListener(v -> {
            Intent intent = new Intent(this, BatDauTracNghiemActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnHinhAnh.setOnClickListener(v -> {
            Intent intent = new Intent(this, BatDauHinhAnhActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}