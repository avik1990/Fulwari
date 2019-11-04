package com.app.mbazzar.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static boolean getisVerified(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("d_ride_later", false);
        return flag;
    }

    public static void setisVerified(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("d_ride_later", isVerified);
        editor.apply();
    }

    public static String get_userPhone(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_phone", "0");
        return a_key;
    }

    public static void set_userPhone(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_phone", a_key);
        editor.commit();
    }


    public static String get_firstName(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("fuser_name", "0");
        return a_key;
    }

    public static void set_firstuserName(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fuser_name", a_key);
        editor.commit();
    }



    public static String get_lastName(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("luser_name", "0");
        return a_key;
    }

    public static void set_lastName(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("luser_name", a_key);
        editor.commit();
    }



    public static String get_userEmail(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_Email", "0");
        return a_key;
    }

    public static void set_userEmail(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_Email", a_key);
        editor.commit();
    }

    public static String get_userId(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_ID", "0");
        return a_key;
    }

    public static void set_userId(Context mContext, String value) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_ID", value);
        editor.commit();
    }


    public static String get_UniqueId(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("_UniqueId", "0");
        return a_key;
    }

    public static void set_UniqueId(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_UniqueId", a_key);
        editor.commit();
    }

    public static boolean isgenerateUniqueKey(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("isCheckedOut", false);
        return flag;
    }

    public static void setUniqueKey(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isCheckedOut", isVerified);
        editor.apply();
    }

    public static String get_Zip(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("zip", "0");
        return a_key;
    }

    public static void set_Zip(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("zip", a_key);
        editor.commit();
    }


    public static String get_Cartount(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("cartcount", "0");
        return a_key;
    }

    public static void set_Cartount(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartcount", a_key);
        editor.commit();
    }



    public static String get_address(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("address", "0");
        return a_key;
    }

    public static void set_address(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", a_key);
        editor.commit();
    }


    public static String get_city(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("city", "0");
        return a_key;
    }

    public static void set_city(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", a_key);
        editor.commit();
    }

    public static String get_state(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("state", "0");
        return a_key;
    }

    public static void set_state(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state", a_key);
        editor.commit();
    }


    public static String get_checkClicked(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("checkClicked", "0");
        return a_key;
    }

    public static void set_checkClicked(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("checkClicked", a_key);
        editor.commit();
    }


    public static String get_dashCatId(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("dashCatId", "0");
        return a_key;
    }

    public static void set_dashCatId(Context mContext, String value) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dashCatId", value);
        editor.commit();
    }


    public static String get_contactNo(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_phone1231231", "0");
        return a_key;
    }

    public static void set_contactNo(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_phone1231231", a_key);
        editor.commit();
    }


}
