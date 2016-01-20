package com.project.geotaggingtz.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.project.geotaggingtz.models.ImageItem;
import com.project.geotaggingtz.R;
import com.project.geotaggingtz.utilities.UtilityClass;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<ImageItem> {

    Context context;
    LayoutInflater mInflater;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<ImageItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ImageItem imageItem = getItem(position);
        if (convertView == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_images, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.imageView.setImageBitmap(UtilityClass.getImage(imageItem.getBlob()));
        return convertView;
    }
}
