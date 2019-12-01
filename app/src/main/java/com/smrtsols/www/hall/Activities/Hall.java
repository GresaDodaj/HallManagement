package com.smrtsols.www.hall.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.smrtsols.www.hall.R;

public class Hall extends AppCompatActivity {

    CardView add,view,reserved,vacant;String IntentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        add=(CardView)findViewById(R.id.card_Add);
        view=(CardView)findViewById(R.id.card_View);
        reserved=(CardView)findViewById(R.id.card_Reserved);
        vacant=(CardView)findViewById(R.id.card_Vacant);

//        Toast.makeText(getApplicationContext(),getIntent().getExtras().getString("UserName"),Toast.LENGTH_LONG).show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Hall.this,Add_NewHall.class);
                intent.putExtra("UserName",getIntent().getExtras().getString("UserName"));
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Hall.this,Reservation.class);
                intent.putExtra("UserName",getIntent().getExtras().getString("UserName"));
                startActivity(intent);
            }
        });

        reserved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Hall.this,ReservedHall.class);
                intent.putExtra("UserName",getIntent().getExtras().getString("UserName"));
                startActivity(intent);
            }
        });

        vacant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Hall.this,ViewHall.class);
                intent.putExtra("UserName",getIntent().getExtras().getString("UserName"));
                startActivity(intent);
            }
        });

    }
}
