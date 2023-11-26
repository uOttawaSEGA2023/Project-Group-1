package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;

public class SearchAppointmentAdapter extends RecyclerView.Adapter<SearchAppointmentAdapter.MyViewHolder> {
    private String specialty;
    private Context context;
    private ArrayList<TimeSlot> timeSlots;
    public SearchAppointmentAdapter(Context context, ArrayList<TimeSlot> timeSlots, String specialty){
        this.context = context;
        this.timeSlots = timeSlots;
        this.specialty = specialty;
    }
    @NonNull
    @Override
    public SearchAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_slot_card, parent, false);
        return new SearchAppointmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAppointmentAdapter.MyViewHolder holder, int position) {
        holder.timeTV.setText(timeSlots.get(position).getStartTime() + " - " + timeSlots.get(position).getEndTime());
        holder.dateTV.setText(timeSlots.get(position).getDate());
        holder.nameTV.setText("Dr " + timeSlots.get(position).getDoctorName());
        holder.specialtyTV.setText(specialty);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OCEANE FER HA GOGOT LA", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView timeTV, dateTV, nameTV, specialtyTV;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.timetxt);
            dateTV = itemView.findViewById(R.id.datetxt);
            nameTV = itemView.findViewById(R.id.nametxt);
            specialtyTV = itemView.findViewById(R.id.specialtytxt);
            linearLayout = itemView.findViewById(R.id.linearlayoutTS);
        }
    }
}
