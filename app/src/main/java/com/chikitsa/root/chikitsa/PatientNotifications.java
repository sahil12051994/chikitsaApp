package com.chikitsa.root.chikitsa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientNotifications extends AppCompatActivity {
    RecyclerView notification_list;
    static SessionManager session;
    NotificationAdapter adapter;
    final List<String> data = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_notification_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(PatientNotifications.this, ControllerActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_notifications);

        getSupportActionBar().setTitle("Notifications");


        session = new SessionManager(this);

        notification_list = (RecyclerView) findViewById(R.id.patient_notification_list);
        getData(new CallBack() {
            @Override
            public void onSuccess(List<String> notfication_data_list) {
                adapter = new NotificationAdapter(PatientNotifications.this, notfication_data_list);
                PatientNotifications.this.notification_list.setAdapter(adapter);
                PatientNotifications.this.notification_list.setLayoutManager(new LinearLayoutManager(PatientNotifications.this, LinearLayoutManager.VERTICAL, false));
                PatientNotifications.this.notification_list.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public void getData(final PatientNotifications.CallBack onCallback) {
        String url = "";
        String session_type = session.getUserDetails().get("type");
//        Toast.makeText(NotificationActivity.this, session.getUserDetails().get("type"), Toast.LENGTH_LONG).show();

        if("doctor".equals(session_type)) {
//            Toast.makeText(NotificationActivity.this, session.getUserDetails().get("type"), Toast.LENGTH_LONG).show();

            url = ApplicationController.get_base_url() + "dhadkan/api/notification/doctor/" + session.getUserDetails().get("id");
        }
        else if("patient".equals(session_type)){
            url = ApplicationController.get_base_url() + "dhadkan/api/notification/patient/" + session.getUserDetails().get("id");

        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            List<String> data = new ArrayList<String>();
                            JSONArray notifications = response.getJSONArray("notifications");
                            for (int i = 0; i < notifications.length(); i++) {
                                JSONObject no = (JSONObject) notifications.get(i);
                                String n = no.getString("text");
                                data.add(n);
                            }
                            onCallback.onSuccess(data);

//                          edit.putInt("U_ID", (Integer) response.get("id"));
//                            Log.d("TAG", "" + response.get("pk"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                            edit.commit();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Message: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
                Log.d("TAG", "Token " + session.getUserDetails().get("Token"));
                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public interface CallBack {
        void onSuccess(List<String> patient_notification_list);

        void onFail(String msg);
    }
}
