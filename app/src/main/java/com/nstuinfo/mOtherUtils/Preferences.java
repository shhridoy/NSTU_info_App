package com.nstuinfo.mOtherUtils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by whoami on 10/30/2018.
 */

public class Preferences {

    public static final String THEME = "Theme";

    public static void setDarkTheme(Context context, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(THEME, b)
                .apply();
    }

    public static boolean isDarkTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(THEME, false);
    }
}
