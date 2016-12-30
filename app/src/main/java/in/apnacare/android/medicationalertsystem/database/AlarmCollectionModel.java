package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.utils.Constants;

import static android.content.ContentValues.TAG;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_A_ENABLED;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_E_ENABLED;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_M_ENABLED;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_N_ENABLED;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_SNOOZE;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.COLUMN_TAKEN;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.database;
import static in.apnacare.android.medicationalertsystem.database.DatabaseHandler.getDatabase;

/**
 * Created by dell on 22-11-2016.
 */

public class AlarmCollectionModel {
    public String[] allColumns = {};

    public static final String ALARM_TABLE = "alarm";
    public static final String COLUMN_ALARM_ID = "_id";
    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_MED_ID = "amed_id";
    public static final String COLUMN_ALARM_TIME = "alarm_time";
    public static final String COLUMN_ALARM_DAYS = "alarm_days";

    public static final String COLUMN_ALARM_TONE = "alarm_tone";
    public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    public static final String COLUMN_M_ENABLED = "m_enabled";
    public static final String COLUMN_A_ENABLED = "a_enabled";
    public static final String COLUMN_E_ENABLED = "e_enabled";
    public static final String COLUMN_N_ENABLED = "n_enabled";
    public static final String COLUMN_TAKEN = "taken";
    public static final String COLUMN_SNOOZE = "snooze";


    private Context med_ctx;
    private DatabaseHandler db;
    private SQLiteDatabase database;


    public AlarmCollectionModel(Context context) {
        med_ctx = context;
        try {
            db = DatabaseHandler.getInstance(med_ctx);
            database = db.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void open() {
        try {
            database = db.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void close() {
        database.close();
        db.close();
    }


    public static Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[]{
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_MED_ID,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME,
                COLUMN_M_ENABLED,
                COLUMN_A_ENABLED,
                COLUMN_E_ENABLED,
                COLUMN_N_ENABLED
        };
        return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
                null);


    }

    public long addAlarm(Alarm alarm) {

        long insertId = 0;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_MED_ID, alarm.getMedAlarmId());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }


        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        Log.e(Constants.TAG, "On cerate alarm name  " + alarm.getAlarmName());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());
        cv.put(COLUMN_M_ENABLED, alarm.getMorn());
        cv.put(COLUMN_A_ENABLED, alarm.getNoon());
        cv.put(COLUMN_E_ENABLED, alarm.getEven());
        cv.put(COLUMN_N_ENABLED, alarm.getNight());
        cv.put(COLUMN_TAKEN, alarm.getTaken());
        cv.put(COLUMN_SNOOZE, alarm.getSnooze());


        try {
            if (!database.isOpen()) {
                this.open();
            }

            insertId = database.insert(ALARM_TABLE, null, cv);
            Log.e(Constants.TAG, "After inserting Alarm" + insertId);
            Log.e(Constants.TAG, "Refill Collection InsertID " + insertId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "addRefillCollection() Exception: " + e.toString());
        }

