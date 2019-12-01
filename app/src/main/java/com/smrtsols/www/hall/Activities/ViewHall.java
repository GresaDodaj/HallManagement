package com.smrtsols.www.hall.Activities;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.smrtsols.www.hall.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewHall extends AppCompatActivity {

    String TAG="View Hall";
    ListView recyclerView;
    FirebaseFirestore firebaseFirestore;
    String HallName,Price,Area,Status,Facilities,Image1,Image2;
    ArrayList<String> Name,RoomPrice,RoomArea,RoomStatus,RoomFacilities,RoomImage1,RoomImage2,Added_By;
    private List<Type> mArrayList;
    CollectionReference collectionReference ;
    private  DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hall);
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Hall");
//        documentReference=firebaseFirestore.document("Hall/Add");
        ReadSingleContact();
        Name=new ArrayList<>();
        RoomArea=new ArrayList<>();
        RoomPrice=new ArrayList<>();
        RoomStatus=new ArrayList<>();
        RoomImage1=new ArrayList<>();
        RoomFacilities=new ArrayList<>();
        RoomImage2=new ArrayList<>();
        Added_By=new ArrayList<>();


        recyclerView=(ListView)findViewById(R.id.recyclerView);

    }


    private void ReadSingleContact() {


        collectionReference.whereEqualTo("Added_By",getIntent().getExtras().getString("UserName")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data="";
                for(DocumentSnapshot documentSnapshot2:documentSnapshots)
                {
                    HallClass hallClass=documentSnapshot2.toObject(HallClass.class);
                    data=hallClass.getHallName();

                    Name.add(data);

                    data=hallClass.getArea();
                    RoomArea.add(data);

                    data=hallClass.getPrice();

                    RoomPrice.add(data);
                    data=hallClass.getStatus();

                    RoomStatus.add(data);
                    data=hallClass.getFacilities();

                    RoomFacilities.add(data);
                    data=hallClass.getImage1();
                    RoomImage1.add(data);

                    data=hallClass.getImage2();
                    RoomImage2.add(data);

                    data=hallClass.getAdded_By();
                    Added_By.add(data);
                }

                ViewHallAdapter viewHallAdapter=new ViewHallAdapter(ViewHall.this,Name,RoomPrice,RoomArea,RoomStatus,RoomFacilities,RoomImage1,RoomImage2,Added_By);
                recyclerView.setAdapter(viewHallAdapter);
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
            }
        });

        {
        }
    }



}
