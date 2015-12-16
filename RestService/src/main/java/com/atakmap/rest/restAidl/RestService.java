package com.atakmap.rest.restAidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atakmap.rest.base.App;
import com.atakmap.rest.dataModel.GeocodeObject;
import com.atakmap.rest.dataModel.RouteObject;
import com.atakmap.rest.dataModel.WikipediaObject;
import com.atakmap.rest.network.GeocodeRequests;
import com.atakmap.rest.network.GsonGetRequest;
import com.atakmap.rest.network.RouteRequests;
import com.atakmap.rest.network.WikipediaRequests;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * @author Sam Davis
 */
public class RestService extends Service {
    private static final String TAG = "RestService";


    private RestServiceImplementation service;




    @Override
    public void onCreate() {
        super.onCreate();
        service = new RestServiceImplementation();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return service;
        }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        this.service = null;
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }





}
