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
import android.widget.Toast;
import com.demoproject.R;
import com.demoproject.activities.ProductDetails;
import com.demoproject.database.DB_Handler;
import com.demoproject.pojo.Cart;
import com.demoproject.utils.Util;
import java.util.List;

public class ShoppingCartListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Cart> shoppingCart;

    public interface MonitorListItems {
        void finishActivity(List<Cart> list);
    }

    public interface UpdatePayableAmount {
        void updatePayableAmount(List<Cart> list);
    }

    public class Holder {
        TextView color;
        RelativeLayout itemLay;
        ImageView minus;
        ImageView plus;
        TextView price;
        TextView qty;
        ImageView remove;
        TextView size;
        TextView tax;
        TextView title;
    }

    public ShoppingCartListAdapter(Context context, List<Cart> shoppingCart) {
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
        final int[] quantity;
        String taxName;
        final Double taxValue;
        final Double priceValue;
        final int i;
        final Holder holder = new Holder();
        View rowView = this.inflater.inflate(R.layout.shoppingcart_item, null);
        holder.title = (TextView) rowView.findViewById(R.id.title);
        holder.size = (TextView) rowView.findViewById(R.id.size);
        holder.color = (TextView) rowView.findViewById(R.id.color);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.tax = (TextView) rowView.findViewById(R.id.tax);
        holder.qty = (TextView) rowView.findViewById(R.id.quantityValue);
        holder.remove = (ImageView) rowView.findViewById(R.id.remove);
        holder.minus = (ImageView) rowView.findViewById(R.id.minus);
        holder.plus = (ImageView) rowView.findViewById(R.id.plus);
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
                    holder.qty.setText(String.valueOf(quantity[0]));
                    holder.price.setText("Rs." + Util.formatDouble(calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
                    holder.tax.setText("(" + taxName + ": Rs." + taxValue + ")");
                    holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
                    holder.itemLay.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(ShoppingCartListAdapter.this.context, ProductDetails.class);
                            intent.putExtra("ProductId", ((Cart) ShoppingCartListAdapter.this.shoppingCart.get(position)).getProduct().getId());
                            ShoppingCartListAdapter.this.context.startActivity(intent);
                        }
                    });
                    holder.remove.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            if (new DB_Handler(ShoppingCartListAdapter.this.context).deleteCartItem(((Cart) ShoppingCartListAdapter.this.shoppingCart.get(position)).getId().intValue())) {
                                ShoppingCartListAdapter.this.shoppingCart.remove(position);
                                ShoppingCartListAdapter.this.notifyDataSetChanged();
                                if (ShoppingCartListAdapter.this.context instanceof UpdatePayableAmount) {
                                    ((UpdatePayableAmount) ShoppingCartListAdapter.this.context).updatePayableAmount(ShoppingCartListAdapter.this.shoppingCart);
                                }
                                if (ShoppingCartListAdapter.this.context instanceof MonitorListItems) {
                                    ((MonitorListItems) ShoppingCartListAdapter.this.context).finishActivity(ShoppingCartListAdapter.this.shoppingCart);
                                    return;
                                }
                                return;
                            }
                            Toast.makeText(ShoppingCartListAdapter.this.context, "error deleting item", 1).show();
                        }
                    });
                    i = position;
                    holder.minus.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            if (quantity[0] != 1) {
                                int[] iArr = quantity;
                                iArr[0] = iArr[0] - 1;
                                ShoppingCartListAdapter.this.updateQuantity(quantity[0], i);
                                ((Cart) ShoppingCartListAdapter.this.shoppingCart.get(i)).setItemQuantity(Integer.valueOf(quantity[0]));
                                holder.qty.setText(String.valueOf(quantity[0]));
                                holder.price.setText("Rs." + Util.formatDouble(ShoppingCartListAdapter.this.calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
                                if (ShoppingCartListAdapter.this.context instanceof UpdatePayableAmount) {
                                    ((UpdatePayableAmount) ShoppingCartListAdapter.this.context).updatePayableAmount(ShoppingCartListAdapter.this.shoppingCart);
                                }
                            }
                        }
                    });
                    i = position;
                    holder.plus.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            int[] iArr = quantity;
                            iArr[0] = iArr[0] + 1;
                            ShoppingCartListAdapter.this.updateQuantity(quantity[0], i);
                            ((Cart) ShoppingCartListAdapter.this.shoppingCart.get(i)).setItemQuantity(Integer.valueOf(quantity[0]));
                            holder.qty.setText(String.valueOf(quantity[0]));
                            holder.price.setText("Rs." + Util.formatDouble(ShoppingCartListAdapter.this.calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
                            if (ShoppingCartListAdapter.this.context instanceof UpdatePayableAmount) {
                                ((UpdatePayableAmount) ShoppingCartListAdapter.this.context).updatePayableAmount(ShoppingCartListAdapter.this.shoppingCart);
                            }
                        }
                    });
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
        holder.qty.setText(String.valueOf(quantity[0]));
        holder.price.setText("Rs." + Util.formatDouble(calculatePrice(taxValue, priceValue, quantity[0]).doubleValue()));
        holder.tax.setText("(" + taxName + ": Rs." + taxValue + ")");
        holder.itemLay = (RelativeLayout) rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(/* anonymous class already generated */);
        holder.remove.setOnClickListener(/* anonymous class already generated */);
        i = position;
        holder.minus.setOnClickListener(/* anonymous class already generated */);
        i = position;
        holder.plus.setOnClickListener(/* anonymous class already generated */);
        return rowView;
    }

    private Double calculatePrice(Double taxValue, Double priceValue, int quantity) {
        return Double.valueOf((taxValue.doubleValue() + priceValue.doubleValue()) * ((double) quantity));
    }

    private void updateQuantity(int quantity, int position) {
        new DB_Handler(this.context).updateItemQuantity(quantity, ((Cart) this.shoppingCart.get(position)).getId().intValue());
    }
}
