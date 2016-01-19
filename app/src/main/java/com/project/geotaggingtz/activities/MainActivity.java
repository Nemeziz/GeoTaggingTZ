package com.project.geotaggingtz.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.geotaggingtz.R;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by Andrey on 17.01.2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.btnTag)
    Button btnTag;
    @Bind(R.id.btnMap)
    Button btnMap;
    @Bind(R.id.btnTotal)
    Button btnTotal;
    @Bind(R.id.txtTitle)
    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnMap.setOnClickListener(this);
        btnTag.setOnClickListener(this);
        btnTotal.setOnClickListener(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Lobster_1.3.otf");
        txtTitle.setTypeface(tf);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnTag:
                intent = new Intent(getApplicationContext(),TaggingActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMap:
                intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTotal:
                intent = new Intent(getApplicationContext(),TotalActivity.class);
                startActivity(intent);
                break;
        }
    }
}
