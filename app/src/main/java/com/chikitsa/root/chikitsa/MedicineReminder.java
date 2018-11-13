package com.chikitsa.root.chikitsa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MedicineReminder extends AppCompatActivity {

    ListView patient_list_view;
    static SessionManager session;
    Dialog add_medicine_dialog;
    Button addMedButton;
    Button add_pic;
    ImageView ivImage;
    private String userChoosenTask;
    EditText medName, medStart, medEnd, medComments;
    int clickedPatientId;
    RadioGroup radioGroupFReq;
    RadioButton radioGroupFReqDaily, radioGroupFReqWeekly, radioGroupFReqMonthly;
    FloatingActionButton fab;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Dialog choose_doc;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private Button done;
    EditText doc_number;
    TextView doc_name, docHospital, docSpeciality;
    private SimpleDateFormat dateFormatterShow, dateFormatterServer;
    String medStartTime, medEndTime;
    int doc_id;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String ImgBytes = "";
    Calendar newDate1 = Calendar.getInstance();
    Calendar newDate2 = Calendar.getInstance();
    Menu myMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.action_logout){
            logout(this);
            return true;
        } else if (item.getItemId() == R.id.action_change_doctor){
            change_doctor();
        } else if (item.getItemId() == R.id.action_notification){
            i = new Intent(this, PatientNotifications.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.hospitalsNearYou){
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=nearbychemist");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(Context _c) {
        session = new SessionManager(_c);
        session.logoutUser();
        Intent i = new Intent(MedicineReminder.this, ControllerActivity.class);
        startActivity(i);
    }

    private void change_doctor() {
        choose_doc = new Dialog(this);
        choose_doc.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choose_doc.setContentView(R.layout.choose_doc_dialog);
        choose_doc.setCancelable(true);
        done = choose_doc.findViewById(R.id.changeDocButton);

        doc_number = choose_doc.findViewById(R.id.enteredNumber);
        doc_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 10) {
                    String url = ApplicationController.get_base_url() + "api/doctor?mobile=" + editable.toString();
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());

                                    try {
                                        doc_name.setText(response.get("name") + "");
                                        docHospital.setText(response.get("hospital") + "");
                                        docSpeciality.setText(response.get("speciality") + "");
                                        doc_id = (int) response.get("pk");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        doc_name.setText("");
                                        Toast.makeText(MedicineReminder.this, "No doctor with this mobile number is registered", Toast.LENGTH_SHORT).show();

                                    }
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
//                params.put("Authorization", "Token f00a64734d608991730ccba944776c316c38c544");
                            return params;
                        }

                    };
                    ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
                }
            }
        });

        doc_name = choose_doc.findViewById(R.id.docName);
        docHospital = choose_doc.findViewById(R.id.docHospital);
        docSpeciality = choose_doc.findViewById(R.id.docSpeciality);

        choose_doc.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(choose_doc.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 80%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.65f);
        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        // Apply the newly created layout parameters to the alert dialog window
        choose_doc.getWindow().setAttributes(layoutParams);


        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.login) {


                    String url = ApplicationController.get_base_url() + "api/patient/" + session.getUserDetails().get("id");
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());
                                    choose_doc.dismiss();
                                    Toast.makeText(MedicineReminder.this, "Doctor changed", Toast.LENGTH_LONG).show();

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
                                params.put("d_id", doc_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return params.toString().getBytes();

                        }

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

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        dateFormatterShow = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:dd'Z'");

        session = new SessionManager(this);
        clickedPatientId = getIntent().getIntExtra("EXTRA_PATIENT_ID", 0);
