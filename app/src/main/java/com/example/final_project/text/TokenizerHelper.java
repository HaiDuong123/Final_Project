package com.example.final_project.text;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

public class TokenizerHelper {
    private HashMap<String, Integer> wordIndex = new HashMap<>();
    private int oovIndex = 1;

    public TokenizerHelper(Context context) {
        try {
            InputStream is = context.getAssets().open("tokenizer.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);

            // Lấy đúng cấu trúc word_index từ file export của Keras
            JSONObject config = obj.getJSONObject("config");
            String wordIndexStr = config.getString("word_index");
            JSONObject word_index = new JSONObject(wordIndexStr);

            Iterator<String> keys = word_index.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int value = word_index.getInt(key);
                wordIndex.put(key, value);
            }
            Log.d("TOKENIZER", "✅ Loaded vocab size: " + wordIndex.size());
        } catch (Exception e) {
            Log.e("TOKENIZER", "❌ Load tokenizer failed", e);
        }
    }

    public int[] textToSequence(String text, int maxLen) {
        String cleanText = text.toLowerCase()
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ") // Giữ được chữ cái có dấu tiếng Việt
                .replaceAll("\\s+", " ")
                .trim();

        String[] words = cleanText.split("\\s+");
        int[] sequence = new int[maxLen];

        int start = Math.max(0, maxLen - words.length);
        int wordOffset = Math.max(0, words.length - maxLen); // Nếu câu dài hơn maxLen

        for (int i = 0; i < Math.min(words.length, maxLen); i++) {
            sequence[start + i] = wordIndex.getOrDefault(words[wordOffset + i], oovIndex);
        }

        Log.d("SEQ_DEBUG", "Sequence: " + java.util.Arrays.toString(sequence));
        return sequence;
    }
}