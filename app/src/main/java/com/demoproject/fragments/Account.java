package com.demoproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.demoproject.R;
import com.demoproject.SplashActivity;
import com.demoproject.activities.MyOrders;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.interfaces.FinishActivity;
import com.demoproject.pojo.User;
import com.demoproject.utils.Constants;

public class Account extends Fragment {
    DB_Handler db_handler;
    TextView email;
    FinishActivity finishActivityCallback;
    RelativeLayout logoutLay;
    TextView mobile;
    TextView name;
    RelativeLayout orders;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account, container, false);
        this.db_handler = new DB_Handler(getActivity());
        User user = this.db_handler.getUser(new SessionManager(getActivity()).getSessionData(Constants.SESSION_EMAIL));
        setIds(view);
        setValues(user);
        setClickListeners();
        return view;
    }

    private void setIds(View view) {
        this.logoutLay = (RelativeLayout) view.findViewById(R.id.logoutLay);
        this.name = (TextView) view.findViewById(R.id.name);
        this.email = (TextView) view.findViewById(R.id.email);
        this.mobile = (TextView) view.findViewById(R.id.mobile);
        this.orders = (RelativeLayout) view.findViewById(R.id.myOrdersLay);
    }

    private void setValues(User user) {
        this.name.setText(user.getName());
        this.email.setText(user.getEmail());
        this.mobile.setText(user.getMobile());
    }

    private void setClickListeners() {
        this.orders.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Account.this.startActivity(new Intent(Account.this.getActivity(), MyOrders.class));
                Account.this.getActivity().overridePendingTransition(0, 0);
            }
        });
        this.logoutLay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new SessionManager(Account.this.getActivity()).clearPreferences();
                Intent intent = new Intent(Account.this.getActivity(), SplashActivity.class);
                intent.setFlags(335544320);
                Account.this.startActivity(intent);
                Account.this.finishActivityCallback.finishActivity();
            }
        });
    }
}
