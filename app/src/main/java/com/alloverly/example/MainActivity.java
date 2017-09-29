package com.alloverly.example;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by ipicomar on 18/09/2017.
 * ref: https://gist.github.com/bjoernQ/6975256
 *
 * For versions >= 6.0 when runtime permission is allow.
 * Cloak demostration.
 * Toast message is hiding read contacts permission with other message.
 *
 * Click message to hide the toast.
 *
 *
 */

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CONTACT = 123;
    private static final int PICK_CONTACT = 124;
    private ComponentName componentName;
    private Intent svc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        svc = new Intent(this, OverlayShowingService.class);


        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M)

                    Toast.makeText(getApplicationContext(),
                            "Just it works with Android Mashmallow and above", Toast.LENGTH_LONG).show();

                else {
                    componentName = startService(svc);
                    askForContactPermission();
                }
            }
        });

    }

    private void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {

                    requestPermissions(
                            new String[]
                                    {Manifest.permission.READ_CONTACTS}
                            , PERMISSION_REQUEST_CONTACT);

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.d("TAGTAG", "No permission for contacts");
                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(svc);
    }
}
