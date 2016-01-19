package com.project.geotaggingtz.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.geotaggingtz.utilities.DataBaseHelper;
import com.project.geotaggingtz.R;
import com.project.geotaggingtz.utilities.UtilityClass;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataBaseHelper dbHelper;
    public static final int PADDING = 150;
    LatLngBounds bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHelper = new DataBaseHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showGeoTagsOnMap(mMap);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, PADDING));
            }
        });
    }


    public void showGeoTagsOnMap(GoogleMap map){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.deleteAll();
        Cursor cursor = db.query(DataBaseHelper.TABLE_NAME, null, null, null, null, null, null);
        LatLngBounds.Builder lngBounds = new LatLngBounds.Builder();
        if (cursor.moveToFirst()) {
            int latitudeColIndex = cursor.getColumnIndex(DataBaseHelper.LATITUDE);
            int longtitudeColIndex = cursor.getColumnIndex(DataBaseHelper.LONGTITUDE);
            int msgColIndex = cursor.getColumnIndex(DataBaseHelper.MESSAGE);
            int imageColIndex = cursor.getColumnIndex(DataBaseHelper.IMAGE);
            do {
                LatLng lng = new LatLng(cursor.getDouble(latitudeColIndex), cursor.getDouble(longtitudeColIndex));
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
    }

}

