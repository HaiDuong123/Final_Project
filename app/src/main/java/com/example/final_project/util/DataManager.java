package com.example.final_project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {

    private static final String PREF_NAME = "CareSenseData";


    public static void saveQuizScore(Context context, int score) {
        getPrefs(context).edit().putInt("S_QUIZ", score).apply();
    }

    public static void saveVoiceResult(Context context, int label) {
        getPrefs(context).edit().putInt("S_VOICE", label).apply();
    }

    public static void saveFaceResult(Context context, boolean isDepressed) {
        getPrefs(context).edit().putBoolean("S_FACE", isDepressed).apply();
    }


    public static int getQuizScore(Context context) {
        return getPrefs(context).getInt("S_QUIZ", 0);
    }

    public static int getVoiceResult(Context context) {
        return getPrefs(context).getInt("S_VOICE", -1);
    }

    public static boolean getFaceResult(Context context) {
        return getPrefs(context).getBoolean("S_FACE", false);
    }


    public static boolean isQuizCompleted(Context context) {
        return getPrefs(context).contains("S_QUIZ");
    }

    public static boolean isVoiceCompleted(Context context) {
        return getPrefs(context).contains("S_VOICE");
    }

    public static boolean isFaceCompleted(Context context) {
        return getPrefs(context).contains("S_FACE");
    }

    public static void clearAllData(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}