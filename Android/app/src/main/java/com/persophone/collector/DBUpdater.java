package com.persophone.collector;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.service.voice.VoiceInteractionSession;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.persophone.app.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Guy on 14/03/2017.
 */

public class DBUpdater /*extends Thread*/ {

    public static final int GET_DATA = 1;
    public static final int SEND_DATA = 2;
    public static final String URL_APP_SERVER = "http://ec2-35-157-26-139.eu-central-1.compute.amazonaws.com/";
    public static final String URL_ADD_POST_APP = "phoneUsage";

    public static final String USER_ID = "555";
    public static final String APP_PARAM = "appsuse";
    public static final String USER_PARAM = "user";
    public static final String BATTERY_PARAM = "battery";
    public static final String IDLE_PARAM = "idle";
    public static final String STOR_PARAM = "stor_used";

    protected PhoneUsageData phoneUsageData;
    protected int mode = 0;
    protected Handler h;

    // Singleton
    private static DBUpdater dbUpdater = new DBUpdater();

    // private for Volley
    private RequestQueue requestQueue;
    private Cache cache;
    private Network network;

    private DBUpdater(){
    }

    public static DBUpdater getInstance(){return dbUpdater;
    }

    public boolean isConnectionSet(){
        return (this.requestQueue != null);
    }

    public void setQueueAndConnection(Context context){
        this.cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        this.network =  new BasicNetwork(new HurlStack());

        this.requestQueue = new RequestQueue(this.cache,this.network);
        this.requestQueue.start();
    }

    public void setHandler(Handler h){
        this.h = h;
    }

    public void setPhoneUsage(PhoneUsageData phUsage){
        this.phoneUsageData = phUsage;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    /*@Override
    public void run() {
        switch(this.mode){

            case GET_DATA:
                this.getData();
                break;

            case SEND_DATA:
                this.saveData();
                break;

            default:
                break;
        }
    }*/

    private void saveData() {
        try {

            // Create URL
            int userid = 10;
            String baseURL = new String("http://ec2-35-157-110-69.eu-central-1.compute.amazonaws.com/phoneUsage");

            // setting URL data
            String parameters = new String("user=" + userid);
            parameters += this.phoneUsageData.getURLData();
            URL persoNode = new URL(baseURL + "?" + parameters);

            // Create connection
            HttpURLConnection myConnection =
                    (HttpURLConnection) persoNode.openConnection();


            myConnection.setRequestMethod("POST");

            if (myConnection.getResponseCode() == 200) {
                Logger.writeToErrorLog("success Save to DB");
            } else {
                Logger.writeToErrorLog("ERROR save toDB");
            }

        } catch (Exception e) {
            Logger.writeToErrorLog("Exception!!! : " + e.getMessage() + " to string: " + e.toString());
        }
    }

    private void getData() {
        try {

            // variables for json
            int jUserId = 0;
            int jBatteryUsage = 0;
            int jIdleTime = 0;
            String jAppUsage = new String("");

            // Create URL
            int userid = 10;
            String baseURL = new String("http://ec2-35-157-110-69.eu-central-1.compute.amazonaws.com/phoneUsage");
            String parameters = new String("user=" + userid);

            URL persoNode = new URL(baseURL + "?" + parameters);


            // Create connection
            HttpURLConnection myConnection =
                    (HttpURLConnection) persoNode.openConnection();


            myConnection.setRequestMethod("GET");

            if (myConnection.getResponseCode() == 200) {
                Logger.writeToErrorLog("Success Activate API");
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                String a = myConnection.getResponseMessage();

                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    switch (key){
                        case "user_id":
                            jUserId = jsonReader.nextInt();
                            break;
                        case "battery_usage":
                            this.phoneUsageData.setBattey(jsonReader.nextInt());
                            break;
                        case "idle_time":
                            this.phoneUsageData.setIdleTime(jsonReader.nextInt());
                            break;
                        case "apps_usage":
                            this.phoneUsageData.setAppsByString(jsonReader.nextString());
                            break;

                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }

                // send message to UI
                Message mes = new Message();
                mes.obj = this.phoneUsageData;
                this.h.sendMessage(mes);

                Logger.writeToErrorLog("app server answer - battery: " + jBatteryUsage + " idle: " + jIdleTime + " apps:" + jAppUsage);
            } else {
                Logger.writeToErrorLog("Fail to use API");
            }

        } catch (Exception e) {
            Logger.writeToErrorLog("Exception!!! : " + e.getMessage() + " to string: " + e.toString());
        }
    }

    public void saveData1(){




        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_APP_SERVER + URL_ADD_POST_APP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("OK")){
                            Logger.writeToDebugLog("Data Written to DB");
                        }
                        else{
                            Logger.writeToDebugLog("FAILED - Write to DB");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.writeToDebugLog("Error on volley");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

            @Override
            public byte[] getBody(){
                JSONObject json = DBUpdater.getInstance().phoneUsageData.getJSONObgect();

                byte[] bytesArr = null;
                try{bytesArr = json.toString().getBytes();}catch (Exception e){}

                return bytesArr;
            }
        };

        // Adding request to request queue
       this.requestQueue.add(postRequest);
    }
}
