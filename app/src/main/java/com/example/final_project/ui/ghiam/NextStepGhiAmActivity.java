package com.example.final_project.ui.ghiam;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.final_project.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NextStepGhiAmActivity extends AppCompatActivity {

    // ================= UI =================
    private TextView txtCauHoi, txtKetQuaNoi, txtThoiGian;
    private ImageView btnVoice;
    private LinearLayout btnNext;

    // ================= RECORD =================
    private MediaRecorder recorder;
    private boolean isRecording = false;

    // ================= TIMER =================
    private Handler handler = new Handler();
    private long startTime;
    private long recordSeconds = 0;

    private static final int MIN_RECORD_TIME = 10;

    // ================= FILE =================
    private File audioFile;

    // ================= QUESTION =================
    private String question = "Bạn có gặp khó khăn trong việc ngủ hoặc ngủ không sâu giấc trong thời gian gần đây không?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhcho_ghiam_2);

        requestPermission();
        initViews();
        initFile();

        showQuestion();

        btnVoice.setOnClickListener(v -> toggleRecording());
        btnNext.setOnClickListener(v -> finishTest());
    }

    // ================= PERMISSION =================
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1);
    }

    // ================= INIT =================
    private void initViews() {
        txtCauHoi = findViewById(R.id.textcauhoighiam);
        txtKetQuaNoi = findViewById(R.id.txtKetQuaNoi);
        txtThoiGian = findViewById(R.id.textthoigianghiam);
        btnVoice = findViewById(R.id.btnbatdaughiam);
        btnNext = findViewById(R.id.btn_cautieptheo);
    }

    // ================= FILE =================
    private void initFile() {
        File dir = new File(getExternalFilesDir(null), "audio");
        if (!dir.exists()) dir.mkdirs();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());

        audioFile = new File(dir, "record_" + time + ".mp3");
    }

    // ================= RECORD =================
    private void toggleRecording() {
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Chưa cấp quyền mic!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audioFile.getAbsolutePath());

            recorder.prepare();
            recorder.start();

            isRecording = true;
            setRecordingUI(true);

            startTime = System.currentTimeMillis();
            startTimer();

            txtKetQuaNoi.setText("Đang ghi âm...");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi ghi âm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
        } catch (Exception ignored) {}

        isRecording = false;
        setRecordingUI(false);
        stopTimer();

        txtKetQuaNoi.setText("Đã ghi âm xong!");
    }

    private void forceStopRecording() {
        if (isRecording) {
            stopRecording();
        }
    }

    // ================= UI =================
    private void setRecordingUI(boolean recording) {
        if (recording) {
            btnVoice.setImageResource(R.drawable.dangghiam);
        } else {
            btnVoice.setImageResource(R.drawable.nutghiam);
        }
    }

    // ================= TIMER =================
    private void startTimer() {
        handler.post(timerRunnable);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
        txtThoiGian.setText("00:00");
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            recordSeconds = (System.currentTimeMillis() - startTime) / 1000;

            txtThoiGian.setText(String.format("%02d:%02d",
                    recordSeconds / 60, recordSeconds % 60));

            handler.postDelayed(this, 1000);
        }
    };

    // ================= QUESTION =================
    private void showQuestion() {
        txtCauHoi.setText(question);
        txtKetQuaNoi.setText("Nhấn nút để ghi âm câu trả lời");
    }

    // ================= FINISH =================
    private void finishTest() {

        forceStopRecording();

        if (recordSeconds < MIN_RECORD_TIME) {
            Toast.makeText(this,
                    "Bạn phải ghi âm ít nhất 10 giây!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!audioFile.exists()) {
            Toast.makeText(this, "Chưa có file ghi âm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Đã hoàn thành!\nFile lưu tại:\n" + audioFile.getAbsolutePath(),
                Toast.LENGTH_LONG).show();

        // 👉 Nếu bạn muốn đi tiếp nữa thì thêm Intent ở đây
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        forceStopRecording();
    }
}