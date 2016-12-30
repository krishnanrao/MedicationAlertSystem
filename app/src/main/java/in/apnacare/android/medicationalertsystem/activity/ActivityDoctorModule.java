package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.geeksters.googleplaceautocomplete.lib.CustomAutoCompleteTextView;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;


import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class ActivityDoctorModule extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    EditText docName, hospAddress, doc_hosp_email, doc_hosp_ph, hospCity;
    Button saveDocDetails, btnSearchCity;
    Context mContext;
    CustomAutoCompleteTextView hospName;

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_module);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Add Doctor");
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(ActivityDoctorModule.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        Fabric.with(this, new Crashlytics());
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        docName = (EditText) findViewById(R.id.docName);
        hospAddress = (EditText) findViewById(R.id.hospAddress);
        doc_hosp_email = (EditText) findViewById(R.id.doc_hosp_email);
        saveDocDetails = (Button) findViewById(R.id.saveDoc);
        doc_hosp_ph = (EditText) findViewById(R.id.doc_hosp_number);
        hospCity = (EditText) findViewById(R.id.hospCity);

        docName.setText("Dr. ");

        hospName = (CustomAutoCompleteTextView) findViewById(R.id.hospName);
        hospName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                   // Log.v(Constants.TAG, "Clinic City: " + hospName.googlePlace.getCity());
                    if(hospName.googlePlace != null) {
                        hospName.setText(hospName.googlePlace.getCity());

                        String description = hospName.googlePlace.getDescription();
                        if (description != null) {
                            List<String> str = Arrays.asList(description.split(","));
                            if (str.size() >= 3) {
                                hospCity.setText(str.get(str.size() - 3));
                            }
                        }

                        Places.GeoDataApi.getPlaceById(mGoogleApiClient, hospName.googlePlace.getPlace_id());
                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, hospName.googlePlace.getPlace_id());
                        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                    }
                    else{
                        Log.e(Constants.TAG, "onFocusChange: google connection failed ");
                    }

                }
               else{
                    Log.e(Constants.TAG, "onFocusChange: is false"+b);
                }
            }
        });

        docName.setSelection(docName.length());
        saveDocDetails.setOnClickListener(this);
    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.v(Constants.TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            // Selecting the first object buffer.
            final Place place = places.get(0);
            Log.v(Constants.TAG, "place: " + place.getAddress());

            hospName.setText(Html.fromHtml(place.getName() + ""));
            hospAddress.setText(Html.fromHtml(place.getAddress() + ""));
            doc_hosp_ph.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        }
    };

    @Override
    public void onClick(View v) {
        if (v == saveDocDetails) {

            if (docName.getText().toString().trim().equals("") || hospName.getText().toString().trim().equals("") || hospAddress.getText().toString().trim().equals("")) {// || doc_hosp_email.getText().toString().trim().equals("") || doc_hosp_ph.getText().toString().trim().equals("")){

                Toast.makeText(mContext, "Please enter a doctor details", Toast.LENGTH_SHORT).show();

            } else {
                if (doc_hosp_email.getText().toString().trim().equals("")) {
                    DoctorCollection doctorCollection = new DoctorCollection();
                    PharmacyCollection pharm = new PharmacyCollection();
                    PharmacyCollectionModel pharmamod = new PharmacyCollectionModel(MedicationUsers.getContext());
                    ArrayList<PharmacyCollection> pharmalist = new ArrayList<PharmacyCollection>();
                    pharmalist = pharmamod.getPharmacy();
                    String pharma_name = pharm.getPharmacy_name();
                    doctorCollection.setDoctor_name(docName.getText().toString());
                    Log.e(Constants.TAG, "Saving doctor Name: " + docName.getText().toString());
                    doctorCollection.setHospital_name(hospName.getText().toString());
                    doctorCollection.setHospital_location(hospAddress.getText().toString());
                    doctorCollection.setDoc_city(hospCity.getText().toString());
                    Log.e(Constants.TAG, "Hospital city " + hospCity.getText().toString());
                    doctorCollection.setDoc_email("null");
                    doctorCollection.setDoc_hosp_phnumber("null");
                    doctorCollection.saveDoctor();

                    Toast.makeText(mContext, "Saving Doctor Details", Toast.LENGTH_SHORT).show();
                    if (pharmalist.size() == 0) {
                        Intent intent = new Intent(mContext, ActivityPharmacyModule.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(mContext, ActivityDoctorDetails.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (isValidEmail(doc_hosp_email.getText().toString())) {
                    DoctorCollection doctorCollection = new DoctorCollection();
                    PharmacyCollection pharm = new PharmacyCollection();
                    PharmacyCollectionModel pharmamod = new PharmacyCollectionModel(MedicationUsers.getContext());
                    ArrayList<PharmacyCollection> pharmalist = new ArrayList<PharmacyCollection>();
                    pharmalist = pharmamod.getPharmacy();
                    String pharma_name = pharm.getPharmacy_name();
                    doctorCollection.setDoctor_name(docName.getText().toString());
                    Log.e(Constants.TAG, "Saving doctor Name: " + docName.getText().toString());
                    doctorCollection.setHospital_name(hospName.getText().toString());
                    doctorCollection.setHospital_location(hospAddress.getText().toString());
                    doctorCollection.setDoc_city(hospCity.getText().toString());
                    Log.e(Constants.TAG, "Hospital city " + hospCity.getText().toString());
                    doctorCollection.setDoc_email(doc_hosp_email.getText().toString());
                    doctorCollection.setDoc_hosp_phnumber(doc_hosp_ph.getText().toString());
                    doctorCollection.saveDoctor();

                    Toast.makeText(mContext, "Saving Doctor Details", Toast.LENGTH_SHORT).show();
                    if (pharmalist.size() == 0) {
                        Intent intent = new Intent(mContext, ActivityPharmacyModule.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(mContext, ActivityDoctorDetails.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
