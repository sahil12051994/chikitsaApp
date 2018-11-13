package com.chikitsa.root.chikitsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class PatientListRow extends AppCompatActivity {

    ArrayList<PatientListRowInDoctorClass> patientRowData = new ArrayList<PatientListRowInDoctorClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list_row);
    }
}
