package in.apnacare.android.medicationalertsystem.activity;

/**
 * Created by dell on 03-11-2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                DoctorCollection doc = new DoctorCollection();
                DoctorCollectionModel dmod = new DoctorCollectionModel(MedicationUsers.getContext() );
                ArrayList<DoctorCollection> doctorCollections = new ArrayList<DoctorCollection>();
                doctorCollections = dmod.getDoctor();
                PharmacyCollection phar = new PharmacyCollection();
                PharmacyCollectionModel pharma = new PharmacyCollectionModel(MedicationUsers.getContext());
                ArrayList<PharmacyCollection> pharmaList = new ArrayList<PharmacyCollection>();
                pharmaList = pharma.getPharmacy();
               /* if(doctorCollections.size() <= 0){
                    Intent i = new Intent(SplashScreen.this, ActivityDoctorModule.class);
                    startActivity(i);
                }
                else if(pharmaList.size() <= 0){
                    Intent i = new Intent(SplashScreen.this, ActivityPharmacyModule.class);
                    startActivity(i);
                }
                else
                {*/
                    Intent i = new Intent(SplashScreen.this, ViewMedicationActivity.class);
                    startActivity(i);
                //}


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}