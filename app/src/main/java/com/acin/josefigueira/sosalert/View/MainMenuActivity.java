package com.acin.josefigueira.sosalert.View;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.acin.josefigueira.sosalert.Classes.Languages;
import com.acin.josefigueira.sosalert.Fragments.FormFragment;
import com.acin.josefigueira.sosalert.Fragments.SOSFragment;
import com.acin.josefigueira.sosalert.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jose.figueira on 03-04-2018.
 */

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    Languages languages;
    Snackbar snackbar;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        languages = new Languages();
        languages.Languages(this);
        languages.loadLocales();
        setContentView(R.layout.main_menu);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_menu);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);/*{

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
            }

            @Override
            public void onDrawerClosed(View drawerView){
                getSupportActionBar().setTitle("");
                super.onDrawerClosed(drawerView);
            }
        };*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.setDrawerTitle(Gravity.CENTER,"Menu");
        mToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new SOSFragment()).commit();
        //fragmentSos.setContext(this);
        this.setTitle("");

        //Init Facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

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
        else if (id == R.id.nav_share){
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote("Share SOS Button")
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .build();
            if (shareDialog.canShow(ShareLinkContent.class)){
                shareDialog.show(linkContent);
            }
            //printKeyHash();
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

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.acin.josefigueira.sosalert", PackageManager.GET_SIGNATURES);
            for(Signature signature :info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Toast.makeText(this, "Enter Parent Context", Toast.LENGTH_LONG).show();
    }
}