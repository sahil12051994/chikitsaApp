package com.chikitsa.root.chikitsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorSignup extends AppCompatActivity implements View.OnClickListener {

    Button register, signin;
    EditText name, mobile, hospital, email, password;
    Spinner pre_mobile;
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);
        getSupportActionBar().setTitle("Doctor Registration");


        signin = (Button) findViewById(R.id.signin_modal);
        register = (Button) findViewById(R.id.register);
        name = (EditText) findViewById(R.id.editText13);
        mobile = (EditText) findViewById(R.id.editText14);
        email = (EditText) findViewById(R.id.editText15);
        hospital = (EditText) findViewById(R.id.editText16);
        password = (EditText) findViewById(R.id.editText12);
        register.setOnClickListener(this);
        session = new SessionManager(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            String str_mobile = "" + mobile.getText();
            String str_password = "" + password.getText();
            String str_name = "" + name.getText();
            String str_email = "" + email.getText();
            String str_hospital = "" + hospital.getText();

            if (str_name.length() == 0) {
                Toast.makeText(DoctorSignup.this, "enter your name", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_email.length() == 0) {
                Toast.makeText(DoctorSignup.this, "enter your email address", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_mobile.length() == 0) {
                Toast.makeText(DoctorSignup.this, "enter your mobile number", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_password.length() == 0) {
                Toast.makeText(DoctorSignup.this, "enter your password", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_hospital.length() == 0) {
                Toast.makeText(DoctorSignup.this, "enter your hospital", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_mobile.length() != 10) {
                Toast.makeText(DoctorSignup.this, "Enter 10 digit number", Toast.LENGTH_LONG).show();
                return;
            }


            String url = ApplicationController.get_base_url() + "dhadkan/api/onboard/doc";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("DATA", response.toString());
//                            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor edit = pref.edit();
                            try {


                                int U_ID = Integer.parseInt(response.get("U_ID").toString());
                                String token = "" + response.get("Token");
                                int ID = Integer.parseInt(response.get("ID").toString());

                                session.createLoginSession(token, U_ID, "doctor", ID);
                                Intent i = new Intent(DoctorSignup.this, ControllerActivity.class);
                                startActivity(i);
                                finish();
                                Log.d("DATA", response.toString());
//
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
                public byte[] getBody() {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("name", "" + name.getText());
                        params.put("password", "" + password.getText());
                        params.put("mobile", mobile.getText());
                        params.put("email", "" + email.getText());
                        params.put("hospital", "" + hospital.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return params.toString().getBytes();

                }
            };
            ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

        }
//
//        else if(view.getId() == R.id.sign_in){
//            OnClickSignIn.sign_in_clicked(DocRegisterActivity.this);
//        }

    }
}

