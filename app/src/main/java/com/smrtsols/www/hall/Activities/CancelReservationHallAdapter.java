package com.smrtsols.www.hall.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smrtsols.www.hall.R;

import java.util.ArrayList;

public class CancelReservationHallAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> Name,RoomPrice,RoomArea,RoomStatus,RoomFacilities,RoomImage1,RoomImage2;

    public CancelReservationHallAdapter(Activity context, ArrayList<String> Name, ArrayList<String> RoomPrice, ArrayList<String> RoomArea, ArrayList<String> RoomStatus, ArrayList<String> RoomFacilities, ArrayList<String> RoomImage1) {
        super(context, R.layout.recycyclerview_items_cancel_reservation, Name);
        this.context = context;
        this.Name = Name;
        this.RoomPrice = RoomPrice;
        this.RoomStatus = RoomStatus;
        this.RoomArea = RoomArea;
        this.RoomFacilities = RoomFacilities;
        this.RoomImage1 = RoomImage1;



    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recycyclerview_items_cancel_reservation, null);
            holder = new ViewHolder();
           holder.txtTitle1 = (TextView) convertView.findViewById(R.id.textView3);
            holder.txtTitle2 = (TextView) convertView.findViewById(R.id.textView4);
             holder.txtTitle3 = (TextView) convertView.findViewById(R.id.textView5);
             holder.txtTitle4 = (TextView) convertView.findViewById(R.id.textView6);
            holder.txtTitle5 = (TextView) convertView.findViewById(R.id.textView7);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtTitle1.setText("Hall No: "+Name.get(position));
        holder.txtTitle2.setText("Start Date: "+RoomArea.get(position));
        holder.txtTitle3.setText("End Date: "+RoomPrice.get(position));
        holder.txtTitle4.setText("Price Per Day: "+RoomFacilities.get(position));
        holder.txtTitle5.setText("Total Price: "+RoomStatus.get(position));
holder.imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      CancelReservation.Update(Name.get(position));
        Intent intent=new Intent(context,Hall.class);
        context.finishAffinity();
        context.startActivity(intent);
    }
});
        return convertView;
    }

    private class ViewHolder {
        private TextView txtTitle1,txtTitle2,txtTitle3,txtTitle4,txtTitle5;
        private ImageView imageView;
    }
}