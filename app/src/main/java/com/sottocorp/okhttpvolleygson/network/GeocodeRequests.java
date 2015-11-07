package com.sottocorp.okhttpvolleygson.network;

import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sottocorp.okhttpvolleygson.BuildConfig;
import com.sottocorp.okhttpvolleygson.dataModel.GeocodeObject;
import com.sottocorp.okhttpvolleygson.dataModel.GeocodeObjectDeserializer;
import com.sottocorp.okhttpvolleygson.dataModel.WikipediaObject;

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
     * Returns a dummy object
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
            String searchCriterias
    )
    {
        final String url = BuildConfig.geocodeDomainName + "/geocode/json?address="+searchCriterias;

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
     * Returns a dummy object's array
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
            String searchCriteria
    )
    {
        final String url = BuildConfig.geocodeDomainName + "/geocode/json?address="+searchCriteria;

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
