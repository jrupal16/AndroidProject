package com.listview.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ParkingListAdapter extends BaseAdapter {

    ArrayList<ParkingObject> parkingDataList;
    Context context;
    LayoutInflater inflater;

    public ParkingListAdapter(ArrayList<ParkingObject> parkingData,Context con){
        context             = con;
        parkingDataList     = parkingData;
        inflater            = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return parkingDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return parkingDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.parking_list_item_view,parent,false);

        TextView locationTxt    = (TextView)convertView.findViewById(R.id.location_name);
        TextView addressTxt     = (TextView)convertView.findViewById(R.id.address);
        TextView priceTxt       = (TextView)convertView.findViewById(R.id.price);
        TextView distanceTxt    = (TextView)convertView.findViewById(R.id.distance);

        locationTxt.setText(parkingDataList.get(position).getLocationName());
        addressTxt.setText(parkingDataList.get(position).getAddress());
        priceTxt.setText(parkingDataList.get(position).getPrice());
        distanceTxt.setText(parkingDataList.get(position).getDistance()+" ft");

        return convertView;
    }
}
