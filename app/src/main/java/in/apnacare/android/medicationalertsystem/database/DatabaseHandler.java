package in.apnacare.android.medicationalertsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.apnacare.android.medicationalertsystem.activity.AddMedication;
import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHandler sInstance;
    // Database Name
    private static final String DATABASE_NAME = "k";
    static SQLiteDatabase database = null;




    // Medication table name
    private static final String TABLE_MEDICATION = "medications";
    private static final String TABLE_PHARMACY = "pharmacy";
    private static final String TABLE_DOCTOR = "doctors";
    private static final String TABLE_CARETAKER = "caretaker";
    private static final String TABLE_MEDHISTORY = "medicationHistory";
    private static final String TABLE_REFILL = "refill";
    private static final String TABLE_USER = "user";



    // MASTER Alarm Table
    public static final String ALARM_TABLE = "alarm";
    public static final String COLUMN_ALARM_ID = "_id";
    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_MED_ID = "amed_id";
    public static final String COLUMN_ALARM_TIME = "alarm_time";
    public static final String COLUMN_ALARM_DAYS = "alarm_days";
    /* public static final String COLUMN_ALARM_DIFFICULTY = "alarm_difficulty";*/
    public static final String COLUMN_ALARM_TONE = "alarm_tone";
    public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
    public static final String COLUMN_ALARM_NAME = "alarm_name";
    public static final String COLUMN_M_ENABLED = "m_enabled";
    public static final String COLUMN_A_ENABLED = "a_enabled";
    public static final String COLUMN_E_ENABLED = "e_enabled";
    public static final String COLUMN_N_ENABLED = "n_enabled";
    public static final String COLUMN_TAKEN = "taken";
    public static final String COLUMN_SNOOZE = "snooze";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_USER_QUERY);
        db.execSQL(TABLE_MEDICATION_QUERY);
        db.execSQL(TABLE_PHARMACY_QUERY);
        db.execSQL(TABLE_DOCTOR_QUERY);
        db.execSQL(TABLE_CARETAKER_QUERY);
        db.execSQL(TABLE_MEDHISTORY_QUERY);
        db.execSQL(TABLE_REFILL_QUERY);
        ;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( "
                + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
                + COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, "
                + COLUMN_MED_ID + " INTEGER NOT NULL, "
                + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
                + COLUMN_ALARM_DAYS + " BLOB NOT NULL, "
                + COLUMN_ALARM_TONE + " TEXT NOT NULL, "
                + COLUMN_ALARM_VIBRATE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_NAME + " TEXT NOT NULL, "
                + COLUMN_M_ENABLED + " INTEGER NOT NULL, "
                + COLUMN_A_ENABLED + " INTEGER NOT NULL, "
                + COLUMN_E_ENABLED + " INTEGER NOT NULL, "
                + COLUMN_N_ENABLED + " INTEGER NOT NULL, "
                + COLUMN_TAKEN + " INTEGER NOT NULL, "
                + COLUMN_SNOOZE + " INTEGER NOT NULL);");

        Log.e(Constants.TAG, "Tables Created: " );

    }



    private static final String TABLE_MEDICATION_QUERY = "create table "
            + Constants.TABLE_MEDICATION+ "("
            + MedicationCollectionModel.MED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MedicationCollectionModel.MEDICATION_NAME + " VARCHAR(100), "
            + MedicationCollectionModel.QUANTITY + " INTEGER, "
            + MedicationCollectionModel.DOSAGE + " VARCHAR(30), "
            + MedicationCollectionModel.PHARMACY_NAME + " VARCHAR(200), "
            + MedicationCollectionModel.DOCTOR_NAME + " VARCHAR(200), "
            + MedicationCollectionModel.FROM_DATE + " DATE, "
            + MedicationCollectionModel.TO_DATE + " DATE, "
            + MedicationCollectionModel.M_TIME + " VARCHAR(20), "
            + MedicationCollectionModel.A_TIME + " TEXT, "
            + MedicationCollectionModel.E_TIME + " TEXT, "
            + MedicationCollectionModel.N_TIME + " TEXT);";


    private static final String TABLE_PHARMACY_QUERY = "create table "
            + Constants.TABLE_PHARMACY + "("
            + PharmacyCollectionModel.P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PharmacyCollectionModel.PHARMACY_NAME + " TEXT, "
            + PharmacyCollectionModel.PHARMACY_CITY + " TEXT, "
            + PharmacyCollectionModel.PHARMACY_LOCATION + " TEXT, "
            + PharmacyCollectionModel.PHARMACY_EMAIL + " VARCHAR(30), "
            + PharmacyCollectionModel.PHARMACY_NUMBER + " TEXT);";

    private static final String TABLE_DOCTOR_QUERY = "create table "
            + Constants.TABLE_DOCTOR+ "("
            + DoctorCollectionModel.D_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DoctorCollectionModel.DOCTOR_NAME + " VARCHAR(100), "
            + DoctorCollectionModel.HOSPITAL_NAME + " TEXT, "
            + DoctorCollectionModel.HOSPITAL_CITY + " TEXT, "
            + DoctorCollectionModel.HOSPITAL_LOCATION + " TEXT, "
            + DoctorCollectionModel.DOC_HOSP_NUMBER + " TEXT, "
            + DoctorCollectionModel.DOCTOR_EMAIL + " TEXT);";


    private static final String TABLE_CARETAKER_QUERY = "create table "
            + Constants.TABLE_CARETAKER+ "("
            + CareTakerCollectionModel.CT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CareTakerCollectionModel.CT_NAME + " VARCHAR(100), "
            + CareTakerCollectionModel.CT_RELATIONSHIP + " VARCHAR(30), "
            + CareTakerCollectionModel.CT_EMAIL+ " VARCHAR(30), "
            + CareTakerCollectionModel.CT_NUMBER + " TEXT);";

    private static final String TABLE_MEDHISTORY_QUERY = "create table "
            + Constants.TABLE_MEDHISTORY+ "("
            + MedHistoryCollectionModel.MH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MedHistoryCollectionModel.MEDICINE_NAME + " VARCHAR(100), "
            + MedHistoryCollectionModel.FROM_DATE + " VARCHAR(30), "
            + MedHistoryCollectionModel.TO_DATE+ " VARCHAR(30), "
            + MedHistoryCollectionModel.M_TIME+ " VARCHAR(30), "
            + MedHistoryCollectionModel.A_TIME+ " VARCHAR(30), "
            + MedHistoryCollectionModel.E_TIME+ " VARCHAR(30), "
            + MedHistoryCollectionModel.N_TIME+ " VARCHAR(30), "
            + MedHistoryCollectionModel.TAKEN+ " VARCHAR(30), "
            + MedHistoryCollectionModel.SKIP + " VARCHAR(30));";

    private static final String TABLE_REFILL_QUERY = "create table "
            + Constants.TABLE_REFILL+ "("
            + RefillCollectionModel.R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RefillCollectionModel.MEDICINE_NAME + " VARCHAR(100), "
            + RefillCollectionModel.FROM_DATE + " VARCHAR(30), "
            + RefillCollectionModel.TO_DATE+ " VARCHAR(30), "
            + RefillCollectionModel.MIN_QTY+ " VARCHAR(30), "
            + RefillCollectionModel.MAX_QTY+ " VARCHAR(30), "
            + RefillCollectionModel.TAKEN_QTY + " VARCHAR(30));";

    private static final String TABLE_USER_QUERY = "create table "
            + Constants.TABLE_USER+ "("
            + ManageUserCollectionModel.U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ManageUserCollectionModel.USER_NAME + " VARCHAR(100), "
            + ManageUserCollectionModel.PHONE_NUMBER + " VARCHAR(30), "
            + ManageUserCollectionModel.USER_EMAIL + " VARCHAR(30), "
            + ManageUserCollectionModel.BIRTH_DATE + " VARCHAR(30), "
            + ManageUserCollectionModel.BIRTH_MONTH + " VARCHAR(30));";
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMACY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CARETAKER);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CARETAKER);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_REFILL);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MEDHISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
     // Create tables again
        onCreate(db);
    }
    public static synchronized DatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }
    public static void init(Context context) {
        if (null == sInstance) {
            sInstance = new DatabaseHandler(context);
        }
    }
    public static SQLiteDatabase getDatabase() {
        if (null == database) {
            database = sInstance.getWritableDatabase();
        }
        return database;
    }







    //Alarm Cursor

    public static Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_MED_ID,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
                null);
    }

    //Alarm Model need to be created
        public static long create(Alarm alarm) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
            cv.put(COLUMN_MED_ID,alarm.getMedAlarmId());
            cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = null;
                oos = new ObjectOutputStream(bos);
                oos.writeObject(alarm.getDays());
                byte[] buff = bos.toByteArray();

                cv.put(COLUMN_ALARM_DAYS, buff);

            } catch (Exception e){
            }


            cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
            cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
            Log.e(Constants.TAG, "On create alarm name  "+alarm.getAlarmName());
            cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

            return getDatabase().insert(ALARM_TABLE, null, cv);
        }
    public static int update(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_MED_ID,alarm.getMedAlarmId());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e){
        }


        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);
    }




    public static int updateMedId(Alarm alarm,int medid) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_MED_ID,alarm.getMedAlarmId());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e){
        }

        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().update(ALARM_TABLE, cv, "amed_id =" + medid, null);
    }
    public static int deleteEntry(Alarm alarm){
        return deleteEntry(alarm.getId());
    }

    public static int deleteEntry(int id){
        return getDatabase().delete(ALARM_TABLE, COLUMN_ALARM_ID + "=" + id, null);
    }


    public static int deleteMedEntry(int id){
        return getDatabase().delete(ALARM_TABLE, COLUMN_MED_ID + "=" + id, null);
    }
    public static void deactivate() {
        if (null != database && database.isOpen()) {
            database.close();
        }
        database = null;
        sInstance = null;
    }

    public static int deleteAll(){
        return getDatabase().delete(ALARM_TABLE, "1", null);
    }

    public static Alarm getAlarm(int id) {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_MED_ID,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        Cursor c = getDatabase().query(ALARM_TABLE, columns, COLUMN_ALARM_ID+"="+id, null, null, null,
                null);
        Alarm alarm = null;

        if(c.moveToFirst()){

            alarm =  new Alarm();
            alarm.setId(c.getInt(1));
            alarm.setAlarmActive(c.getInt(2)==1);
            alarm.setMed_id(c.getInt(3));
            alarm.setAlarmTime(c.getString(4));
            byte[] repeatDaysBytes = c.getBlob(5);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysBytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Alarm.Day[] repeatDays;
                Object object = objectInputStream.readObject();
                if(object instanceof Alarm.Day[]){
                    repeatDays = (Alarm.Day[]) object;
                    alarm.setDays(repeatDays);
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            alarm.setAlarmTonePath(c.getString(6));
            alarm.setVibrate(c.getInt(7)==1);
            alarm.setAlarmName(c.getString(8));
        }
        c.close();
        return alarm;
    }


    public static List<Alarm> getAlarmMedId(int id) {
        // TODO Auto-generated method stub
        Log.e(Constants.TAG, "getAlarmMedId: "+id);
        List<Alarm> alarms = new ArrayList<Alarm>();
        String[] columns = new String[] {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_MED_ID,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        Cursor c = getDatabase().query(ALARM_TABLE, columns, COLUMN_MED_ID + "=" +id, null, null, null,
                null);
        Alarm alarm = null;
        Log.e(Constants.TAG, "getAlarmMedId: count "+c.getCount());

        if(c.moveToFirst()){

            alarm =  new Alarm();

            alarm.setId(c.getInt(0));
            alarm.setAlarmActive(c.getInt(1)==1);
            alarm.setMed_id(c.getInt(2));
           alarm.setAlarmTime(c.getString(3));
            byte[] repeatDaysBytes = c.getBlob(4);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysBytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Alarm.Day[] repeatDays;
                Object object = objectInputStream.readObject();
                if(object instanceof Alarm.Day[]){
                    repeatDays = (Alarm.Day[]) object;
                    alarm.setDays(repeatDays);
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            alarm.setAlarmTonePath(c.getString(5));
            alarm.setVibrate(c.getInt(6)==1);
            alarm.setAlarmName(c.getString(7));
            alarms.add(alarm);
        }
        c.close();
        Log.e(Constants.TAG, "getAlarmMedId size : "+alarms.size());
        return alarms;
    }
    public static List<Alarm> getAll() {
        List<Alarm> alarms = new ArrayList<Alarm>();
         Cursor cursor = DatabaseHandler.getCursor();
        if (cursor.moveToFirst()) {

            do {


                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setAlarmActive(cursor.getInt(1) == 1);
                alarm.setMed_id(cursor.getInt(2));

                alarm.setAlarmTime(cursor.getString(3));
                byte[] repeatDaysBytes = cursor.getBlob(4);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        repeatDaysBytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            byteArrayInputStream);
                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


                alarm.setAlarmTonePath(cursor.getString(5));
                alarm.setVibrate(cursor.getInt(6) == 1);
                alarm.setAlarmName(cursor.getString(7));

                alarms.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }


}
