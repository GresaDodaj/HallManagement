package com.smrtsols.www.hall.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.smrtsols.www.hall.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelReservation extends AppCompatActivity {

    String TAG="Reserved Hall";
    ListView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String HallName,Price,Area,Status,Facilities,Image1,Image2;
    ArrayList<String> StartDate,EndDate,HallNo,PricePer,PriceWeek,TotalPrice;
    private List<Type> mArrayList;
    CollectionReference collectionReference ;
    private DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacant_hall);
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Reservation");
//        documentReference=firebaseFirestore.document("Hall/Add");
        StartDate=new ArrayList<>();
        EndDate=new ArrayList<>();
        HallNo=new ArrayList<>();
        PricePer=new ArrayList<>();
        PriceWeek=new ArrayList<>();
        TotalPrice=new ArrayList<>();


        ReadSingleContact();



    }


    private void ReadSingleContact() {


        collectionReference.whereEqualTo("Status","Reserved").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data="";
                for(DocumentSnapshot documentSnapshot2:documentSnapshots)
                {
                    ReservationClass reservationClass=documentSnapshot2.toObject(ReservationClass.class);
                    String Names=reservationClass.getHall_No();
                    data=Names;
                    HallNo.add(data);
                    data=reservationClass.getStart_Date();
                    StartDate.add(data);
                    data=reservationClass.getEnd_Date();
                    EndDate.add(data);
                    data=reservationClass.getPrice_PerDay();
                    PricePer.add(data);
                    data=reservationClass.getPrice_PerWeek();
                    PriceWeek.add(data);
                    data=reservationClass.getTotal_Price();
                    TotalPrice.add(data);


                    //  Toast.makeText(getApplicationContext(),data=Names+""+hallClass.getFacilities()+hallClass.getArea()+hallClass.getPrice(),Toast.LENGTH_LONG).show();

                }
                recyclerView=(ListView)findViewById(R.id.recyclerView);

                CancelReservationHallAdapter cancelReservation=new CancelReservationHallAdapter(CancelReservation.this,HallNo,StartDate,EndDate,PricePer,PriceWeek,TotalPrice);
                recyclerView.setAdapter(cancelReservation);
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
            }
        });


    }
     public static void Update(String id) {

        FirebaseFirestore firebaseFirestore1=FirebaseFirestore.getInstance();
        final CollectionReference complaintsRef = firebaseFirestore1.collection("Reservation");
        complaintsRef.whereEqualTo("Hall_No", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("Status", "Free");
                        complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                    }


                }
            }
        });
    }
}