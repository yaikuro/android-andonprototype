package com.app.andonprototype.Background;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentActivity;

import static android.content.SharedPreferences.*;

public class SaveSharedPreference {
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_ID = "ID";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public static void setID(Context ctx, String ID) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID, ID);
        editor.apply();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getID(FragmentActivity ctx) {
        return getSharedPreferences(ctx).getString(PREF_ID, "");
    }

    public static void clearUserName(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
