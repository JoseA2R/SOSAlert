package com.acin.josefigueira.sosalert.View;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import java.util.*;

import com.acin.josefigueira.sosalert.Classes.Languages;
import com.acin.josefigueira.sosalert.Controller.SMSController;
import com.acin.josefigueira.sosalert.Fragments.FormFragment;
import com.acin.josefigueira.sosalert.Fragments.SOSFragment;
import com.acin.josefigueira.sosalert.R;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SMS_PERMISSION_CODE = 123;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    View view;
    SMSController smscontroller;
    SOSFragment fragmentSos = new SOSFragment();
    Languages languages;
    FragmentManager fragmentManager;
    NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        languages = new Languages();
        languages.Languages(this);
        languages.loadLocales();
        setContentView(R.layout.main_menu);

        mToolbar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_menu);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new SOSFragment()).commit();
        //fragmentSos.setContext(this);
        this.setTitle("");

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        fragmentManager = getFragmentManager();

        if (id == R.id.nav_sos){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SOSFragment()).commit();
        }
        else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FormFragment()).commit();
        }
        else if (id == R.id.language_pt){
            languages.setLocales("pt");
            recreate();
        }
        else if (id == R.id.language_en){
            languages.setLocales("en");
            recreate();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_menu);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void setSupportActionBar(Toolbar mToolbar) {
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Toast.makeText(this, "Enter Parent Context", Toast.LENGTH_LONG).show();
        /*List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null){
            //for (android.support.v4.app.Fragment fragment : fragments){
                fragmentSos.setContext(this);
                fragmentSos.onRequestPermissionsResult(requestCode,permissions,grantResults);
            //}*/


        //}
    }

}
