package com.havudong.havudong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    private int[] icons;

    public CustomAdapter(Context context, String[] items, int[] icons) {
        this.context = context;
        this.items = items;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_custom, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.itemIcon);
        TextView name = convertView.findViewById(R.id.itemName);

        icon.setImageResource(icons[position]);
        name.setText(items[position]);

        return convertView;
    }
}