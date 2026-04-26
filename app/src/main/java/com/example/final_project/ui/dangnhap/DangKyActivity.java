package com.example.final_project.ui.dangnhap;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.data.model.Account;
import com.example.final_project.data.model.ApiResponse;
import com.example.final_project.data.repository.AccountRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {

    private EditText username, password, email;
    private LinearLayout btnRegister;
    private ImageView togglePassword;
    private ImageView btnBack;

    private AccountRepository repository;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        username = findViewById(R.id.user_name);
        password = findViewById(R.id.user_password);
        email = findViewById(R.id.user_email);
        btnRegister = findViewById(R.id.btn_register);

        // 👉 icon mắt (bạn thêm vào XML với id này)
        togglePassword = findViewById(R.id.toggle_password);

        repository = new AccountRepository();

        btnRegister.setOnClickListener(v -> register());

        setupTogglePassword();
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            finish(); // quay lại màn trước (ChonDangNhapActivity)
        });
    }

    // =========================
    // TOGGLE PASSWORD
    // =========================
    private void setupTogglePassword() {
        togglePassword.setOnClickListener(v -> {

            if (isPasswordVisible) {
                // 👉 Ẩn password
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                // 👉 Hiện password
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            // giữ con trỏ ở cuối
            password.setSelection(password.getText().length());

            isPasswordVisible = !isPasswordVisible;
        });
    }

    // =========================
    // REGISTER
    // =========================
    private void register() {

        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String mail = email.getText().toString().trim();

        // ===== 1. CHECK RỖNG =====
        if (user.isEmpty()) {
            username.setError("Không được để trống username");
            return;
        }

        if (pass.isEmpty()) {
            password.setError("Không được để trống password");
            return;
        }

        if (mail.isEmpty()) {
            email.setError("Không được để trống email");
            return;
        }

        // ===== 2. USERNAME PHẢI CÓ CHỮ =====
        if (!user.matches(".*[a-zA-Z].*")) {
            username.setError("Username phải có chữ");
            return;
        }

        // ===== 3. PASSWORD =====
        if (pass.length() < 8) {
            password.setError("Password phải ít nhất 8 ký tự");
            return;
        }

        if (!pass.matches(".*[A-Z].*")) {
            password.setError("Phải có chữ in hoa");
            return;
        }

        if (!pass.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~-].*")) {
            password.setError("Phải có ký tự đặc biệt");
            return;
        }

        // ===== 4. EMAIL =====
        if (!mail.endsWith("@gmail.com")) {
            email.setError("Email phải là @gmail.com");
            return;
        }

        // ===== CALL API =====
        Account account = new Account(user, pass, mail);

        repository.register(account).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ApiResponse res = response.body();

                    if (res.isOk()) {

                        Toast.makeText(DangKyActivity.this,
                                "Đăng ký thành công",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(
                                DangKyActivity.this,
                                DangNhapActivity.class
                        ));
                        finish();

                    } else {
                        // 🔥 username đã tồn tại từ MongoDB
                        Toast.makeText(DangKyActivity.this,
                                "Vui lòng chọn tên tài khoản khác",
                                Toast.LENGTH_SHORT).show();

                        clearAllFields();
                    }

                } else {
                    Toast.makeText(DangKyActivity.this,
                            "Lỗi server",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                Toast.makeText(DangKyActivity.this,
                        "Không kết nối được server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =========================
    // CLEAR FIELD
    // =========================
    private void clearAllFields() {
        username.setText("");
        password.setText("");
        email.setText("");
    }
}