//        final HashMap<String, String> user = session.getUserDetails();
//        if ("doctor".equals(user.get("type"))) {
//            clickedPatientId = getIntent().getIntExtra("EXTRA_PATIENT_ID", 0);
//        } else if ("patient".equals(user.get("type"))) {
//            clickedPatientId = Integer.parseInt(session.getUserDetails().get("id"));
//        }
        Log.i("iiiidddd", "onCreate: " + clickedPatientId);
        getPatientData(clickedPatientId);

        fab = findViewById(R.id.fab);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshMedicine);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMedicineDialog();
            }
        });
        final HashMap<String, String> user = session.getUserDetails();
        if ("doctor".equals(user.get("type"))) {
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showAddMedicineDialog();
//                }
//            });
//            myMenu.findItem(R.id.hospitalsNearYou).setEnabled(false);
//            myMenu.findItem(R.id.action_change_doctor).setEnabled(false);
//            myMenu.findItem(R.id.action_notification).setEnabled(false);
        } else if ("patient".equals(user.get("type"))) {
//            fab.hide();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPatientData(clickedPatientId);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] ba  = bytes.toByteArray();
        ImgBytes = Base64.encodeToString(ba, 0);
        ivImage.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        Bitmap bm=null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte[] ba  = bytes.toByteArray();
        ImgBytes = Base64.encodeToString(ba, 0);
        Log.e("TAG", ImgBytes);
        ivImage.setImageBitmap(bm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MedicineReminder.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MedicineReminder.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void showAddMedicineDialog() {
        add_medicine_dialog = new Dialog(this);
        add_medicine_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_medicine_dialog.setContentView(R.layout.medicine_add_screen);
        add_medicine_dialog.setCancelable(true);
        add_medicine_dialog.show();

        addMedButton = add_medicine_dialog.findViewById(R.id.addMedButton);
        medName = add_medicine_dialog.findViewById(R.id.medName);

        medStart = add_medicine_dialog.findViewById(R.id.medStart);
        medStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate1.set(year, monthOfYear, dayOfMonth);
                medStart.setText(dateFormatterShow.format(newDate1.getTime()));
                medStartTime = dateFormatterServer.format(newDate1.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        medEnd = add_medicine_dialog.findViewById(R.id.medEnd);
        medEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate2.set(year, monthOfYear, dayOfMonth);
                medEnd.setText(dateFormatterShow.format(newDate2.getTime()));
                medEndTime = dateFormatterServer.format(newDate2.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        medComments = add_medicine_dialog.findViewById(R.id.medComments);
        radioGroupFReq = add_medicine_dialog.findViewById(R.id.radioGroupFreq);

        radioGroupFReqDaily = add_medicine_dialog.findViewById(R.id.daily);
        radioGroupFReqWeekly = add_medicine_dialog.findViewById(R.id.weekly);
        radioGroupFReqMonthly = add_medicine_dialog.findViewById(R.id.monthly);

        add_pic = add_medicine_dialog.findViewById(R.id.add_pic);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        addMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ApplicationController.get_base_url() + "chikitsa/medicines/add/" + clickedPatientId;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Intent i = new Intent(MedicineReminder.this, MedicineReminder.class);
                                try {
                                    i.putExtra("EXTRA_PATIENT_ID", response.getInt("patient_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(i);
                                finish();
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
                            params.put("patient_id", clickedPatientId);
                            params.put("medicine_name", medName.getText());

                            String freq = "daily";
                            if (radioGroupFReqDaily.isChecked()) {
                                freq = "daily";
                            } else if (radioGroupFReqMonthly.isChecked()) {
                                freq = "monthly";
                            } else if (radioGroupFReqWeekly.isChecked()) {
                                freq = "weekly";
                            }

                            params.put("medicine_freq", freq);
//                            String extraComments="";
//                            if(medComments.getText().length() == 0){
//                                extraComments = "No Comments";
//                            } else {
//                                extraComments = medComments.getText();
//                            }
                            params.put("medicine_extra_comments", medComments.getText());
                            if(ImgBytes.length() == 0){
                                params.put("medicine_Image", "Sample image byte for Medicine");
                            } else {
                                params.put("medicine_Image", ImgBytes);
                            }
                            params.put("medicine_start", medStartTime);
                            params.put("medicine_end", medEndTime);

                            Log.i("Boddddyyyyy", "getBody: " + params.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return params.toString().getBytes();

                    }

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
        });
    }


    public void getPatientData(int id) {
        String url = ApplicationController.get_base_url() + "chikitsa/medicines/" + id;
        final ArrayList<MedicineListClass> medicineRowAdapter = new ArrayList<MedicineListClass>();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("apihit", response.toString());
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject po = (JSONObject) response.get(i);
                                MedicineListClass pr = new MedicineListClass(po.getString("medicine_name"), po.getString("medicine_start"), po.getString("medicine_end"), po.getString("medicine_freq"), po.getString("medicine_extra_comments"), po.getString("medicine_Image"));
                                medicineRowAdapter.add(pr);
                                Log.i("Data in array", "" + String.valueOf(response.get(i)));
                            }
                            MedicineAdapter itemsAdapter = new MedicineAdapter(MedicineReminder.this, medicineRowAdapter);
                            ListView listView = (ListView) findViewById(R.id.patient_medicine_list);
                            listView.setAdapter(itemsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
