package com.example.autoinvest;

import android.content.Context;
import android.content.SharedPreferences;


public class Constants {
    public static final String WEB_URL = "http://192.168.2.10:8000";

    private static String sessionID;
    public static String getSessionID(){
        return sessionID;
    }
}
