package com.nursinglab.booking.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceComponent {

    Context context;
    public static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String GET_DATA = "GetData";

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //LoginSession
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public SharedPreferenceComponent(Context context) {
        this.context = context;
    }

    //GetDataId
    public void setDataIn(String id) {
        SharedPreferences s = context.getSharedPreferences(GET_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("Id", id);
        editor.apply();
    }

    public String getDataId() {
        SharedPreferences s = context.getSharedPreferences(GET_DATA, Context.MODE_PRIVATE);
        return s.getString("Id", null);
    }

    public void setDataOut(){
        SharedPreferences s = context.getSharedPreferences(GET_DATA, Context.MODE_PRIVATE);
        s.edit().remove("Id").apply();
    }
}
