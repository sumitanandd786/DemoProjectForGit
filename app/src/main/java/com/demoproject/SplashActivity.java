package com.demoproject;

import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.demoproject.database.DB_Handler;
import com.demoproject.interfaces.FinishActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import com.demoproject.database.DB_Handler;
import com.demoproject.database.SessionManager;
import com.demoproject.fragments.SignIn;
import com.demoproject.fragments.SignUp;
import com.demoproject.interfaces.FinishActivity;
import com.demoproject.service.SyncDBService;
import com.demoproject.utils.Constants;

public class SplashActivity extends AppCompatActivity implements FinishActivity {
    TableLayout bottomLay;
    CoordinatorLayout coordinatorLayout;
    DB_Handler db_handler;
    Handler handler;
    Button signIn;
    Button signUp;
    Snackbar snackbar = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setHandler();
        startIntentService();
        this.db_handler = new DB_Handler(this);
        setIds();
        setClickListeners();
    }

    private void setIds() {
        this.signIn = (Button) findViewById(R.id.signin);
        this.signUp = (Button) findViewById(R.id.signup);
        this.bottomLay = (TableLayout) findViewById(R.id.bottomLay);
        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLay);
    }

    private void setClickListeners() {
        this.signIn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentTransaction ft = SplashActivity.this.getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment, new SignIn());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        this.signUp.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentTransaction ft = SplashActivity.this.getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment, new SignUp());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void startIntentService() {
        Intent intent = new Intent(getApplicationContext(), SyncDBService.class);
        intent.putExtra("messenger", new Messenger(this.handler));
        startService(intent);
    }

    @SuppressLint("WrongConstant")
    private void checkSession() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getSessionData(Constants.SESSION_EMAIL) == null || sessionManager.getSessionData(Constants.SESSION_EMAIL).trim().length() <= 0) {
            this.bottomLay.setVisibility(0);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SplashActivity.this.loadNextActivity();
                }
            }, 3000);
        }
    }

    private void loadNextActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @SuppressLint({"HandlerLeak"})
    private void setHandler() {
        try {
            this.handler = new Handler() {
                final boolean $assertionsDisabled = (!SplashActivity.class.desiredAssertionStatus());

                public void handleMessage(Message msg) {
                    Bundle reply = msg.getData();
                    if (reply.getString("message").equals("success")) {
                        SplashActivity.this.checkSession();
                        return;
                    }
                    try {
                        String message = reply.getString("message");
                        if ($assertionsDisabled || message != null) {
                            SplashActivity.this.snackbar = Snackbar.make(SplashActivity.this.coordinatorLayout, message, -2).setAction(R.string.retry, new OnClickListener() {
                                public void onClick(View view) {
                                    SplashActivity.this.snackbar.dismiss();
                                    SplashActivity.this.startIntentService();
                                }
                            });
                            SplashActivity.this.snackbar.setActionTextColor(-65536);
                            SplashActivity.this.snackbar.show();
                            return;
                        }
                        throw new AssertionError();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishActivity() {
        overridePendingTransition(0, 0);
        finish();
    }


}