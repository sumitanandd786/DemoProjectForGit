package com.demoproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SortItemListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int selectedId;
    private String[] sortBy;

    public class Holder {
        TextView name;
        ImageView tick;
    }

    public SortItemListAdapter(Context context, String[] sortBy, int selectedId) {
        this.sortBy = sortBy;
        this.selectedId = selectedId;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.sortBy.length;
    }

    public Object getItem(int i) {
        return this.sortBy[i];
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.sort_filter_listitem, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.tick = (ImageView) rowView.findViewById(R.id.tick);
        holder.name.setText(this.sortBy[position]);
        if (position == this.selectedId) {
            holder.tick.setVisibility(0);
        } else {
            holder.tick.setVisibility(8);
        }
        return rowView;
    }
}
