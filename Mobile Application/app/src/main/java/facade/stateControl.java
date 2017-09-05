package facade;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChewZhijie on 3/13/2017.
 */

public final class stateControl {
    private static final String preferenceName = "Walkera";

    public static void saveData(Context c, String key, String data) {
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName, MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static void saveData(Context c, String key, int data) {
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName, MODE_PRIVATE).edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public static void saveData(Context c, String key, model.user obj)
    {
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.commit();
    }

    public static String getStringData(Context c, String key)
    {
        String name = "";
        SharedPreferences prefs = c.getSharedPreferences(preferenceName, MODE_PRIVATE);
        return prefs.getString(key, "");//"No name defined" is the default value.
    }

    public static int getIntData(Context c, String key)
    {
        int name = 0;
        SharedPreferences prefs = c.getSharedPreferences(preferenceName, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        return  prefs.getInt(key, 0);//"No name defined" is the default value.
    }

    public static model.user getUserData(Context c, String key)
    {
        Gson gson = new Gson();
        SharedPreferences prefs = c.getSharedPreferences(preferenceName, MODE_PRIVATE);
        String json = prefs.getString(key, "");
        model.user obj = gson.fromJson(json, model.user.class);
        return  obj;
    }

    public static void logout(Context c)
    {
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName,MODE_PRIVATE).edit().clear();

        editor.commit();
    }
}
