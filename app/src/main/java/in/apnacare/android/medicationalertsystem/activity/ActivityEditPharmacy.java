package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

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
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import io.fabric.sdk.android.Fabric;

import static in.apnacare.android.medicationalertsystem.utils.MedicationUsers.getContext;


public class ActivityEditPharmacy extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    Context mContext;
    EditText pharma_location,pharma_email,pharma_phone,pharma_city;

    Button btnUpdatePharmacy,btnSearchCity;
    CustomAutoCompleteTextView pharmaName;
    private ArrayList<PharmacyCollection> pharmaList;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;



    public final String EXTRA_MESSAGE = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacy);
        Fabric.with(this, new Crashlytics());
        mContext = this;
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Update Pharmacy Details");
        setSupportActionBar(toolbar);



        mGoogleApiClient = new GoogleApiClient.Builder(ActivityEditPharmacy.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        PharmacyCollectionModel getPharma = new PharmacyCollectionModel(getContext());
        pharmaName = (CustomAutoCompleteTextView) findViewById(R.id.pharmaName);
        pharma_city = (EditText) findViewById(R.id.pharmaCity);
        pharma_location = (EditText) findViewById(R.id.pharmaAddress);
        pharma_email = (EditText) findViewById(R.id.pharma_email);
        pharma_phone = (EditText) findViewById(R.id.pharma_number);

        btnUpdatePharmacy = (Button) findViewById(R.id.updatepharma);

        editMedication();
        pharmaName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {


                    if(pharmaName.googlePlace != null) {
                        Log.v(Constants.TAG, "Clinic City: " + pharmaName.googlePlace.getCity());

                        pharmaName.setText(pharmaName.googlePlace.getCity());

                        String description = pharmaName.googlePlace.getDescription();
                        if (description != null) {
                            List<String> str = Arrays.asList(description.split(","));
                            if (str.size() >= 3) {
                                pharma_city.setText(str.get(str.size() - 3));
                            }
                        }

                        Places.GeoDataApi.getPlaceById(mGoogleApiClient, pharmaName.googlePlace.getPlace_id());
                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, pharmaName.googlePlace.getPlace_id());
                        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                    }
                    else{
                        Log.e(Constants.TAG, "onFocusChange: google connection failed ");
                    }
                }
            }
        });
     btnUpdatePharmacy.setOnClickListener(this);
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

            pharmaName.setText(Html.fromHtml(place.getName() + ""));
            pharma_location.setText(Html.fromHtml(place.getAddress() + ""));
            pharma_phone.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        }
    };
    public void editMedication() {

        pharmaList = new ArrayList<PharmacyCollection>();
        PharmacyCollectionModel getPharma = new PharmacyCollectionModel(getContext());
        PharmacyCollection getCollections = new PharmacyCollection();
        Bundle extras = getIntent().getExtras();
        int messageExtra = extras.getInt("id");

        Log.e("inside edit pharmacy", "editpharmacy: " + messageExtra);



        pharmaList = getPharma.getPharmaByID(messageExtra);

        Log.e("Exception", "MedByID: " + pharmaList.toString());

        try {
            for (int i = 0; i < pharmaList.size(); i++) {
                if (pharmaList.get(i) != null) {
                    Log.e("Exception", "MedList " + i + ": " + pharmaList.get(i).toString());
                    pharmaName.setText(pharmaList.get(i).getPharmacy_name());
                    pharma_city.setText(pharmaList.get(i).getPharmacy_city());
                    pharma_location.setText(pharmaList.get(i).getPharmacyLocation());
                    pharma_email.setText(pharmaList.get(i).getPharmacy_email());
                    pharma_phone.setText(pharmaList.get(i).getPharmacyNumber());

                }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception: " + e.toString());
        }

    }

    public void onClick(View v) {
               if (v == btnUpdatePharmacy) {
            Log.e(Constants.TAG, "btn Edit Click");
            Bundle extras = getIntent().getExtras();
            int messageExtra = extras.getInt("id");
            PharmacyCollection pharmaCollection = new PharmacyCollection();

            pharmaCollection.setPharmacyname(pharmaName.getText().toString());
            pharmaCollection.setPharmacy_city(pharma_city.getText().toString());
            Log.e(Constants.TAG, "On saving google place: "+pharma_city.getText().toString());
            pharmaCollection.setPharmacy_location(pharma_location.getText().toString());
            pharmaCollection.setPharmacy_email(pharma_email.getText().toString());
            pharmaCollection.setPharmacyNumber(pharma_phone.getText().toString());

            Log.e(Constants.TAG, "onClick: "+messageExtra );

            pharmaCollection.updatePharmacy(messageExtra);
            Log.e(Constants.TAG, "Mediction ID: " + pharmaCollection.getP_id());
            Intent intent = new Intent(ActivityEditPharmacy.this,PharmacyDetailsActivity.class);
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