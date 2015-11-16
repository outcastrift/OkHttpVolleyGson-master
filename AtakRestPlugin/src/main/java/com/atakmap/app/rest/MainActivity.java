package com.atakmap.app.rest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.atakmap.app.rest.dataModel.GeocodeObject;
import com.atakmap.app.rest.restAidl.IRestCallback;
import com.atakmap.app.rest.restAidl.IRestService;

public class  MainActivity extends AppCompatActivity {
    private static final String TAG = "AIDLDemo";
    IRestService service;
    RestServiceConnection connection;
    private Handler mHandler = new Handler();
    TextView resultBox;
    Button routeButton;
    Button wikiButton;
    Button geocodeButton;

    class RestServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IRestService.Stub.asInterface(boundService);
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
        routeButton = (Button) findViewById(R.id.buttonRoute);
        wikiButton = (Button) findViewById(R.id.buttonWiki);
        geocodeButton = (Button) findViewById(R.id.buttonGeocode);

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            TextView resultBox=(TextView) findViewById(R.id.result);
            EditText queryURL = (EditText) findViewById(R.id.requestURL);
            EditText geoSearch = (EditText) findViewById(R.id.geocode);
            EditText queryParams = (EditText) findViewById(R.id.queryParams);
            EditText intType = (EditText) findViewById(R.id.intType);
            EditText wikiSearch = (EditText) findViewById(R.id.requestURL);
            EditText routeStart = (EditText) findViewById(R.id.requestURL);
            EditText routeEnd = (EditText) findViewById(R.id.requestURL);


            GeocodeObject finalGeo = new GeocodeObject();
            public void onClick(View v) {
                if (!queryURL.getText().toString().equalsIgnoreCase("")) {
                setURL(queryURL.getText().toString());
                }
                if (!queryParams.getText().toString().equalsIgnoreCase("")) {
                    setQueryParams(queryParams.getText().toString());
                }
                if (!geoSearch.getText().toString().equalsIgnoreCase("")) {
                    setGeoSearch(geoSearch.getText().toString());
                }
                if (!wikiSearch.getText().toString().equalsIgnoreCase("")) {
                    setWikiSearch(wikiSearch.getText().toString());
                } if (!routeStart.getText().toString().equalsIgnoreCase("")) {
                    setGeoSearch(routeStart.getText().toString());
                } if (!routeEnd.getText().toString().equalsIgnoreCase("")) {
                    setGeoSearch(routeEnd.getText().toString());
                }
               /* if(Integer.parseInt(intType.getText().toString())>0){
                    setRequestType(Integer.parseInt(intType.getText().toString()));
                }*/
                //type = Integer.parseInt(intType.getText().toString());
               //queryBox.getText().toString();
                //params = queryParams.getText().toString();
               // url = queryURL.getText().toString();
               // type =1;

                performQuery(getRequestType(), getURL(), getQueryParams(), getGeoSearch(), getRouteStart(), getRouteEnd(), getWikiSearch());
                setGeoSearch("");
                setWikiSearch("");
                setQueryParams("");
                setRequestType(0);
                setRouteEnd("");
                setRouteStart("");
                setURL("");

               }
        });
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeURL="http://maps.googleapis.com/maps/api/directions";

                String routeStart="/json?origin=sherrill ny";
                String routeEnd="&destination=rome ny";
                setURL(routeURL);
                setRequestType(2);
                setRouteStart(routeStart);
                setRouteEnd(routeEnd);

            }
        });
        geocodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUrl="http://maps.google.com/maps/api/geocode/json?";
                String geoParams="address=";
                String geoQuery="tn";
                setGeoSearch(geoQuery);
                setRequestType(1);
                setQueryParams(geoParams);
                setURL(geoUrl);
            }
        });
        wikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wikiURL="http://en.wikipedia.org/w/api.php?";
                String wikiParams="format=json&action=query&prop=extracts&exintro=&formatversion=2&explaintext=&titles=";
                String wikiQuery="War";
                setRequestType(3);
                setWikiSearch(wikiQuery);
                setQueryParams(wikiParams);
                setURL(wikiURL);
            }
        });
    }


    void performQuery(int type, String url, String params, String geo, String rB, String rE, String wS){
        try {
            service.restRequest(type,url, params, geo,rB,rE,wS, new IRestCallback.Stub(){

                @Override
                public void returnResults(final String result) throws RemoteException {
                    TextView resultBox = (TextView)findViewById(R.id.result);
                    if (result != null){
                        resultBox.setText(result.toString());}
                    else{
                        Toast.makeText(getApplicationContext(), "No results were found for the query", Toast.LENGTH_LONG).show();
                    }
                    mHandler.post(new Runnable() {
                                      @Override
                                      public void run() {

                                      }
                                  }

                    );



                }
            });
        } catch (RemoteException e) {
            Log.d(MainActivity.TAG, "onClick failed with: " + e);
        }
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

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getRouteEnd() {
        return routeEnd;
    }

    public void setRouteEnd(String routeEnd) {
        this.routeEnd = routeEnd;
    }

    public String getWikiSearch() {
        return wikiSearch;
    }

    public void setWikiSearch(String wikiSearch) {
        this.wikiSearch = wikiSearch;
    }

    public String getGeoSearch() {
        return geoSearch;
    }

    public void setGeoSearch(String geoSearch) {
        this.geoSearch = geoSearch;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    private int requestType;
    private String URL="";
    private String routeStart="";
    private String routeEnd="";
    private String wikiSearch="";
    private String queryParams="";
    private String geoSearch="";


}
