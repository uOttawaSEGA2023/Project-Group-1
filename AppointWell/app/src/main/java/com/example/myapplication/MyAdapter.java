package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Context context;
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");
    String nameMsg, emailMsg, addressMsg, phoneNumMsg, typeMsg, healthCardMsg;


    ArrayList<AppointmentRequest> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }


    public MyAdapter(Context context, ArrayList<AppointmentRequest> list) {
        this.context = context;
        this.list = list;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return  new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AppointmentRequest request = list.get(position);

        // get doctor name from data base using doctor uID
        String doctorUID = request.getDoctorUID();

        // get patient uID
        String patientUID = request.getPatientUID();

        DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
        DatabaseReference reference = database.child("Users").child("Approved Users").child(patientUID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String docFirstName = String.valueOf(snapshot.child("firstName").getValue());
                String docLastName = String.valueOf(snapshot.child("lastName").getValue());
                holder.name.setText(docFirstName+ " " + docLastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String countID = String.valueOf(request.getCountID());



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);

            try {
                LocalDate date = LocalDate.parse(request.getDate(), formatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                String strDate = newFormatter.format(date);
                holder.date.setText(strDate);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                // Handle the exception or log an error message
            }
        }

        holder.time.setText(request.getStartTime() + " - " + request.getEndTime());



        // Reject btn
        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    //getting patient object from DB
                    approvedDB.child(patientUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Patient patient = snapshot.getValue(Patient.class);
                                if(patient!=null){
                                    String patientName = patient.getFirstName() + " " + patient.getLastName();
                                    AppointmentRequest appointmentRequest = new AppointmentRequest(patientName,patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                    patient.removeUpcomingAppointment(patientUID,appointmentRequest);
                                    list.remove(request);
                                    notifyDataSetChanged(); // Notify the adapter that the data set has changed
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
                                    AppointmentRequest appointmentRequest = new AppointmentRequest(request.getPatientName(),patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                    doctor.removeUpcomingAppointment(doctorUID, appointmentRequest);
                                    //approvedDB.child(doctorUID).setValue(doctor);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        });

        // end - reject btn


        // start - approve btn
        holder.approveRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getting patient object from DB
                approvedDB.child(patientUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Patient patient = snapshot.getValue(Patient.class);
                            if(patient!=null){
                                String patientName = patient.getFirstName() + " " + patient.getLastName();
                                AppointmentRequest appointmentRequest = new AppointmentRequest(patientName,patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                patient.approveAppointment(patientUID,appointmentRequest);
                                list.remove(request);
                                notifyDataSetChanged(); // Notify the adapter that the data set has changed
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
                                AppointmentRequest appointmentRequest = new AppointmentRequest(request.getPatientName(),patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                doctor.approveAppointment(doctorUID, appointmentRequest);
                                //approvedDB.child(doctorUID).setValue(doctor);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        // end - approve btn


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use LayoutInflater from the context
                LayoutInflater inflater = LayoutInflater.from(context);
                View slideView = inflater.inflate(R.layout.appointment_request_bottom_dialogue, null, false);
                TextView name = (TextView) slideView.findViewById(R.id.slideInName);
                TextView address = (TextView) slideView.findViewById(R.id.slideInAddressText);
                TextView healthCard = (TextView) slideView.findViewById(R.id.slideInHealthCardNumberText);
                TextView email = (TextView) slideView.findViewById(R.id.slideInEmailText);
                TextView phoneNum = (TextView) slideView.findViewById(R.id.slideInPhoneNumberText);
                TextView type = (TextView) slideView.findViewById(R.id.slideInType);
                DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
                database.child("Users").child("Approved Users").child(request.getPatientUID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserAccount userAccount = snapshot.getValue(UserAccount.class);

                            if (userAccount!=null){
                                if (userAccount.getType().equals("Patient")){

                                    Patient patient=snapshot.getValue(Patient.class);

                                    nameMsg = patient.getFirstName()+" "+patient.getLastName();

                                    addressMsg = "Address: "+patient.getAddress();

                                    healthCardMsg = "Health Card Number: " + patient.getHealthCardNumber();

                                    emailMsg = "Email: "+patient.getEmail();

                                    phoneNumMsg = "Phone Number: " + patient.getPhoneNumber();

                                    typeMsg = patient.getType();
                                }
                            }
                            name.setText(nameMsg);
                            address.setText(addressMsg);
                            healthCard.setText(healthCardMsg);
                            email.setText(emailMsg);
                            phoneNum.setText(phoneNumMsg);
                            type.setText(typeMsg);

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                if (request.getStatus().equals("Approved")){
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton completedBtn = (ImageButton) slideView.findViewById(R.id.completedBtn);
                    completedBtn.setVisibility(view.VISIBLE);
                    completedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //getting doctor object from DB
                            approvedDB.child(doctorUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Doctor doctor = snapshot.getValue(Doctor.class);
                                        if(doctor!=null){
                                            AppointmentRequest appointmentRequest = new AppointmentRequest(request.getPatientName(),patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                            doctor.completeAppointment(doctorUID, appointmentRequest);
                                            list.remove(request);
                                            notifyDataSetChanged(); // Notify the adapter that the data set has changed
                                            //approvedDB.child(doctorUID).setValue(doctor);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            //getting patient object from DB
                            approvedDB.child(request.getPatientUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Patient patient = snapshot.getValue(Patient.class);
                                        if(patient!=null){
                                            AppointmentRequest appointmentRequest = new AppointmentRequest(request.getPatientName(),patientUID,"Pending", request.getStartTime(),request.getEndTime(),request.getDate(),doctorUID);
                                            patient.completeAppointment(request.getPatientUID(), appointmentRequest);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    });
                }

                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(slideView);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
















    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, name, time;
        ImageButton approveRequestBtn, rejectbtn;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.patientName);
            time = itemView.findViewById(R.id.time);
            approveRequestBtn = itemView.findViewById(R.id.approveRequestBtn);
            rejectbtn = itemView.findViewById(R.id.rejectRequestBtn);
            linearLayout = itemView.findViewById(R.id.clickformoreinfo);

        }
    }


}
