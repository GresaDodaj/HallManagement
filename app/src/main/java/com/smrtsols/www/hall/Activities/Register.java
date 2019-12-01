package com.smrtsols.www.hall.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smrtsols.www.hall.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Register extends AppCompatActivity {
    private Button Save;
    private ProgressBar progressBar;
    String Field;
    private TextView UserName,FirstName,LastName,Email,Password;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Save=(Button)findViewById(R.id.btn_signup);
        UserName=(TextView)findViewById(R.id.txt_UserName);
        FirstName=(TextView)findViewById(R.id.txt_FirstName);
        LastName=(TextView)findViewById(R.id.txt_LastName);
        Password=(TextView)findViewById(R.id.txt_Password);
        Email=(TextView)findViewById(R.id.txt_Email);
        firebaseFirestore=FirebaseFirestore.getInstance();
       // progressBar=(ProgressBar)findViewById(R.id.signupProgress);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> Users=new HashMap<>();
                Users.put("FirstName",FirstName.getText().toString().trim());
                Users.put("LastName",LastName.getText().toString().trim());
                Users.put("Email",Email.getText().toString().trim());
                Users.put("Password",Password.getText().toString().trim());


                firebaseFirestore.collection("Users").document(Field=getDocumentName()).set(Users)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.VISIBLE);

                                new Thread(new Runnable() {
                                    public void run() {
                                        doWork();
                                    }
                                }).start();
                                Toast.makeText(Register.this,"Registered Sucessfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Register.this,Login.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);

                            }
                        });
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
    private void doWork() {

        for (int progress=0; progress<40; progress+=10) {

            try {

                Thread.sleep(2000);
                progressBar.setProgress(progress);

            }
            catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
