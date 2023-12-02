package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class SearchAppointmentAdapter extends RecyclerView.Adapter<SearchAppointmentAdapter.MyViewHolder> {
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");
    private String specialty;
    private List<AppointmentRequest> appointmentRequestList;

    private Context context;
    private ArrayList<TimeSlot> timeSlots;
    private String patientUID;
    private String patientName;
    public SearchAppointmentAdapter(Context context, ArrayList<TimeSlot> timeSlots, String specialty, String patientUID){
        this.context = context;
        this.timeSlots = timeSlots;
        this.specialty = specialty;
        this.patientUID = patientUID;
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
        holder.specialtyTV.setText("Specialty: " + specialty);

        Log.d("kk", "1"+timeSlots.get(position).getStartTime() + " - " + timeSlots.get(position).getEndTime());

        String doctorUID = timeSlots.get(position).getDoctorID();

        //KIFR LI P ADD ALWAYS THE NEXT TIME SLOT PARTOU????
        holder.bookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting patient object from DB
                approvedDB.child(patientUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Patient patient = snapshot.getValue(Patient.class);
                            if(patient!=null){
                                patientName = patient.getFirstName() + " " + patient.getLastName();
                                AppointmentRequest appointmentRequest = new AppointmentRequest(patientName,patientUID,"Pending", timeSlots.get(position).getStartTime(),timeSlots.get(position).getEndTime(),timeSlots.get(position).getDate(),doctorUID);
                                patient.addUpcomingAppointment(patientUID, appointmentRequest);


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //getting doctor object from DB
                approvedDB.child(doctorUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if(doctor!=null){
                                AppointmentRequest appointmentRequest = new AppointmentRequest(patientName,patientUID,"Pending", timeSlots.get(position).getStartTime(),timeSlots.get(position).getEndTime(),timeSlots.get(position).getDate(),doctorUID);
                                doctor.addUpcomingAppointment(doctorUID, appointmentRequest);
                                //remove timeslot from doctors list of available timeslots
                                doctor.removeAvailableTimeSlot(doctorUID,timeSlots.get(position));
                                //approvedDB.child(doctorUID).setValue(doctor);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //remove time slot from available time slots

                DatabaseReference tsDB =FirebaseDatabase.getInstance().getReference().child("Available Time Slots");

                tsDB.child(timeSlots.get(position).getDate()+"-"+timeSlots.get(position).getStartTime()+"-"+timeSlots.get(position).getDoctorID()).removeValue();

            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView timeTV, dateTV, nameTV, specialtyTV;
        private ImageButton bookBTN;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.timetxt);
            dateTV = itemView.findViewById(R.id.datetxt);
            nameTV = itemView.findViewById(R.id.nametxt);
            specialtyTV = itemView.findViewById(R.id.specialtytxt);
            bookBTN = itemView.findViewById(R.id.bookBTN);
        }
    }


}
