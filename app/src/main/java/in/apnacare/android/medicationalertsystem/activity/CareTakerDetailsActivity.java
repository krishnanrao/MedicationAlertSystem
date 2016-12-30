package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import in.apnacare.android.medicationalertsystem.adapter.CareTakerAdapter;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import io.fabric.sdk.android.Fabric;

public class CareTakerDetailsActivity extends BaseActivity {

    Context mContext;
    FloatingActionButton btnAddMedication;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CareTakerAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_details);

        Fabric.with(this, new Crashlytics());
        mContext = this;
        Log.e(Constants.TAG, "onCreateDoctorDetails");
       // sendPrescription = (Button) findViewById(R.id.sendPrescription);
        btnAddMedication= (FloatingActionButton) findViewById(R.id.floatingAddActionButton);
        btnAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CareTakerActivity.class);
                startActivity(intent);
            }
        });

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Kin Details");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }



        mRecyclerView = (RecyclerView) findViewById(R.id.care_taker_view);
        mAdapter = new CareTakerAdapter(mRecyclerView.getContext());
        Log.e(Constants.TAG, "mRecyclerView");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Log.e(Constants.TAG, "LinearLayoutManager");
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
