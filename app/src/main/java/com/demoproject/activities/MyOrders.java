package com.demoproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.demoproject.adapters.MyOrdersAdapter;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.utils.Constants;

public class MyOrders extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorders);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((TextView) findViewById(R.id.titleToolbar)).setText(R.string.my_orders);
        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(0);
        backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyOrders.this.onBackPressed();
            }
        });
        ((ImageView) findViewById(R.id.cart)).setVisibility(8);
        ((ListView) findViewById(R.id.listview)).setAdapter(new MyOrdersAdapter(this, new DB_Handler(this).getOrders(new SessionManager(this).getSessionData(Constants.SESSION_EMAIL))));
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
