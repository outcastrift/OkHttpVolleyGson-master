package com.atakmap.app.rest.restAidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atakmap.app.rest.base.App;
import com.atakmap.app.rest.dataModel.GeocodeObject;
import com.atakmap.app.rest.dataModel.RouteObject;
import com.atakmap.app.rest.dataModel.WikipediaObject;
import com.atakmap.app.rest.network.GeocodeRequests;
import com.atakmap.app.rest.network.GsonGetRequest;
import com.atakmap.app.rest.network.RouteRequests;
import com.atakmap.app.rest.network.WikipediaRequests;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * @author Sam Davis
 */
public class RestService extends Service {
    private static final String TAG = "RestService";



    private static String geocodeResponse = null;
    String routeResponse;
    String wikiResponse;
    String response;
    private static final String FAILED = "Request failed due to invalid input";

    private void log(String message) {
        Log.v("RestService", message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new IRestService.Stub() {

            /**
             * Implementation of the restRequest() method
             * @param typeOfRequest 1= Geocoding, 2 = Routes, 3 = Wikipedia
             * @param queryParameters The specific part of a rest url that needs to be replaced, for example address=, or q= or titles=
             * @param requestURL The entire URL for the rest call
             * example typeOfRequest = 1 (Geocoding) requestURL = http://maps.google.com/maps/api/geocode/json?   queryParameters= address= geocode=New York City
             *
             *@return RESPONSE = A string containing everything that came from the request.
             */

            //TODO Need to fix the deserializer for geocode objects its currently tuned for Google but needs to be capable of parsing any geocode response from anywhere.
            //TODO Maybe the right idea here is to return the entire JSON response in a string.
            @Override
            public String restRequest(int typeOfRequest,
                                      String requestURL,
                                      String queryParameters,
                                      @Nullable String routeStart,
                                      @Nullable String routeEnd,
                                      @Nullable String geocode,
                                      @Nullable String wikiSearch)
                    throws RemoteException {
                String finalResposne =null;
                switch(typeOfRequest){
                    case 1:{
                        final GsonGetRequest<GeocodeObject> gsonGetRequest =
                                GeocodeRequests.getGeocodeObject
                                        (
                                                new Response.Listener<GeocodeObject>() {
                                                    @Override
                                                    public void onResponse(GeocodeObject geocodeObject) {
                                                        // Deal with the WikipediaObject here
                                                        setGeocodeData(geocodeObject);

                                                    }
                                                }
                                                ,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        geocodeResponse = "Network or transmission error has occurred.";
                                                    }
                                                },
                                                queryParameters,
                                                requestURL,
                                                geocode
                                        );

                        App.addRequest(gsonGetRequest, TAG);

                        finalResposne = geocodeResponse;

                        return  finalResposne;
                    }
                    case 2:{
                        validateAndPerformRoute(requestURL, queryParameters, routeStart, routeEnd);
                        finalResposne= routeResponse;
                        return finalResposne;
                    }
                    case 3:{
                        validateAndPerformWikiSearch(requestURL, queryParameters, wikiSearch);
                        finalResposne= wikiResponse;
                    }
                    default:{
                    }
                }
                return finalResposne;
        }
    };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }


    String validateAndPerformGeocode(String URL, String queryParams, String geocode) {
        String result = "No results";
        String searchCriteria = null;
        searchCriteria = queryParams;

        if (searchCriteria == null
                || URL == null
                || geocode == null) {
            //do nothing
        } else {
            final GsonGetRequest<GeocodeObject> gsonGetRequest =
                    GeocodeRequests.getGeocodeObject
                            (
                                    new Response.Listener<GeocodeObject>() {
                                        @Override
                                        public void onResponse(GeocodeObject geocodeObject) {
                                            // Deal with the WikipediaObject here
                                         setGeocodeData(geocodeObject);

                                        }
                                    }
                                    ,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            geocodeResponse = "Network or transmission error has occurred.";
                                        }
                                    },
                                    searchCriteria,
                                    URL,
                                    geocode
                            );
            App.addRequest(gsonGetRequest, TAG);

        }
        return geocodeResponse;

    }

    private void setGeocodeData(@NonNull final GeocodeObject geocodeObjectArrayList) {
        String geo = geocodeObjectArrayList.getTitle() + geocodeObjectArrayList.getBody();
       geocodeResponse =geo;
        //mTitle.setText(geocodeObjectArrayList.getTitle());
        // mBody.setText(geocodeObjectArrayList.getBody());
        //mSecondTitle.setText(geocodeObjectArrayList.get(1).getTitle());
        //  mSecondBody.setText(geocodeObjectArrayList.get(1).getBody());

    }

    void validateAndPerformRoute(String URL, String query, String routeStart, String routeEnd) {
        String orgin = null;
        String dest = null;
        dest = routeEnd;
        orgin = routeStart;
        if (orgin != null) {

            if (dest != null) {


                if (query != null) {


                    if (URL != null) {


                        final GsonGetRequest<RouteObject> gsonGetRequest =
                                RouteRequests.getRouteObject
                                        (
                                                new Response.Listener<RouteObject>() {
                                                    @Override
                                                    public void onResponse(RouteObject routeObject) {
                                                        // Deal with the RouteObject here
                                                        setRouteData(routeObject);

                                                    }
                                                }
                                                ,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        routeResponse = "Network or transmission error has occurred.";
                                                    }
                                                },
                                                orgin,
                                                dest,
                                                URL,
                                                query
                                        );


                        App.addRequest(gsonGetRequest, TAG);
                    }
                }
            }

        }
        //do nothing
        else {
            routeResponse = FAILED;

        }
    }

    /**
     * Sets the data in the UI
     *
     * @param routeObject is the object to get the data from
     */
    private void setRouteData(@NonNull final RouteObject routeObject) {
        StringBuilder directionList = new StringBuilder();

        //mTitle.setText(routeObject.getTitle());
        directionList.append(routeObject.getBody() + "\n");
        directionList.append("\n \n Directions are as follows:" + "\n");
        HashMap<Integer, Step> map = constructRouteFromJson(routeObject.getDirections());
        for (int i = 0; i < map.size(); i++) {
            directionList.append("\n Step " + String.valueOf(i + 1) + "\n");
            directionList
                    .append(" Start:" + map.get(i).startGeopoint)
                    .append("\n EndPoint:" + map.get(i).endGeopoint)
                    .append("\n Distance:" + map.get(i).distance)
                    .append("\n Duration:" + map.get(i).duration)
                    .append("\n Duration:" + map.get(i).duration)
                    .append("\n Directions:" + map.get(i).instructions);

        }

        routeResponse =directionList.toString();
    }

    /**
     * Sets the turns into a Hashmap we can manipulate
     *
     * @param routeObject is the object to get the data from
     */
    private HashMap<Integer, Step> constructRouteFromJson(JsonArray routeObject) {
        HashMap<Integer, Step> map = new HashMap<Integer, Step>();
        int counter = 0;
        for (int i = 0; i < routeObject.size(); i++) {
            JsonObject keywordsObject = routeObject.get(i).getAsJsonObject();
            //Get Values for Step
            String startGeopoint = keywordsObject.getAsJsonObject("start_location").get("lat").getAsString() + keywordsObject.getAsJsonObject("start_location").get("lat").getAsString();
            String endGepoint = keywordsObject.getAsJsonObject("end_location").get("lat").getAsString() + keywordsObject.getAsJsonObject("end_location").get("lat").getAsString();
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
        String startGeopoint = null;
        String endGeopoint = null;
        String instructions = null;
        String duration = null;
        String distance = null;

        public Step(String startAddress, String endAddress, String instructions, String duration, String distance) {
            this.startGeopoint = startAddress;
            this.duration = duration;
            this.instructions = instructions;
            this.endGeopoint = endAddress;
            this.distance = distance;

        }
    }

    void validateAndPerformWikiSearch(String URL, String queryParams, String wikiSearch) {
        String searchCriteria = queryParams;

        if (!searchCriteria.equalsIgnoreCase("") || searchCriteria == null) {
            //do nothing
            if (!URL.equalsIgnoreCase("") || URL == null) {
                //do nothing
                if ( !wikiSearch.equalsIgnoreCase("") || wikiSearch == null) {
                    //do nothing


                    final GsonGetRequest<WikipediaObject> gsonGetRequest =
                            WikipediaRequests.getWikipediaObject
                                    (
                                            new Response.Listener<WikipediaObject>() {
                                                @Override
                                                public void onResponse(WikipediaObject wikipediaObject) {
                                                    // Deal with the WikipediaObject here
                                                    setWikiData(wikipediaObject);
                                                }
                                            }
                                            ,
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Deal with the error here
                                                    wikiResponse = FAILED;
                                                }
                                            },
                                            URL,
                                            searchCriteria,
                                            wikiSearch
                                    );


                    App.addRequest(gsonGetRequest, TAG);
                }
            }
        } else {
            wikiResponse = FAILED;
        }

    }

    private void setWikiData(@NonNull final WikipediaObject wikipediaObject) {
        wikiResponse=wikipediaObject.getTitle() + wikipediaObject.getBody();

    }


  /*  @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Received start command.");
        return START_STICKY;
    }*/
/*
    @Override
    public IBinder onBind(Intent intent) {
        log("Received binding.");
        return mBinder;
    }

    private final IRestService.Stub mBinder = new IRestService.Stub() {
        @Override
        public RestResponse[] listFiles(String path) throws RemoteException {
            log("Received list command for: " + path);
            List<RestResponse> toSend = new ArrayList<>();
            // Generates a list of 1000 objects that aren't sent back to the binding Activity
            for (int i = 0; i < 1000; i++)
                toSend.add(new RestResponse("/example/item" + (i + 1)));
            return toSend.toArray(new RestResponse[toSend.size()]);
        }

        @Override
        public void exit() throws RemoteException {
            log("Received exit command.");
            stopSelf();
        }
    };*/
}
