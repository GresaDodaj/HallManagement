package com.smrtsols.www.hall.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import com.smrtsols.www.hall.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Reservation extends AppCompatActivity {
    EditText Start_Date,End_Date,Price,PriceWeek;
    Button Reserve;
    Spinner Hall_no;
    TextView Total;
    ArrayList<String> GetResrvedHall;
    FirebaseFirestore firebaseFirestore;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        GetResrvedHall=new ArrayList<>();
        Start_Date=(EditText)findViewById(R.id.txt_Facilities);
        End_Date=(EditText)findViewById(R.id.txt_EndDate);
        Price=(EditText)findViewById(R.id.txt_PricePerDay);
        PriceWeek=(EditText)findViewById(R.id.txt_PricePerWeek);
        Hall_no=(Spinner)findViewById(R.id.spinner_HallNo);
        Total=(TextView)findViewById(R.id.txt_Total);
        firebaseFirestore=FirebaseFirestore.getInstance();
        Reserve=(Button)findViewById(R.id.btn_reservation);

        PriceWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reserve.setVisibility(View.VISIBLE);
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        Start_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Reservation.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        End_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog =      datePickerDialog=new DatePickerDialog(Reservation.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));;

                datePickerDialog.show();



            }
        });

        ReadHallNo();

        Reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Start_Date.getText().toString().length()<1 || End_Date.getText().toString().length()<0 || Price.getText().toString().length()<0 || PriceWeek.getText().toString().length()<0)
                {
                    Toast.makeText(getApplicationContext(),"Fill All Require Fields First",Toast.LENGTH_LONG).show();
                }
                if(Start_Date.getText().toString().equals(End_Date.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Select Correct End Date",Toast.LENGTH_LONG).show();
                    End_Date.requestFocus();
                }

                else
                {

                    Long longs=getDaysBetweenDates(Start_Date.getText().toString(),End_Date.getText().toString());
                    //  Toast.makeText(Reservation.this,"Days Difference: "+longs,Toast.LENGTH_LONG).show();
                    int Total_Price= (int) (Integer.parseInt(Price.getText().toString())*longs);
                    Total.setText("Total Price: "+Total_Price);
                    Map<String,Object> Reservations=new HashMap<>();
                    Reservations.put("Reserved_By",getIntent().getExtras().getString("UserName"));
                    Reservations.put("Hall_No",Hall_no.getSelectedItem().toString().toString().trim());
                    Reservations.put("Start_Date",Start_Date.getText().toString().trim());
                    Reservations.put("End_Date",End_Date.getText().toString().trim());
                    Reservations.put("Price_PerDay",Price.getText().toString().trim());
                    Reservations.put("Price_PerWeek",PriceWeek.getText().toString().trim());
                    Reservations.put("Total_Price",String.valueOf(Total_Price));

                    firebaseFirestore.collection("Reservation").document(getDocumentName()).set(Reservations)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Update();
                                    Toast.makeText(Reservation.this,"Reservation Done Sucessfully",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Reservation.this,Hall.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }

            }
        });


    }
    private void ReadHallNo() {
        Query query = firebaseFirestore.collection("Hall");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> titleList = new ArrayList<String>();
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){
                        String titlename = readData.get("ID").toString();
                        titleList.add(titlename);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Reservation.this, android.R.layout.simple_spinner_item, titleList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Hall_no.setAdapter(arrayAdapter);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reservation.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("Reservation Error: ", e.getMessage());
                    }
                });
    }

    private void ReadReserved_Hall() {
        Query query = firebaseFirestore.collection("Reservation");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> titleList = new ArrayList<String>();
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){
                        String titlename = readData.get("Hall_No").toString();
                        GetResrvedHall.add(titlename);
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reservation.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("Reservation Error: ", e.getMessage());
                    }
                });
    }
    private void Update() {

        final CollectionReference complaintsRef = firebaseFirestore.collection("Hall");
        complaintsRef.whereEqualTo("ID", 2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("Status", "Reserved");
                        complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });
    }
    protected String getDocumentName() {
        String SALTCHARS = "ABCDEFGHIJK";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    private void updateLabel() {
        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Start_Date.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        End_Date.setText(sdf.format(myCalendar.getTime()));
    }
    public static long getDaysBetweenDates(String start, String end) {
        String DATE_FORMATs = "dd/mm/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATs, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }


}
