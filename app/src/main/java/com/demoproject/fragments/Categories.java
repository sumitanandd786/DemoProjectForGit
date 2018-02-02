package com.demoproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.demoproject.R;
import com.demoproject.adapters.CategoryListAdapter;
import com.demoproject.database.DB_Handler;

public class Categories extends Fragment {
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview, container, false);
        ((ListView) view.findViewById(R.id.listview)).setAdapter(new CategoryListAdapter(getActivity(), new DB_Handler(getActivity()).getCategoryList()));
        return view;
    }
}
