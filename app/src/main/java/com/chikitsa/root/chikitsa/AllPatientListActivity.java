package com.chikitsa.root.chikitsa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
import java.util.Map;

public class AllPatientListActivity extends AppCompatActivity {

//    private Button whoGuideLines, logOutButton;
    static SessionManager session;
    ArrayList<PatientListRowInDoctorClass> patientRowData = new ArrayList<PatientListRowInDoctorClass>();
    ListView patient_list_view;
    AllPatientListInDoctorAdapter adapter;

    public int clickedPatientId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.action_logout){
            logout(this);
            return true;
        } else if (item.getItemId() == R.id.action_notification){
            i = new Intent(this, PatientNotifications.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(AllPatientListActivity.this, ControllerActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patient_list);

        getSupportActionBar().setTitle("All Patients");

        session = new SessionManager(this);

        getAllPatientList();
    }

    /*
     * to fill the doctor details
     * API for doctor details
     * */
    public void getAllPatientList(){
        String url = ApplicationController.get_base_url() + "api/doctor/" + session.getUserDetails().get("id");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("AllPatientList", response.toString());
                        try {

                            JSONArray allPatientsArray = response.getJSONArray("patients");

                            for (int i = 0; i < allPatientsArray.length(); i++) {
                                JSONObject po = (JSONObject) allPatientsArray.get(i);
                                PatientListRowInDoctorClass pr = new PatientListRowInDoctorClass(po.getInt("pk"),po.getString("name"), po.getString("gender"), po.getString("email"), po.getInt("mobile"));
                                patientRowData.add(pr);
                            }

                            AllPatientListInDoctorAdapter itemsAdapter = new AllPatientListInDoctorAdapter(AllPatientListActivity.this, patientRowData);
                            ListView listView = (ListView) findViewById(R.id.allPatientsDoctorListView);
                            listView.setAdapter(itemsAdapter);
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
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token " + session.getUserDetails().get("Token"));
                return params;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
