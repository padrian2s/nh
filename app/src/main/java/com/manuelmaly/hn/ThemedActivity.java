package com.manuelmaly.hn;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.manuelmaly.hn.util.ThemeHelper;

/**
 * Applies the user's color theme and recreates itself when it changes.
 */
public class ThemedActivity extends AppCompatActivity {

    private String mTheme;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mThemeCheck = new Runnable() {
        @Override
        public void run() {
            checkThemeChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTheme = Settings.getTheme(this);
        ThemeHelper.applyPalette(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // The back arrow is a dark bitmap; tint it to match dark action bars
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && ThemeHelper.isDarkTheme(this)) {
            Drawable back = getResources().getDrawable(R.drawable.back, getTheme()).mutate();
            back.setTint(ThemeHelper.getColor(this, R.attr.hnActionBarText));
            actionBar.setHomeAsUpIndicator(back);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkThemeChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mThemeCheck);
    }

    private void checkThemeChanged() {
        if (!mTheme.equals(Settings.getTheme(this))) {
            recreate();
            return;
        }
        // Switch live if the app stays open across the scheduled time
        mHandler.removeCallbacks(mThemeCheck);
        mHandler.postDelayed(mThemeCheck, Settings.getMillisUntilThemeSwitch() + 1000);
    }
}
