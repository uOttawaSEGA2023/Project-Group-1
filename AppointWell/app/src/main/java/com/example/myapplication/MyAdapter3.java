package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder> {

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


    public MyAdapter3(Context context, ArrayList<AppointmentRequest> list) {
        this.context = context;
        this.list = list;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.completed_patient_appointment_card,parent,false);
        return  new MyViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AppointmentRequest request = list.get(position);

        // get doctor name from data base using doctor uID
        String doctorUID = request.getDoctorUID();

        // get patient uID
        String patientUID = request.getPatientUID();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference = database.child("Users").child("Approved Users").child(doctorUID);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Doctor doc = snapshot.getValue(Doctor.class);
                    if (doc!=null){
                        holder.name.setText(doc.getFirstName()+" "+doc.getLastName());

                        holder.rating.setText(String.valueOf(doc.getAvgRating()));
                    }

//                    String docFirstName = String.valueOf(snapshot.child("firstName").getValue());
//                    String docLastName = String.valueOf(snapshot.child("lastName").getValue());
                  //  holder.name.setText(docFirstName + " " + docLastName);
                }
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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
//
//            try {
//                LocalDate date = LocalDate.parse(request.getDate(), formatter);
//                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
//                String strDate = newFormatter.format(date);
//                holder.date.setText(strDate);
//            } catch (DateTimeParseException e) {
//                e.printStackTrace();
//                // Handle the exception or log an error message
//            }
//        }

        holder.date.setText(request.getDate());
        holder.time.setText(request.getStartTime() + " - " + request.getEndTime());


        holder.rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PopUpActivity.class);
                intent.putExtra("doctorUID",request.getDoctorUID());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });





        // new cancel btn
//        holder.rateBtn.setOnClickListener(new View.OnClickListener() {
//
//        });

        // end - new cancel btn

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, name, time, rating;
        Button rateBtn;
//        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            rating = itemView.findViewById(R.id.rating);
            rateBtn = itemView.findViewById(R.id.rate);
//            linearLayout = itemView.findViewById(R.id.clickformoreinfo);

        }
    }

}
