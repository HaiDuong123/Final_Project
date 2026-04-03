package com.example.final_project.ui.dangnhap;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.data.model.ApiResponse;
import com.example.final_project.data.network.ApiService;
import com.example.final_project.data.network.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauActivity extends AppCompatActivity {

    private EditText edtUsername, edtNewPassword;
    private LinearLayout btnSend;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhau);

        edtUsername = findViewById(R.id.user_name);
        edtNewPassword = findViewById(R.id.user_new_password);
        btnSend = findViewById(R.id.btn_send);

        apiService = RetrofitClient
                .getInstance()
                .create(ApiService.class);

        btnSend.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {

        String username = edtUsername.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();

        if (username.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 ĐÚNG KEY THEO BACKEND
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("newPassword", newPassword);

        apiService.changePassword(body).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(QuenMatKhauActivity.this,
                            "Lỗi server",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiResponse res = response.body();

                if (res.isOk()) {

                    Toast.makeText(QuenMatKhauActivity.this,
                            "Đổi mật khẩu thành công",
                            Toast.LENGTH_SHORT).show();

                    finish();

                } else {

                    Toast.makeText(QuenMatKhauActivity.this,
                            res.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(QuenMatKhauActivity.this,
                        "Không kết nối được server: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}