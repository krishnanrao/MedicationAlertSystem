package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.DoctorCollection;
import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 23-10-2016.
 */

public class DoctorCollectionModel {

    private int pharmacy_id;
    private String pharmacy_name,pharmacy_location,pharmacy_number;

    public String[] allColumns = {};

    public static final String D_ID = "d_id";
    public static final String DOCTOR_NAME  = "doctor_name";
    public static final String HOSPITAL_NAME  = "hospital_name";
    public static final String HOSPITAL_LOCATION = "hospital_location";
    public static final String HOSPITAL_CITY = "hospital_city";
    public static final String DOC_HOSP_NUMBER = "doc_hosp_number";
    public static final String DOCTOR_EMAIL = "doc_email";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public DoctorCollectionModel(Context context) {
        med_ctx = context;
        try {
            db = DatabaseHandler.getInstance(med_ctx);
            database = db.getWritableDatabase();
        }
        catch (Exception e){
            Log.e(TAG,e.toString() );
        }
    }

    public void open(){
        try {
            database = db.getWritableDatabase();
        }
        catch (Exception e){
            Log.e(TAG,e.toString() );
        }
    }
    public void close() {
        database.close();
        db.close();
    }

    public long addDoctorCollection(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG,"inside add Doctor method"+data.toString());
        try {
            this.open();
            Log.e(Constants.TAG,"data"+data.toString());
            insertId = database.insert(Constants.TABLE_DOCTOR, null, data);
            Log.e("Inserted ","After inserting ");
            Log.e("Returning ","Returning InsertID "+insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addMedication() Exception: " + e.toString());

        }

        return insertId;

    }

    public ArrayList<DoctorCollection> getDoctor() {
        ArrayList<DoctorCollection> doc_collection = new ArrayList<DoctorCollection>();

        Cursor cursor = database.query(Constants.TABLE_DOCTOR,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DoctorCollection docCollection = cursorToContact(cursor);
            doc_collection.add(docCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "DoctorDetails: "+ cursor.toString());
        return doc_collection;

    }
    public ArrayList<DoctorCollection>  getDoctorByID(int doctor_id) {

        Log.e("Inside get Doctor", "getDoctorByID: "+doctor_id );

        ArrayList<DoctorCollection> doc = new ArrayList<DoctorCollection>();
        String whereClause = null;

        if(doctor_id != 0){
            whereClause = " d_id = '"+doctor_id+"'";
        }

        Cursor cursor = database.query(Constants.TABLE_DOCTOR,
                allColumns, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DoctorCollection doctorCollection = cursorToContact(cursor);
            doc.add(doctorCollection);
            cursor.moveToNext();
        }
        Log.e(Constants.TAG, "getDoctorByID count: "+cursor.getCount());
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "getDoctorByID: "+doc.toString());
        return doc;
    }
    public void removeByID(int doctor_id){
        Log.e("Inside Remove by ID","Removing... "+doctor_id);
        database.execSQL("DELETE FROM doctor Where d_id = '" + doctor_id + "'");

    }


    public long updateDoctorDetails(ContentValues data, int doc_id) {
        long res = 0;
        Log.e(Constants.TAG, "updateDoctorId"+doc_id);
        Log.e(Constants.TAG, "updateDoctorData"+data.toString());
        try {
            this.open();

            res = database.update(Constants.TABLE_DOCTOR, data, "d_id = '"+doc_id+"'",null);
            Log.e(Constants.TAG, "query"+res);
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "updateMedication() Exception: " + e.toString());
        }

        return res;
    }

    private DoctorCollection cursorToContact(Cursor cursor) {

        DoctorCollection doctor_collection = new DoctorCollection();

        doctor_collection.setD_id(cursor.getInt(0));
        doctor_collection.setDoctor_name(cursor.getString(1));
        doctor_collection.setHospital_name(cursor.getString(2));
        doctor_collection.setDoc_city(cursor.getString(3));
        doctor_collection.setHospital_location(cursor.getString(4));
        doctor_collection.setDoc_hosp_phnumber(cursor.getString(5));
        doctor_collection.setDoc_email(cursor.getString(6));



        return doctor_collection;
    }
}
