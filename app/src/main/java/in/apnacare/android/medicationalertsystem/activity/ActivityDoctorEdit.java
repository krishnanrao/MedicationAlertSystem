package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.geeksters.googleplaceautocomplete.lib.CustomAutoCompleteTextView;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

import static in.apnacare.android.medicationalertsystem.R.id.doc_hosp_email;
import static in.apnacare.android.medicationalertsystem.R.id.hospAddress;

public class ActivityDoctorEdit extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    Context mContext;
    EditText docName,hospLocation,docPhoneNumber,docEmail,hospCity;
    Button btnUpdateDoctorDetails,btnSearchCity;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    CustomAutoCompleteTextView hospName;

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private ArrayList<DoctorCollection> docList;


    final String EXTRA_MESSAGE = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit);
        mContext = this;
        mGoogleApiClient = new GoogleApiClient.Builder(ActivityDoctorEdit.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Update Doctor Details");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        Fabric.with(this, new Crashlytics());
        DoctorCollectionModel doctorCollectionModel = new DoctorCollectionModel(MedicationUsers.getContext());


        DoctorCollectionModel getDoctor = new DoctorCollectionModel(MedicationUsers.getContext());
        docName = (EditText) findViewById(R.id.docName);
        hospName = (CustomAutoCompleteTextView) findViewById(R.id.hospName);
        hospCity = (EditText) findViewById(R.id.hospCity);
        hospLocation = (EditText) findViewById(R.id.hospLocation);
        docPhoneNumber = (EditText) findViewById(R.id.doc_hosp_number);
        docEmail = (EditText)findViewById(R.id.doc_hosp_email);
        btnUpdateDoctorDetails = (Button) findViewById(R.id.updateDoc);


        editDoctorDetails();




        hospName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if(hospName.googlePlace != null) {
                        Log.v(Constants.TAG, "Clinic City: " + hospName.googlePlace.getCity());

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
            }
        });
        btnUpdateDoctorDetails.setOnClickListener(this);



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
            hospLocation.setText(Html.fromHtml(place.getAddress() + ""));
            docPhoneNumber.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        }
    };

    public void editDoctorDetails() {

        docList = new ArrayList<DoctorCollection>();
        DoctorCollectionModel getDoctorDetails = new DoctorCollectionModel(MedicationUsers.getContext());
        DoctorCollection getDoctorCollections = new DoctorCollection();
        Bundle extras = getIntent().getExtras();
        int messageExtra = extras.getInt("id");

        Log.e("inside edit doctor", "editDoctor: " + messageExtra);



        docList = getDoctorDetails.getDoctorByID(messageExtra);

        Log.e("Exception", "MedByID: " + docList.toString());

        try {
            for (int i = 0; i < docList.size(); i++) {
                if (docList.get(i) != null) {
                    Log.e(Constants.TAG, "MedList " + i + ": " + docList.get(i).getDoc_city());
                    docName.setText(docList.get(i).getDoctor_name());
                    hospName.setText(docList.get(i).getHospital_name());
                    hospCity.setText(docList.get(i).getDoc_city());
                    hospLocation.setText(docList.get(i).getHospital_location());
                    docEmail.setText(docList.get(i).getDoc_email());
                    docPhoneNumber.setText(docList.get(i).getDoc_hosp_phnumber());

                }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception: " + e.toString());
        }

    }

    @Override
    public void onClick(View v) {

        if(v == btnSearchCity){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
            Intent intent = new Intent(this,GooglePlaces.class);

            if(!docName.getText().toString().trim().equals("")){// && ){
                Log.e(Constants.TAG, "doctor module docname "+docName.getText());
                intent.putExtra("docName",docName.getText().toString());

            }
            if(!hospLocation.getText().toString().trim().equals("")){
                Log.e(Constants.TAG, "doctor module location "+hospLocation.getText());
                intent.putExtra("location",hospLocation.getText().toString());
            }
            if(!hospName.getText().toString().trim().equals("")){
                Log.e(Constants.TAG, "doctor module location "+hospName.getText());
                intent.putExtra("clinic",hospName.getText().toString());
            }
            if(!docPhoneNumber.getText().toString().trim().equals("")){
                Log.e(Constants.TAG, "doctor module location "+hospLocation.getText());
                intent.putExtra("phone",docPhoneNumber.getText().toString());
            }
            if(!docEmail.getText().toString().trim().equals("")){
                Log.e(Constants.TAG, "doctor module location "+docEmail.getText());
                intent.putExtra("email",docEmail.getText().toString());
            }
            intent.putExtra("edit","city");
            startActivity(intent);

        }
        if (v == btnUpdateDoctorDetails) {
            Log.e(Constants.TAG, "btn Edit Click");
            Bundle extras = getIntent().getExtras();
            int messageExtra = extras.getInt("id");
            DoctorCollection doctorCollection = new DoctorCollection();
            //Log.e(Constants.TAG, "med_qty.getSelectedItem(): " + med_qty.getSelectedItem());
            doctorCollection.setDoctor_name(docName.getText().toString());
            doctorCollection.setHospital_name(hospName.getText().toString());
            doctorCollection.setDoc_city(hospCity.getText().toString());
            doctorCollection.setHospital_location(hospLocation.getText().toString());
            doctorCollection.setDoc_email(docEmail.getText().toString());
            doctorCollection.setDoc_hosp_phnumber(docPhoneNumber.getText().toString());

            Log.e(Constants.TAG, "onClick: "+messageExtra );

            doctorCollection.updateDoctorDetails(messageExtra);

            Log.e(Constants.TAG, "Mediction ID: " + doctorCollection.getD_id());

             Toast.makeText(mContext, "Updating Doctor Detail's", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext,ActivityDoctorDetails.class);
            startActivity(intent);
            finish();
        }
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
