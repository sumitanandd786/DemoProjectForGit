package com.demoproject.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences SharedPref;
    private SharedPreferences.Editor SharedPref_Editor;

    @SuppressLint(value={"CommitPrefEdits"})
    public SessionManager(Context context) {
        this.SharedPref = context.getSharedPreferences("com.demoproject.database.PREFERENCE.", 0);
        this.SharedPref_Editor = this.SharedPref.edit();
    }

    public void clearPreferences() {
        this.SharedPref_Editor.clear();
        this.SharedPref_Editor.commit();
    }

    public String getSessionData(String string2) {
        return this.SharedPref.getString(string2, "");
    }

    public void saveSession(String string2, String string3) {
        this.SharedPref_Editor.putString(string2, string3);
        this.SharedPref_Editor.commit();
    }
}

