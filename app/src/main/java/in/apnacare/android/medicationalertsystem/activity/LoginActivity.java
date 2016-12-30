package in.apnacare.android.medicationalertsystem.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import in.apnacare.android.medicationalertsystem.Mandrill.Attachment;
import in.apnacare.android.medicationalertsystem.Mandrill.EmailMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.MandrillMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.Recipient;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.interfaces.AppUser;
import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private ProgressDialog mProgressDialog;
    private EditText mEmailView, userName, phoneNumber;
    private View mProgressView;
    private View mLoginFormView;
    Button signup;
    Spinner bDate, bMonth;
    static String birthMonth, birthdate;
    Context mContext;
    ProgressDialog pd;
    boolean isValidDob = false;
    private static final int GET_ACCOUNT_PERMISSION = 1;
    private static final int INTERNET_PERMISSION =2;
    private static final int WRITE_SETTINGS_PERMISSION = 3;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 4;
    private static final int READ_CONTACTS_PERMISSION = 5;
    private static final int ACCESS_NETWORK_STATE_PERMISSION = 6;
    private static final int SEND_SMS_PERMISSION = 7;
    private static final int READ_PHONE_STATE_PERMISSION = 8;
    private static final int WAKE_LOCK_PERMISSION = 9;
    private static final int VIBRATE_PERMISSION = 10;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    AppUser appuserinterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.logscreen);
        setContentView(R.layout.activity_login);
        mContext = this;
        Fabric.with(this, new Crashlytics());
        String email = MedicationUsers.preferences.getString("email", null);
        if (email != null) {
            Intent i = new Intent(LoginActivity.this, SplashScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        // Set up the login form.
        Log.e(Constants.TAG, "UserEmailID ");
        Bundle loginBundle = getIntent().getExtras();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        /*signInButton.setOnClickListener(this);*/
        mEmailView = (EditText) findViewById(R.id.email);
        userName = (EditText) findViewById(R.id.user_name);
        phoneNumber = (EditText) findViewById(R.id.ph_no);
        bDate = (Spinner) findViewById(R.id.dateSpinner);
        bMonth = (Spinner) findViewById(R.id.monthSpinner);

        if(loginBundle != null){
            userName.setText(loginBundle.getString("uName"));
            mEmailView.setText(loginBundle.getString("uEmail"));

            Log.e(Constants.TAG, "UserName "+loginBundle.getString("uName"));
            Log.e(Constants.TAG, "UserEmailID "+loginBundle.getString("uEmail"));
            Log.e(Constants.TAG, "PhotoUrl "+loginBundle.getString("uPhotoUrl"));
        }



        bMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                birthMonth = bMonth.getSelectedItem().toString();
                Log.e(Constants.TAG, "Month of birth" + birthMonth);
                Log.e(Constants.TAG, "Selected item position" + arg2);
                Log.e(Constants.TAG, "month spinner  Position: " + bMonth.getSelectedItemPosition());


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        bDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                birthdate = bDate.getSelectedItem().toString();
                Log.e(Constants.TAG, "Month of birth" + birthMonth);
                Log.e(Constants.TAG, "Selected item position" + arg2);
                Log.e(Constants.TAG, "month spinner  Position: " + bMonth.getSelectedItemPosition());


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        signup = (Button) findViewById(R.id.email_sign_in_button);
/*        ArrayAdapter doseAdap = (ArrayAdapter) bDate.getAdapter();
        ArrayAdapter qtyAdap = (ArrayAdapter) bMonth.getAdapter();*/
        SharedPreferences preferences = getSharedPreferences("Login_Preferences", MODE_PRIVATE);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        signup.setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        GET_ACCOUNT_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;

        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(Constants.TAG, "SEND_SMS ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        tm.getLine1Number();
        Log.e(Constants.TAG, "Telephone number "+number);





    }
    private static final String[] uEmail = new String[]{

    };

    public void onClick(View v) {

        if(v == findViewById(R.id.sign_in_button)){
            signIn();
        }
        if (v == signup) {
            Context mContext;
            mContext = this;

            ManageUserCollection userCollection = new ManageUserCollection();
            String uName = userName.getText().toString();
            String pNo = phoneNumber.getText().toString();
            String emailId = mEmailView.getText().toString();
            String birthDate = bDate.getSelectedItem().toString();
            String birthMonth = bMonth.getSelectedItem().toString();

            Log.e(Constants.TAG, "login Email: " + mEmailView.getText().toString());
            Log.e(Constants.TAG, "login birth month: " +birthMonth);
            Log.e(Constants.TAG, "login birth date: " + birthDate);



            Log.v(Constants.TAG,"isValidDob: "+isValidDob);

            if(isValidEmail(emailId) && isValidDate(birthMonth,birthDate))
            {
                MedicationUsers.e.putString("userName", uName);
                MedicationUsers.e.putString("phno", pNo);
                MedicationUsers.e.putString("email", emailId);
                MedicationUsers.e.apply();

                if (emailId.toString().contains(" ")) {

                    Toast.makeText(mContext, "No Spaces Allowed",Toast.LENGTH_SHORT).show();
                }


                userCollection.setUser_name(uName);
                userCollection.setPhone_number(pNo);
                userCollection.setU_email(emailId);
                userCollection.setB_date(birthDate);
                userCollection.setB_month(birthMonth);
                userCollection.saveUserDetails();
                checkInternetConenction();
                prepareForm();


                String msg = "Hi "+uName+"Thank you for downloading Smart Pill Reminder You have successfully Registered";
                msg = msg.replaceAll(" ","%20");
                GetXMLTask task = new GetXMLTask();
                String URL ="https://apnacare.in/portal/notification/sms?to="+pNo+"&message="+msg;

                task.execute(new String[] { URL });

                sendEmail(emailId,uName);
                Intent intent = new Intent(LoginActivity.this, LoginSuccess.class);
                startActivity(intent);
                finish();

            }
            else{
                Toast.makeText(mContext, "Please enter a valid email address or please select valid date", Toast.LENGTH_SHORT).show();
                Log.e(Constants.TAG, "enter valid email");
            }


        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public final static boolean isValidDate(String month, String date){
        Boolean valid = false;
        Log.e(Constants.TAG," isValiddate month: "+month);
        Log.e(Constants.TAG,"isValiddate date: "+date);
        if(birthMonth.equals("Feb") || birthMonth.equals("Apr") || birthMonth.equals("Jun") || birthMonth.equals("Aug") || birthMonth.equals("Sep") || birthMonth.equals("Nov") ){
            if(birthMonth.equals("Feb") && birthdate.equals("30")){

                Log.e(Constants.TAG,"isValiddate valid: "+valid);
                return valid;
            }

            if(birthdate.equals("31")){

                Log.e(Constants.TAG,"isValiddate valid: "+valid);
                return valid;
            }
        }


            valid = true;
            return valid;

    }

    public void prepareForm(){
        String name = userName.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String email = mEmailView.getText().toString();

        if(name.equals("") || email.equals("")){
            Toast.makeText(getApplicationContext(),"Name and Email are mandatory!",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e(Constants.TAG,"isValidDob: "+isValidDob);


        try {
            new signupUser().execute();
        }catch (Exception e){
            Log.v("Main.class","Async Exception: "+e.toString());
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public class signupUser extends AsyncTask<String, Void, String> {
        String name,mobile,email;

        protected void onPreExecute(){

            name = userName.getText().toString();
            mobile = phoneNumber.getText().toString();
            email = mEmailView.getText().toString();

            signup.setClickable(false);

            if(pd !=null) {
                pd.dismiss();
                pd = null;
            }
            pd = new ProgressDialog(mContext);

            pd.setTitle("Signing Up");
            pd.setMessage("Please wait..");
            pd.setIndeterminate(true);
            pd.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://apnacare.in/portal/notification/appSignup"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", name);
                postDataParams.put("mobile", mobile);
                postDataParams.put("email", email);
                Log.e(Constants.TAG,"params"+postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                Log.e(Constants.TAG, "responseCode: "+responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e(Constants.TAG, "doInBackground: "+responseCode);
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();

                    Log.e(Constants.TAG, "Response doInBackground: "+sb.toString());

                    return sb.toString();

                }
                else {

                    return new String("false : "+responseCode);
                }

            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }


        }

        @Override
        protected void onPostExecute(String result) {
            if(pd !=null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }
    private class GetXMLTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            Log.e(Constants.TAG, "getOutputFromUrl: "+url);
            try {
                InputStream stream = getHttpConnection(url);
                Log.e(Constants.TAG, "getOutputFromUrl:stream "+stream);
                if(stream != null){
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(stream));
                    String s = "";
                    Log.e(Constants.TAG, "getOutputFromUrl  inside try abd stream !=null "+url);
                    while ((s = buffer.readLine()) != null)
                        output.append(s);
               }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            Log.e(Constants.TAG, "getHttpConnection: Url connection url = "+url);
            Log.e(Constants.TAG, "getHttpConnection: Connection is  = "+connection);
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.setConnectTimeout(5000);
                httpConnection.connect();
                Log.e(Constants.TAG, "getHttpConnection: responseCode "+httpConnection.getResponseCode());
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                    Log.e(Constants.TAG, "getHttpConnection: http is ok "+stream);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(Constants.TAG, "getHttpConnection: exception "+ex.toString());
            }
            Log.e(Constants.TAG, "getHttpConnection: Returning stream  = "+stream);
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            Log.e(Constants.TAG, "onPostExecute: "+output);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }



    public void sendEmail(String Recipient, String Recipient_name){


        MandrillMessage allMessage = new MandrillMessage("D2J_JGYO9LU63X68aTJ7zQ");
        // create your message
        EmailMessage message = new EmailMessage();
        message.setFromEmail("no-reply@apnacare.in");
        message.setFromName("ApnaCare");
        message.setHtml("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body style=\"background: #e2e1e0; font-family: 'Roboto', sans-serif !important;\">\n" +
                "\t<div style=\"display:block; width: 88%; margin: 0 auto; background: #fff; border-radius: 2px; box-shadow: 0 10px 20px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);\">\n" +
                "\t\t<!-- Header -->\n" +
                "\t\t<div style=\"display: block; max-width: 99%; min-height: 50px; border-bottom: 1px solid #E0E0E0; padding: 10px 10px 5px 10px; \">\n" +
                "\t\t\t<div style=\"display: inline-block; float:left;\">\n" +
                "\t\t\t\t<div style=\"font-size: 28px; padding-left: 15px; font-weight: 300; height: 45px; color: #2899dc; line-height: 45px; vertical-align: middle; margin-right: 10px; letter-spacing: 1px\"><img src=\"https://apnacare.in/uploads/smart_pill_reminder.png\" alt=\"Smart Pill Reminder\" title=\"ApnaCare India Pvt Ltd\" style = \"margin:0px 5px 0px 10px; width:50px; height:50px;vertical-align: middle;\" /> Smart Pill Reminder</div>\n" +
        "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t\t<!-- Main Content -->\n" +
                "\t\t<div style=\"min-height: 200px; padding: 10px 15px; font-size: 14px; line-height: 22px; text-align: justify; margin: 10px 0; border-bottom: 1px solid #E0E0E0\">\n" +
                "            <span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\"></span>\n" +
                "\t\t\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Dear "+ Recipient_name +",</span><br>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Thank you for downloading <b> Smart Pill Reminder</b>,</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Hope you enjoy using this app.</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Also check our other apps :<a href=\"https://play.google.com/store/search?q=apnacare\" style=\"color: #2899dc !important\">Click here</a> </span>\n" +
                "\n" +
                "            <br><br>\n" +
                "            Regards,<br>\n" +
                "            <table>\n" +
                "                <tr>\n" +
                "                    <td style=\"vertical-align: top; font-size: 14px !important;\">\n" +
                "                        <span style=\"font-weight: bold\">ApnaCare India Private Limited</span><br>\n" +
                "#1583, 13th Main, 20th Cross,<br>\n" +
                "B-Block, Sahakar Nagar,<br>\n" +
                "Bengaluru 560092.<br>" +"Phone: <br>Bangalore: +91 (080) 30752584 \n<br><br>" +
                "Website: <a href=\"https://apnacare.in\">https://apnacare.in</a> \n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t\t<!-- Footer -->\n" +
                "\t\t<div style=\"display: block; position:relative; bottom: 0; max-width: 99%; min-height: 90px; padding: 5px 10px 0px 10px; \">\n" +
                "\t\t\t<div style=\"width: 50%; display: inline-block; font-size: 12px;\">\n" +
                "\t\t\t\t<span>Copyright @2016 ApnaCare India Pvt. Ltd. All Rights Reserved</span><br><br>\n" +
                "\t\t\t\t<span style=\"font-size: 12px; font-weight: 600\">\n" +
                "\t\t\t\t\t<a href=\"\" style=\"color: #2899dc !important\">Terms and Conditions</a> |\n" +
                "\t\t\t\t\t<a href=\"\" style=\"color: #2899dc !important\">Privacy Policy</a>\n" +
                "\t\t\t\t</span>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"width: 10%; display: inline-block; text-align: center; vertical-align: top; float: right; font-size: 14px\">\n" +
                "\t\t\t\t<p style=\"margin-bottom: 10px;margin-top:0;padding-top:0\">Powered by ApnaCare</p>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<br><br>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>");
       // message.setText("blah blah blah .... ");
        message.setSubject("Welcome");
        Log.e(Constants.TAG, "Inside Send Email "+ "Recipient:" +Recipient+" Name :"+Recipient_name);
        // add recipients
        Recipient recipient = new Recipient();
        List<Recipient> recipients = new ArrayList<Recipient>();
        recipient.setEmail(Recipient);
        recipient.setName(Recipient_name);
        recipients.add(recipient);


        message.setTo(recipients);


        allMessage.setMessage(message);
        allMessage.send();
    }

    @Override
    public void onPause(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(pd !=null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
        super.onPause();
    }

   /* */@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GET_ACCOUNT_PERMISSION: {
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

        }
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            in.apnacare.android.medicationalertsystem.logger.Log.d(Constants.TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        if(mProgressDialog != null)
            mProgressDialog.show();
        //mProgressDialog.dismiss();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(Constants.TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            userName.setText(acct.getDisplayName());
            mEmailView.setText(acct.getEmail());
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);

        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

            Log.e(Constants.TAG, "updateUI: "+isSignedIn);
        } else {

            Log.e(Constants.TAG, "updateUI: "+isSignedIn);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        in.apnacare.android.medicationalertsystem.logger.Log.d(Constants.TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(pd !=null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
        super.onDestroy();
    }

}


