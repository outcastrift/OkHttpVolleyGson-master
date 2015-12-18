package com.atakmap.app.rest.network;

import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.atakmap.app.rest.BuildConfig;
import com.atakmap.app.rest.dataModel.GeocodeObject;
import com.atakmap.app.rest.dataModel.GeocodeObjectDeserializer;
import com.atakmap.app.rest.dataModel.RestResponseDeserializer;
import com.atakmap.app.rest.dataModel.WikipediaObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Api requests
 */
public class RestRequests
{
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(com.atakmap.app.rest.dataModel.Response.class, new RestResponseDeserializer())
            .create();
    /**
     * Returns a geocode object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<com.atakmap.app.rest.dataModel.Response> getResponseObject
    (
            @NonNull final Response.Listener<com.atakmap.app.rest.dataModel.Response> listener,
            @NonNull final Response.ErrorListener errorListener,
            String requestURL,
            String queryParams1,
            String queryParams2,
            String searchString1,
            String searchString2
    )
    {
        String query = null;
        if (queryParams2 ==null ||queryParams2.equalsIgnoreCase("")){
            query=queryParams1+"&"+queryParams2;
        }else{
            query = queryParams1;
        }
        String search = null;
        if (searchString2 ==null ||searchString2.equalsIgnoreCase("")){
            query=searchString1+"&"+searchString2;
        }else{
            search = searchString1;
        }

        final String url = requestURL+query+search;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<Response>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }

    /**
     * Returns a geocode object's array
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<ArrayList<Response>> getResponseObjectArray
    (
            @NonNull final Response.Listener<ArrayList<Response>> listener,
            @NonNull final Response.ErrorListener errorListener,
            String searchCriteria,
            String requestURL
    )
    {
        final String url = requestURL+searchCriteria;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<Response>>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }


    /**
     * An example call to demonstrate how to do a Volley POST call
     * and parse the response with Gson.
     *
     * @param listener is the listener for the success response
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonPostRequest}
     */
    public static GsonPostRequest getDummyObjectArrayWithPost
    (
            @NonNull final Response.Listener<Response> listener,
            @NonNull final Response.ErrorListener errorListener
    )
    {
        final String url = BuildConfig.wikiDomainName + "/dummyPath";

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Ficus");
        jsonObject.addProperty("surname", "Kirkpatrick");

        final JsonArray squareGuys = new JsonArray();
        final JsonObject dev1 = new JsonObject();
        final JsonObject dev2 = new JsonObject();
        dev1.addProperty("name", "Jake Wharton");
        dev2.addProperty("name", "Jesse Wilson");
        squareGuys.add(dev1);
        squareGuys.add(dev2);

        jsonObject.add("squareGuys", squareGuys);

        final GsonPostRequest gsonPostRequest = new GsonPostRequest<>
                (
                        url,
                        jsonObject.toString(),
                        new TypeToken<Response>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );

        gsonPostRequest.setShouldCache(false);

        return gsonPostRequest;
    }
}
