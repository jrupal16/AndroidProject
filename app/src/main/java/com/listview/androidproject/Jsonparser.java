package com.listview.androidproject;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;


public class Jsonparser {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    Context context;


    public Jsonparser(Context con){
        context             = con;
    }

    public String getJsonResponse(String url, int method) {
        return this.getJsonResponse(url, method, "");
    }

    public String getJsonFromUrl(String urlString){
        InputStream stream = null;
        URL url = null;
        StringBuffer output = new StringBuffer("");
        try {
            url = new URL(urlString);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public String getJsonResponse(String urlStr, int method, String s){
        String json = "";
        URL url;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        InputStream input;
        try{
            url = new URL (urlStr);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setDoInput(true);
            if(s!="" && method==POST){
                urlConn.setDoOutput(true);
            }
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setConnectTimeout(1000);
            urlConn.connect();
            if(s!="" && method==POST){
                printout = new DataOutputStream(urlConn.getOutputStream());
                printout.write(s.getBytes());
                printout.flush();
                printout.close();
            }

            Log.e("url code","Code"+urlConn.getResponseCode());
            if(urlConn.getResponseCode() == 200){
                Log.e("url id", urlStr);

                Log.e("url id", "Connection found");
                try {
                    input = urlConn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    input.close();
                    json = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
            }else{

                return "";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public String getJsonResponse(String urlStr, int method, JSONObject jsonObj){
        String json = "";
        URL url;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        InputStream input;
        try{
            url = new URL (urlStr);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setDoInput(true);
            if(method==POST){
                urlConn.setDoOutput(true);
            }
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setConnectTimeout(4000);
            urlConn.connect();
            if(method==POST){
                printout = new DataOutputStream(urlConn.getOutputStream());
                //printout.write(URLEncoder.encode(jsonObj.toString(), "UTF-8").getBytes());
                printout.write(jsonObj.toString().getBytes());
                printout.flush();
                printout.close();
            }

            //Log.e("url code","Code"+urlConn.getResponseCode());
            try {
                input = urlConn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                input.close();
                json = sb.toString();
                //Log.e("json data","json"+json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*if(urlConn.getResponseCode() == 200){
                Log.e("url id", urlStr);

                Log.e("url id", "Connection found");

            }else{

                return "";
            }*/

        }catch (SocketTimeoutException se){
            se.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();

            return String.valueOf(false);
        }catch (Exception e){
            e.printStackTrace();
        }

        return json;
    }
}
