package in.apnacare.android.medicationalertsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.ManageUserCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;

import static in.apnacare.android.medicationalertsystem.utils.MedicationUsers.getContext;

public class ManageUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText uName,uPhone,uEmail;
    Button btnUpdateUser;
    ManageUserCollection manageUserCollection;
    ManageUserCollectionModel userCollectionModel;
    Spinner uDate,uMonth;
    static String birthMonth,birthdate;
    private ArrayList<ManageUserCollection> userList;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
            mContext = this;
        Fabric.with(this, new Crashlytics());
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.apnacareBlue));
        toolbar.setTitle("Update User Details");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        uName = (EditText) findViewById(R.id.userName);
        uPhone = (EditText) findViewById(R.id.userNumber);
        uEmail = (EditText) findViewById(R.id.userEmail);
        uDate = (Spinner) findViewById(R.id.dateSpinner);
        uMonth = (Spinner) findViewById(R.id.monthSpinner);

        int id = 0;
        btnUpdateUser = (Button) findViewById(R.id.updateUser);


        userCollectionModel = new ManageUserCollectionModel(MedicationUsers.getContext());

        manageUserCollection =  new ManageUserCollection();

        editUsers();

        btnUpdateUser.setOnClickListener(this);


    }

    public void editUsers() {

        userList = new ArrayList<ManageUserCollection>();
        ManageUserCollectionModel getUser = new ManageUserCollectionModel(getContext());
        ManageUserCollection getUserCollection = new ManageUserCollection();



        ArrayAdapter uMonthAdapter = (ArrayAdapter) uMonth.getAdapter(); //cast to an ArrayAdapter
        ArrayAdapter uDateAdapter = (ArrayAdapter) uDate.getAdapter(); //cast to an ArrayAdapter
        int uMonthPosition, uDatePosition;
        userList = getUser.getUsers();


        Log.e("Exception", "MedByID: " + userList.toString());

        try {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i) != null) {
                    Log.e("Exception", "MedList " + i + ": " + userList.get(i).toString());
                    btnUpdateUser.setTag(userList.get(i).getU_id());
                    uName.setText(userList.get(i).getUser_name());
                    uPhone.setText(userList.get(i).getPhone_number());
                    uEmail.setText(userList.get(i).getU_email());
                    uMonthPosition = (uMonthAdapter.getPosition(String.valueOf(userList.get(i).getB_month())));
                    uDatePosition = (uDateAdapter.getPosition(String.valueOf(userList.get(i).getB_date())));
                    uMonth.setSelection(uMonthPosition);
                    uDate.setSelection(uDatePosition);

          }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception: " + e.toString());
        }

    }
    @Override
    public void onClick(View v) {
        Log.e(Constants.TAG, "update user onClick: ");
        int id = Integer.parseInt(btnUpdateUser.getTag().toString());
        ManageUserCollection usersCollection = new ManageUserCollection();

        usersCollection.setUser_name(uName.getText().toString());
        usersCollection.setPhone_number(uPhone.getText().toString());
        usersCollection.setU_email(uEmail.getText().toString());
        usersCollection.setB_date(uDate.getSelectedItem().toString());
        usersCollection.setB_month(uMonth.getSelectedItem().toString());
        birthMonth = uMonth.getSelectedItem().toString();
        birthdate = uDate.getSelectedItem().toString();

        if(isValidEmail(uEmail.getText().toString()) && isValidDate(uDate.getSelectedItem().toString(),uMonth.getSelectedItem().toString(),getApplicationContext()))
        {
        usersCollection.updateRefQty(id);
        Intent nav =  new Intent(this,ViewMedicationActivity.class);
        startActivity(nav);
        finish();
            }
        else{
            Toast.makeText(this,"Please Enter Valid Email Id and Valid Date",Toast.LENGTH_SHORT).show();
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public final static boolean isValidDate(String month, String date,Context context){
        Boolean valid = false;
        Log.e(Constants.TAG," isValiddate month: "+month);
        Log.e(Constants.TAG,"isValiddate date: "+date);
        if(birthMonth.equals("Feb") || birthMonth.equals("Apr") || birthMonth.equals("Jun") || birthMonth.equals("Aug") || birthMonth.equals("Sep") || birthMonth.equals("Nov") ){
            if(birthMonth.equals("Feb") && birthdate.equals("30")){
                Toast.makeText(context,"Please Select Valid Date",Toast.LENGTH_SHORT).show();
                Log.e(Constants.TAG,"isValiddate valid: "+valid);
                return valid;
            }

            if(birthdate.equals("31")){
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                Log.e(Constants.TAG,"isValiddate valid: "+valid);
                return valid;
            }
        }


        valid = true;
        return valid;

    }
}
