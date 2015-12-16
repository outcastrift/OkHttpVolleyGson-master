package com.atakmap.app.rest.restAidl;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atakmap.app.rest.base.App;
import com.atakmap.app.rest.dataModel.*;
import com.atakmap.app.rest.dataModel.RestResponse;
import com.atakmap.app.rest.network.GeocodeRequests;
import com.atakmap.app.rest.network.GsonGetRequest;
import com.atakmap.app.rest.network.RouteRequests;
import com.atakmap.app.rest.network.WikipediaRequests;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sam on 15-Nov-15.
 */
public class RestServiceImplementation extends IRestService.Stub {
    private String routeResponse = new String();
    private String wikiResponse;
    private String response;
    private static final String TAG = "RestServiceImplementation";
    private static final String FAILED = "Request failed due to invalid input";
    private static String geocodeResponse = null;
    private IRestCallback callback;

    @Override
    public void restRequest(String url, String queryParams, String geo, String routeBegin, String routeEnd, String wikiSearch, IRestCallback callback) throws RemoteException {
        this.callback = callback;
        if (!geo.equalsIgnoreCase("")){
        validateAndPerformGeocode(url, queryParams, geo);
        }
        if (!routeBegin.equalsIgnoreCase("")&&!routeEnd.equalsIgnoreCase("")){
            validateAndPerformRoute(url, queryParams, routeBegin, routeEnd);
        }
        if (!wikiSearch.equalsIgnoreCase("")){
            validateAndPerformWikiSearch(url, queryParams, wikiSearch);
        }


    }

