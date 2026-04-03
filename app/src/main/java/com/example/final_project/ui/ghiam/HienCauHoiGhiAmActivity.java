package com.example.final_project.ui.ghiam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.final_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HienCauHoiGhiAmActivity extends AppCompatActivity {

    // ================= UI =================
    private TextView txtCauHoi, txtKetQuaNoi, txtThoiGian;
    private ImageView btnVoice;
    private LinearLayout btnNext;

    // ================= SPEECH =================
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private boolean isListening = false;

    // ================= TIMER =================
    private Handler handler = new Handler();
    private long startTime;
    private long recordSeconds = 0;

    private static final int MIN_RECORD_TIME = 10;

    // ================= FILE =================
    private File txtFile;

    // ================= QUESTION =================
    private String question = "Trong 2 tuần gần đây, bạn có thường cảm thấy buồn bã, chán nản hoặc mất hứng thú với mọi việc không?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhcho_ghiam);

        requestPermission();
        initViews();
        initSpeech();
        initFile();

        showQuestion();

        btnVoice.setOnClickListener(v -> toggleSpeech());
        btnNext.setOnClickListener(v -> finishTest());
    }

    // ================= PERMISSION =================
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
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

    // ================= SAFE STOP =================
    private void forceStopRecording() {
        try {
            if (isListening && speechRecognizer != null) {
                speechRecognizer.stopListening();
            }
        } catch (Exception ignored) {}

        isListening = false;
        setRecordingUI(false);
        stopTimer();
    }

    // ================= SPEECH =================
    private void initSpeech() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                txtKetQuaNoi.setText("Đang nghe...");
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION);

                if (data != null && !data.isEmpty()) {
                    String text = data.get(0);
                    txtKetQuaNoi.setText(text);
                    saveTextToFile(text);
                }

                isListening = false;
                setRecordingUI(false);
                stopTimer();
            }

            @Override
            public void onError(int error) {
                txtKetQuaNoi.setText("Lỗi nhận diện giọng nói!");
                isListening = false;
                setRecordingUI(false);
                stopTimer();
            }

            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void toggleSpeech() {
        if (!isListening) {
            startListening();
        } else {
            forceStopRecording();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Chưa cấp quyền mic!", Toast.LENGTH_SHORT).show();
            return;
        }

        speechRecognizer.startListening(speechIntent);
        isListening = true;

        setRecordingUI(true);

        startTime = System.currentTimeMillis();
        startTimer();
    }

    // ================= UI =================
    private void setRecordingUI(boolean isRecording) {
        if (isRecording) {
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

    // ================= FILE =================
    private void initFile() {
        File dir = new File(getExternalFilesDir(null), "text");
        if (!dir.exists()) dir.mkdirs();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());

        txtFile = new File(dir, "speech_" + time + ".txt");
    }

    private void saveTextToFile(String text) {
        try (FileOutputStream fos = new FileOutputStream(txtFile, true)) {
            fos.write((text + "\n").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= QUESTION =================
    private void showQuestion() {
        txtCauHoi.setText(question);
        txtKetQuaNoi.setText("Nhấn nút để trả lời bằng giọng nói");
    }

    // ================= FINISH =================
    private void finishTest() {

        forceStopRecording();

        if (recordSeconds < MIN_RECORD_TIME) {
            Toast.makeText(this,
                    "Bạn phải nói ít nhất 10 giây!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String answer = txtKetQuaNoi.getText().toString();

        if (answer.isEmpty() || answer.contains("Nhấn nút")) {
            Toast.makeText(this, "Bạn chưa trả lời!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 👉 CHUYỂN MÀN HÌNH NGAY
        Intent intent = new Intent(HienCauHoiGhiAmActivity.this, NextStepGhiAmActivity.class);
        intent.putExtra("answer", answer);
        intent.putExtra("filePath", txtFile.getAbsolutePath());

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        forceStopRecording();

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}