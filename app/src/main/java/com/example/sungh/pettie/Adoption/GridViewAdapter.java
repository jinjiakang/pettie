package com.example.sungh.pettie.Adoption;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.sungh.pettie.R;

import java.util.ArrayList;



public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

    public GridViewAdapter(Context context, int layoutResourceId,
                           ArrayList<ImageItem> data) {
        //向父類別取得這些的值?
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public ImageItem getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.adoption_image = (ImageView) row.findViewById(R.id.adoption_imgview);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position);
        holder.adoption_image.setImageBitmap(item.getImage());
        holder.adoption_image.setMaxHeight(50);
        holder.adoption_image.setMaxWidth(50);
        return row;
    }




    static class ViewHolder {
        ImageView adoption_image;
    }


}