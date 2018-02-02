package com.demoproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.demoproject.R;
import com.demoproject.database.DB_Handler;
import com.demoproject.pojo.Category;
import com.demoproject.utils.ExpandableHeightGridView;
import java.util.List;

public class CategoryListAdapter extends BaseAdapter {
    private List<Category> categoryList;
    private Context context;
    private DB_Handler db_handler;
    private LayoutInflater inflater;

    public class Holder {
        TextView category;
        ExpandableHeightGridView expandableHeightGridView;
    }

    public CategoryListAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.db_handler = new DB_Handler(context);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.categoryList.size();
    }

    public Object getItem(int i) {
        return this.categoryList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.categories_list_item, null);
        holder.category = (TextView) rowView.findViewById(R.id.category);
        holder.category.setText(((Category) this.categoryList.get(position)).getName());
        List<Category> subCategoryList = this.db_handler.getSubcategoryList(((Category) this.categoryList.get(position)).getId().intValue());
        holder.expandableHeightGridView = (ExpandableHeightGridView) rowView.findViewById(R.id.subcategories);
        holder.expandableHeightGridView.setAdapter(new SubcategoryGridAdapter(this.context, subCategoryList));
        holder.expandableHeightGridView.setExpanded(true);
        return rowView;
    }
}
