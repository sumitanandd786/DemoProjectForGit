package com.demoproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.pojo.Product;
import com.demoproject.pojo.Variant;
import com.demoproject.utils.Constants;
import java.util.List;
import org.apmem.tools.layouts.FlowLayout;
import org.apmem.tools.layouts.FlowLayout.LayoutParams;

public class ProductDetails extends AppCompatActivity {
    Button buyNow;
    Button cart;
    int cartCount = 0;
    LinearLayout colorParentLay;
    FlowLayout colorsLay;
    DB_Handler db_handler;
    ImageView minus;
    ImageView plus;
    TextView price;
    Product product;
    TextView quantityValue;
    String selectedColor = null;
    String selectedItemPrice = null;
    int selectedItemQuantity = 1;
    int selectedItemVariantId = 0;
    String selectedSize = null;
    SessionManager sessionManager;
    FlowLayout sizeLay;
    LinearLayout sizeParentLay;
    Toolbar toolbar;
    String userEmail = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        ((TextView) findViewById(R.id.titleToolbar)).setVisibility(8);
        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(0);
        backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductDetails.this.onBackPressed();
            }
        });
        this.sessionManager = new SessionManager(this);
        this.userEmail = this.sessionManager.getSessionData(Constants.SESSION_EMAIL);
        int id = getIntent().getIntExtra("ProductId", 0);
        this.db_handler = new DB_Handler(this);
        this.product = this.db_handler.getProductDetailsById(id, this.userEmail);
        setIds();
        setValues();
        setToolbarIconsClickListeners();
        setQuantityUpdateListeners();
        setBottomPanelClickListeners();
    }

    private void setIds() {
        this.buyNow = (Button) findViewById(R.id.buyNow);
        this.cart = (Button) findViewById(R.id.cartButton);
        this.colorParentLay = (LinearLayout) findViewById(R.id.colorParentLay);
        this.sizeParentLay = (LinearLayout) findViewById(R.id.sizeParentLay);
        this.colorsLay = (FlowLayout) findViewById(R.id.colorsLay);
        this.sizeLay = (FlowLayout) findViewById(R.id.sizesLay);
        this.price = (TextView) findViewById(R.id.price);
        this.quantityValue = (TextView) findViewById(R.id.quantityValue);
        this.minus = (ImageView) findViewById(R.id.minus);
        this.plus = (ImageView) findViewById(R.id.plus);
    }

    private void setToolbarIconsClickListeners() {
        ((ImageView) findViewById(R.id.cart)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductDetails.this.cartCount > 0) {
                    ProductDetails.this.startActivity(new Intent(ProductDetails.this.getApplicationContext(), ShoppingCart.class));
                } else {
                    Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.cart_empty, 1).show();
                }
            }
        });
    }

    private void setBottomPanelClickListeners() {
        this.cart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductDetails.this.isSuccessAddingToCart(false)) {
                    Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.add_success, 1).show();
                    ProductDetails.this.updateCartCount();
                }
            }
        });
        this.buyNow.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductDetails.this.isSuccessAddingToCart(true)) {
                    ProductDetails.this.startActivity(new Intent(ProductDetails.this.getApplicationContext(), ShoppingCart.class));
                    ProductDetails.this.overridePendingTransition(0, 0);
                }
            }
        });
    }

    private boolean isSuccessAddingToCart(boolean isBuyNow) {
        try {
            if (!this.selectedSize.equals("-") && this.selectedSize == null) {
                Toast.makeText(getApplicationContext(), R.string.size_select, 1).show();
                return false;
            } else if (this.selectedColor == null) {
                Toast.makeText(getApplicationContext(), R.string.color_select, 1).show();
                return false;
            } else if (this.db_handler.insertIntoCart(this.product.getId().intValue(), this.selectedItemVariantId, this.selectedItemQuantity, this.userEmail) > 0 || isBuyNow) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), R.string.item_exists, 1).show();
                return false;
            }
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), R.string.size_select, 1).show();
            return false;
        }
    }

    private void setValues() {
        final ImageView heart = (ImageView) findViewById(R.id.heart);
        if (this.product.getShortlisted().booleanValue()) {
            heart.setImageResource(R.drawable.ic_heart_grey);
        }
        heart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductDetails.this.product.getShortlisted().booleanValue()) {
                    heart.setImageResource(R.drawable.ic_heart_grey600_24dp);
                    if (ProductDetails.this.db_handler.removeShortlistedItem(ProductDetails.this.product.getId().intValue(), ProductDetails.this.userEmail)) {
                        ProductDetails.this.product.setShortlisted(Boolean.valueOf(false));
                        Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.item_rem_wishlist, 1).show();
                        return;
                    }
                    return;
                }
                heart.setImageResource(R.drawable.ic_heart_grey);
                if (ProductDetails.this.db_handler.shortlistItem(ProductDetails.this.product.getId().intValue(), ProductDetails.this.userEmail) > 0) {
                    ProductDetails.this.product.setShortlisted(Boolean.valueOf(true));
                    Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.item_add_wishlist, 1).show();
                }
            }
        });
        ((TextView) findViewById(R.id.title)).setText(this.product.getName());
        setSizeLayout(this.db_handler.getSizeByProductId(this.product.getId().intValue()));
        setColorLayout(this.db_handler.getProductColorsById(this.product.getId().intValue()));
        this.price.setText(this.db_handler.getProductPriceRangeById(this.product.getId().intValue()));
    }

    private void setSizeLayout(final List<String> sizeList) {
        this.sizeLay.removeAllViews();
        if (sizeList.size() > 0) {
            for (int i = 0; i < sizeList.size(); i++) {
                TextView size = new TextView(this);
                size.setText((CharSequence) sizeList.get(i));
                if (VERSION.SDK_INT >= 16) {
                    size.setBackground(getResources().getDrawable(R.drawable.border_grey));
                } else {
                    size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                }
                try {
                    if (this.selectedSize.equals(sizeList.get(i))) {
                        if (VERSION.SDK_INT >= 16) {
                            size.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        } else {
                            size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_blue));
                        }
                    }
                } catch (NullPointerException e) {
                }
                if (VERSION.SDK_INT >= 23) {
                    size.setTextColor(getResources().getColor(R.color.black, null));
                } else {
                    try {
                        size.setTextColor(getResources().getColor(R.color.black));
                    } catch (NullPointerException e2) {
                        this.sizeParentLay.setVisibility(8);
                        this.selectedSize = "-";
                        return;
                    }
                }
                size.setFocusableInTouchMode(false);
                size.setFocusable(true);
                size.setClickable(true);
                size.setTextSize(16.0f);
                int margin = (int) (((float) 8) * getResources().getDisplayMetrics().density);
                LayoutParams params = new LayoutParams(-2, -2);
                params.setMargins(margin, margin, 0, 0);
                size.setLayoutParams(params);
                this.sizeLay.addView(size);
                size.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TextView textView = (TextView) view;
                        ProductDetails.this.selectedSize = textView.getText().toString();
                        ProductDetails.this.selectedColor = null;
                        ProductDetails.this.selectedItemPrice = null;
                        ProductDetails.this.setSizeLayout(sizeList);
                        ProductDetails.this.setColorLayout(ProductDetails.this.db_handler.getColorBySelectedSize(ProductDetails.this.product.getId().intValue(), ProductDetails.this.selectedSize));
                    }
                });
            }
            return;
        }
        this.sizeParentLay.setVisibility(8);
        this.selectedSize = "-";
    }

    private void setColorLayout(final List<String> colorList) {
        this.colorsLay.removeAllViews();
        if (colorList.size() > 0) {
            for (int i = 0; i < colorList.size(); i++) {
                TextView color = new TextView(this);
                color.setText((CharSequence) colorList.get(i));
                if (VERSION.SDK_INT >= 16) {
                    color.setBackground(getResources().getDrawable(R.drawable.border_grey));
                } else {
                    color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                }
                try {
                    if (this.selectedColor.equals(colorList.get(i))) {
                        if (VERSION.SDK_INT >= 16) {
                            color.setBackground(getResources().getDrawable(R.drawable.border_blue));
                        } else {
                            color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_blue));
                        }
                    }
                } catch (NullPointerException e) {
                }
                if (VERSION.SDK_INT >= 23) {
                    color.setTextColor(getResources().getColor(R.color.black, null));
                } else {
                    try {
                        color.setTextColor(getResources().getColor(R.color.black));
                    } catch (NullPointerException e2) {
                        this.colorParentLay.setVisibility(8);
                        return;
                    }
                }
                color.setFocusableInTouchMode(false);
                color.setFocusable(true);
                color.setClickable(true);
                color.setTextSize(16.0f);
                int margin = (int) (((float) 8) * getResources().getDisplayMetrics().density);
                LayoutParams params = new LayoutParams(-2, -2);
                params.setMargins(margin, margin, 0, 0);
                color.setLayoutParams(params);
                this.colorsLay.addView(color);
                color.setOnClickListener(new OnClickListener() {
                    @SuppressLint({"SetTextI18n"})
                    public void onClick(View view) {
                        try {
                            if (ProductDetails.this.selectedSize.equals("-") || ProductDetails.this.selectedSize != null) {
                                Variant variant;
                                TextView textView = (TextView) view;
                                ProductDetails.this.selectedColor = textView.getText().toString();
                                if (ProductDetails.this.selectedSize.equals("-")) {
                                    variant = ProductDetails.this.db_handler.getProductVariant(ProductDetails.this.product.getId().intValue(), null, ProductDetails.this.selectedColor);
                                    ProductDetails.this.selectedItemPrice = variant.getPrice();
                                } else {
                                    variant = ProductDetails.this.db_handler.getProductVariant(ProductDetails.this.product.getId().intValue(), ProductDetails.this.selectedSize, ProductDetails.this.selectedColor);
                                    ProductDetails.this.selectedItemPrice = variant.getPrice();
                                }
                                ProductDetails.this.selectedItemVariantId = variant.getId().intValue();
                                ProductDetails.this.price.setText("Rs." + ProductDetails.this.selectedItemPrice);
                                ProductDetails.this.setColorLayout(colorList);
                                return;
                            }
                            Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.size_select, 1).show();
                        } catch (NullPointerException e) {
                            Toast.makeText(ProductDetails.this.getApplicationContext(), R.string.size_select, 1).show();
                        }
                    }
                });
            }
            return;
        }
        this.colorParentLay.setVisibility(8);
    }

    private void setQuantityUpdateListeners() {
        this.minus.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ProductDetails.this.selectedItemQuantity != 1) {
                    ProductDetails productDetails = ProductDetails.this;
                    productDetails.selectedItemQuantity--;
                    ProductDetails.this.quantityValue.setText(String.valueOf(ProductDetails.this.selectedItemQuantity));
                }
            }
        });
        this.plus.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductDetails productDetails = ProductDetails.this;
                productDetails.selectedItemQuantity++;
                ProductDetails.this.quantityValue.setText(String.valueOf(ProductDetails.this.selectedItemQuantity));
            }
        });
    }

    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    private void updateCartCount() {
        this.cartCount = this.db_handler.getCartItemCount(this.sessionManager.getSessionData(Constants.SESSION_EMAIL));
        TextView count = (TextView) findViewById(R.id.count);
        if (this.cartCount > 0) {
            count.setVisibility(0);
            count.setText(String.valueOf(this.cartCount));
            return;
        }
        count.setVisibility(8);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
