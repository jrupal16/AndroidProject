package com.listview.androidproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParkingDetails extends Activity {

    ListView parkingData;
    ProgressDialog progressDialog;
    double lat,lang;
    ArrayList<ParkingObject> parkingDataList;
    ParkingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkinglist);

        parkingData     = (ListView)findViewById(R.id.parking_list);
        progressDialog  = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Data Fetching");

        parkingDataList = new ArrayList<>();

        lat             = getIntent().getDoubleExtra("lat",0);
        lang            = getIntent().getDoubleExtra("lang",0);

        new AsyncFetchParkingDetails().execute();

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    class AsyncFetchParkingDetails extends AsyncTask<Void,Void,Void> {

        Jsonparser jsonparser;
        String json="";
        String url = "http://api.parkwhiz.com/search/?lat="+lat+"&lng="+lang+"&key=ef0bc8ec7dfc392df40e24947a0fe768";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonparser = new Jsonparser(ParkingDetails.this);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            json = jsonparser.getJsonResponse(url,Jsonparser.GET);

            try {
                JSONObject parkingDataObj = new JSONObject(json);

                JSONArray parkingJsonList = parkingDataObj.getJSONArray("parking_listings");

                for(int i=0;i<parkingJsonList.length();i++){
                    String location = parkingJsonList.getJSONObject(i).getString("location_name");
                    String addr     = parkingJsonList.getJSONObject(i).getString("available_spots");
                    String price    = parkingJsonList.getJSONObject(i).getString("price");
                    String distance = parkingJsonList.getJSONObject(i).getString("distance");
                    ParkingObject parkingObject = new ParkingObject(location,addr,price,distance);

                    parkingDataList.add(parkingObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            adapter = new ParkingListAdapter(parkingDataList,ParkingDetails.this);
            parkingData.setAdapter(adapter);
        }
    }
}
