package com.acin.josefigueira.sosalert.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jose.figueira on 20-04-2018.
 */

public class Languages {

    Context mContext;

    public void Languages(Context context){
        mContext = context;
    }

    public void setLocales(String lang){

        SharedPreferences.Editor editor = mContext.getSharedPreferences("Settings",MODE_PRIVATE).edit();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
        editor.putString("lang",lang);
        editor.apply();

    }

    public void loadLocales(){
        SharedPreferences SPreferences = mContext.getSharedPreferences("Settings", MODE_PRIVATE);
        String language = SPreferences.getString("lang","");
        setLocales(language);
    }

}
