/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:14 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:38 PM
 *
 */

package com.pebertli.aequilibrium.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Singleton for the shared preferences
 */
public class Session
{
    private static final Session instance = new Session();

    public static Session getInstance()
    {
        return instance;
    }

    private SharedPreferences prefs;
    public void init(Context context){
        prefs = context.getSharedPreferences("com.pebertli.aequilibrium", Context.MODE_PRIVATE);
    }

    //
    public String getSessionStringValue(String key)
    {
        return prefs.getString(key, null);
    }

    public void setSessionStringValue(String key, String value)
    {
        prefs.edit().putString(key, value).apply();
    }
}
