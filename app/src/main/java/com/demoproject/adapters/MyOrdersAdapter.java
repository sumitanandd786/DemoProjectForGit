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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.demoproject.activities.ProductDetails;
import com.demoproject.pojo.Cart;
import com.demoproject.utils.Util;
import java.util.List;

public class MyOrdersAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Cart> shoppingCart;

    public class Holder {
        TextView color;
        RelativeLayout itemLay;
        TextView price;
        TextView qty;
        LinearLayout qtyLay;
        ImageView remove;
        TextView size;
        TextView tax;
        TextView title;
    }

    public MyOrdersAdapter(Context context, List<Cart> shoppingCart) {
        this.context = context;
        this.shoppingCart = shoppingCart;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.shoppingCart.size();
    }

    public Object getItem(int i) {
        return this.shoppingCart.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    public View getView(final int position, View view, ViewGroup viewGroup) {
        int[] quantity;
        String taxName;
        Double taxValue;
        Double priceValue;
        Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.shoppingcart_item, null);
        holder.title = (TextView) rowView.findViewById(R.id.title);
        holder.size = (TextView) rowView.findViewById(R.id.size);
        holder.color = (TextView) rowView.findViewById(R.id.color);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.tax = (TextView) rowView.findViewById(R.id.tax);
        holder.qty = (TextView) rowView.findViewById(R.id.quantity);
        holder.remove = (ImageView) rowView.findViewById(R.id.remove);
        holder.qtyLay = (LinearLayout) rowView.findViewById(R.id.qtyLay);
        holder.title.setText(((Cart) this.shoppingCart.get(position)).getProduct().getName());
        holder.color.setText("Color: " + ((Cart) this.shoppingCart.get(position)).getVariant().getColor());
        String size = String.valueOf(((Cart) this.shoppingCart.get(position)).getVariant().getSize());
        if (size != null) {
            try {
                if (!(size.equalsIgnoreCase("null") || size.equalsIgnoreCase("0.0"))) {
                    holder.size.setText("Size: " + size);
                    quantity = new int[]{((Cart) this.shoppingCart.get(position)).getItemQuantity().intValue()};
                    taxName = ((Cart) this.shoppingCart.get(position)).getProduct().getTax().getName();
                    taxValue = ((Cart) this.shoppingCart.get(position)).getProduct().getTax().getValue();
                    priceValue = Double.valueOf(((Cart) this.shoppingCart.get(position)).getVariant().getPrice());
                    holder.qty.setVisibility(0);
                    holder.qty.setText(String.valueOf("Quantity: " + quantity[0]));
                    holder.price.setText("Rs." + Util.formatDouble(calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
                    holder.tax.setText("(" + taxName + ": Rs." + taxValue + ")");
                    holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
                    holder.itemLay.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(MyOrdersAdapter.this.context, ProductDetails.class);
                            intent.putExtra("ProductId", ((Cart) MyOrdersAdapter.this.shoppingCart.get(position)).getProduct().getId());
                            MyOrdersAdapter.this.context.startActivity(intent);
                        }
                    });
                    holder.remove.setVisibility(8);
                    holder.qtyLay.setVisibility(8);
                    return rowView;
                }
            } catch (NullPointerException e) {
                holder.size.setVisibility(8);
            }
        }
        holder.size.setVisibility(8);
        quantity = new int[]{((Cart) this.shoppingCart.get(position)).getItemQuantity().intValue()};
        taxName = ((Cart) this.shoppingCart.get(position)).getProduct().getTax().getName();
        taxValue = ((Cart) this.shoppingCart.get(position)).getProduct().getTax().getValue();
        priceValue = Double.valueOf(((Cart) this.shoppingCart.get(position)).getVariant().getPrice());
        holder.qty.setVisibility(0);
        holder.qty.setText(String.valueOf("Quantity: " + quantity[0]));
        holder.price.setText("Rs." + Util.formatDouble(calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
        holder.tax.setText("(" + taxName + ": Rs." + taxValue + ")");
        holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(/* anonymous class already generated */);
        holder.remove.setVisibility(8);
        holder.qtyLay.setVisibility(8);
        return rowView;
    }

    private Double calculatePrice(Double taxValue, Double priceValue, int quantity) {
        return Double.valueOf((taxValue.doubleValue() + priceValue.doubleValue()) * ((double) quantity));
    }
}
