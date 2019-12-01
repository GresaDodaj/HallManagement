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

public class ReservedHallAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> Name,RoomPrice,RoomArea,RoomStatus,RoomFacilities,RoomImage1,RoomImage2;

    public ReservedHallAdapter(Activity context, ArrayList<String> Name, ArrayList<String> RoomPrice, ArrayList<String> RoomArea, ArrayList<String> RoomStatus, ArrayList<String> RoomFacilities, ArrayList<String> RoomImage1) {
        super(context, R.layout.recycyclerview_items_reserved, Name);
        this.context = context;
        this.Name = Name;
        this.RoomPrice = RoomPrice;
        this.RoomStatus = RoomStatus;
        this.RoomArea = RoomArea;
        this.RoomFacilities = RoomFacilities;
        this.RoomImage1 = RoomImage1;



    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.recycyclerview_items_reserved, null, true);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.textView3);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.textView4);
        TextView txtTitle3 = (TextView) rowView.findViewById(R.id.textView5);
        TextView txtTitle4 = (TextView) rowView.findViewById(R.id.textView6);
        TextView txtTitle5 = (TextView) rowView.findViewById(R.id.textView7);

        txtTitle1.setText("Hall No: "+Name.get(position));
        txtTitle2.setText("Start Date: "+RoomArea.get(position));
        txtTitle3.setText("End Date: "+RoomPrice.get(position));
        txtTitle4.setText("Price Per Day: "+RoomFacilities.get(position));
        txtTitle5.setText("Total Price: "+RoomStatus.get(position));


        return rowView;
    }
}