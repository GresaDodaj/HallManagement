package com.smrtsols.www.hall.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smrtsols.www.hall.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Add_NewHall extends AppCompatActivity {
    ImageView imgView1,imgView2;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath,filepath2;
    Button Upload,save;
    ProgressBar pd;
    int upload,id=0;
    String Field;
    String DownloadUrl1,DownloadUrl2;
    FirebaseFirestore firebaseFirestore;
    private EditText Name,Area,Price;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://hall-reservation-management.appspot.com/Hall_Management_Images");    //change the url according to your firebase app
    private Spinner Type;
    private CheckBox ch1,ch2,ch3,ch4,ch5,ch6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_hall);
        firebaseFirestore=FirebaseFirestore.getInstance();
        Name=(EditText)findViewById(R.id.txtName);
        Area=(EditText)findViewById(R.id.txt_Area);

        Price =(EditText)findViewById(R.id.txt_Price);

        ch1=(CheckBox)findViewById(R.id.Airconditioner);
        ch2=(CheckBox)findViewById(R.id.alarm);
        ch3=(CheckBox)findViewById(R.id.wifi);
        ch4=(CheckBox)findViewById(R.id.equipment);
        ch5=(CheckBox)findViewById(R.id.storage);
        ch6=(CheckBox)findViewById(R.id.cafeteria);

        imgView1 = (ImageView)findViewById(R.id.image1);
        imgView2 = (ImageView)findViewById(R.id.image2);
        Type=(Spinner)findViewById(R.id.spinner_Type);
        save=(Button)findViewById(R.id.btn_Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Facilities = null;
                if(ch1.isChecked())
                {
                    Facilities=", "+ch1.getText().toString();
                }
                if(ch2.isChecked())
                {
                    Facilities+=", "+ch2.getText().toString();
                }
                if(ch3.isChecked())
                {
                    Facilities+=", "+ch3.getText().toString();
                }
                if(ch4.isChecked())
                {
                    Facilities+=", "+ch4.getText().toString();
                }
                if(ch5.isChecked())
                {
                    Facilities+=", "+ch5.getText().toString();
                }
                if(ch6.isChecked())
                {
                    Facilities+=", "+ch6.getText().toString();
                }
                Map<String,Object> Hall=new HashMap<>();
                Hall.put("Added_By",getIntent().getExtras().getString("UserName"));
                Hall.put("HallName",Name.getText().toString().trim());
                Hall.put("Area",Area.getText().toString().trim());
                Hall.put("Type",Type.getSelectedItem().toString());
                Hall.put("Facilities",Facilities.toString().trim());
                Hall.put("Price",Price.getText().toString().trim());
                Hall.put("ID",id);
                Hall.put("Status","Free");
                Hall.put("Image1"," ");
                Hall.put("Image2"," ");

                firebaseFirestore.collection("Hall").document(Field=getDocumentName().toString()).set(Hall)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Update(DownloadUrl1,1);
                                Update(DownloadUrl2,2);
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "New Hall Added Sucessfully", Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(Add_NewHall.this, com.smrtsols.www.hall.Activities.Hall.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Something Wrong!", Snackbar.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        List<String> list = new ArrayList<String>();
        list.add("Large");
        list.add("Medium");
        list.add("Small");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Type.setAdapter(dataAdapter);
        pd=(ProgressBar) findViewById(R.id.progressbar);
        Upload=(Button)findViewById(R.id.btn_Upload);
        pd.setVisibility(View.INVISIBLE);

        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload=1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload=2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=intrandrom();
                final String FileName;
                if(filePath != null) {

                    pd.setVisibility(View.VISIBLE);
                    String Image_Name=random();
                    FileName=Image_Name;
                    final StorageReference childRef = storageRef.child(Image_Name+".jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);
                    // UploadTask uploadTask2 = childRef.putFile(filepath2);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            DownloadUrl1 = taskSnapshot.getDownloadUrl().toString();
                            //Update(DownloadUrl1,1);
                            Log.e("Download: ",DownloadUrl1);
                            pd.setVisibility(View.INVISIBLE);
                            Toast.makeText(Add_NewHall.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.setVisibility(View.INVISIBLE);
                            Toast.makeText(Add_NewHall.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else {
                    Toast.makeText(Add_NewHall.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
                if(filepath2 != null) {
                    pd.setVisibility(View.VISIBLE);
                    String Image_Name=random();
                    StorageReference childRef = storageRef.child(Image_Name+".jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filepath2);
                    //UploadTask uploadTask2 = childRef.putFile(filepath2);



                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            DownloadUrl2 = taskSnapshot.getDownloadUrl().toString();
                            pd.setVisibility(View.INVISIBLE);
                            Toast.makeText(Add_NewHall.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.setVisibility(View.INVISIBLE);
                            Toast.makeText(Add_NewHall.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else {
                    Toast.makeText(Add_NewHall.this, "Select Second", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            filepath2 = data.getData();
            if(upload==1) {
                try {
                    //getting image from gallery
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    //Setting image to ImageView
                    imgView1.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(upload==2) {
                try {
                    //getting image from gallery
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath2);

                    //Setting image to ImageView
                    imgView2.setImageBitmap(bitmap);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void Update(final String Value,Integer uploadva) {
        if(uploadva==1) {
            final CollectionReference complaintsRef = firebaseFirestore.collection("Hall");
            complaintsRef.whereEqualTo("ID",id ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Map<Object, String> map = new HashMap<>();
                            map.put("Image1", Value);
                            complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                        }
                    }
                }
            });
        }
        if(uploadva==2) {
            final CollectionReference complaintsRef = firebaseFirestore.collection("Hall");
            complaintsRef.whereEqualTo("ID", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Map<Object, String> map = new HashMap<>();
                            map.put("Image2", Value);
                            complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                        }
                    }
                }
            });
        }
    }


    private  int intrandrom()
    {
        Random r = new Random();
        int i1 = r.nextInt(45 - 28) + 28;
        return  i1;
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
}
