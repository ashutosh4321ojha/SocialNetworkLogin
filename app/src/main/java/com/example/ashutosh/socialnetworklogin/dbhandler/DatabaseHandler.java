package com.example.ashutosh.socialnetworklogin.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ashutosh.socialnetworklogin.util.UserInformation;



/**
 * Created by ashutosh on 2/29/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userManager";
    private static final String TABLE_INFO = "information";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_ADDRESS = "address";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);

        onCreate(db);
    }

   public void addUser(UserInformation userInformation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

      Log.d("TAG","FRIST"+userInformation.getfName());

        values.put(KEY_FIRST_NAME, userInformation.getfName());
        values.put(KEY_LAST_NAME, userInformation.getlName());
        values.put(KEY_GENDER, userInformation.getGender());
        values.put(KEY_EMAIL, userInformation.getEmail());
        values.put(KEY_ADDRESS, userInformation.getAddress());
        values.put(KEY_IMAGE_URL, userInformation.getImageUri());
        values.put(KEY_PH_NO, userInformation.getPhoneNo());

       db.insert(TABLE_INFO, null, values);
        db.close();
    }




    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

}
