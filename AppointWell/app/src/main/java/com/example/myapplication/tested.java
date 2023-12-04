//package com.example.myapplication;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//
//    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    Context context;
//    Dialog dialog;
//    String nameMsg, emailMsg, addressMsg, phoneNumMsg, typeMsg, healthCardMsg;
//
//    ArrayList<AppointmentRequest> list;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener clickListener){
//        listener = clickListener;
//    }
//
//
//    public MyAdapter(Context context, ArrayList<AppointmentRequest> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//
//
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
//        return  new MyViewHolder(v, listener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
//
//        AppointmentRequest request = list.get(position);
//        holder.name.setText(request.getPatientName());
//        String countID = String.valueOf(request.getCountID());
//
//        // Assuming your AppointmentRequest class has getDate() and getStartTime() methods
////        holder.date.setText(request.getDate());
////        Date last_date_date = null;
////        try {
////            last_date_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getDate());
////            holder.date.setText(new SimpleDateFormat("MMM dd yyyy").format(last_date_date));
////        } catch (ParseException e) {
////            throw new RuntimeException(e);
////        }
//
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
////            LocalDate date = LocalDate.parse(request.getDate(), formatter);
////            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd      MMM yyyy");
////            String strDate = newFormatter.format(date);
////            holder.date.setText(strDate);
////        }
//
//        holder.date.setText(request.getDate());
//
//        holder.time.setText(request.getStartTime() + " - " + request.getEndTime());
//
//        // Reject button on click
//        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
//                DatabaseReference reference = database.child("Users").child("Approved Users").child(uID).child("Appointments");
//
//                reference.child(countID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // If the removal from Firebase is successful, remove the item from the local list
//                        list.remove(request);
//                        notifyDataSetChanged(); // Notify the adapter that the data set has changed
//                    }
//                });
//            }
//        });
//
//        if(request.getStatus().equals("Approved") || request.getStatus().equals("Completed")){
//            holder.approveRequestBtn.setVisibility(View.GONE);
//        }
//
//        if (request.getStatus().equals("Pending")){
//            holder.approveRequestBtn.setVisibility(View.VISIBLE);
//            // Approve button on click
//            holder.approveRequestBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
//                    DatabaseReference reference = database.child("Users").child("Approved Users").child(uID).child("Appointments");
//                    reference.child(countID).child("status").getRef().setValue("Approved").addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            list.remove(request);
//                            notifyDataSetChanged(); // Notify the adapter that the data set has changed
//                        }
//                    });
//                }
//            });
//
//        }
//
//
//
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Use LayoutInflater from the context
//                LayoutInflater inflater = LayoutInflater.from(context);
//                View slideView = inflater.inflate(R.layout.appointment_request_bottom_dialogue, null, false);
//                TextView name = (TextView) slideView.findViewById(R.id.slideInName);
//                TextView address = (TextView) slideView.findViewById(R.id.slideInAddressText);
//                TextView healthCard = (TextView) slideView.findViewById(R.id.slideInHealthCardNumberText);
//                TextView email = (TextView) slideView.findViewById(R.id.slideInEmailText);
//                TextView phoneNum = (TextView) slideView.findViewById(R.id.slideInPhoneNumberText);
//                TextView type = (TextView) slideView.findViewById(R.id.slideInType);
//                DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
//                database.child("Users").child("Approved Users").child(request.getPatientUID()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            UserAccount userAccount = snapshot.getValue(UserAccount.class);
//
//                            if (userAccount!=null){
//                                if (userAccount.getType().equals("Patient")){
//
//                                    Patient patient=snapshot.getValue(Patient.class);
//
//                                    nameMsg = patient.getFirstName()+" "+patient.getLastName();
//
//                                    addressMsg = "Address: "+patient.getAddress();
//
//                                    healthCardMsg = "Health Card Number: " + patient.getHealthCardNumber();
//
//                                    emailMsg = "Email: "+patient.getEmail();
//
//                                    phoneNumMsg = "Phone Number: " + patient.getPhoneNumber();
//
//                                    typeMsg = patient.getType();
//                                }
//                            }
//                            name.setText(nameMsg);
//                            address.setText(addressMsg);
//                            healthCard.setText(healthCardMsg);
//                            email.setText(emailMsg);
//                            phoneNum.setText(phoneNumMsg);
//                            type.setText(typeMsg);
//
//                        }
//                    }
//
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//                if (request.getStatus().equals("Approved")){
//                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton completedBtn = (ImageButton) slideView.findViewById(R.id.completedBtn);
//                    completedBtn.setVisibility(view.VISIBLE);
//                    completedBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://new-database-b712b-default-rtdb.firebaseio.com/");
//                            DatabaseReference reference = database.child("Users").child("Approved Users").child(uID).child("Appointments");
//                            reference.child(countID).child("status").getRef().setValue("Completed").addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    notifyDataSetChanged(); // Notify the adapter that the data set has changed
//                                }
//                            });
//                        }
//                    });
//                }
//
//                dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(slideView);
//                dialog.show();
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//        TextView date, name, time;
//        ImageButton approveRequestBtn, rejectbtn;
//        LinearLayout linearLayout;
//        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
//            super(itemView);
//            date = itemView.findViewById(R.id.date);
//            name = itemView.findViewById(R.id.patientName);
//            time = itemView.findViewById(R.id.time);
//            approveRequestBtn = itemView.findViewById(R.id.approveRequestBtn);
//            rejectbtn = itemView.findViewById(R.id.rejectRequestBtn);
//            linearLayout = itemView.findViewById(R.id.clickformoreinfo);
//
//        }
//    }
//
//}
//
