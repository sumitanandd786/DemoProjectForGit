package com.demoproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.demoproject.MainActivity;
import com.demoproject.R;
import com.demoproject.adapters.ShoppingCartListAdapter;
import com.demoproject.adapters.ShoppingCartListAdapter.MonitorListItems;
import com.demoproject.adapters.ShoppingCartListAdapter.UpdatePayableAmount;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.pojo.Cart;
import com.demoproject.utils.Constants;
import java.text.DecimalFormat;
import java.util.List;

public class ShoppingCart extends AppCompatActivity implements MonitorListItems, UpdatePayableAmount {
    Toolbar toolbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        ((TextView) findViewById(R.id.titleToolbar)).setText(R.string.shopping_cart);
        ((ImageView) findViewById(R.id.cart)).setVisibility(8);
        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(0);
        backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShoppingCart.this.onBackPressed();
            }
        });
        final SessionManager sessionManager = new SessionManager(this);
        final DB_Handler db_handler = new DB_Handler(this);
        final List<Cart> shoppingCart = db_handler.getCartItems(sessionManager.getSessionData(Constants.SESSION_EMAIL));
        ((ListView) findViewById(R.id.listview)).setAdapter(new ShoppingCartListAdapter(this, shoppingCart));
        setPayableAmount(shoppingCart);
        ((Button) findViewById(R.id.placeOrder)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                db_handler.deleteCartItems();
                db_handler.insertOrderHistory(shoppingCart, sessionManager.getSessionData(Constants.SESSION_EMAIL));
                Toast.makeText(ShoppingCart.this.getApplicationContext(), "Order Placed Successfully", 1).show();
                Intent intent = new Intent(ShoppingCart.this.getApplicationContext(), MainActivity.class);
                intent.setFlags(335544320);
                ShoppingCart.this.startActivity(intent);
                ShoppingCart.this.overridePendingTransition(0, 0);
                ShoppingCart.this.finish();
            }
        });
    }

    @SuppressLint({"SetTextI18n"})
    private void setPayableAmount(List<Cart> shoppingCart) {
        Double totalAmount = Double.valueOf(0.0d);
        for (int i = 0; i < shoppingCart.size(); i++) {
            int itemQuantity = ((Cart) shoppingCart.get(i)).getItemQuantity().intValue();
            totalAmount = Double.valueOf(totalAmount.doubleValue() + Double.valueOf((Double.valueOf(((Cart) shoppingCart.get(i)).getVariant().getPrice()).doubleValue() + ((Cart) shoppingCart.get(i)).getProduct().getTax().getValue().doubleValue()) * ((double) itemQuantity)).doubleValue());
        }
        ((TextView) findViewById(R.id.payableAmt)).setText("Rs." + new DecimalFormat("#,###.00").format(totalAmount));
    }

    public void updatePayableAmount(List<Cart> shoppingCart) {
        setPayableAmount(shoppingCart);
    }

    public void finishActivity(List<Cart> shoppingCart) {
        try {
            if (shoppingCart.size() == 0) {
                overridePendingTransition(0, 0);
                finish();
            }
        } catch (Exception e) {
            overridePendingTransition(0, 0);
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
