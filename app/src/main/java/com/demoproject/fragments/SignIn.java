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

public class SignIn extends Fragment {
    ImageView back;
    EditText email;
    FinishActivity finishActivityCallback;
    boolean isPasswordShown = false;
    EditText password;
    ImageView showpassword;
    Button signIn;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in, container, false);
        setIds(view);
        setClickListeners();
        return view;
    }

    private void setIds(View view) {
        this.email = (EditText) view.findViewById(R.id.email);
        this.password = (EditText) view.findViewById(R.id.password);
        this.signIn = (Button) view.findViewById(R.id.signin);
        this.back = (ImageView) view.findViewById(R.id.back);
        this.showpassword = (ImageView) view.findViewById(R.id.showpassword);
    }

    private void setClickListeners() {
        this.signIn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                User user = new User();
                user.setEmail(SignIn.this.email.getText().toString());
                user.setPassword(SignIn.this.password.getText().toString());
                if (user.getEmail().trim().length() <= 0) {
                    SignIn.this.showErrorToast(SignIn.this.getActivity().getResources().getString(R.string.email));
                } else if (!Util.isValidEmail(user.getEmail())) {
                    SignIn.this.showErrorToastEmailNotValid();
                } else if (user.getPassword().trim().length() > 0) {
                    user = new DB_Handler(SignIn.this.getActivity()).getUser(user.getEmail());
                    try {
                        if (user.getEmail().trim().length() > 0) {
                            SessionManager sessionManager = new SessionManager(SignIn.this.getActivity());
                            sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                            sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getPassword());
                            SignIn.this.startActivity(new Intent(SignIn.this.getActivity(), MainActivity.class));
                            SignIn.this.finishActivityCallback.finishActivity();
                            return;
                        }
                        SignIn.this.showInvalidUser();
                    } catch (NullPointerException e) {
                        SignIn.this.showInvalidUser();
                    }
                } else {
                    SignIn.this.showErrorToast(SignIn.this.getActivity().getResources().getString(R.string.password));
                }
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentTransaction ft = SignIn.this.getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.fragment, new BlankFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        this.showpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SignIn.this.isPasswordShown) {
                    SignIn.this.password.setTransformationMethod(new PasswordTransformationMethod());
                    SignIn.this.showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    SignIn.this.isPasswordShown = false;
                    return;
                }
                SignIn.this.password.setTransformationMethod(null);
                SignIn.this.showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                SignIn.this.isPasswordShown = true;
            }
        });
    }

    private void showErrorToast(String value) {
        Toast.makeText(getActivity(), value + getResources().getString(R.string.BlankError), 1).show();
    }

    private void showErrorToastEmailNotValid() {
        Toast.makeText(getActivity(), R.string.EmailError, 1).show();
    }

    private void showInvalidUser() {
        Toast.makeText(getActivity(), R.string.InvalidUser, 1).show();
    }
}
