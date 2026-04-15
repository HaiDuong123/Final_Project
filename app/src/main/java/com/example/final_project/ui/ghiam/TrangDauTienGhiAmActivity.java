package com.example.final_project.ui.ghiam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.final_project.R;
import com.example.final_project.text.TFLiteHelper;

import java.util.ArrayList;
import java.util.Locale;

public class TrangDauTienGhiAmActivity extends AppCompatActivity {

    private TextView txtCauHoi, txtKetQuaNoi, txtThoiGian;
    private ImageView btnVoice, btnBack;
    private LinearLayout btnNext;

    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private boolean isListening = false;

    private StringBuilder fullTextBuilder = new StringBuilder();
    private String lastRecognizedText = "";

    private Handler handler = new Handler();
    private long startTime;

    private TFLiteHelper tfLiteHelper;

    private final String question =
            "Trong 2 tuần gần đây, bạn có thường cảm thấy buồn bã, chán nản hoặc mất hứng thú với mọi việc không?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhcho_ghiam);

        tfLiteHelper = new TFLiteHelper(this);

        requestPermission();
        initViews();
        initSpeech();

        setNextButtonEnabled(false);
        txtCauHoi.setText(question);

        btnVoice.setOnClickListener(v -> toggleSpeech());
        btnNext.setOnClickListener(v -> finishTest());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        txtCauHoi = findViewById(R.id.textcauhoighiam);
        txtKetQuaNoi = findViewById(R.id.txtKetQuaNoi);
        txtThoiGian = findViewById(R.id.textthoigianghiam);
        btnVoice = findViewById(R.id.btnbatdaughiam);
        btnNext = findViewById(R.id.btn_cautieptheo);
        btnBack = findViewById(R.id.btn_back);
    }

    private void initSpeech() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                txtKetQuaNoi.setText("Hệ thống đang nghe...");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> data =
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (data != null && !data.isEmpty()) {
                    String currentPartial = data.get(0);

                    String displayText = fullTextBuilder.toString() + " " + currentPartial;

                    txtKetQuaNoi.setText(displayText.trim());
                    lastRecognizedText = displayText.trim();
                }
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (data != null && !data.isEmpty()) {
                    String finalSegment = data.get(0);

                    if (!fullTextBuilder.toString().contains(finalSegment)) {
                        fullTextBuilder.append(finalSegment).append(" ");
                    }

                    lastRecognizedText = fullTextBuilder.toString().trim();
                    txtKetQuaNoi.setText(lastRecognizedText);
                }

                if (isListening) {
                    speechRecognizer.startListening(speechIntent);
                }
            }

            @Override
            public void onError(int error) {
                if (isListening) {
                    handler.postDelayed(() -> {
                        if (isListening) {
                            speechRecognizer.startListening(speechIntent);
                        }
                    }, 500);
                }
            }

            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void toggleSpeech() {
        if (!isListening) startListening();
        else stopListening();
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Vui lòng cấp quyền Mic!", Toast.LENGTH_SHORT).show();
            return;
        }

        isListening = true;
        fullTextBuilder.setLength(0);
        lastRecognizedText = "";

        speechRecognizer.startListening(speechIntent);
        btnVoice.setImageResource(R.drawable.dangghiam);

        setNextButtonEnabled(false);

        startTime = System.currentTimeMillis();
        handler.post(timerRunnable);
    }

    private void stopListening() {
        isListening = false;

        try {
            speechRecognizer.stopListening();
        } catch (Exception ignored) {}

        btnVoice.setImageResource(R.drawable.nutghiam);
        handler.removeCallbacks(timerRunnable);

        setNextButtonEnabled(true);

        if (lastRecognizedText.isEmpty()) {
            txtKetQuaNoi.setText("Nhấn nút để trả lời lại");
        }
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long sec = (System.currentTimeMillis() - startTime) / 1000;

            txtThoiGian.setText(String.format(Locale.getDefault(),
                    "%02d:%02d", sec / 60, sec % 60));

            handler.postDelayed(this, 1000);
        }
    };

    private void finishTest() {
        stopListening();

        String answer = lastRecognizedText.trim();

        if (answer.isEmpty() ||
                answer.equalsIgnoreCase("Hệ thống đang nghe...")) {

            Toast.makeText(this,
                    "Vui lòng trả lời câu hỏi!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String result = tfLiteHelper.predict(answer);
        int score = mapResultToScore(result);

        Log.d("AI_DEBUG", "Text: " + answer);
        Log.d("AI_DEBUG", "Label: " + result);
        Log.d("AI_DEBUG", "Score: " + score);

        Intent intent = new Intent(this, TrangTiepTheoGhiAmActivity.class);
        intent.putExtra("score_text", score);
        intent.putExtra("result_text", result);

        startActivity(intent);
        finish();
    }

    private int mapResultToScore(String result) {
        if (result == null) return 2;

        switch (result.toLowerCase()) {
            case "normal": return 2;
            case "minimal": return 7;
            case "mild": return 12;
            case "moderate": return 17;
            case "severe": return 22;
            default: return 2;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void setNextButtonEnabled(boolean enabled) {
        btnNext.setEnabled(enabled);
        btnNext.setAlpha(enabled ? 1f : 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (speechRecognizer != null) speechRecognizer.destroy();
        handler.removeCallbacks(timerRunnable);
    }
}