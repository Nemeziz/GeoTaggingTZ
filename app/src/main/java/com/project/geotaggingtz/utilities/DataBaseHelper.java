package com.project.geotaggingtz.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;

/**
 * Created by Andrey on 14.01.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = "MyLogs";
    public static final String LATITUDE = "latitude";
    public static final String LONGTITUDE = "longtitude";
    public static final String MESSAGE = "message";
    public static final String IMAGE = "image";
    public static final String TABLE_NAME = "geotable";
    public static final String DATABASE_NAME = "myDB";
    public static final String DATE = "date";
    public static ContentValues cv = new ContentValues();

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table " + TABLE_NAME + " ("
                + "id integer primary key autoincrement, "
                + LATITUDE + " real,"
                + LONGTITUDE + " real,"
                + MESSAGE + " text,"
                + DATE + " long,"
                + IMAGE + " blob" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public static void writeGeoTagToDB(DataBaseHelper dbHelper, TextView longtitude, TextView latitude, String msg, ImageView imageView) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put(LONGTITUDE, (longtitude.getText().toString()));
        cv.put(LATITUDE, (latitude.getText().toString()));
        cv.put(MESSAGE, msg);
        cv.put(DATE, UtilityClass.countCurrentDate());
        cv.put(IMAGE, UtilityClass.getByteImage(imageView));
        db.insert(DataBaseHelper.TABLE_NAME, null, cv);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public static LatLngBounds showGeoTagsOnMap(GoogleMap map,DataBaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.deleteAll();
        LatLngBounds bounds = null;
        Cursor cursor = db.query(DataBaseHelper.TABLE_NAME, null, null, null, null, null, null);
        LatLngBounds.Builder lngBounds = new LatLngBounds.Builder();
        if (cursor.moveToFirst()) {
            int latitudeColIndex = cursor.getColumnIndex(DataBaseHelper.LATITUDE);
            int longtitudeColIndex = cursor.getColumnIndex(DataBaseHelper.LONGTITUDE);
            int msgColIndex = cursor.getColumnIndex(DataBaseHelper.MESSAGE);
            int imageColIndex = cursor.getColumnIndex(DataBaseHelper.IMAGE);
            do {
                LatLng lng = new LatLng(Double.parseDouble(cursor.getString(latitudeColIndex))
                        , Double.parseDouble(cursor.getString(longtitudeColIndex)));
                lngBounds.include(lng);
                String text = cursor.getString(msgColIndex);
                Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(imageColIndex), 0, cursor.getBlob(imageColIndex).length);
                map.addMarker(new MarkerOptions().position(lng)
                        .title(text)
                        .icon(BitmapDescriptorFactory.fromBitmap(UtilityClass.getResizedBitmap(bitmap, 150, 150))));
            }
            while (cursor.moveToNext());
            bounds = lngBounds.build();
        }
        cursor.close();
        db.close();
        return bounds;
    }



    public void logDataBase() {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        Cursor c = database.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int lontdColIndex = c.getColumnIndex(LONGTITUDE);
            int latiColIndex = c.getColumnIndex(LATITUDE);
            int msgColIndex = c.getColumnIndex(MESSAGE);
            int dateColIndex = c.getColumnIndex(DATE);
            int imgColIndex = c.getColumnIndex(IMAGE);
            do {
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", latitude = " + c.getDouble(latiColIndex) +
                                ", longtitude = " + c.getDouble(lontdColIndex) +
                                ", message = " + c.getString(msgColIndex) +
                                ", date = " + c.getLong(dateColIndex) +
                                ", img = " + Arrays.toString(c.getBlob(imgColIndex)));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }
}