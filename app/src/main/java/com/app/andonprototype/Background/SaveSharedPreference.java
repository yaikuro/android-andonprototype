package com.app.andonprototype.Background;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentActivity;

import static android.content.SharedPreferences.*;

// Menyimpan data-data seperti Npk dan Nama user di dalam aplikasi

public class SaveSharedPreference {
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_ID = "ID";
    private static final String PREF_NAMA = "Nama";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setID(Context ctx, String ID) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID, ID);
        editor.apply();
    }

    public static void setNama(Context ctx, String ID) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NAMA, ID);
        editor.apply();
    }

    public static String getID(FragmentActivity ctx) {
        return getSharedPreferences(ctx).getString(PREF_ID, "");
    }

    public static String getNama(FragmentActivity ctx) {
        return getSharedPreferences(ctx).getString(PREF_NAMA, "");
    }

    public static void clearUserName(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
