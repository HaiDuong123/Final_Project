package com.example.final_project.ui.trangchu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.ui.tracnghiem.BatDauTracNghiemActivity;
import com.example.final_project.ui.hinhanh.BatDauHinhAnhActivity;
import com.example.final_project.util.DataManager;

import com.example.final_project.data.repository.AccountRepository;
import com.example.final_project.data.model.Account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuActivity extends AppCompatActivity {

    private ImageView btnTracNghiem, btnHinhAnh;
    private ImageView imgAvatar;

    private TextView txtHello, txtFinalResult;

    private String username;
    private AccountRepository repo;

    private Integer serverScore = null;
    private String serverLevel = null;
    private Date lastTestTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        initViews();

        repo = new AccountRepository();

        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "User";
        }

        txtHello.setText("Chào, " + username);

        setupNavigation();
        setupAvatarMenu();

        loadUserFromServer();
    }

    private void initViews() {
        btnTracNghiem = findViewById(R.id.btn_tracnghiem);
        btnHinhAnh = findViewById(R.id.btn_hinhanh);
        txtHello = findViewById(R.id.txtHello);
        txtFinalResult = findViewById(R.id.txtFinalResult);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserFromServer();
    }

    // =========================
    // LOAD USER FROM MONGO
    // =========================
    private void loadUserFromServer() {

        repo.getUser(username).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Account user = response.body();

                    serverScore = user.getFinalScore();
                    serverLevel = user.getLevel();
                    lastTestTime = user.getLastTestTime(); // ⭐ LẤY TIME

                    if (serverScore != null && serverLevel != null) {
                        showServerResult();
                    } else {
                        calculateAndSaveLocalResult();
                    }

                } else {
                    calculateAndSaveLocalResult();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                calculateAndSaveLocalResult();
            }
        });
    }

    // =========================
    // FORMAT DATE
    // =========================
    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    // =========================
    // HIỂN THỊ SERVER RESULT
    // =========================
    private void showServerResult() {

        String result = serverScore + " điểm - " + serverLevel;

        if (lastTestTime != null) {
            result += "\nNgày test: " + formatDate(lastTestTime);
        }

        txtFinalResult.setText(result);
        txtFinalResult.setTextColor(Color.WHITE);
        txtFinalResult.setTypeface(null, Typeface.BOLD);
        txtFinalResult.setTextSize(22);
    }

    // =========================
    // TÍNH LOCAL + LƯU SERVER
    // =========================
    private void calculateAndSaveLocalResult() {

        boolean completedAll = DataManager.isQuizCompleted(this) &&
                DataManager.isVoiceCompleted(this) &&
                DataManager.isFaceCompleted(this);

        if (!completedAll) {
            txtFinalResult.setText("Chưa có kết quả test");
            return;
        }

        double sQuiz = DataManager.getQuizScore(this) / 24.0;
        double sVoice = (DataManager.getVoiceResult(this) == 1) ? 1.0 : 0.0;
        double sFace = DataManager.getFaceResult(this) ? 1.0 : 0.0;

        double sFinal = (0.50 * sQuiz) + (0.30 * sVoice) + (0.20 * sFace);
        int finalScore = (int) Math.round(sFinal * 24);

        String level;
        if (finalScore <= 4) level = "Bình thường";
        else if (finalScore <= 9) level = "Nhẹ";
        else if (finalScore <= 14) level = "Vừa";
        else if (finalScore <= 19) level = "Nặng vừa";
        else level = "Nặng";

        String result = finalScore + " điểm - " + level +
                "\nNgày test: " + formatDate(new Date());

        txtFinalResult.setText(result);
        txtFinalResult.setTextColor(Color.WHITE);
        txtFinalResult.setTypeface(null, Typeface.BOLD);

        saveResultToServer(finalScore, level);
    }

    // =========================
    // UPDATE SERVER
    // =========================
    private void saveResultToServer(int finalScore, String level) {

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("finalScore", finalScore);
        body.put("level", level);

        repo.updateResult(body).enqueue(new Callback<com.example.final_project.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.final_project.data.model.ApiResponse> call,
                                   Response<com.example.final_project.data.model.ApiResponse> response) {
            }

            @Override
            public void onFailure(Call<com.example.final_project.data.model.ApiResponse> call, Throwable t) {
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

    // =========================
    // POPUP AVATAR
    // =========================
    private void setupAvatarMenu() {

        imgAvatar.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(
                    TrangChuActivity.this,
                    imgAvatar,
                    0,
                    0,
                    R.style.PopupMenuStyle
            );

            popupMenu.getMenuInflater().inflate(R.menu.menu_avatar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_logout) {
                    logout();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

    private void logout() {
        Intent intent = new Intent(this,
                com.example.final_project.ui.dangnhap.DangNhapActivity.class);
        startActivity(intent);
        finish();
    }
}