        return insertId;

    }


    public long updateAlarmMedId(Alarm alarm, int m_id) {
        ContentValues cv = new ContentValues();
        Log.e(Constants.TAG, "updateAlarmMedId: " + m_id);
        long res = 0;
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_MED_ID, alarm.getMedAlarmId());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());
        Log.e(Constants.TAG, "updateAlarmMedId: in snooze time " + alarm.getSnooze());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }


        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());
        cv.put(COLUMN_TAKEN, alarm.getTaken());
        cv.put(COLUMN_SNOOZE, alarm.getSnooze());


        try {
            if (!database.isOpen()) {
                this.open();
            }

            res = database.update(ALARM_TABLE, cv, "_id = '" + m_id + "'", null);
            Log.e(Constants.TAG, "Alarm Update Query" + res);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "UpdateQtyRefil() Exception: " + e.toString());
        }

        return res;
    }


    public long updateSnooze(int alarm_id, int snooze) {
        ContentValues cv = new ContentValues();
        Alarm alarm = new Alarm();
        cv.put(COLUMN_SNOOZE, snooze);
        long res = 0;
        String query;
        Log.e(Constants.TAG, "updateSnooze:in snooze time " + snooze);
        Log.e(Constants.TAG, "updateSnooze:for alarm id " + alarm_id);

        try {
            if (!database.isOpen()) {
                this.open();
            }

            res = database.update(ALARM_TABLE, cv, "_id = '" + alarm_id + "'", null);

            Log.e(Constants.TAG, "Alarm Update Query result" + res);


            //Log.e(Constants.TAG, "updateSnooze: in snooze time after updating "+alarm.getSnooze());
            //this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "UpdateQtyRefil() Exception: " + e.toString());
        }

        return res;
    }

    public long updateTaken(int alarm_id, int taken) {
        ContentValues cv = new ContentValues();
        Alarm alarm = new Alarm();
        cv.put(COLUMN_TAKEN, taken);
        long res = 0;
        String query;
        Log.e(Constants.TAG, "updateSnooze:in taken time " + taken);
        Log.e(Constants.TAG, "updateSnooze:for alarm id " + alarm_id);

        try {
            if (!database.isOpen()) {
                this.open();
            }

            res = database.update(ALARM_TABLE, cv, "_id = '" + alarm_id + "'", null);

            Log.e(Constants.TAG, "Alarm Update Query result" + res);


            //Log.e(Constants.TAG, "updateSnooze: in snooze time after updating "+alarm.getSnooze());
            //this.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v(Constants.TAG, "UpdateQtyRefil() Exception: " + e.toString());
        }

        return res;
    }

    public int getSnoozeCountByAlarmId(int alarmID) {
        Log.e("Inside get Alarm", "getSnoozeCountByAlarmId: " + alarmID);
        int snoozeCount = 0;

        ArrayList<Alarm> med = new ArrayList<Alarm>();
        String whereClause = null;

        if (alarmID != 0) {
            whereClause = "_id = " + alarmID;
        }
        Cursor cursor;
        if (!database.isOpen()) {
            this.open();
        }

        try {
            cursor = database.query(ALARM_TABLE,
                    allColumns, whereClause, null, null, null, null);
            if(cursor.getCount() == 1){
                cursor.moveToFirst();
                snoozeCount = cursor.getInt(cursor.getColumnIndex("snooze"));
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
        }

        Log.e("medicationCollectionMod", "getSnoozeCountByAlarmId snoozeCount: " + snoozeCount);
        return snoozeCount;
    }
    public int getTakenCountByAlarmId(int alarmID) {
        Log.e("Inside get Alarm", "getTakenCountByAlarmId: " + alarmID);
        int takenCount = 0;

        ArrayList<Alarm> med = new ArrayList<Alarm>();
        String whereClause = null;

        if (alarmID != 0) {
            whereClause = "_id = " + alarmID;
        }
        Cursor cursor;
        if (!database.isOpen()) {
            this.open();
        }

        try {
            cursor = database.query(ALARM_TABLE,
                    allColumns, whereClause, null, null, null, null);
            if(cursor.getCount() == 1){
                cursor.moveToFirst();
                takenCount = cursor.getInt(cursor.getColumnIndex("taken"));
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
        }

        Log.e("medicationCollectionMod", "getSnoozeCountByAlarmId snoozeCount: " + takenCount);
        return takenCount;
    }

    //get alarm_id's for medid

    public ArrayList<Alarm> getAlarmsByMedID(int medId) {
        Log.e("Inside get Alarm", "getAlarmsByMedID: " + medId);

        ArrayList<Alarm> med = new ArrayList<Alarm>();
        String whereClause = null;

        if (medId != 0) {
            whereClause = " amed_id = '" + medId + "'";
        }
        Cursor cursor;
        if (!database.isOpen()) {
            this.open();
        }

        try {
            cursor = database.query(ALARM_TABLE,
                    allColumns, whereClause, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Alarm medCollection = cursorToContact(cursor);
                med.add(medCollection);
                cursor.moveToNext();
            }
            Log.e(TAG, "getMedicationsByID count: " + cursor.getCount());
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
        }

        Log.e("medicationCollectionMod", "getMedicationsByID: " + med.toString());
        return med;
    }


    public ArrayList<Alarm> getAlarmCollection() {
        ArrayList<Alarm> doc_collection = new ArrayList<Alarm>();

        Cursor cursor = database.query(ALARM_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Alarm docCollection = cursorToContact(cursor);
            doc_collection.add(docCollection);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Log.e(Constants.TAG, "DoctorDetails: " + cursor.toString());
        return doc_collection;

    }

    public void deleteEntry(int id) {

        database.execSQL("DELETE FROM alarm Where _id = '" + id + "'");
    }


    public void deleteMedEntry(int id) {
        database.execSQL("DELETE FROM alarm Where amed_id = '" + id + "'");
    }

    private Alarm cursorToContact(Cursor cursor) {

        Alarm alarms = new Alarm();

        ;

        alarms.setId(cursor.getInt(0));
        alarms.setAlarmActive(cursor.getInt(1) == 1);
        alarms.setMed_id(cursor.getInt(2));
        alarms.setAlarmTime(cursor.getString(3));

        byte[] repeatDaysBytes = cursor.getBlob(4);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysBytes);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Alarm.Day[] repeatDays;
            Object object = objectInputStream.readObject();
            if (object instanceof Alarm.Day[]) {
                repeatDays = (Alarm.Day[]) object;
                alarms.setDays(repeatDays);
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        alarms.setAlarmTonePath(cursor.getString(5));
        alarms.setVibrate(cursor.getInt(6) == 1);
        alarms.setAlarmName(cursor.getString(7));
        alarms.setMorn(cursor.getInt(8));
        alarms.setNoon(cursor.getInt(9));
        alarms.setEven(cursor.getInt(10));
        alarms.setNight(cursor.getInt(11));
        alarms.setTaken(cursor.getInt(12));
        alarms.setSnooze(cursor.getInt(13));
        Log.e(Constants.TAG, "cursorToContact refill " + cursor.getString(3));

        return alarms;
    }


}
