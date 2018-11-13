package com.chikitsa.root.chikitsa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PatientSignup extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Button register;
    EditText date_of_birth, name, address, email, mobile, password, doctor_number, doctor_name, lmpDate;

    int doc_id;
    String token, type;
    int u_id, id;
    SessionManager session;
    CheckBox highbp, histPree, motherPre, histObes, moreThanOneBaby, diseases;
    private DatePickerDialog lmpDatePickerDialog;

    private SimpleDateFormat dateFormatterShow, dateFormatterServer;
    String lmpTime;

    Calendar newDate1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);

        getSupportActionBar().setTitle("Patient Registration");
        name = (EditText) findViewById(R.id.editText);
        address = (EditText) findViewById(R.id.editText2);
        email = (EditText) findViewById(R.id.editText4);
        mobile = (EditText) findViewById(R.id.editText5);
        password = (EditText) findViewById(R.id.editText6);

        doctor_number = (EditText) findViewById(R.id.editText7);
        doctor_number.addTextChangedListener(this);
        doctor_name = (EditText) findViewById(R.id.editText10);



        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        date_of_birth = (EditText) findViewById(R.id.editText3);

        session = new SessionManager(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            String str_mobile = "" + mobile.getText();
            String str_password = "" + password.getText();
            String str_name = "" + name.getText();
            String str_address = "" + address.getText();
            String str_dob = "" + date_of_birth.getText();
            String str_doctor_mobile = "" + doctor_number.getText();
            String str_doc_name = "" +doctor_name.getText();
            String str_lmpDate = ""+lmpDate.getText();
            if (str_name.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your name", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_address.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your address", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_mobile.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your mobile number", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_password.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your password", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_dob.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your date of birth", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_doctor_mobile.length() == 0) {
                Toast.makeText(PatientSignup.this, "Enter your doctor's number", Toast.LENGTH_LONG).show();
                return;
            }

            if (str_doc_name.length() == 0 ) {
                Toast.makeText(PatientSignup.this, "Enter a valid doctor's number", Toast.LENGTH_LONG).show();
                return;
            }

            String url = ApplicationController.get_base_url() + "api/onboard/patient";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());
//                            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor edit = pref.edit();
                            try {
                                int U_ID = Integer.parseInt(response.get("U_ID").toString());
                                String token = "" + response.get("Token");
                                int ID = Integer.parseInt(response.get("ID").toString());
                                session.createLoginSession(token, U_ID, "patient", ID);
                                Intent i = new Intent(PatientSignup.this, ControllerActivity.class);
                                startActivity(i);
                                finish();
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
//                    int selectedId = sexRadioGroup.getCheckedRadioButtonId();
//                    int gender;
//                    if (selectedId == R.id.radioMale) {
//                        gender = 1;
//                    }
//                    else{
//                        gender = 0;
//                    }

                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    try {
                        params.put("name", "" + name.getText());
                        params.put("address", "" + address.getText());
                        params.put("password", "" + password.getText());
                        params.put("mobile", mobile.getText());
                        params.put("email", "" + email.getText());
                        params.put("doctor", doc_id);
                        params.put("date_of_birth", date_of_birth.getText());
                        params.put("gender", 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return params.toString().getBytes();

                }
            };
            ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(final Editable editable) {
        if (editable.toString().length() == 10) {
            String url = ApplicationController.get_base_url() + "api/doctor?mobile=" + editable.toString();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                doctor_name.setText(response.get("name") + "");
                                doc_id = (int) response.get("pk");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                doctor_name.setText("");
                                Toast.makeText(PatientSignup.this, "No doctor with this mobile number is registered", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TAG", "Error Message: " + error.getMessage());
                }
            }) {

            };
            ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }
}
