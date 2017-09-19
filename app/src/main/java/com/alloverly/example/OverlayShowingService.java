package com.alloverly.example;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ipicomar on 18/09/2017.
 * ref: https://gist.github.com/bjoernQ/6975256
 * <p>
 */

public class OverlayShowingService extends Service implements View.OnClickListener {

    private View topLeftView;

    private Button overlayedButton;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                wm.addView(overlayedButton, params);
            }
        }, 4000);

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        Point start = new Point();

        overlyPermission(start);

        overlayedButton = new Button(this);
        overlayedButton.setText("Allow this application to access Internet?");
        overlayedButton.setAlpha(0.9f);
        overlayedButton.setTextColor(Color.DKGRAY);
        overlayedButton.setBackgroundColor(Color.WHITE);
        overlayedButton.setOnClickListener(this);
        overlayedButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        overlayedButton.setPadding(40,0,0,0);

        params = new WindowManager.
                LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.y = start.y;
        params.height = 240;
        params.width = 800;

    }


    private void overlyPermission(Point start) {
        int heightDialog = dpToPx(740);
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


    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show();
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