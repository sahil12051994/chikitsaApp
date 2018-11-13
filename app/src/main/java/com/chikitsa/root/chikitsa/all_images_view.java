package com.chikitsa.root.chikitsa;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class all_images_view extends AppCompatActivity {

    static SessionManager session;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int clickedPatientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images_view);

        session = new SessionManager(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshImagesList);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPatientImageData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        clickedPatientId= getIntent().getIntExtra("EXTRA_PATIENT_ID", 1);
        getSupportActionBar().setTitle("Patient Images");
        getPatientImageData();
    }

//    private void logout(Context _c) {
//        session = new SessionManager(_c);
//        session.logoutUser();
//        Intent i = new Intent(all_images_view.this, ControllerActivity.class);
//        startActivity(i);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.patient_detail_in_doctor, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent i;
//        if (item.getItemId() == R.id.action_logout){
//            logout(this);
//            return true;
//        }
//        else if (item.getItemId() == R.id.patientImages){
//            i = new Intent(this, all_images_view.class);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getPatientImageData(){
        String url = ApplicationController.get_base_url() + "api/allimages/" + clickedPatientId;
        final ArrayList<WholeImagesListClass> imageRowAdapter = new ArrayList<WholeImagesListClass>();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("AllPatientList", response.toString());
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject po = (JSONObject) response.get(i);
                                WholeImagesListClass pr = new WholeImagesListClass(po.getString("extra_comments_image"), po.getString("time_stamp"));
                                imageRowAdapter.add(pr);
                            }

                            WholeImagesListAdapter itemsAdapter = new WholeImagesListAdapter(all_images_view.this, imageRowAdapter);
                            ListView listView = (ListView) findViewById(R.id.patient_images_list);
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
