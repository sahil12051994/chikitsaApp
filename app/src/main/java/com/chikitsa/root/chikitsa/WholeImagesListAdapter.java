package com.chikitsa.root.chikitsa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WholeImagesListAdapter extends ArrayAdapter<WholeImagesListClass> {

    static SessionManager session;

    public WholeImagesListAdapter(Activity context, ArrayList<WholeImagesListClass> patientData) {
        super(context, 0, patientData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WholeImagesListClass current_medicine_data = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.patient_image_row, parent, false);
        }

        final TextView extraComments = (TextView)listItemView.findViewById(R.id.extraCommentsInImage);
        extraComments.setText(current_medicine_data.getExtraComments());

        final TextView dateText = (TextView)listItemView.findViewById(R.id.image_date);
        dateText.setText(current_medicine_data.getDateString());
//        session = new SessionManager(getContext());
//        final HashMap<String, String> user = session.getUserDetails();
//        Log.i("typeeeeee", "getView: " + user.get("type"));
//        if ("patient".equals(user.get("type"))){
//
//        } else if ("doctor".equals(user.get("type"))){
//
//        }
        return listItemView;
    }
}
