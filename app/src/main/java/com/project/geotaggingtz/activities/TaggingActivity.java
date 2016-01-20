package com.project.geotaggingtz.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.project.geotaggingtz.utilities.DataBaseHelper;
import com.project.geotaggingtz.R;
import com.project.geotaggingtz.utilities.UtilityClass;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrey on 14.01.2016.
 */
public class TaggingActivity extends AppCompatActivity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
    @Bind(R.id.btnURL)
    Button btnURL;
    @Bind(R.id.btnGallery)
    Button btnGallery;
    @Bind(R.id.btnAddMsg)
    Button btnMsg;
    @Bind(R.id.btnAddGeoTag)
    Button btnAddGeoTag;
    @Bind(R.id.img)
    ImageView imageView;
    @Bind(R.id.coordLong)
    TextView coorLongtitude;
    @Bind(R.id.coordLatit)
    TextView coorLatitude;
    private GoogleApiClient mGoogleApiClient;
    private DataBaseHelper dbHelper;
    private String msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);
        ButterKnife.bind(this);
        btnURL.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAddGeoTag.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        buildGoogleApiClient();
        dbHelper = new DataBaseHelper(this);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            coorLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
            coorLongtitude.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            Toast.makeText(getApplicationContext(), "Could not get location. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnURL:
                setImageWithDialog();
                break;
            case R.id.btnGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            case R.id.btnAddMsg:
                setMessageWithDialog();
                break;
            case R.id.btnAddGeoTag:
                if (imageView.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Set image first!", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseHelper.writeGeoTagToDB(dbHelper, coorLongtitude, coorLatitude, msgText, imageView);
//                    dbHelper.logDataBase();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Picasso.with(this).load(data.getData()).into(imageView);
            displayLocation();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void setImageWithDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_url, null);
        builder.setView(dialogView);
        final EditText urlImageEdtTxt = (EditText) dialogView.findViewById(R.id.urlImage);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                UtilityClass.setImage(urlImageEdtTxt, getApplicationContext(), imageView);
                displayLocation();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        })
                .create()
                .show();
    }

    public void setMessageWithDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View msgView = inflater.inflate(R.layout.layout_message, null);
        builder.setView(msgView);
        final EditText msgEdtTxt = (EditText) msgView.findViewById(R.id.editMessage);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                msgText = msgEdtTxt.getText().toString();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}



