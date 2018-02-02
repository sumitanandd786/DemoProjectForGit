package com.demoproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.demoproject.MainActivity;
import com.demoproject.database.DB_Handler;
import com.demoproject.fragments.Products;
import com.demoproject.fragments.Subcategories;
import com.demoproject.pojo.Category;
import com.demoproject.utils.Constants;
import java.io.Serializable;
import java.util.List;

public class SubcategoryGridAdapter extends BaseAdapter {
    private Context context;
    private DB_Handler db_handler;
    private LayoutInflater inflater;
    private List<Category> subCategoryList;

    public class Holder {
        TextView category;
        RelativeLayout gridItemLayout;
    }

    public SubcategoryGridAdapter(Context context, List<Category> subCategoryList) {
        this.context = context;
        this.subCategoryList = subCategoryList;
        this.db_handler = new DB_Handler(context);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.subCategoryList.size();
    }

    public Object getItem(int i) {
        return this.subCategoryList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.categories_grid_item, null);
        holder.category = (TextView) rowView.findViewById(R.id.name);
        holder.category.setText(((Category) this.subCategoryList.get(position)).getName());
        holder.gridItemLayout = (RelativeLayout) rowView.findViewById(R.id.gridItemLayouut);
        holder.gridItemLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int id = ((Category) SubcategoryGridAdapter.this.subCategoryList.get(position)).getId().intValue();
                List<Category> childCategories = SubcategoryGridAdapter.this.db_handler.getSubcategoryList(id);
                Bundle bundle = new Bundle();
                FragmentTransaction ft = ((MainActivity) SubcategoryGridAdapter.this.context).getSupportFragmentManager().beginTransaction();
                if (childCategories.size() > 0) {
                    bundle.putString(Constants.TITLE, ((Category) SubcategoryGridAdapter.this.subCategoryList.get(position)).getName());
                    bundle.putSerializable(Constants.CAT_KEY, (Serializable) childCategories);
                    Subcategories subcategories = new Subcategories();
                    subcategories.setArguments(bundle);
                    ft.replace(R.id.content, subcategories, Constants.FRAG_SUBCAT);
                    ft.addToBackStack(null);
                    ft.commit();
                    return;
                }
                bundle.putInt(Constants.CAT_ID_KEY, id);
                bundle.putString(Constants.TITLE, ((Category) SubcategoryGridAdapter.this.subCategoryList.get(position)).getName());
                Products products = new Products();
                products.setArguments(bundle);
                ft.replace(R.id.content, products, Constants.FRAG_PDT);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return rowView;
    }
}
