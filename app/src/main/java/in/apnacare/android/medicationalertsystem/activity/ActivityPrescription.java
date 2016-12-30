package in.apnacare.android.medicationalertsystem.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.apnacare.android.medicationalertsystem.Mandrill.Attachment;
import in.apnacare.android.medicationalertsystem.Mandrill.EmailMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.MandrillMessage;
import in.apnacare.android.medicationalertsystem.Mandrill.Recipient;
import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;
import io.fabric.sdk.android.Fabric;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityPrescription extends BaseActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private  String pharma_name;
    private Uri picUri;
    final int PIC_CROP = 4571;
    private ArrayList<PharmacyCollection> pharmaList;
    private static final int GET_INTERNET_PERMISSION = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        checkCameraHardware(mContext);
        Fabric.with(this, new Crashlytics());
        this.imageView = (ImageView)this.findViewById(R.id.picture);
        Button btnTakePrescription = (Button) this.findViewById(R.id.takePrescription);
        final Button btnEmailPrescription = (Button) this.findViewById(R.id.sendPrescription);
      // Button btnEmailPrescription = (Button) this.findViewById(R.id.sendPrescription);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("Add Medicine");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);

        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        GET_INTERNET_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;

        }

        btnTakePrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        btnEmailPrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(Constants.TAG, "Outside onActivityResume: "+btnEmailPrescription.getTag());
            }
        });

    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            Log.e(Constants.TAG, "checkCameraHardware: true ");
            return true;
        } else {
            Log.e(Constants.TAG, "checkCameraHardware: false ");// no camera on this device
            // no camera on this device
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final Button btnEmailPrescription = (Button) this.findViewById(R.id.sendPrescription);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);


            imageView.setImageURI(data.getData());
            Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            Log.e(Constants.TAG, "onActivityResult: "+bm.toString());
            saveImageFile(bm);

            Bundle extras = getIntent().getExtras();
            int messageExtra = extras.getInt("id");

            String pharma_email;
            pharmaList = new ArrayList<PharmacyCollection>();
            PharmacyCollection pharmacyCollection = new PharmacyCollection();
            PharmacyCollectionModel pharmaModel = new PharmacyCollectionModel(MedicationUsers.getContext());
            pharmaList = pharmaModel.getPharmaByID(messageExtra);

            for (int i=0; i<pharmaList.size();i++)
            {
                if (pharmaList.get(i) != null) {
                    Log.e("Exception", "MedList " + i + ": " + pharmaList.get(i).toString());
                    pharma_email = pharmaList.get(i).getPharmacy_email();
                    btnEmailPrescription.setTag(pharmaList.get(i).getPharmacy_email());
                    pharma_name = pharmaList.get(i).getPharmacy_name();
                }
            }


        }


        btnEmailPrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                imageView.getDrawable();
                Log.e(Constants.TAG, "Inside onActivityResume: "+btnEmailPrescription.getTag());
                Log.e(Constants.TAG, "Inside onActivityResume: "+imageView.getDrawable());

                try{

                    String filename = getFilename();
                    File dir = Environment.getExternalStorageDirectory();
                    Log.e(Constants.TAG, "onClick: "+dir);
                    File attachfile = new File(filename.toString());

                    String phone = MedicationUsers.preferences.getString("phno",null);
                    String name = MedicationUsers.preferences.getString("userName",null);
                    String email = btnEmailPrescription.getTag().toString();
                    String uEmail = MedicationUsers.preferences.getString("email",null);
                    Log.e(Constants.TAG, "onClick: "+phone);


                    if (imageView.getDrawable() != null) {
                        Log.e(Constants.TAG, "image path: "+attachfile);
                        Log.e(Constants.TAG, "send email through mandrill");

                        Bitmap bm = BitmapFactory.decodeFile(attachfile.toString());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                        byte[] byteArrayImage = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);





                        sendEmail(email,uEmail,encodedImage,pharma_name,name,phone);


                    }

                } catch (Throwable t) {
                    Toast.makeText(mContext,"Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public String saveImageFile(Bitmap bitmap) {
        Log.e(Constants.TAG, "saveImageFile: "+bitmap.toString());
        FileOutputStream out = null;
        String filename = getFilename();
        Log.e("Dimensions", bitmap.getWidth()+" "+bitmap.getHeight());
        Log.e(Constants.TAG, "saveImageFile: "+filename.toString() );
        try {

            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 55 , out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(Constants.TAG, "saveImageFile: "+filename);
        return filename;
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "ApnaCarePrescriptionImages");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + "prescription" + ".png");
        return uriSting;
    }


    public void sendEmail(String sender, String reciever, String fileUri,String pharma_name,String user_name,String user_number){
        Log.e(Constants.TAG, "send email through mandrill sender"+sender);
        Log.e(Constants.TAG, "send email through mandrill reciever"+reciever);
        Log.e(Constants.TAG, "send email through mandrill"+fileUri);
        Log.e(Constants.TAG, "send email through mandrill");

        MandrillMessage allMessage = new MandrillMessage("D2J_JGYO9LU63X68aTJ7zQ");
        // create your message
        EmailMessage message = new EmailMessage();
        message.setFromEmail(sender);
        message.setFromName(user_name);
        message.setHtml("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body style=\"background: #e2e1e0; font-family: 'Roboto', sans-serif !important;\">\n" +
                "\t<div style=\"display:block; width: 88%; margin: 0 auto; background: #fff; border-radius: 2px; box-shadow: 0 10px 20px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);\">\n" +
                "\t\t<!-- Header -->\n" +
                "\t\t<div style=\"display: block; max-width: 99%; min-height: 50px; border-bottom: 1px solid #E0E0E0; padding: 10px 10px 5px 10px; \">\n" +
                "\t\t\t<div style=\"display: inline-block; \">\n" +
                "\t\t\t\t<div style=\"font-size: 28px; padding-left: 15px; font-weight: 300; color: #2899dc; line-height: 50px; vertical-align: middle; margin-right: 10px; letter-spacing: 1px\"><img src=\"https://apnacare.in/uploads/smart_pill_reminder.png\" alt=\"Smart Pill Reminder\" title=\"ApnaCare India Pvt Ltd\" style = \"margin:0px 5px 0px 10px; width:50px; height:50px;vertical-align: middle;\" /> Smart Pill Reminder </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\n" +
                "\t\t<!-- Main Content -->\n" +
                "\t\t<div style=\"min-height: 200px; padding: 10px 15px; font-size: 14px; line-height: 22px; text-align: justify; margin: 10px 0; border-bottom: 1px solid #E0E0E0\">\n" +
                "            <span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\"></span>\n" +
                "\t\t\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Dear "+ pharma_name +",</span><br>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">I would like to buy this prescription</b>,</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\">Please do call me when it is ready</span>\n" +
                "\t\t\t\t<span style=\"font-size: 16px; font-weight: 500; margin-bottom: 20px; display: block;\"></span>\n" +
                "\n" +
                "            <br><br>\n" +
                "\rRegards,<br>\n" +
                "            <table>\n" +
                "                <tr>\n" +
                "                    <td style=\"vertical-align: top; font-size: 14px !important;\">\n" +
                "                        <span style=\"font-weight: bold\">"+user_name+"</span><br>\n" + user_number +"<br>\n" +"</td>\n" +
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
        message.setText("i would like to buy this prescription ");
        message.setSubject("Prescription Request");


        // add recipients
        Recipient recipient = new Recipient();
        Recipient recipient1 = new Recipient();
        List<Recipient> recipients = new ArrayList<Recipient>();
        recipient.setEmail(reciever);
        recipient.setName(reciever);
        recipient1.setEmail(sender);
        recipient1.setName("Krishna");
        recipients.add(recipient);
        recipients.add(recipient1);

        // add attachment if you want
        Attachment attachment = new Attachment();
        List<Attachment> attachments = new ArrayList<Attachment>();

        attachment.setType("image/png");
        //Log.e(Constants.TAG, "sendEmail: setType "+Uri.fromFile(fileUri).toString());
        attachment.setName("hai");
        attachment.setContent(fileUri);
        Log.e(Constants.TAG, "image to base64: "+fileUri);
        //attachment.setImage(fileUri);
        attachments.add(attachment);

        message.setTo(recipients);
        message.setImages(attachments);

        allMessage.setMessage(message);
        allMessage.send();
        Toast.makeText(this,"Sending Email..",Toast.LENGTH_SHORT).show();
        Intent details = new Intent(this,PharmacyDetailsActivity.class);
        startActivity(details);
        finish();
    }
    private void performCrop(){

        try {
            Log.e(Constants.TAG, "Inside the performCrop");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//            Log.e(Constants.TAG, "performCrop: "+picUri.toString());

            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 350);
            cropIntent.putExtra("outputY", 350);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GET_INTERNET_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(Constants.TAG, "onRequestPermissionsResult: "+grantResults.length);
                    finish();
                    startActivity(getIntent());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
