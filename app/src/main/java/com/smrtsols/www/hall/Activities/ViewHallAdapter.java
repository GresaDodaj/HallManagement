package com.smrtsols.www.hall.Activities;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smrtsols.www.hall.R;

import java.util.ArrayList;

public class ViewHallAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> Name, RoomPrice, RoomArea, RoomStatus, RoomFacilities, RoomImage1, RoomImage2, Added_By;

    public ViewHallAdapter(Activity context, ArrayList<String> Name, ArrayList<String> RoomPrice, ArrayList<String> RoomArea, ArrayList<String> RoomStatus, ArrayList<String> RoomFacilities, ArrayList<String> RoomImage1, ArrayList<String> RoomImage2, ArrayList<String> added_By) {
        super(context, R.layout.recycyclerview_items_viewhall, Name);
        this.context = context;
        this.Name = Name;
        this.RoomPrice = RoomPrice;
        this.RoomStatus = RoomStatus;
        this.RoomArea = RoomArea;
        this.RoomFacilities = RoomFacilities;
        this.RoomImage1 = RoomImage1;
        this.RoomImage2 = RoomImage2;
        this.Added_By = added_By;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.recycyclerview_items_viewhall, null, true);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.txt1);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.textView3);
        TextView txtTitle3 = (TextView) rowView.findViewById(R.id.textView4);
        TextView txtTitle4 = (TextView) rowView.findViewById(R.id.textView5);
        TextView txtTitle5 = (TextView) rowView.findViewById(R.id.textView6);
        TextView txtTitle6 = (TextView) rowView.findViewById(R.id.textView7);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtTitle1.setText("Hall Name: "+Name.get(position));
        txtTitle2.setText("Area: "+RoomArea.get(position));
        txtTitle3.setText("Facilities: "+RoomFacilities.get(position));
        txtTitle4.setText("Price: "+RoomPrice.get(position));
        txtTitle5.setText("Status: "+RoomStatus.get(position));
        txtTitle6.setText("Added By: "+Added_By.get(position));


        Glide.with(context)
                .load(Uri.parse(RoomImage1.get(position)))
                .into(imageView);
        return rowView;
    }
}