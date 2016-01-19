package com.project.geotaggingtz.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.geotaggingtz.utilities.DataBaseHelper;
import com.project.geotaggingtz.R;
import com.project.geotaggingtz.utilities.UtilityClass;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TotalActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.btnWatchImg)
    Button btnWatchImgs;
    @Bind(R.id.txtCountTotal)
    TextView txtCountTotalDist;
    private SQLiteDatabase sqDb;
    private DataBaseHelper dbHelper;
    private boolean isFirst = true;
    private double latitude1 = 0;
    private double longtitude1 = 0;
    private double latitude2 = 0;
    private double longtitude2 = 0;
    private double totalDistance = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_info);
        ButterKnife.bind(this);
        btnWatchImgs.setOnClickListener(this);
        dbHelper = new DataBaseHelper(this);
        sqDb = dbHelper.getWritableDatabase();
        countDistance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWatchImg:
                Intent intent = new Intent(getApplicationContext(),ImageListActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void countDistance(){
        Cursor cursor = sqDb.query(DataBaseHelper.TABLE_NAME, new String[]{DataBaseHelper.LATITUDE,DataBaseHelper.LONGTITUDE}, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                int longIndex = cursor.getColumnIndex(DataBaseHelper.LONGTITUDE);
                int latitIndex = cursor.getColumnIndex(DataBaseHelper.LATITUDE);
                latitude2 = cursor.getDouble(latitIndex);
                longtitude2 = cursor.getDouble(longIndex);
                if (!isFirst)
                    totalDistance += UtilityClass.distance(latitude1, longtitude1, latitude2, longtitude2);
                latitude1 = latitude2;
                longtitude1 = longtitude2;
                isFirst = false;
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqDb.close();
        txtCountTotalDist.setText(Double.toString(UtilityClass.round(totalDistance)) + " kilometres");
    }
}
