package com.demoproject.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.demoproject.R;
import com.demoproject.activities.ProductDetails;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.pojo.Product;
import com.demoproject.utils.Constants;
import java.util.List;

public class ProductListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Product> productList;

    public class Holder {
        ImageView heart;
        RelativeLayout itemLay;
        TextView name;
        TextView price;
    }

    public ProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.productList.size();
    }

    public Object getItem(int i) {
        return this.productList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.product_grid_item, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.heart = (ImageView) rowView.findViewById(R.id.heart);
        holder.name.setText(((Product) this.productList.get(position)).getName());
        holder.price.setText(((Product) this.productList.get(position)).getPrice_range());
        holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ProductListAdapter.this.context, ProductDetails.class);
                intent.putExtra("ProductId", ((Product) ProductListAdapter.this.productList.get(position)).getId());
                ProductListAdapter.this.context.startActivity(intent);
                ((Activity) ProductListAdapter.this.context).overridePendingTransition(0, 0);
            }
        });
        if (((Product) this.productList.get(position)).getShortlisted().booleanValue()) {
            holder.heart.setImageResource(R.drawable.ic_heart_grey);
        }
        holder.heart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DB_Handler db_handler = new DB_Handler(ProductListAdapter.this.context);
                SessionManager sessionManager = new SessionManager(ProductListAdapter.this.context);
                if (((Product) ProductListAdapter.this.productList.get(position)).getShortlisted().booleanValue()) {
                    holder.heart.setImageResource(R.drawable.ic_heart_grey600_24dp);
                    if (db_handler.removeShortlistedItem(((Product) ProductListAdapter.this.productList.get(position)).getId().intValue(), sessionManager.getSessionData(Constants.SESSION_EMAIL))) {
                        ((Product) ProductListAdapter.this.productList.get(position)).setShortlisted(Boolean.valueOf(false));
                        Toast.makeText(ProductListAdapter.this.context, "Item Removed From Wish List", 1).show();
                        return;
                    }
                    return;
                }
                holder.heart.setImageResource(R.drawable.ic_heart_grey);
                if (db_handler.shortlistItem(((Product) ProductListAdapter.this.productList.get(position)).getId().intValue(), sessionManager.getSessionData(Constants.SESSION_EMAIL)) > 0) {
                    ((Product) ProductListAdapter.this.productList.get(position)).setShortlisted(Boolean.valueOf(true));
                    Toast.makeText(ProductListAdapter.this.context, "Item Added To Wish List", 1).show();
                }
            }
        });
        return rowView;
    }
}
