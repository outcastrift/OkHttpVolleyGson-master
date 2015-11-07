package com.sottocorp.okhttpvolleygson.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sottocorp.okhttpvolleygson.R;
import com.sottocorp.okhttpvolleygson.base.App;
import com.sottocorp.okhttpvolleygson.dataModel.RouteObject;
import com.sottocorp.okhttpvolleygson.network.GsonGetRequest;
import com.sottocorp.okhttpvolleygson.network.RouteRequests;

import java.util.HashMap;

/**
 * Demonstrates how to make a JSON Object request
 */
public class RouteObjectActivity extends AppCompatActivity
{
    private static final String sTag = "tagOne";

    private TextView mTitle, mBody;
    private ProgressBar mProgressBar;
    private LinearLayout mContent, mErrorView;
    private Button mButton;
    private EditText mOrigin;
    private EditText mDest;
    private StringBuilder  directionList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_object_request);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mButton=(Button)findViewById(R.id.search_button);
        mOrigin =(EditText)findViewById(R.id.search_box);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateAndPerformSearch();
            }
        };
        mDest = (EditText)findViewById(R.id.dest_search_box);
        mButton.setOnClickListener(clickListener);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTitle = (TextView) findViewById(R.id.my_title);
        mBody = (TextView) findViewById(R.id.my_body);
        mBody.setMovementMethod(new ScrollingMovementMethod());
        mErrorView = (LinearLayout) findViewById(R.id.error_view);
        mContent = (LinearLayout) findViewById(R.id.content);
        mProgressBar.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);



    }
    void validateAndPerformSearch(){
        String orgin = null;
        String dest = null;
        dest=mDest.getText().toString();
        orgin= mOrigin.getText().toString();
        if(!orgin.equalsIgnoreCase("")||orgin==null){
            if(!dest.equalsIgnoreCase("")||dest==null){
                mProgressBar.setVisibility(View.VISIBLE);
                mContent.setVisibility(View.GONE);

                final GsonGetRequest<RouteObject> gsonGetRequest =
                        RouteRequests.getRouteObject
                                (
                                        new Response.Listener<RouteObject>() {
                                            @Override
                                            public void onResponse(RouteObject routeObject) {
                                                // Deal with the RouteObject here
                                                mProgressBar.setVisibility(View.GONE);
                                                mContent.setVisibility(View.VISIBLE);
                                                setData(routeObject);
                                            }
                                        }
                                        ,
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Deal with the error here
                                                mProgressBar.setVisibility(View.GONE);
                                                mErrorView.setVisibility(View.VISIBLE);
                                            }
                                        },
                                        orgin,
                                        dest
                                );


                App.addRequest(gsonGetRequest, sTag);
            }
            }
            //do nothing
        else{
           Toast.makeText(getApplicationContext(), "Please enter both a destination and a origin", Toast.LENGTH_LONG).show();

    }
    }
    @Override
    protected void onStop()
    {
        App.cancelAllRequests(sTag);

        super.onStop();
    }

    /**
     * Sets the data in the UI
     *
     * @param routeObject is the object to get the data from
     */
    private void setData(@NonNull final RouteObject routeObject)
    {
        directionList = new StringBuilder();

        mTitle.setText(routeObject.getTitle());
        directionList.append(routeObject.getBody()+"\n");
        directionList.append("\n \n Directions are as follows:"+"\n");
        HashMap<Integer,Step> map =constructRouteFromJson(routeObject.getDirections());
        for (int i = 0; i < map.size(); i++) {
            directionList.append("\n Step "+String.valueOf(i+1)+"\n");
            directionList
                    .append(" Start:" + map.get(i).startGeopoint)
                    .append("\n EndPoint:" + map.get(i).endGeopoint)
                    .append("\n Distance:"+map.get(i).distance)
                    .append("\n Duration:"+map.get(i).duration)
                    .append("\n Duration:"+map.get(i).duration)
                    .append("\n Directions:" + map.get(i).instructions);

        }

        mBody.setText(directionList.toString());
    }
    /**
     * Sets the turns into a Hashmap we can manipulate
     *
     * @param routeObject is the object to get the data from
     */
    private HashMap<Integer,Step> constructRouteFromJson( JsonArray routeObject)
    {
        HashMap<Integer,Step> map = new HashMap<Integer, Step>();
        int counter = 0;
        for (int i = 0; i < routeObject.size(); i++) {
            JsonObject keywordsObject = routeObject.get(i).getAsJsonObject();
            //Get Values for Step
            String startGeopoint = keywordsObject.getAsJsonObject("start_location").get("lat").getAsString()+keywordsObject.getAsJsonObject("start_location").get("lat").getAsString();
            String endGepoint = keywordsObject.getAsJsonObject("end_location").get("lat").getAsString()+keywordsObject.getAsJsonObject("end_location").get("lat").getAsString();
            String instructions = keywordsObject.get("html_instructions").getAsString();
            instructions = String.valueOf(android.text.Html.fromHtml(instructions));
            String duration = keywordsObject.getAsJsonObject("duration").get("text").getAsString();
            String distance = keywordsObject.getAsJsonObject("distance").get("text").getAsString();
            //Create the step object
            Step step = new Step(startGeopoint, endGepoint, instructions, duration, distance);
            //populate into HashMap
            map.put(counter, step);
            counter = counter + 1;

        }
        return map;
    }
    public static class Step {
        String startGeopoint=null;
        String endGeopoint=null;
        String instructions = null;
        String duration =null;
        String distance=null;

        public Step(String startAddress, String endAddress, String instructions, String duration, String distance) {
            this.startGeopoint=startAddress;
            this.duration=duration;
            this.instructions=instructions;
            this.endGeopoint=endAddress;
            this.distance=distance;

        }
    }


}
