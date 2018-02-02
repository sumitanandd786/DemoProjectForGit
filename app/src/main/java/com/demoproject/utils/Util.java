package com.demoproject.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import com.demoproject.R;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;

public class Util {
    public static String formatDouble(double num) {
        if (num == ((double) ((long) num))) {
            return String.format(Locale.US, "%d", new Object[]{Long.valueOf((long) num)});
        }
        return String.format(Locale.US, "%s", new Object[]{Double.valueOf(num)});
    }

    public static String getInClause(List<String> array) {
        return array.toString().replace("[", "(").replace("]", ")");
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getErrorMessage(Throwable t, Context context) {
        if ((t instanceof SocketTimeoutException) || (t instanceof UnknownHostException) || (t instanceof ConnectException)) {
            return context.getResources().getString(R.string.NoInternet);
        }
        return context.getResources().getString(R.string.Error500);
    }
}
