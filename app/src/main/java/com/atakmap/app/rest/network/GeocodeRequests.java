package com.atakmap.app.rest.network;

import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.atakmap.app.rest.BuildConfig;
import com.atakmap.app.rest.dataModel.GeocodeObject;
import com.atakmap.app.rest.dataModel.GeocodeObjectDeserializer;
import com.atakmap.app.rest.dataModel.WikipediaObject;

import java.util.ArrayList;

/**
 * Api requests
 */
public class GeocodeRequests
{
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GeocodeObject.class, new GeocodeObjectDeserializer())
            .create();
    /**
     * Returns a geocode object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<GeocodeObject> getGeocodeObject
    (
            @NonNull final Response.Listener<GeocodeObject> listener,
            @NonNull final Response.ErrorListener errorListener,
            String searchCriterias,
            String requestURL,
            String geocode
    )
    {
        final String url = requestURL+searchCriterias+geocode;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<GeocodeObject>() {}.getType(),
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
    public static GsonGetRequest<ArrayList<GeocodeObject>> getGeocodeObjectArray
    (
            @NonNull final Response.Listener<ArrayList<GeocodeObject>> listener,
            @NonNull final Response.ErrorListener errorListener,
            String searchCriteria,
            String requestURL
    )
    {
        final String url = requestURL+searchCriteria;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<GeocodeObject>>() {}.getType(),
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
            @NonNull final Response.Listener<WikipediaObject> listener,
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
                        new TypeToken<WikipediaObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );

        gsonPostRequest.setShouldCache(false);

        return gsonPostRequest;
    }
}
