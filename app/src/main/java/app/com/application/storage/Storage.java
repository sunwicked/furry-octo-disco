package app.com.application.storage;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import app.com.application.main.DataModel;

/**
 * Created by admin on 09/09/17.
 */


public class Storage {

    private final SharedPreferences sp;
    private final Gson gson;
    Type baseType = new TypeToken<List<DataModel>>() {
    }.getType();

    public Storage(SharedPreferences sp, Gson gson) {
        this.sp = sp;
        this.gson = gson;
    }

    public void saveDataToLocal(String key, List<DataModel> dataModels) {

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, gson.toJson(dataModels, baseType)).apply();
    }

    public List<DataModel> getDataFromLocal(String key) {
        final String string = sp.getString(key, null);
        return gson.fromJson(string, baseType);
    }
}

