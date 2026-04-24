package com.example.final_project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {

    private static final String PREF_NAME = "CareSenseData";

    // =========================
    // CURRENT USER
    // =========================
    public static void setCurrentUser(Context context, String username) {
        getPrefs(context).edit().putString("CURRENT_USER", username).apply();
    }

    public static String getCurrentUser(Context context) {
        return getPrefs(context).getString("CURRENT_USER", "default");
    }

    // =========================
    // KEY theo từng user
    // =========================
    private static String key(Context context, String base) {
        String user = getCurrentUser(context);
        return user + "_" + base;
    }

    // =========================
    // SAVE DATA
    // =========================
    public static void saveQuizScore(Context context, int score) {
        getPrefs(context).edit()
                .putInt(key(context, "QUIZ"), score)
                .apply();
    }

    public static void saveVoiceResult(Context context, int label) {
        getPrefs(context).edit()
                .putInt(key(context, "VOICE"), label)
                .apply();
    }

    public static void saveFaceResult(Context context, boolean isDepressed) {
        getPrefs(context).edit()
                .putBoolean(key(context, "FACE"), isDepressed)
                .apply();
    }

    // =========================
    // GET DATA
    // =========================
    public static int getQuizScore(Context context) {
        return getPrefs(context).getInt(key(context, "QUIZ"), 0);
    }

    public static int getVoiceResult(Context context) {
        return getPrefs(context).getInt(key(context, "VOICE"), -1);
    }

    public static boolean getFaceResult(Context context) {
        return getPrefs(context).getBoolean(key(context, "FACE"), false);
    }

    // =========================
    // CHECK COMPLETED
    // =========================
    public static boolean isQuizCompleted(Context context) {
        return getPrefs(context).contains(key(context, "QUIZ"));
    }

    public static boolean isVoiceCompleted(Context context) {
        return getPrefs(context).contains(key(context, "VOICE"));
    }

    public static boolean isFaceCompleted(Context context) {
        return getPrefs(context).contains(key(context, "FACE"));
    }

    // =========================
    // CLEAR DATA (logout / đổi user)
    // =========================
    public static void clearAllData(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    // =========================
    // PREF
    // =========================
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}