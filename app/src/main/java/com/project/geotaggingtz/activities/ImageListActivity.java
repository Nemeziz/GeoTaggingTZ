package com.project.geotaggingtz.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.project.geotaggingtz.adapters.CustomListViewAdapter;
import com.project.geotaggingtz.utilities.DataBaseHelper;
import com.project.geotaggingtz.models.ImageItem;
import com.project.geotaggingtz.R;
import com.project.geotaggingtz.utilities.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 18.01.2016.
 */
public class ImageListActivity extends AppCompatActivity {
    private ListView listView;
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private List<ImageItem> imageItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        dbHelper = new DataBaseHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME, new String[]{DataBaseHelper.IMAGE,DataBaseHelper.DATE},
                DataBaseHelper.DATE + " = " + UtilityClass.countCurrentDate(), null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                int imageColIndex = cursor.getColumnIndex(DataBaseHelper.IMAGE);
                ImageItem item = new ImageItem(cursor.getBlob(imageColIndex));
                imageItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        listView = (ListView) findViewById(R.id.list);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.list_images, imageItemList);
        listView.setAdapter(adapter);
    }
}
