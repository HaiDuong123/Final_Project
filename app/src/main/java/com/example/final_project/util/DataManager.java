package com.example.final_project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {

    private static final String PREF_NAME = "CareSenseData";


    public static void saveQuizScore(Context context, int score) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt("S_QUIZ", score).apply();
    }


    public static void saveTextScore(Context context, int score) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt("S_TEXT", score).apply();
    }


    public static void saveVoiceResult(Context context, int label) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt("S_VOICE", label).apply();
    }


    public static void saveFaceResult(Context context, boolean isDepressed) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean("S_FACE", isDepressed).apply();
    }


    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}