package in.apnacare.android.medicationalertsystem.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.crashlytics.android.Crashlytics;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

public class LoginSuccess extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    DoctorCollection doc = new DoctorCollection();
    DoctorCollectionModel dmod = new DoctorCollectionModel(MedicationUsers.getContext() );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_login_success);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
//                Log.e(Constants.TAG, "Doctor collection Module"+dmod.getDoctor());
  //              Log.e(Constants.TAG, "Doctor collection doctor name"+doc.getDoctor_name());


                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(LoginSuccess.this, ViewMedicationActivity.class);
                    startActivity(i);
                    finish();

                // close this activity

            }
        }, SPLASH_TIME_OUT);

    }
}
