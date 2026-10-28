package com.manuelmaly.hn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class Settings {
    
    public static final String PREF_FONTSIZE = "pref_fontsize";
    public static final String PREF_HTMLPROVIDER = "pref_htmlprovider";
    public static final String PREF_HTMLVIEWER = "pref_htmlviewer";
    public static final String PREF_USER = "pref_user";
    public static final String PREF_REPORTING = "pref_crashlytics";
    public static final String PREF_PULLDOWNREFRESH = "pref_pulldownrefresh";
    public static final String PREF_THEME = "pref_theme";
    public static final String PREF_THEME_PERIOD = "pref_theme_period";
    public static final String PREF_AUTOTHEME = "pref_autotheme";

    // With automatic theme on, between NIGHT_START_HOUR and NIGHT_END_HOUR the
    // theme is reset to Gruvbox Dark, the rest of the day to Classic. A manual
    // choice holds until the next switch.
    public static final int NIGHT_START_HOUR = 0;
    public static final int NIGHT_END_HOUR = 8;
    private static final String PERIOD_NIGHT = "night";
    private static final String PERIOD_DAY = "day";
    
    public static final String USER_DATA_SEPARATOR = ":";
    
    public static String getFontSize(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getString(PREF_FONTSIZE, c.getString(R.string.pref_default_fontsize));
    }

    public static String getHtmlProvider(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getString(PREF_HTMLPROVIDER, c.getString(R.string.pref_default_htmlprovider));
    }

    public static String getHtmlViewer(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getString(PREF_HTMLVIEWER, c.getString(R.string.pref_default_htmlviewer));
    }

    public static String getTheme(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        String period = isNight() ? PERIOD_NIGHT : PERIOD_DAY;
        if (isAutoTheme(c) && !period.equals(sharedPref.getString(PREF_THEME_PERIOD, null))) {
            String scheduledTheme = c.getString(period.equals(PERIOD_NIGHT)
                    ? R.string.pref_theme_gruvbox : R.string.pref_theme_classic);
            sharedPref.edit().putString(PREF_THEME, scheduledTheme)
                    .putString(PREF_THEME_PERIOD, period).commit();
            return scheduledTheme;
        }
        return sharedPref.getString(PREF_THEME, c.getString(R.string.pref_default_theme));
    }

    public static boolean isAutoTheme(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getBoolean(PREF_AUTOTHEME,
                c.getResources().getBoolean(R.bool.pref_default_autotheme));
    }

    /** Makes the automatic theme apply the current period's theme right away. */
    public static void resetThemePeriod(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        sharedPref.edit().remove(PREF_THEME_PERIOD).commit();
    }

    public static boolean isNight() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour >= NIGHT_START_HOUR && hour < NIGHT_END_HOUR;
    }

    /** Milliseconds until the scheduled theme next switches. */
    public static long getMillisUntilThemeSwitch() {
        Calendar now = Calendar.getInstance();
        Calendar next = (Calendar) now.clone();
        next.set(Calendar.HOUR_OF_DAY, isNight() ? NIGHT_END_HOUR : NIGHT_START_HOUR);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        if (!next.after(now)) {
            next.add(Calendar.DAY_OF_MONTH, 1);
        }
        return next.getTimeInMillis() - now.getTimeInMillis();
    }

    public static boolean isPullDownRefresh(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getBoolean(PREF_PULLDOWNREFRESH, false);
    }

    public static boolean isUserLoggedIn(Context c) {
        return !getUserName(c).equals("");
        
    }
    
    public static String getUserName(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        String[] userData = sharedPref.getString(PREF_USER, "").split(USER_DATA_SEPARATOR);
        if (userData.length > 0)
            return userData[0];
        return null;
    }
    
    public static String getUserToken(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        String[] userData = sharedPref.getString(PREF_USER, "").split(USER_DATA_SEPARATOR);
        if (userData.length > 1)
            return userData[1];
        return null;
    }

    public static void setUserData(String userName, String userToken, Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        sharedPref.edit().putString(PREF_USER, userName + USER_DATA_SEPARATOR + userToken).commit();
    }

    public static void clearUserData(Context c) {
      SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
      sharedPref.edit().remove(PREF_USER).commit();
    }

}
