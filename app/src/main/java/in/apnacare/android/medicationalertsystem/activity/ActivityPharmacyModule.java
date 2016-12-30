package in.apnacare.android.medicationalertsystem.activity;

import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;

import co.geeksters.googleplaceautocomplete.lib.CustomAutoCompleteTextView;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.CareTakerCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedHistoryCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.model.MedHistoryCollection;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class ActivityPharmacyModule extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {
    EditText pharmaAddress,pharmaNumber,pharmaEmail,pharmacyCity;
    Button btnSaveButton,btnSearchCity;
    Context mContext;
    CustomAutoCompleteTextView pharmaName;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_module);
        Fabric.with(this, new Crashlytics());
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Add Pharmacy Details");
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(ActivityPharmacyModule.this)
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


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

          pharmaName = (CustomAutoCompleteTextView) findViewById(R.id.pharmaName);
        Log.e(Constants.TAG, "PharmacyName: "+pharmaName.getText().toString());
        pharmaAddress =  (EditText)findViewById(R.id.pharmaAddress);
        pharmacyCity = (EditText) findViewById(R.id.pharmaCity);
        pharmaNumber =      (EditText) findViewById(R.id.pharma_number);
        pharmaEmail = (EditText) findViewById(R.id.pharma_email);

        final String selectPharmaName =  pharmaName.getText().toString();
        btnSaveButton = (Button) findViewById(R.id.savePharma);



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
                                pharmacyCity.setText(str.get(str.size() - 3));
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




        btnSaveButton.setOnClickListener(this);

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
            pharmaAddress.setText(Html.fromHtml(place.getAddress() + ""));
            pharmaNumber.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        }
    };


    private static final String[] COUNTRIES = new String[] {
            "Apollo","MedPlus","Vigneshwara"
    };

    @Override
    public void onClick(View v) {

    if(v == btnSaveButton) {
    Log.e(Constants.TAG, "PharmacyName: " + pharmaName.getText());
    Log.e(Constants.TAG, "PharmacyEmail to string: " + pharmaNumber.getText());

    final PharmacyCollection pharmacyCollection = new PharmacyCollection();
        if(pharmaName.getText().toString().trim().equals("")  || pharmacyCity.getText().toString().trim().equals("") || pharmaAddress.getText().toString().trim().equals(""))// || pharmaNumber.getText().toString().trim().equals(""))
     {
        Log.e(Constants.TAG, "vinside condition PharmacyName: " + pharmaName.getText().toString());
        Log.e(Constants.TAG, "vinside condition pharmaEmail: " + pharmaEmail.getText());
        Log.e(Constants.TAG, "vinside condition pharmaAddress: " + pharmaAddress.getText().toString());
        Log.e(Constants.TAG, "vinside condition pharmaNumber: " + pharmaNumber.getText().toString());
        Toast.makeText(mContext, "Please Enter Pharmacy Details", Toast.LENGTH_SHORT).show();
      }
        else{
            if(isValidEmail(pharmaEmail.getText().toString()) )
            {
                pharmacyCollection.setPharmacyname(pharmaName.getText().toString());
                pharmacyCollection.setPharmacy_city(pharmacyCity.getText().toString());
                pharmacyCollection.setPharmacy_email(pharmaEmail.getText().toString());
                pharmacyCollection.setPharmacy_location(String.valueOf(pharmaAddress.getText().toString()));
                pharmacyCollection.setPharmacyNumber(String.valueOf(pharmaNumber.getText().toString()));
                pharmacyCollection.savePharmacy();

                Toast.makeText(mContext, "Saving Details", Toast.LENGTH_SHORT).show();
                CareTakerCollectionModel careTaker = new CareTakerCollectionModel(MedicationUsers.getContext());
                ArrayList<CareTakerCollection> careTakerList = new ArrayList<CareTakerCollection>();

                MedicationCollectionModel medmod = new MedicationCollectionModel(MedicationUsers.getContext());
                ArrayList<MedicationCollection> med = new ArrayList<MedicationCollection>();
                med = medmod.getMedications();
                Log.e(Constants.TAG, "MedMod size "+med.size());
                careTakerList = careTaker.getCareTaker();


                if (careTakerList.size() == 0 ) {

                    Intent intent = new Intent(ActivityPharmacyModule.this, CareTakerActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(ActivityPharmacyModule.this, PharmacyDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

        else{

                Toast.makeText(mContext, "Please valid email", Toast.LENGTH_SHORT).show();
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
