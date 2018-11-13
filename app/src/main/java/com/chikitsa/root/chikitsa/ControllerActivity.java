package com.chikitsa.root.chikitsa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import java.util.HashMap;

public class ControllerActivity extends Activity {
    SessionManager session;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    MyFirebaseInstanceIDService fcm;
    Button sign_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                session = new SessionManager(ControllerActivity.this);
                session.checkLogin();
                fcm = new MyFirebaseInstanceIDService(ControllerActivity.this);
                final HashMap<String, String> user = session.getUserDetails();
                Log.d("DATA", user.toString());

                fcm.onTokenRefresh();
                if ("doctor".equals(user.get("type"))) {
                    Intent i = new Intent(ControllerActivity.this, AllPatientListActivity.class);
                    startActivity(i);
                    finish();
                } else if ("patient".equals(user.get("type"))) {
                    Intent i = new Intent(ControllerActivity.this, MedicineReminder.class);
                    i.putExtra("EXTRA_PATIENT_ID", Integer.parseInt(session.getUserDetails().get("id")));
                    startActivity(i);
                    finish();
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}