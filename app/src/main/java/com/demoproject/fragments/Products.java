package com.demoproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.demoproject.R;
import com.demoproject.adapters.FilterItemListAdapter;
import com.demoproject.adapters.ProductListAdapter;
import com.demoproject.adapters.SortItemListAdapter;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.interfaces.ShowBackButton;
import com.demoproject.interfaces.ToolbarTitle;
import com.demoproject.pojo.Product;
import com.demoproject.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Products extends Fragment {
    static final boolean $assertionsDisabled = (!Products.class.desiredAssertionStatus());
    int cat_id = 0;
    List<String> colorFilter = new ArrayList();
    RelativeLayout filter;
    List<Product> productList;
    ProductListAdapter productListAdapter;
    GridView productsGrid;
    ShowBackButton showBackButtonCallback;
    List<String> sizeFilter = new ArrayList();
    RelativeLayout sort;
    String[] sortByArray = new String[]{"Most Recent", "Most Orders", "Most Shares", "Most Viewed"};
    int sortById = 0;
    TextView sortByText;
    ToolbarTitle toolbarTitleCallback;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.toolbarTitleCallback = (ToolbarTitle) context;
        this.showBackButtonCallback = (ShowBackButton) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list, container, false);
        setIds(view);
        setSortListener();
        setFilterListener();
        Bundle args = getArguments();
        if ($assertionsDisabled || args != null) {
            this.cat_id = args.getInt(Constants.CAT_ID_KEY);
            if (this.cat_id > 0) {
                this.showBackButtonCallback.showBackButton();
                this.toolbarTitleCallback.setToolbarTitle(args.getString(Constants.TITLE));
            }
            this.sortByText.setText(this.sortByArray[0]);
            fillGridView();
            return view;
        }
        throw new AssertionError();
    }

    public void onResume() {
        super.onResume();
        try {
            List<Product> productList = new DB_Handler(getActivity()).getProductsList(this.sortById, this.sizeFilter, this.colorFilter, this.cat_id, new SessionManager(getActivity()).getSessionData(Constants.SESSION_EMAIL));
            this.productList.clear();
            this.productList.addAll(productList);
            this.productListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setIds(View view) {
        this.sort = (RelativeLayout) view.findViewById(R.id.sortLay);
        this.filter = (RelativeLayout) view.findViewById(R.id.filterLay);
        this.sortByText = (TextView) view.findViewById(R.id.sortBy);
        this.productsGrid = (GridView) view.findViewById(R.id.productsGrid);
    }

    private void fillGridView() {
        this.productList = new DB_Handler(getActivity()).getProductsList(this.sortById, this.sizeFilter, this.colorFilter, this.cat_id, new SessionManager(getActivity()).getSessionData(Constants.SESSION_EMAIL));
        this.productsGrid.setNumColumns(2);
        this.productListAdapter = new ProductListAdapter(getActivity(), this.productList);
        this.productsGrid.setAdapter(this.productListAdapter);
    }

    private void setSortListener() {
        this.sort.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Products.this.getActivity());
                dialog.setContentView(R.layout.listview);
                ListView listView = (ListView) dialog.findViewById(R.id.listview);
                listView.setAdapter(new SortItemListAdapter(Products.this.getActivity(), Products.this.sortByArray, Products.this.sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Products.this.sortById = i;
                        Products.this.sortByText.setText(Products.this.sortByArray[Products.this.sortById]);
                        Products.this.fillGridView();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setFilterListener() {
        this.filter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Products.this.getActivity());
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.filterlayout);
                DB_Handler db_handler = new DB_Handler(Products.this.getActivity());
                final List<String> colors = db_handler.getAllColors();
                final List<String> sizes = db_handler.getAllSizes();
                HashMap<String, List<String>> listHashMap = new HashMap();
                listHashMap.put("Size", sizes);
                listHashMap.put("Color", colors);
                List<String> headers = new ArrayList();
                headers.add("Size");
                headers.add("Color");
                ExpandableListView listView = (ExpandableListView) dialog.findViewById(R.id.expandableList);
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(Products.this.getActivity(), headers, listHashMap, Products.this.sizeFilter, Products.this.colorFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();
                listView.setOnChildClickListener(new OnChildClickListener() {
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_gravity /*0*/:
                                if (!Products.this.sizeFilter.contains(sizes.get(childPosition))) {
                                    Products.this.sizeFilter.add(sizes.get(childPosition));
                                    break;
                                }
                                Products.this.sizeFilter.remove(sizes.get(childPosition));
                                break;
                            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_orientation /*1*/:
                                if (!Products.this.colorFilter.contains("'" + ((String) colors.get(childPosition)) + "'")) {
                                    Products.this.colorFilter.add("'" + ((String) colors.get(childPosition)) + "'");
                                    break;
                                }
                                Products.this.colorFilter.remove("'" + ((String) colors.get(childPosition)) + "'");
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
                ((Button) dialog.findViewById(R.id.apply)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Products.this.fillGridView();
                        dialog.dismiss();
                    }
                });
                ((Button) dialog.findViewById(R.id.clear)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        try {
                            Products.this.sizeFilter.clear();
                        } catch (NullPointerException e) {
                        }
                        try {
                            Products.this.colorFilter.clear();
                        } catch (NullPointerException e2) {
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });
                ((ImageView) dialog.findViewById(R.id.close)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
