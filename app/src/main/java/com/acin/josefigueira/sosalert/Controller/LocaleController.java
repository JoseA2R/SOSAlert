package com.acin.josefigueira.sosalert.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.acin.josefigueira.sosalert.View.MainActivity;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jose.figueira on 18-04-2018.
 */

public class LocaleController {

    private final Context context;
    public MainActivity main;

    public LocaleController(Context context) {
        this.context = context;
    }


}
