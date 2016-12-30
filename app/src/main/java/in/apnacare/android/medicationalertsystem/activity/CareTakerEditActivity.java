package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.BuildConfig;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.CareTakerCollectionModel;
import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class CareTakerEditActivity extends BaseActivity implements View.OnClickListener {

    Context mContext;
    Spinner ctRelation;
    EditText ctName,ctEmail,ctNumber;
    Button btnUpdateCareTakerDetails;

    private ArrayList<CareTakerCollection> careTakerList;


    final String EXTRA_MESSAGE = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_edit);
        Fabric.with(this, new Crashlytics());
        mContext = this;

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Update Kin Details");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        CareTakerCollectionModel ctCollection = new CareTakerCollectionModel(MedicationUsers.getContext());


        CareTakerCollectionModel ctModel = new CareTakerCollectionModel(MedicationUsers.getContext());

        ctName = (EditText) findViewById(R.id.ctName);
        ctEmail = (EditText) findViewById(R.id.ctEmail);
        ctNumber = (EditText) findViewById(R.id.ctNumber);

        ctRelation = (Spinner) findViewById(R.id.ctSpinner);
        btnUpdateCareTakerDetails = (Button) findViewById(R.id.ctUpdate);

        editCareTakerDetails();
        btnUpdateCareTakerDetails.setOnClickListener(this);


    }


    public void editCareTakerDetails() {

        careTakerList = new ArrayList<CareTakerCollection>();
        CareTakerCollectionModel getCareTakerDetails = new CareTakerCollectionModel(MedicationUsers.getContext());
        CareTakerCollection getCareTakerCollections = new CareTakerCollection();
        int relationSpinnerPosition;
        Bundle extras = getIntent().getExtras();
        int messageExtra = extras.getInt("id");
        ArrayAdapter relAdap = (ArrayAdapter) ctRelation.getAdapter();
        Log.e("inside editCareTaker", "editCareTaker: " + messageExtra);



        careTakerList = getCareTakerDetails.getCareTakerByID(messageExtra);

        Log.e(Constants.TAG, "CareListByID: " + careTakerList.toString());

        try {
            for (int i = 0; i < careTakerList.size(); i++) {
                if (careTakerList.get(i) != null) {
                    Log.e("Exception", "careTakerList " + i + ": " + careTakerList.get(i).toString());
                    ctName.setText(careTakerList.get(i).getCare_taker_name());
                    ctEmail.setText(careTakerList.get(i).getCare_taker_email());
                    ctNumber.setText(careTakerList.get(i).getCare_taker_phnumber());

                    relationSpinnerPosition = (relAdap.getPosition(String.valueOf(careTakerList.get(i).getCare_taker_relation())));
                    Log.e(Constants.TAG, "relationSpinnerPosition: " + relationSpinnerPosition);
                    Log.e(Constants.TAG, "relationSpinnerValueFronDB: " + careTakerList.get(i).getCare_taker_relation());
                    Log.e(Constants.TAG, "relationSpinnerPosition at position: " + ctRelation.getItemAtPosition(relationSpinnerPosition));
                    ctRelation.setSelection(relationSpinnerPosition);

                }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception: " + e.toString());
        }

    }

    @Override
    public void onClick(View v) {

        Log.e(Constants.TAG, "btn Click");


        Bundle extras = getIntent().getExtras();
        int messageExtra = extras.getInt("id");
        CareTakerCollection careTaker = new CareTakerCollection();
        Log.e(Constants.TAG, "ctRelation.getSelectedItem(): " + ctRelation.getSelectedItem());
        Log.e(Constants.TAG, "Relation spinner  " + ctRelation.getSelectedItem().toString());
        Log.e(Constants.TAG, "Number text  " + ctNumber.getText());
        careTaker.setCare_taker_name(ctName.getText().toString());
        careTaker.setCare_taker_email(ctEmail.getText().toString());
        careTaker.setCare_taker_phnumber(ctNumber.getText().toString());
        careTaker.setCare_taker_relation(ctRelation.getSelectedItem().toString());

        Log.e(Constants.TAG, "medID before Update "+messageExtra);


        careTaker.updateCareTaker(messageExtra);

            /* careTaker.saveMedication(); */


        Log.e(Constants.TAG, "Mediction ID: " + careTaker.getCare_taker_name());
            /*Log.e("medName", "medName: " + mediName.getText().toString() );*/
//            Toast.makeText(mContext, "Saving medications's", Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(), "Updating medications's", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CareTakerDetailsActivity.class);
        startActivity(intent);
        finish();
        
    }
}
