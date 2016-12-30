package in.apnacare.android.medicationalertsystem.model;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.apnacare.android.medicationalertsystem.database.ManageUserCollectionModel;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 30-11-2016.
 */

public class ManageUserCollection {
    int u_id;

    @SerializedName("name")
    @Expose
    private String user_name,name,email,phonenumber,birthdate,birthMonth;
    @SerializedName("phonenumber")
    @Expose
    private String  phone_number;
    @SerializedName("birthdate")
    @Expose
    private String  b_date;
    @SerializedName("birthmonth")
    @Expose
    private String b_month;
    @SerializedName("email")
    @Expose
    private String u_email;



    public ManageUserCollection(){

    }
    public ManageUserCollection(int u_id,String user_name,String phone_number, String b_date,String b_month,String u_email){
        this.u_id = u_id;
        this.user_name = user_name;
        this.phone_number = phone_number;
        this.b_date = b_date;
        this.b_month = b_month;
        this.u_email = u_email;
    }

    public int getU_id() {
        return u_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getU_email() {
        return u_email;
    }

    public String getB_date() {
        return b_date;
    }

    public String getB_month() {
        return b_month;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public void setB_date(String b_date) {
        this.b_date = b_date;
    }

    public void setB_month(String b_month) {
        this.b_month = b_month;
    }
    public void saveUserDetails() {
        ContentValues data = new ContentValues();

        Log.e(Constants.TAG, "Inside saveRefillDetails: "+data.toString());

        try {
            data.put(ManageUserCollectionModel.USER_NAME, this.user_name);
            data.put(ManageUserCollectionModel.PHONE_NUMBER, this.phone_number);
            data.put(ManageUserCollectionModel.USER_EMAIL, this.u_email);
            data.put(ManageUserCollectionModel.BIRTH_DATE, this.b_date);
            data.put(ManageUserCollectionModel.BIRTH_MONTH, this.b_month);

            ManageUserCollectionModel userCollectionModel = new ManageUserCollectionModel(MedicationUsers.getContext());

            userCollectionModel.addUserCollection(data);
        } catch (Exception e) {
            Log.e(Constants.TAG, "In save Refil details"+this.user_name);
            Log.e(Constants.TAG, "Save RefilDetails: " + e.toString());
        }

    }

    public void updateRefQty(int rh_id)  {
        ContentValues data = new ContentValues();
        Log.e(Constants.TAG, "updateRefil: "+rh_id);

        try {


            data.put(ManageUserCollectionModel.USER_NAME, this.user_name);
            data.put(ManageUserCollectionModel.PHONE_NUMBER, this.phone_number);
            data.put(ManageUserCollectionModel.USER_EMAIL, this.u_email);
            data.put(ManageUserCollectionModel.BIRTH_DATE, this.b_date);
            data.put(ManageUserCollectionModel.BIRTH_MONTH, this.b_month);
            ManageUserCollectionModel rfcm = new ManageUserCollectionModel(MedicationUsers.getContext());

            rfcm.updateUser(data,rh_id);
        }
        catch (Exception e) {
            Log.e("INSI Update refil:", this.u_email);
            Log.e(Constants.TAG, "update Refil: "+ e.toString());
        }

    }


}

