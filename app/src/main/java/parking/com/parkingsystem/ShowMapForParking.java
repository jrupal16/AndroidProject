package parking.com.parkingsystem;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowMapForParking extends FragmentActivity implements OnMapLongClickListener, OnMapClickListener, OnMarkerDragListener, GoogleMap.OnMyLocationButtonClickListener {

    private static GoogleMap map;
    ListView parkingData;
    ProgressDialog progressDialog;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_main);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.e("hello","rupal1");
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.e("hello","rupal");
                    return true;

                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    map.clear();
                    CameraPosition INIT =
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(17.5F)
                                    .bearing(300F) // orientation
                                    .tilt(50F) // viewing angle
                                    .build();

                    // use map to move camera into position
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
                    /*map.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .draggable(true)
                            .title("Location")
                            .snippet("First Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();*/
                    Log.e("location", String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
                    AsyncFetchParkingDetails task = new AsyncFetchParkingDetails(location.getLatitude(), location.getLongitude());
                    task.execute();
                } else {

                }
                return true;
            }
        });

        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(new LatLng(41.8857256, -87.6369590))
                        .zoom(17.5F)
                        .bearing(300F) // orientation
                        .tilt(50F) // viewing angle
                        .build();

        // use map to move camera into position
        map.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

        //create initial marker
        map.addMarker(new MarkerOptions()
                .position(new LatLng(41.8857256, -87.6369590))
                .draggable(true)
                .title("Location")
                .snippet("First Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Log.e("Client", client.toString());
    }


    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();

        Intent i = new Intent(ShowMapForParking.this, ParkingDetails.class);
        i.putExtra("lat", dragLat);
        i.putExtra("lang", dragLong);
        AsyncFetchParkingDetails task = new AsyncFetchParkingDetails(dragLat, dragLong);
        task.execute();
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        map.animateCamera(CameraUpdateFactory.newLatLng(arg0));
    }


    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

        //create new marker when user long clicks
        /*map.addMarker(new MarkerOptions()
                .position(arg0)
                .draggable(true));*/
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShowMapForParking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://parking.com.parkingsystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShowMapForParking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://parking.com.parkingsystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.e("hello","rupal1");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("hello","rupal");
            return true;

        }
        Location location = locationManager.getLastKnownLocation(provider);
        Log.e("location",String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude()));
        return false;
    }

    class AsyncFetchParkingDetails extends AsyncTask<Void,Void,Void> {

        Jsonparser jsonparser;
        String json="";
        String url ;
        double latitude;
        double longitude;
        ArrayList<ParkingObject> parkingDataList;


        public AsyncFetchParkingDetails(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            url = "http://api.parkwhiz.com/search/?lat="+latitude+"&lng="+longitude+"&key=ef0bc8ec7dfc392df40e24947a0fe768";
            parkingDataList = new ArrayList<ParkingObject>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonparser = new Jsonparser(ShowMapForParking.this);
           // progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            json = jsonparser.getJsonFromUrl(url);

            try {
                JSONObject parkingDataObj = new JSONObject(json);
                Log.e("Parking obj", parkingDataObj.toString());

                JSONArray parkingJsonList = parkingDataObj.getJSONArray("parking_listings");
                parkingDataList = new ArrayList<ParkingObject>();
                Log.e("size is" , parkingJsonList.toString());
                for(int i=0;i<parkingJsonList.length();i++){
                    String location = parkingJsonList.getJSONObject(i).getString("location_name");
                    String addr     = parkingJsonList.getJSONObject(i).getString("address");
                    String price    = parkingJsonList.getJSONObject(i).getString("price");
                    String distance = parkingJsonList.getJSONObject(i).getString("distance");
                    double latitude = parkingJsonList.getJSONObject(i).getDouble("lat");
                    double longitude = parkingJsonList.getJSONObject(i).getDouble("lng");

                    ParkingObject parkingObject = new ParkingObject(location,addr,price,distance);
                    parkingObject.setLatitude(latitude);
                    parkingObject.setLongitude(longitude);

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
            //progressDialog.dismiss();
           /* adapter = new ParkingListAdapter(parkingDataList,ShowMapForParking.this);
            parkingData.setAdapter(adapter);*/
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .draggable(true)
                    .title("Location")
                    .snippet("Moved Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
            Log.e("Marker moved", "true" +parkingDataList.size());
            for (ParkingObject parking :parkingDataList ) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(parking.getLatitude(), parking.getLongitude()))
                        .title(parking.getLocationName())
                        .snippet("$"+parking.getPrice()));
            }
        }
    }
}