package com.chikitsa.root.chikitsa;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllPatientListInDoctorAdapter extends ArrayAdapter<PatientListRowInDoctorClass> {

    public AllPatientListInDoctorAdapter(Activity context, ArrayList<PatientListRowInDoctorClass> patientData) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PatientListRowInDoctorClass current_patient_data = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_patient_list_row, parent, false);
        }

        TextView patientName = (TextView)listItemView.findViewById(R.id.PatientNameInList);
        patientName.setText(current_patient_data.getName());

        TextView patientGender = (TextView)listItemView.findViewById(R.id.patientGender);
        patientGender.setText(current_patient_data.getGender());

        TextView patientMobile = (TextView)listItemView.findViewById(R.id.patientMobile);
        patientMobile.setText(String.valueOf(current_patient_data.getMobile()));

        final int patientId = current_patient_data.getPatientId();

        ImageView getInsideParticularPatient = (ImageView) listItemView.findViewById(R.id.getInsidePatient);
        getInsideParticularPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MedicineReminder.class);
                intent.putExtra("EXTRA_PATIENT_ID", patientId);
                v.getContext().startActivity(intent);
            }
        });

        return listItemView;
    }
}
