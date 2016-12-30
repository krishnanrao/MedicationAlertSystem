package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.Services.AlarmServiceBroadcastReciever;
import in.apnacare.android.medicationalertsystem.database.ManageUserCollectionModel;
import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.preferences.AlarmPreference;
import in.apnacare.android.medicationalertsystem.preferences.AlarmPreferencesActivity;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    TextView headerProviderName, headerProviderEmail;
    protected Context mContext;
    public boolean isLoggedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);

        mContext = this;
        Fabric.with(this, new Crashlytics());
    }

    protected void setUpNavigation(String title){
        try{
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 0);
            if(!title.equals(null)){
                toolbar.setTitle(title);
            }
            setSupportActionBar(toolbar);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ArrayList<ManageUserCollection> userCollections = new ArrayList<ManageUserCollection>();
            ManageUserCollectionModel userModel = new ManageUserCollectionModel(MedicationUsers.getContext());
            View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
            userCollections = userModel.getUsers();
            navigationView.addHeaderView(header);

            headerProviderName = (TextView) header.findViewById(R.id.lblHeaderProviderName);
            headerProviderEmail = (TextView) header.findViewById(R.id.lblHeaderProviderDesignation);
            for(int i=0;i<userCollections.size();i++){

                headerProviderName.setText(userCollections.get(i).getUser_name());
                headerProviderEmail.setText(userCollections.get(i).getU_email());

            }
            setStatusBarColor();

        }catch (Exception e){
            Log.v(Constants.TAG,"setUpNavigation() Exception: "+e.toString());
        }
    }

    public void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.v(Constants.TAG,"id: "+id);

        switch (id){
            case R.id.menu_home: startActivity(new Intent(BaseActivity.this,ViewMedicationActivity.class));break;
            case R.id.menu_medicine: startActivity(new Intent(BaseActivity.this,MedicationDetailsActivity.class));break;
            case R.id.menu_doctors: startActivity(new Intent(BaseActivity.this,ActivityDoctorDetails.class)); break;
            case R.id.menu_pharmacy: startActivity(new Intent(BaseActivity.this,PharmacyDetailsActivity.class)); break;
            case R.id.menu_care_taker: startActivity(new Intent(BaseActivity.this,CareTakerDetailsActivity.class)); break;
          /*  case R.id.alarm_menu: startActivity(new Intent(BaseActivity.this,GoogleLogin.class)); break;*/
            case R.id.user_menu : startActivity(new Intent(BaseActivity.this, ManageUserActivity.class));break;
            case R.id.faq_menu : startActivity(new Intent(BaseActivity.this, FaqActivity.class));break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public boolean isLoggedIn(){
        return MedicationUsers.preferences.getBoolean("isLoggedIn",false);
    }

    public void logout(){
        MedicationUsers.e.putBoolean("isLoggedIn",false);
        MedicationUsers.e.putInt("userID",-1);
        MedicationUsers.e.apply();

        Intent i = new Intent(BaseActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    protected void callMathAlarmScheduleService() {
        Log.e(Constants.TAG, "callMathAlarmScheduleService:from medicine details ");
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
