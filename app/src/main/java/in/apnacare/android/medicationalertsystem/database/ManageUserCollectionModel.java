package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.model.ManageUserCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 30-11-2016.
 */

public class ManageUserCollectionModel {

    public String[] allColumns = {};

    public static final String U_ID = "u_id";
    public static final String USER_NAME  = "medicine_name";
    public static final String BIRTH_DATE  = "b_date";
    public static final String BIRTH_MONTH = "b_year";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String USER_EMAIL = "email";
    public static final String USER_CITY = "u_city";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;

    public ManageUserCollectionModel(Context context) {
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
    public long addUserCollection(ContentValues data) {
        long insertId = 0;
        Log.e(Constants.TAG,"inside add user method"+data.toString());
        try {
            this.open();
            Log.e("addUSer data",data.toString());
            insertId = database.insert(Constants.TABLE_USER, null, data);
            Log.e(Constants.TAG,"After inserting user");
            Log.e(Constants.TAG,"User Collection InsertID "+insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addUserCollection() Exception: " + e.toString());
        }

        return insertId;

    }


    public long updateUser(ContentValues data, int u_id) {
        long res = 0;
        Log.e(Constants.TAG, "updateUserId"+u_id);
        Log.e(Constants.TAG, "updateUserData"+data.toString());
        try {
            this.open();

            res = database.update(Constants.TABLE_USER, data, "u_id = '"+u_id+"'",null);
            Log.e(Constants.TAG, "query"+res);
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "UpdateQtyRefil() Exception: " + e.toString());
        }

        return res;
    }

    public ArrayList<ManageUserCollection> getUsers() {
        ArrayList<ManageUserCollection> userCollections = new ArrayList<ManageUserCollection>();

        Cursor cursor = database.query(Constants.TABLE_USER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ManageUserCollection userCollection = cursorToContact(cursor);
            userCollections.add(userCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.v(Constants.TAG, "getMedications: " + cursor.toString());
        return userCollections;

    }
    private ManageUserCollection cursorToContact(Cursor cursor) {


        ManageUserCollection userCollection = new ManageUserCollection();

        userCollection.setU_id(cursor.getInt(0));
        userCollection.setUser_name(cursor.getString(1));
        userCollection.setPhone_number(cursor.getString(2));
        userCollection.setU_email(cursor.getString(3));
        userCollection.setB_date(cursor.getString(4));
        userCollection.setB_month(cursor.getString(5));


        Log.e(Constants.TAG, "cursorToContact refill "+cursor.getString(3));

        return userCollection;
    }

}
