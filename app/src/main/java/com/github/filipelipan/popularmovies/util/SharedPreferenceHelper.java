package com.github.filipelipan.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.filipelipan.popularmovies.model.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;

;

/**
 * Created by lispa on 28/05/2016.
 */
public class SharedPreferenceHelper {
    private static final String SIZE = "SIZE";
    private static final String STORAGE = "STORAGE";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public SharedPreferenceHelper(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public ArrayList<Movie> getStoragePreference() {
        Gson gson = new Gson();
        ArrayList<Movie> exercises = new ArrayList<>();

        int size = mSharedPreferences.getInt(SIZE , 0);
        for(int i = 0; i < size; i++) {
            String exercise = mSharedPreferences.getString(STORAGE + i, "");
            exercises.add(gson.fromJson(exercise, Movie.class));
        }
        return exercises;
    }

    public void setSharedPreferences(ArrayList<Movie> exercises) {
        Gson gson = new Gson();
        mEditor.putInt(SIZE , exercises.size());
        for(int i = 0; i < exercises.size(); i++){
            mEditor.putString(STORAGE + i, gson.toJson(exercises.get(i)));
        }
        mEditor.apply();
    }

    public void clearSharedPreferences(){
        mEditor.clear();
        mEditor.commit();
    }
}
