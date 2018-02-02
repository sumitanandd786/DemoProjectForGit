package com.demoproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.demoproject.MainActivity;
import com.demoproject.R;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.interfaces.FinishActivity;
import com.demoproject.pojo.User;
import com.demoproject.utils.Constants;
import com.demoproject.utils.Util;

public class SignUp extends Fragment {
    ImageView back;
    EditText email;
    FinishActivity finishActivityCallback;
    boolean isPasswordShown = false;
    EditText mobile;
    EditText name;
    EditText password;
    ImageView showpassword;
    Button signUp;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up, container, false);
        setIds(view);
        setClickListeners();
        return view;
    }

    private void setIds(View view) {
        this.name = (EditText) view.findViewById(R.id.name);
        this.email = (EditText) view.findViewById(R.id.email);
        this.password = (EditText) view.findViewById(R.id.password);
        this.mobile = (EditText) view.findViewById(R.id.mobile);
        this.signUp = (Button) view.findViewById(R.id.signup);
        this.back = (ImageView) view.findViewById(R.id.back);
        this.showpassword = (ImageView) view.findViewById(R.id.showpassword);
    }

    private void setClickListeners() {
        this.signUp.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                User user = new User();
                user.setName(SignUp.this.name.getText().toString());
                user.setEmail(SignUp.this.email.getText().toString());
                user.setMobile(SignUp.this.mobile.getText().toString());
                user.setPassword(SignUp.this.password.getText().toString());
                if (user.getName().trim().length() <= 0) {
                    SignUp.this.showErrorToast(SignUp.this.getActivity().getResources().getString(R.string.name));
                } else if (user.getEmail().trim().length() <= 0) {
                    SignUp.this.showErrorToast(SignUp.this.getActivity().getResources().getString(R.string.email));
                } else if (!Util.isValidEmail(user.getEmail())) {
                    SignUp.this.showErrorToastEmailNotValid();
                } else if (user.getMobile().trim().length() <= 0) {
                    SignUp.this.showErrorToast(SignUp.this.getActivity().getResources().getString(R.string.mobile));
                } else if (user.getPassword().trim().length() <= 0) {
                    SignUp.this.showErrorToast(SignUp.this.getActivity().getResources().getString(R.string.password));
                } else if (new DB_Handler(SignUp.this.getActivity()).registerUser(user.getName(), user.getEmail(), user.getMobile(), user.getPassword()) != -1) {
                    SessionManager sessionManager = new SessionManager(SignUp.this.getActivity());
                    sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                    sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getPassword());
                    SignUp.this.startActivity(new Intent(SignUp.this.getActivity(), MainActivity.class));
                    SignUp.this.finishActivityCallback.finishActivity();
                } else {
                    SignUp.this.showErrorToastEmailExists();
                }
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentTransaction ft = SignUp.this.getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.fragment, new BlankFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        this.showpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SignUp.this.isPasswordShown) {
                    SignUp.this.password.setTransformationMethod(new PasswordTransformationMethod());
                    SignUp.this.showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    SignUp.this.isPasswordShown = false;
                    return;
                }
                SignUp.this.password.setTransformationMethod(null);
                SignUp.this.showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                SignUp.this.isPasswordShown = true;
            }
        });
    }

    private void showErrorToast(String value) {
        Toast.makeText(getActivity(), value + getResources().getString(R.string.BlankError), 1).show();
    }

    private void showErrorToastEmailNotValid() {
        Toast.makeText(getActivity(), R.string.EmailError, 1).show();
    }

    private void showErrorToastEmailExists() {
        Toast.makeText(getActivity(), R.string.EmailExistsError, 1).show();
    }
}
