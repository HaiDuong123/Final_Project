package com.example.final_project.ui.hinhanh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
// NẾU FILE TRANG CHỦ CỦA BẠN TÊN LÀ TrangChuActivity THÌ IMPORT NÓ VÀO ĐÂY:
// import com.example.final_project.ui.trangchu.TrangChuActivity;

public class KetQuaHinhAnhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua_hinhanh);

        // Ánh xạ các thành phần dựa theo ID trong file XML của bạn
        TextView txtKetQua = findViewById(R.id.rxkmwbhgwgz);
        TextView txtMoTa = findViewById(R.id.rrk64zpie7uj);
        LinearLayout btnKetThuc = findViewById(R.id.raf9m3etc9f6);

        // 1. Nhận kết quả từ trang chụp ảnh gửi sang
        boolean isDepressed = getIntent().getBooleanExtra("isDepressed", false);

        // 2. Hiển thị chữ và màu sắc tương ứng
        if (isDepressed) {
            txtKetQua.setText("CÓ DẤU HIỆU TRẦM CẢM");
            txtKetQua.setTextColor(Color.parseColor("#FF0000")); // Màu Đỏ
            txtMoTa.setText("Bạn nên trò chuyện với bác sĩ hoặc chuyên gia tâm lý để được hỗ trợ kịp thời.");
        } else {
            txtKetQua.setText("TÂM TRẠNG BÌNH THƯỜNG");
            txtKetQua.setTextColor(Color.parseColor("#008000")); // Màu Xanh lá
            txtMoTa.setText("Tình trạng tinh thần của bạn hiện tại khá ổn định. Hãy tiếp tục duy trì lối sống tích cực nhé!");
        }

        // 3. Xử lý nút KẾT THÚC -> Về Trang Chủ
        btnKetThuc.setOnClickListener(v -> {
            // LƯU Ý: Đổi MainActivity.class thành tên Activity Trang chủ thực sự của bạn (VD: TrangChuActivity.class)
            Intent intent = new Intent(KetQuaHinhAnhActivity.this, com.example.final_project.ui.trangchu.TrangChuActivity.class);

            // Xóa toàn bộ các màn hình cũ đi để ấn nút Back không bị quay lại trang kết quả
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}