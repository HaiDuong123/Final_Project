package com.example.final_project.text;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteHelper {
    private Interpreter interpreter;
    private TokenizerHelper tokenizer;
    private static final int MAX_LEN = 120;

    private final String[] labels = {"mild", "minimal", "moderate", "normal", "severe"};

    public TFLiteHelper(Context context) {
        try {
            interpreter = new Interpreter(loadModelFile(context));
            tokenizer = new TokenizerHelper(context);
        } catch (Exception e) {
            Log.e("TFLITE", "❌ Init error", e);
        }
    }

    private MappedByteBuffer loadModelFile(Context context) throws Exception {
        AssetFileDescriptor fd = context.getAssets().openFd("depression_model.tflite");
        FileInputStream inputStream = new FileInputStream(fd.getFileDescriptor());
        FileChannel channel = inputStream.getChannel();
        return channel.map(FileChannel.MapMode.READ_ONLY, fd.getStartOffset(), fd.getDeclaredLength());
    }

    public String predict(String text) {
        if (interpreter == null || tokenizer == null) return "normal";

        try {
            String cleanText = text.toLowerCase().trim();

            if (cleanText.contains("tự tử") || cleanText.contains("muốn chết")) return "severe";
            if (cleanText.contains("vui") || cleanText.contains("ổn") || cleanText.contains("hạnh phúc")) return "normal";

            return runInference(cleanText);
        } catch (Exception e) {
            return "normal";
        }
    }

    private String runInference(String cleanText) {
        int[] sequence = tokenizer.textToSequence(cleanText, MAX_LEN);
        float[][] input = new float[1][MAX_LEN];
        for (int i = 0; i < MAX_LEN; i++) input[0][i] = (float) sequence[i];

        float[][] output = new float[1][5];
        interpreter.run(input, output);

        // Tìm nhãn có xác suất cao nhất
        int maxIndex = 0;
        float maxValue = -1f;
        for (int i = 0; i < 5; i++) {
            if (output[0][i] > maxValue) {
                maxValue = output[0][i];
                maxIndex = i;
            }
        }

        Log.d("AI_RESULT", "Dự đoán: " + labels[maxIndex] + " (" + (maxValue*100) + "%)");
        return labels[maxIndex];
    }
}