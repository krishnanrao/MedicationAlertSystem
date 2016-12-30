package in.apnacare.android.medicationalertsystem.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.googleplaces.SampleActivityBase;
import in.apnacare.android.medicationalertsystem.utils.Constants;

public class GooglePlaces  extends SampleActivityBase {
    private TextView mPlaceDetailsText;

    private TextView mPlaceAttribution;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String googlePlaces = "googlePlaces";
        Bundle bundle = getIntent().getExtras();
        Log.e(Constants.TAG,"bundle: "+bundle.toString());

        setContentView(R.layout.activity_google_places);
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);
        openAutocompleteActivity();
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            //  Log.e(Constants.TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent returnIntent = new Intent(this,ActivityDoctorModule.class);
        Intent editReturnIntent = new Intent(this,ActivityDoctorEdit.class);
        Intent pharmacyReturnIntent = new Intent(this,ActivityPharmacyModule.class);
        Intent pharmacyEditReturnIntent = new Intent(this,ActivityEditPharmacy.class);
        String googlePlaces = "googlePlaces";
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        if(!bundle.equals("null") && bundle.containsKey("type")){

            String docName = bundle.getString("docName");
            returnIntent.putExtra("docName",docName);

        }
        if(!bundle.equals(null) && bundle.containsKey("edit")){
            if(bundle.containsKey("docName")){
                Log.e(Constants.TAG, "from edit activity doc name: "+bundle.getString("docName"));
                editReturnIntent.putExtra("docName",bundle.getString("docName"));
            }if(bundle.containsKey("location")){
                Log.e(Constants.TAG, "from edit activity  doc name: "+bundle.getString("location"));
                editReturnIntent.putExtra("location",bundle.getString("location"));
            }if(bundle.containsKey("clinic")){
                Log.e(Constants.TAG, "from edit activity  clinic name: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("clinic",bundle.getString("clinic"));
            }if(bundle.containsKey("phone")){
                Log.e(Constants.TAG, "from edit activity  phone: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("phone",bundle.getString("phone"));
            }if(bundle.containsKey("email")){
                Log.e(Constants.TAG, "from edit activity  email: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("email",bundle.getString("email"));
            }
        }
        if(!bundle.equals(null) && bundle.containsKey("pharma")){
            if(bundle.containsKey("pharmaName")){
                Log.e(Constants.TAG, "from edit activity doc name: "+bundle.getString("pharmaName"));
                pharmacyReturnIntent.putExtra("pharmaName",bundle.getString("pharmaName"));
            }/*if(bundle.containsKey("location")){
                Log.e(Constants.TAG, "from edit activity  doc name: "+bundle.getString("location"));
                editReturnIntent.putExtra("location",bundle.getString("location"));
            }if(bundle.containsKey("clinic")){
                Log.e(Constants.TAG, "from edit activity  clinic name: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("clinic",bundle.getString("clinic"));
            }if(bundle.containsKey("phone")){
                Log.e(Constants.TAG, "from edit activity  phone: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("phone",bundle.getString("phone"));
            }if(bundle.containsKey("email")){
                Log.e(Constants.TAG, "from edit activity  email: "+bundle.getString("clinic"));
                editReturnIntent.putExtra("email",bundle.getString("email"));
            }*/
        }
        if(!bundle.equals(null) && bundle.containsKey("pharmaEdit")){
           /* if(bundle.containsKey("pharmaEdit")){
                Log.e(Constants.TAG, "from edit activity doc name: "+bundle.getString("docName"));
                pharmacyEditReturnIntent.putExtra("pharmaEdit",bundle.getString("pharmaName"));
            }*/if(bundle.containsKey("pharmaName")){
                Log.e(Constants.TAG, "from edit activity  doc name: "+bundle.getString("pharmaName"));
                pharmacyEditReturnIntent.putExtra("pharmaName",bundle.getString("pharmaName"));
            }if(bundle.containsKey("pharmaEmail")){
                Log.e(Constants.TAG, "from edit activity  clinic name: "+bundle.getString("pharmaEmail"));
                pharmacyEditReturnIntent.putExtra("pharmaEmail",bundle.getString("pharmaEmail"));
            }if(bundle.containsKey("pharmaNumber")){
                Log.e(Constants.TAG, "from edit activity  phone: "+bundle.getString("pharmaNumber"));
                pharmacyEditReturnIntent.putExtra("pharmaNumber",bundle.getString("pharmaNumber"));
            }if(bundle.containsKey("pharmaLocation")){
                Log.e(Constants.TAG, "from edit activity  email: "+bundle.getString("pharmaLocation"));
                pharmacyEditReturnIntent.putExtra("pharmaLocation",bundle.getString("pharmaLocation"));
            }
        }
        Log.e(Constants.TAG,"bundle: "+type+" | "+bundle.getString("location"));




        //returnIntent.putExtra("city",city);
        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                //  Log.i(Constants.TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
               /* mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/
                //  android.util.Log.e(Constants.TAG, "onActivityResult:  Place Name"+place.getName());

                if(!bundle.equals(null) && bundle.containsKey("type") && bundle.getString("type").equals("city")){
                    Log.e(Constants.TAG, "onActivityResult: for city "+bundle.getString("type"));
                    returnIntent.putExtra("googlePlaces",googlePlaces);
                    returnIntent.putExtra("placeName",place.getName());
                    returnIntent.putExtra("type","city");
                }

                if(!bundle.equals(null) && bundle.containsKey("edit") && bundle.getString("edit").equals("city")){
                    Log.e(Constants.TAG, "onActivityResult edit: for city "+bundle.getString("edit"));
                    editReturnIntent.putExtra("googlePlaces",googlePlaces);
                    editReturnIntent.putExtra("placeName",place.getName());
                    editReturnIntent.putExtra("edit","city");
                }
                if(!bundle.equals(null) && bundle.containsKey("pharma") && bundle.getString("pharma").equals("city")){
                    Log.e(Constants.TAG, "onActivityResult pharma: for city "+bundle.getString("pharma"));
                    pharmacyReturnIntent.putExtra("googlePlaces",googlePlaces);
                    pharmacyReturnIntent.putExtra("placeName",place.getName());
                    pharmacyReturnIntent.putExtra("pharma","city");
                }
                if(!bundle.equals(null) && bundle.containsKey("pharmaEdit") && bundle.getString("pharmaEdit").equals("city")){
                    Log.e(Constants.TAG, "onActivityResult pharmaEdit: for city "+bundle.getString("pharma"));
                    pharmacyEditReturnIntent.putExtra("googlePlaces",googlePlaces);
                    pharmacyEditReturnIntent.putExtra("placeName",place.getName());
                    pharmacyEditReturnIntent.putExtra("pharmaEdit","city");
                }



                //returnIntent.putExtra("placeName",place.getName());
                //returnIntent.putExtra("placeAddress",place.getAddress());
                //returnIntent.putExtra("place",place);
               /* Log.e(Constants.TAG, "Result is ok and the format: "+formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                  /*mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));*/
                    returnIntent.putExtra("attributions",Html.fromHtml(attributions.toString()));
                    editReturnIntent.putExtra("attributions",Html.fromHtml(attributions.toString()));
                    pharmacyEditReturnIntent.putExtra("attributions",Html.fromHtml(attributions.toString()));
                    pharmacyReturnIntent.putExtra("attributions",Html.fromHtml(attributions.toString()));
                    //Log.e(Constants.TAG, "onActivityResult: "+Html.fromHtml(attributions.toString()));
                } else {
                    //mPlaceAttribution.setText("");

                    Log.e(Constants.TAG, "Empty string ");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                returnIntent.putExtra("error",status.toString());
                editReturnIntent.putExtra("error",status.toString());
                pharmacyReturnIntent.putExtra("error",status.toString());
                pharmacyEditReturnIntent.putExtra("error",status.toString());
               // Log.e(Constants.TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }

        }
        if(!bundle.equals(null) && bundle.containsKey("edit") && bundle.getString("edit").equals("city")){
            startActivity(editReturnIntent);
            finish();
        }
        if(!bundle.equals(null) && bundle.containsKey("pharma") && bundle.getString("pharma").equals("city")){
            startActivity(pharmacyReturnIntent);
            finish();
        }if(!bundle.equals(null) && bundle.containsKey("pharmaEdit") && bundle.getString("pharmaEdit").equals("city")){
            startActivity(pharmacyEditReturnIntent);
            finish();
        }
        if(bundle.containsKey("type")){
            startActivity(returnIntent);
            finish();
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
       /* Log.e(Constants.TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));*/
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }
}
