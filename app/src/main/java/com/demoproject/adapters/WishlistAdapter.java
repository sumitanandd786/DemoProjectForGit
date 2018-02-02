package com.demoproject.adapters;

import android.annotation.SuppressLint;
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
import com.demoproject.activities.ProductDetails;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.pojo.Product;
import com.demoproject.utils.Constants;
import java.util.List;

public class WishlistAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Product> productList;

    public class Holder {
        RelativeLayout itemLay;
        TextView price;
        ImageView remove;
        TextView title;
    }

    public WishlistAdapter(Context context, List<Product> productList) {
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
        Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.wishlist_item, null);
        holder.title = (TextView) rowView.findViewById(R.id.title);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.remove = (ImageView) rowView.findViewById(R.id.remove);
        holder.title.setText(((Product) this.productList.get(position)).getName());
        holder.price.setText(((Product) this.productList.get(position)).getPrice_range());
        holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(WishlistAdapter.this.context, ProductDetails.class);
                intent.putExtra("ProductId", ((Product) WishlistAdapter.this.productList.get(position)).getId());
                WishlistAdapter.this.context.startActivity(intent);
            }
        });
        holder.remove.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (new DB_Handler(WishlistAdapter.this.context).removeShortlistedItem(((Product) WishlistAdapter.this.productList.get(position)).getId().intValue(), new SessionManager(WishlistAdapter.this.context).getSessionData(Constants.SESSION_EMAIL))) {
                    WishlistAdapter.this.productList.remove(position);
                    WishlistAdapter.this.notifyDataSetChanged();
                }
            }
        });
        return rowView;
    }
}
