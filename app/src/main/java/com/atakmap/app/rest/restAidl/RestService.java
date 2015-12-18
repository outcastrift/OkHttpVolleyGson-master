package com.atakmap.app.rest.restAidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;


/**
 * @author Sam Davis
 */
public class RestService extends Service {
    private static final String TAG = "RestService";


    private RestResponseImpl service;




    @Override
    public void onCreate() {
        super.onCreate();
        service = new RestResponseImpl();
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
