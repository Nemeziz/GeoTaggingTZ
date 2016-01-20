package com.project.geotaggingtz.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrey on 18.01.2016.
 */
public class UtilityClass {

    public static Bitmap getImage(byte[] arrImage) {
        return BitmapFactory.decodeByteArray(arrImage, 0, arrImage.length);
    }

    public static Bitmap getImageForListView(byte[] arrImage) {
        return UtilityClass.getResizedBitmap(BitmapFactory.decodeByteArray(arrImage, 0, arrImage.length), 400, 400);
    }

    public static byte[] getByteImage(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    public static void setImage(EditText txt, Context context, ImageView imageView) {
        String urlText = txt.getText().toString();
        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            Toast.makeText(context,"Incorrect URL!! Try again!",Toast.LENGTH_SHORT).show();
        }
        Picasso.with(context).load(String.valueOf(url)).into(imageView);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.60934;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static double round(double d) {
        return new BigDecimal(d).setScale(2, RoundingMode.UP).doubleValue();
    }

    public static long countCurrentDate() {
        Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        return new Date(year, month, day).getTime();
    }

}
