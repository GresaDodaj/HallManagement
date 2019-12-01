package com.smrtsols.www.hall.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smrtsols.www.hall.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ReservedHall extends AppCompatActivity {
    String TAG = "View Hall";
    ListView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String HallName, Price, Area, Status, Facilities, Image1, Image2;
    ArrayList<String> Name, RoomPrice, RoomArea, RoomStatus, RoomFacilities, RoomImage1, RoomImage2;
    private List<Type> mArrayList;
    CollectionReference collectionReference;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_hall);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Reservation");
//        documentReference=firebaseFirestore.document("Hall/Add");
        ReadSingleContact();
        Name = new ArrayList<>();
        RoomArea = new ArrayList<>();
        RoomPrice = new ArrayList<>();
        RoomStatus = new ArrayList<>();
        RoomImage1 = new ArrayList<>();
        RoomFacilities = new ArrayList<>();
        recyclerView = (ListView) findViewById(R.id.recyclerView);

    }


    private void ReadSingleContact() {


        collectionReference.whereEqualTo("Reserved_By",getIntent().getExtras().getString("UserName")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data = "";
                for (DocumentSnapshot documentSnapshot2 : documentSnapshots) {
                    ReservationClass hallClass = documentSnapshot2.toObject(ReservationClass.class);
                    data = hallClass.getHall_No();
                    Name.add(data);
                    data = hallClass.getStart_Date();
                    RoomArea.add(data);

                    data = hallClass.getEnd_Date();
                    RoomPrice.add(data);

                    data = hallClass.getTotal_Price();

                    RoomStatus.add(data);
                    data = hallClass.getReserved_By();

                    RoomImage1.add(data);

                }

                ReservedHallAdapter viewHallAdapter = new ReservedHallAdapter(ReservedHall.this, Name, RoomArea, RoomPrice, RoomStatus,RoomFacilities, RoomImage1);
                recyclerView.setAdapter(viewHallAdapter);
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
            }
        });
    }
}