package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;


public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Context context;
    private DatabaseReference approvedDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Approved Users");
    String nameMsg, emailMsg, addressMsg, phoneNumMsg, typeMsg, healthCardMsg;
    boolean timeDiff = false;


    ArrayList<AppointmentRequest> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }


    public MyAdapter2(Context context, ArrayList<AppointmentRequest> list) {
        this.context = context;
        this.list = list;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.approved_patient_appointment_card,parent,false);
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
        DatabaseReference reference = database.child("Users").child("Approved Users").child(doctorUID);
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

        // Assuming your AppointmentRequest class has getDate() and getStartTime() methods
//        holder.date.setText(request.getDate());
//        Date last_date_date = null;
//        try {
//            last_date_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getDate());
//            holder.date.setText(new SimpleDateFormat("MMM dd yyyy").format(last_date_date));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
//            LocalDate date = LocalDate.parse(request.getDate(), formatter);
//            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd      MMM yyyy");
//            String strDate = newFormatter.format(date);
//            holder.date.setText(strDate);
//        }

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


        // Get the current time
        LocalTime currentTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
        }

// Parse the start time from the appointment request
        String startTimeString = request.getStartTime();
        LocalTime startTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(currentTime.compareTo(startTime.minusHours(1)) > 0){
                timeDiff = true;
            }
            else{
                timeDiff= false;
            }
        }


        // new cancel btn
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if ( timeDiff == true){
                    Toast.makeText(context,  "The appointment is less than 1 hour away from the start time", Toast.LENGTH_SHORT).show();
                }
                else{
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

            }
        });

        // end - new cancel btn

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, name, time;
        ImageButton cancelBtn;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.doctorName);
            time = itemView.findViewById(R.id.time);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            linearLayout = itemView.findViewById(R.id.clickformoreinfo);

        }
    }

}
