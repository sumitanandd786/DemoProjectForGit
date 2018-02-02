package com.demoproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class FilterItemListAdapter extends BaseExpandableListAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = (!FilterItemListAdapter.class.desiredAssertionStatus());
    private Context _context;
    private HashMap<String, List<String>> _listDataChild;
    private List<String> _listDataHeader;
    private List<String> colorFilter;
    private List<String> sizeFilter;

    public FilterItemListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, List<String> sizeFilter, List<String> colorFilter) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.sizeFilter = sizeFilter;
        this.colorFilter = colorFilter;
    }

    public Object getChild(int groupPosition, int childPosititon) {
        return ((List) this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosititon);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    @SuppressLint({"InflateParams"})
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService("layout_inflater");
            if ($assertionsDisabled || infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.sort_filter_listitem, null);
            } else {
                throw new AssertionError();
            }
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.name);
        ImageView img = (ImageView) convertView.findViewById(R.id.tick);
        switch (groupPosition) {
            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_gravity /*0*/:
                try {
                    if (!this.sizeFilter.contains(childText)) {
                        img.setVisibility(8);
                        break;
                    }
                    img.setVisibility(0);
                    break;
                } catch (Exception e) {
                    img.setVisibility(8);
                    break;
                }
            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_orientation /*1*/:
                try {
                    if (!this.colorFilter.contains("'" + childText + "'")) {
                        img.setVisibility(8);
                        break;
                    }
                    img.setVisibility(0);
                    break;
                } catch (Exception e2) {
                    img.setVisibility(8);
                    break;
                }
        }
        txtListChild.setText(childText);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return ((List) this._listDataChild.get(this._listDataHeader.get(groupPosition))).size();
    }

    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    @SuppressLint({"InflateParams"})
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService("layout_inflater");
            if ($assertionsDisabled || infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.sort_filter_listitem, null);
            } else {
                throw new AssertionError();
            }
        }
        ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
        arrow.setVisibility(0);
        if (isExpanded) {
            arrow.setImageResource(R.drawable.ic_chevron_down_grey600_24dp);
        } else {
            arrow.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
        }
        TextView count = (TextView) convertView.findViewById(R.id.count);
        switch (groupPosition) {
            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_gravity /*0*/:
                try {
                    if (this.sizeFilter.size() <= 0) {
                        count.setVisibility(8);
                        break;
                    }
                    count.setVisibility(0);
                    count.setText(String.valueOf(this.sizeFilter.size()));
                    break;
                } catch (NullPointerException e) {
                    count.setVisibility(8);
                    break;
                }
            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_orientation /*1*/:
                try {
                    if (this.colorFilter.size() <= 0) {
                        count.setVisibility(8);
                        break;
                    }
                    count.setVisibility(0);
                    count.setText(String.valueOf(this.colorFilter.size()));
                    break;
                } catch (NullPointerException e2) {
                    count.setVisibility(8);
                    break;
                }
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.name);
        lblListHeader.setTypeface(null, 1);
        lblListHeader.setTextColor(-16777216);
        lblListHeader.setTextSize(16.0f);
        lblListHeader.setText(headerTitle);
        ((ImageView) convertView.findViewById(R.id.tick)).setVisibility(8);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
