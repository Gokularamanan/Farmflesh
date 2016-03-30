package in.farmflesh.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by RBP687 on 3/29/2016.
 */
public class SharedPreferenceFarm {
    public static final String PREFS_NAME = "FARM_PREFS";
    public static final String PREFS_KEY_EMAIL = "FARM_PREFS_EMAIL";
    public static final String PREFS_KEY_NAME = "FARM_PREFS_NAME";
    public static final String PREFS_KEY_IS_GMAIL_REG = "FARM_PREFS_IS_GMAIL_REG";
    public static final String PREFS_KEY_IS_GCM_REG = "FARM_PREFS_IS_GCM_REG";

    public SharedPreferenceFarm() {
        super();
    }


    public void saveStringPrefByKey(Context context, String prefsKey, String text) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(prefsKey, text); //3

        editor.commit(); //4
    }

    public String getStringPrefByKey(Context context, String prefsKey) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(prefsKey, null);
        return text;
    }

    public void saveBooleanPrefByKey(Context context, String prefsKey, Boolean value) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putBoolean(prefsKey, value); //3

        editor.commit(); //4
    }

    public Boolean getBooleanPrefByKey(Context context, String prefsKey) {
        SharedPreferences settings;
        Boolean value;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        value = settings.getBoolean(prefsKey, false);
        return value;
    }

    public void removePrefByKey(Context context, String prefsKey) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(prefsKey);
        editor.commit();
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }
}
