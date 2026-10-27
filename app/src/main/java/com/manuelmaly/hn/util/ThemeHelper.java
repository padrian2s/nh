package com.manuelmaly.hn.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.manuelmaly.hn.R;
import com.manuelmaly.hn.Settings;

public class ThemeHelper {

    public static int getPaletteStyle(Context c) {
        String theme = Settings.getTheme(c);
        if (theme.equals(c.getString(R.string.pref_theme_tokyonight)))
            return R.style.Palette_Dark_TokyoNight;
        if (theme.equals(c.getString(R.string.pref_theme_tokyonight_storm)))
            return R.style.Palette_Dark_TokyoNightStorm;
        if (theme.equals(c.getString(R.string.pref_theme_dracula)))
            return R.style.Palette_Dark_Dracula;
        if (theme.equals(c.getString(R.string.pref_theme_catppuccin)))
            return R.style.Palette_Dark_CatppuccinMocha;
        if (theme.equals(c.getString(R.string.pref_theme_gruvbox)))
            return R.style.Palette_Dark_GruvboxDark;
        if (theme.equals(c.getString(R.string.pref_theme_nord)))
            return R.style.Palette_Dark_Nord;
        if (theme.equals(c.getString(R.string.pref_theme_onedark)))
            return R.style.Palette_Dark_OneDark;
        if (theme.equals(c.getString(R.string.pref_theme_amoled)))
            return R.style.Palette_Dark_AmoledBlack;
        return R.style.Palette_Classic;
    }

    public static boolean isDarkTheme(Context c) {
        return getPaletteStyle(c) != R.style.Palette_Classic;
    }

    /**
     * Applies the selected color palette on top of the activity's theme. Must
     * be called before the content view is inflated.
     */
    public static void applyPalette(Activity a) {
        a.getTheme().applyStyle(getPaletteStyle(a), true);
    }

    public static int getColor(Context c, int attr) {
        TypedValue value = new TypedValue();
        c.getTheme().resolveAttribute(attr, value, true);
        return value.data;
    }

    /**
     * HN fades downvoted comments from black to light gray. In dark themes,
     * map that fade onto the palette's text -> background range instead.
     */
    public static int getCommentColor(Context c, int hnColor) {
        if (!isDarkTheme(c))
            return hnColor;
        int text = getColor(c, R.attr.hnCommentText);
        int bg = getColor(c, R.attr.hnBg);
        float fade = Color.red(hnColor) / 255f;
        return Color.rgb(
                (int) (Color.red(text) + (Color.red(bg) - Color.red(text)) * fade),
                (int) (Color.green(text) + (Color.green(bg) - Color.green(text)) * fade),
                (int) (Color.blue(text) + (Color.blue(bg) - Color.blue(text)) * fade));
    }
}
