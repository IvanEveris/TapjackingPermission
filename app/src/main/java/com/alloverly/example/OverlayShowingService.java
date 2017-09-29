package com.alloverly.example;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.compat.*;
import android.support.compat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class OverlayShowingService extends Service  {

    private View topLeftView;

    private Button overlayedButton;
    private WindowManager wm;
    private WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//            }
//        }, 4000);

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        Point start = new Point();

        overlyPermission(start);

        overlayedButton = new Button(this);
        overlayedButton.setText("Allow this application to access Internet?");
        overlayedButton.setAlpha(0.85f);
        overlayedButton.setTextColor(Color.DKGRAY);
        overlayedButton.setBackgroundColor(Color.WHITE);

        overlayedButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        overlayedButton.setPadding(40, 0, 0, 0);


        params = new WindowManager.
                LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.y = start.y;
        params.height = 240;
        params.width = 800;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

            wm.addView(overlayedButton, params);

    }


    private void overlyPermission(Point start) {
        int heightDialog = dpToPx(700);
        int widthDialog = dpToPx(720);
        Point screenSize = new Point();

        WindowManager winMng = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        winMng.getDefaultDisplay().getRealSize(screenSize);

        start.y = (screenSize.y / 2) - (heightDialog / 2);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton);
            overlayedButton = null;
            topLeftView = null;
        }
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}