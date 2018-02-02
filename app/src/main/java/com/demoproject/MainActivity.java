package com.demoproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.demoproject.database.DB_Handler;
import com.demoproject.fragments.Subcategories;
import com.demoproject.interfaces.FinishActivity;
import com.demoproject.interfaces.ShowBackButton;
import com.demoproject.interfaces.ToolbarTitle;
import com.demoproject.pojo.Category;

import java.util.List;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.demoproject.activities.ShoppingCart;
import com.demoproject.database.SessionManager;
import com.demoproject.fragments.Account;
import com.demoproject.fragments.Categories;
import com.demoproject.fragments.Products;
import com.demoproject.fragments.WishList;
import com.demoproject.utils.Constants;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Subcategories.ChildCategories, FinishActivity, ShowBackButton, ToolbarTitle {

    ImageView backButton;
    int cartCount = 0;
    List<Category> childCategories;
    DB_Handler db_handler;

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MainActivity.this.backButton.setVisibility(4);
            FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_account /*2131230853*/:
                    ft.replace(R.id.content, new Account());
                    ft.commit();
                    MainActivity.this.titleToolbar.setText(R.string.TitleAccount);
                    return true;
                case R.id.nav_categories /*2131230854*/:
                    ft.replace(R.id.content, new Categories());
                    ft.commit();
                    MainActivity.this.titleToolbar.setText(R.string.TitleCategories);
                    return true;
                case R.id.nav_home /*2131230855*/:
                    try {
                        if (fm.findFragmentByTag(Constants.FRAG_HOME).isVisible()) {
                            return true;
                        }
                        MainActivity.this.callProductsFragment();
                        MainActivity.this.titleToolbar.setText(R.string.TitleHome);
                        return true;
                    } catch (NullPointerException e) {
                        MainActivity.this.callProductsFragment();
                        MainActivity.this.titleToolbar.setText(R.string.TitleHome);
                        return true;
                    }
                case R.id.nav_shortlist /*2131230856*/:
                    ft.replace(R.id.content, new WishList());
                    ft.commit();
                    MainActivity.this.titleToolbar.setText(R.string.TitleShortlist);
                    return true;
                default:
                    return false;
            }
        }
    };
    BottomNavigationView navigation;
    SessionManager sessionManager;
    String subCategoryTitle = null;
    TextView titleToolbar;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.db_handler = new DB_Handler(this);
        this.sessionManager = new SessionManager(this);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.titleToolbar = (TextView) findViewById(R.id.titleToolbar);
        this.titleToolbar.setText(R.string.TitleHome);
        this.backButton = (ImageView) findViewById(R.id.backButton);
        this.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.backButtonClick();
            }
        });
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.navigation.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        callProductsFragment();
        setToolbarIconsClickListeners();
    }

    private void setToolbarIconsClickListeners() {
        ((ImageView) findViewById(R.id.cart)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this.cartCount > 0) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ShoppingCart.class));
                    MainActivity.this.overridePendingTransition(0, 0);
                    return;
                }
                Toast.makeText(MainActivity.this.getApplicationContext(), R.string.cart_empty, 1).show();
            }
        });
    }

    private void callProductsFragment() {
        Bundle args = new Bundle();
        args.putInt(Constants.CAT_ID_KEY, 0);
        Products products = new Products();
        products.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, products, Constants.FRAG_HOME);
        ft.commit();
    }

    protected void onResume() {
        super.onResume();
        this.cartCount = this.db_handler.getCartItemCount(this.sessionManager.getSessionData(Constants.SESSION_EMAIL));
        TextView count = (TextView) findViewById(R.id.count);
        if (this.cartCount > 0) {
            count.setVisibility(0);
            count.setText(String.valueOf(this.cartCount));
            return;
        }
        count.setVisibility(8);
    }

    private void backButtonClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            if (fragmentManager.findFragmentByTag(Constants.FRAG_PDT).isVisible()) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TITLE, this.subCategoryTitle);
                bundle.putSerializable(Constants.CAT_KEY, (Serializable) this.childCategories);
                Subcategories subcategories = new Subcategories();
                subcategories.setArguments(bundle);
                fragmentTransaction.replace(R.id.content, subcategories, Constants.FRAG_SUBCAT);
                fragmentTransaction.commit();
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            if (fragmentManager.findFragmentByTag(Constants.FRAG_SUBCAT).isVisible()) {
                fragmentTransaction.replace(R.id.content, new Categories());
                fragmentTransaction.commit();
                this.titleToolbar.setText(R.string.TitleCategories);
                this.backButton.setVisibility(4);
            }
        } catch (NullPointerException e2) {
            super.onBackPressed();
        }
    }

    public void saveChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    public void onBackPressed() {
        backButtonClick();
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.titleToolbar.setText(toolbarTitle);
    }

    public void showBackButton() {
        this.backButton.setVisibility(0);
    }

    public void saveSubcategoryTitle(String toolbaTitle) {
        this.subCategoryTitle = toolbaTitle;
    }

    public void finishActivity() {
        overridePendingTransition(0, 0);
        finish();
    }
}
