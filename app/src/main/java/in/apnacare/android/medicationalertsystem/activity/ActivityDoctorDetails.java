package in.apnacare.android.medicationalertsystem.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.adapter.DoctorListAdapter;
import in.apnacare.android.medicationalertsystem.adapter.PharmacyListAdapter;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import io.fabric.sdk.android.Fabric;

public class ActivityDoctorDetails extends BaseActivity {
    Context mContext;
    FloatingActionButton btnAddMedication;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DoctorListAdapter mAdapter;
    Button sendPrescription;
    private static final int CALL_PHONE_PERMISSION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        final String details = "details";
        mContext = this;
        Fabric.with(this, new Crashlytics());
        Log.e(Constants.TAG, "onCreateDoctorDetails");
        sendPrescription = (Button) findViewById(R.id.sendPrescription);
        btnAddMedication = (FloatingActionButton) findViewById(R.id.floatingAddActionButton);
        btnAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityDoctorModule.class);
                startActivity(intent);
            }
        });

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.apnacareBlue));
        toolbar.setTitle("Doctor Details");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.pharmacy_view);
        mAdapter = new DoctorListAdapter(mRecyclerView.getContext());
        Log.e(Constants.TAG, "mRecyclerView");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Log.e(Constants.TAG, "LinearLayoutManager");
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(Constants.TAG, "CALL_PHONE ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PHONE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case CALL_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    finish();
                    startActivity(getIntent());
                    Log.e(Constants.TAG, "onRequestPermissionsResult: " + grantResults.length);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

        }
    }
}