    @Override
    public void googleRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        this.callback = callback;
        if (!origin.equalsIgnoreCase("")&&!destination.equalsIgnoreCase("")){
            validateAndPerformGoogleRoute(origin, destination);
        }
    }

    @Override
    public void mapquestRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        this.callback = callback;
        if (!origin.equalsIgnoreCase("")&&!destination.equalsIgnoreCase("")){
            validateAndPerformMapquestRoute(origin, destination);
        }
    }

    @Override
    public void osmRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        this.callback = callback;
        if (!origin.equalsIgnoreCase("")&&!destination.equalsIgnoreCase("")){
            validateAndPerformOsmRoute(origin, destination);
        }
    }


    void validateAndPerformGeocode(String URL, String queryParams, String geocode) {
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
                                            String results = setGeocodeData(geocodeObject);
                                            try {
                                                callback.returnResults(results);
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                callback.restResponse(new RestResponse(geocodeObject.getTitle(),
                                                        geocodeObject.getBody()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    ,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            try {
                                                callback.returnResults("Network or transmission error has occurred.");
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                        "Empty"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    searchCriteria,
                                    URL,
                                    geocode
                            );


            App.addRequest(gsonGetRequest, TAG);

        }
    }



    private String setGeocodeData(@NonNull final GeocodeObject geocodeObjectArrayList) {
        String geo = geocodeObjectArrayList.getTitle() + geocodeObjectArrayList.getBody();
        geocodeResponse = geo;
        //mTitle.setText(geocodeObjectArrayList.getTitle());
        // mBody.setText(geocodeObjectArrayList.getBody());
        //mSecondTitle.setText(geocodeObjectArrayList.get(1).getTitle());
        //  mSecondBody.setText(geocodeObjectArrayList.get(1).getBody());
        return geocodeResponse;

    }
    void validateAndPerformOsmRoute(String routeStart, String routeEnd) {
        String URL ="http://router.project-osrm.org/viaroute?";
        String query ="";
        String dest = routeEnd;
        String orgin = routeStart;


        final GsonGetRequest<RouteObject> gsonGetRequest =
                RouteRequests.getOsmRoute
                        (
                                new Response.Listener<RouteObject>() {
                                    @Override
                                    public void onResponse(RouteObject routeObject) {
                                        // Deal with the RouteObject here

                                        String results =  setRouteData(routeObject);
                                        try {
                                            callback.returnResults(results);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse(routeObject.getTitle(),
                                                    stepNumber, stepDirections, startPoint, endPoint, distance, duration));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        try {
                                            callback.returnResults("Network or transmission error has occurred.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                    "Empty"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                orgin,
                                dest,
                                URL,
                                query
                        );


        App.addRequest(gsonGetRequest, TAG);

    } void validateAndPerformMapquestRoute(String routeStart, String routeEnd) {
        String URL ="http://www.mapquestapi.com/directions/v2/";
        String query ="route?key=YOUR_KEY_HERE&";
        String dest = "from="+routeEnd;
        String orgin = "&to="+routeStart+"&callback=renderNarrative";


        final GsonGetRequest<RouteObject> gsonGetRequest =
                RouteRequests.getMapQuestRoute
                        (
                                new Response.Listener<RouteObject>() {
                                    @Override
                                    public void onResponse(RouteObject routeObject) {
                                        // Deal with the RouteObject here

                                        String results =  setRouteData(routeObject);
                                        try {
                                            callback.returnResults(results);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse(routeObject.getTitle(),
                                                    stepNumber, stepDirections, startPoint, endPoint, distance, duration));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        try {
                                            callback.returnResults("Network or transmission error has occurred.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                    "Empty"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                orgin,
                                dest,
                                URL,
                                query
                        );


        App.addRequest(gsonGetRequest, TAG);

    }
    void validateAndPerformGoogleRoute( String routeStart, String routeEnd) {
        String URL ="http://www.mapquestapi.com/directions/v2/";
        String query ="route?key=YOUR_KEY_HERE&";
        String dest = "from="+routeEnd;
        String orgin = "&to="+routeStart+"&callback=renderNarrative";


        final GsonGetRequest<RouteObject> gsonGetRequest =
                RouteRequests.getRouteObject
                        (
                                new Response.Listener<RouteObject>() {
                                    @Override
                                    public void onResponse(RouteObject routeObject) {
                                        // Deal with the RouteObject here

                                        String results =  setRouteData(routeObject);
                                        try {
                                            callback.returnResults(results);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse(routeObject.getTitle(),
                                                    stepNumber, stepDirections, startPoint, endPoint, distance, duration));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        try {
                                            callback.returnResults("Network or transmission error has occurred.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                    "Empty"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                orgin,
                                dest,
                                URL,
                                query
                        );


        App.addRequest(gsonGetRequest, TAG);

    }
    void validateAndPerformRoute(String URL, String query, String routeStart, String routeEnd) {

        String dest = routeEnd;
        String orgin = routeStart;


                        final GsonGetRequest<RouteObject> gsonGetRequest =
                                RouteRequests.getRouteObject
                                        (
                                                new Response.Listener<RouteObject>() {
                                                    @Override
                                                    public void onResponse(RouteObject routeObject) {
                                                        // Deal with the RouteObject here

                                                        String results =  setRouteData(routeObject);
                                                        try {
                                                            callback.returnResults(results);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            callback.restResponse(new RestResponse(routeObject.getTitle(),
                                                                    stepNumber, stepDirections, startPoint, endPoint, distance, duration));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }
                                                ,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        try {
                                                            callback.returnResults("Network or transmission error has occurred.");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                                    "Empty"));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                orgin,
                                                dest,
                                                URL,
                                                query
                                        );


                        App.addRequest(gsonGetRequest, TAG);

    }
    public List<String> stepNumber =new ArrayList<String>();
    public List<String> stepDirections=new ArrayList<String>();
    public List<String> startPoint=new ArrayList<String>();
    public List<String> endPoint=new ArrayList<String>();
    public List<String> distance=new ArrayList<String>();
    public List<String> duration=new ArrayList<String>();
    /**
     * Sets the data in the UI
     *
     * @param routeObject is the object to get the data from
     */
    private String setRouteData(@NonNull final RouteObject routeObject) {
        StringBuilder directionList = new StringBuilder();

        //mTitle.setText(routeObject.getTitle());
        directionList.append(routeObject.getBody() + "\n");
        directionList.append("\n \n Directions are as follows:" + "\n");
        HashMap<Integer, Step> map = constructRouteFromJson(routeObject.getDirections());
        for (int i = 0; i < map.size(); i++) {
            stepNumber.add(String.valueOf(i+1));
            stepDirections.add(i,map.get(i).instructions);
            startPoint.add(i,map.get(i).startGeopoint);
            endPoint.add(i,map.get(i).endGeopoint);
            distance.add(i, map.get(i).distance);
            duration.add(i, map.get(i).duration);
            directionList.append("\n Step " + String.valueOf(i + 1) + "\n");
            directionList
                    .append(" Start:" + map.get(i).startGeopoint)
                    .append("\n EndPoint:" + map.get(i).endGeopoint)
                    .append("\n Distance:" + map.get(i).distance)
                    .append("\n Duration:" + map.get(i).duration)
                    .append("\n Duration:" + map.get(i).duration)
                    .append("\n Directions:" + map.get(i).instructions);

        }

        routeResponse = directionList.toString();
        return routeResponse;
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
                if (!wikiSearch.equalsIgnoreCase("") || wikiSearch == null) {
                    //do nothing


                    final GsonGetRequest<WikipediaObject> gsonGetRequest =
                            WikipediaRequests.getWikipediaObject
                                    (
                                            new Response.Listener<WikipediaObject>() {
                                                @Override
                                                public void onResponse(WikipediaObject wikipediaObject) {
                                                    // Deal with the WikipediaObject here
                                                   String results = setWikiData(wikipediaObject);
                                                    try {
                                                        callback.returnResults(results);
                                                    } catch (RemoteException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        callback.restResponse(new RestResponse(wikipediaObject.getTitle(),
                                                                wikipediaObject.getBody()));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            ,
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Deal with the error here
                                                    try {
                                                        callback.returnResults("Network or transmission error has occurred.");
                                                    } catch (RemoteException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        callback.restResponse(new RestResponse("Network or transmission error has occurred.",
                                                                "Empty"));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
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

    private String setWikiData(@NonNull final WikipediaObject wikipediaObject) {
        wikiResponse = wikipediaObject.getTitle() + wikipediaObject.getBody();
        return wikiResponse;

    }

}
