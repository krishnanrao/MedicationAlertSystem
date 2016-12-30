package in.apnacare.android.medicationalertsystem.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class CareTakerActivity extends BaseActivity implements View.OnClickListener{


    Context mcontext;
    EditText ctName,ctEmail,ctNumber;
    Spinner ctRelation;
    Button btnSaveCareTaker;
    String ct_name,ct_email,ct_number,ct_relation;
    private static final int SEND_SMS_PERMISSION = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker);
        mcontext = this;
        Fabric.with(this, new Crashlytics());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Kin Information");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }





        ctName = (EditText)findViewById(R.id.ct_name);
        ctEmail = (EditText) findViewById(R.id.ctEmail);
        ctNumber = (EditText) findViewById(R.id.ctNumber);
        ctRelation = (Spinner) findViewById(R.id.ctSpinner);

        btnSaveCareTaker = (Button) findViewById(R.id.ctSave);


        btnSaveCareTaker.setOnClickListener(this);


        ctRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ct_relation = ctRelation.getSelectedItem().toString();
                Log.v(Constants.TAG,"Care Taker RelationShip"+ct_relation);

                Log.v(Constants.TAG, "Care Taker RelationShip Position: " + ctRelation.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(Constants.TAG, "SEND_SMS ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        SEND_SMS_PERMISSION);

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
            case SEND_SMS_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(Constants.TAG, "onRequestPermissionsResult: "+grantResults.length);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onClick(View v) {

        Log.e(Constants.TAG, "On click function");

        CareTakerCollection careTaker = new CareTakerCollection();

        careTaker.setCare_taker_name(ctName.getText().toString());
        careTaker.setCare_taker_email(ctEmail.getText().toString());
        careTaker.setCare_taker_relation(ct_relation);
        careTaker.setCare_taker_phnumber(ctNumber.getText().toString());

        Log.e(Constants.TAG, "Inside onclick event Selected relation item " + ctRelation.getSelectedItem());
        Log.e(Constants.TAG, "Inside onclick event Selected relation item position"+ct_relation);


        MedicationCollectionModel medmod = new MedicationCollectionModel(MedicationUsers.getContext());
        ArrayList<MedicationCollection> med = new ArrayList<MedicationCollection>();
        med = medmod.getMedications();
        Log.e(Constants.TAG, "MedMod size "+med.size());


        //String medName = medCol.getM_name();
        if (med.size() == 0 ) {
            Toast.makeText(mContext,"Saving Family Details",Toast.LENGTH_SHORT).show();
            careTaker.saveCareTaker();
            /// Log.e(Constants.TAG, "Mename on intent activity: "+medName);
            Intent intent = new Intent(CareTakerActivity.this, AddMedication.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(mContext,"Saving Family Details",Toast.LENGTH_SHORT).show();
            careTaker.saveCareTaker();
            Intent intent = new Intent(CareTakerActivity.this, CareTakerDetailsActivity.class);
            startActivity(intent);
            finish();
        }




    }
}
