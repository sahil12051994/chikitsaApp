package com.chikitsa.root.chikitsa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter <NotificationAdapter.MyViewHolder>{

    List<String> data = Collections.emptyList();
    private LayoutInflater inflater;


    public NotificationAdapter(Context context, List<String> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.notificationrow, parent, false);
        NotificationAdapter.MyViewHolder viewHolder = new NotificationAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        String current = data.get(position);
//        String[] _age = current.age.split("-");
        holder.text.setText(current);
//        Log.d("TAG", _age.toString());
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", data.size()+"");
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        public MyViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.TextView17);

        }
    }
}
