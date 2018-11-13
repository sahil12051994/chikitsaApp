package com.chikitsa.root.chikitsa;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class PatientListRowInDoctorClass {

    private String patientName;
    private String gender, endDate, email;
    private int patientId, mobile;
    private ArrayList<Entry> yValues = new ArrayList<Entry>();
    private ArrayList<Entry> y2Values = new ArrayList<Entry>();


    public PatientListRowInDoctorClass(int patientId, String name, String gender, String email, int mobile){

        this.patientName = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.patientId = patientId;
    }

    public String getName() {return patientName; }

    public int getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public int getPatientId() {return patientId; }

}
