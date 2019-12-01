package com.smrtsols.www.hall.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smrtsols.www.hall.R;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smrtsols.www.hall.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    Button login,Signup;
    EditText Username,Password;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseFirestore=FirebaseFirestore.getInstance();
        login=(Button)findViewById(R.id.btn_signin);
        Signup=(Button)findViewById(R.id.signup);
        Username=(EditText)findViewById(R.id.txt_Email);
        Password=(EditText)findViewById(R.id.txt_Password);

        Password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            ReadHallNo();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadHallNo();
            }
        });
    }

    private void ReadHallNo() {
        Query query = firebaseFirestore.collection("Users");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> titleList = new ArrayList<String>();
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){

                        String Email,Pass,Usernam;
                        String encrypted = "";

                        Email= (String) readData.get("Email");
                        encrypted= (String) readData.get("Password");
                        Usernam= (String) readData.get("FirstName");
                        Usernam+=" "+ (String) readData.get("LastName");
                        String decrypted = "";
                        try {
                            decrypted = AESUtils.decrypt(encrypted);
                            Log.d("TEST", "decrypted:" + decrypted);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(Email.equals(Username.getText().toString()) || decrypted.equals(Password.getText().toString()) )
                        {
                            Intent intent=new Intent(Login.this,Hall.class);

                            intent.putExtra("UserName",Usernam);
                            startActivity(intent);
                        }
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("Reservation Error: ", e.getMessage());
                    }
                });
    }
}
