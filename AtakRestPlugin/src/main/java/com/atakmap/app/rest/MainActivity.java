package com.atakmap.app.rest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.atakmap.app.rest.restAidl.IRestService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AIDLDemo";
    IRestService service;
    RestServiceConnection connection;

    class RestServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IRestService.Stub.asInterface((IBinder) boundService);
            Log.d(MainActivity.TAG, "onServiceConnected() connected");
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG)
                    .show();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(MainActivity.TAG, "onServiceDisconnected() disconnected");
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Binds this activity to the service.
     */
    private void initService() {
        connection = new RestServiceConnection();
        Intent i = new Intent();
        i.setClassName("com.atakmap.app.rest", "com.atakmap.app.rest.restAidl.RestService");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound with " + ret);
    }

    /**
     * Unbinds this activity from the service.
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService() unbound.");
    }

    /**
     * This class represents the actual service connection. It casts the bound
     * stub implementation of the service to the AIDL interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
        // Setup the UI
        Button buttonCalc = (Button) findViewById(R.id.buttonCalc);

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            TextView result = (TextView) findViewById(R.id.result);
            EditText queryURL = (EditText) findViewById(R.id.requestURL);
            EditText geocode = (EditText) findViewById(R.id.geocode);
            EditText queryParams = (EditText) findViewById(R.id.queryParams);
            EditText intType = (EditText) findViewById(R.id.intType);


            public void onClick(View v) {
                int type;
                String geo, url, params;
                String res = "search something";
                type = Integer.parseInt(intType.getText().toString());
                geo = geocode.getText().toString();
                params = queryParams.getText().toString();
                url = queryURL.getText().toString();

                try {
                    res = service.restRequest(type, url,params,null, null,geo, null);
                } catch (RemoteException e) {
                    Log.d(MainActivity.TAG, "onClick failed with: " + e);
                    e.printStackTrace();
                }
                result.setText(res);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the activity is about to be destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initService();
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseService();
    }


}
