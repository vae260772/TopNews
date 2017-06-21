package com.example.lihui20.testhttp.tools;

import android.content.Context;
import android.widget.Toast;

import com.example.lihui20.testhttp.application.CustomNewsApplication;

/**
 * Created by lihui20 on 2016/12/19.
 */
public class ToastUtils {
    private static Toast toast = null;

    public static void setToastText(String msg) {
        if (toast == null) {
            toast = Toast.makeText(CustomNewsApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

    }

    //    public static void getToast() {
//        toast.show();
//    }
    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(CustomNewsApplication.getInstance(), msg, Toast.LENGTH_SHORT);

        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }


}
