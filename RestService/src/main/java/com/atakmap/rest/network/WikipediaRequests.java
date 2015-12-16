package com.atakmap.rest.network;

import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.atakmap.rest.service.BuildConfig;
import com.atakmap.rest.dataModel.WikipediaObject;
import com.atakmap.rest.dataModel.WikipediaObjectDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Api requests
 */
public class WikipediaRequests
{
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(WikipediaObject.class, new WikipediaObjectDeserializer())
            .create();
    /**
     * Returns a dummy object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<WikipediaObject> getWikipediaObject
    (
            @NonNull final Response.Listener<WikipediaObject> listener,
            @NonNull final Response.ErrorListener errorListener,
            String requestURL,
            String searchCriterias,
            String wikiSearch
    )
    {
        //final String url = BuildConfig.wikiDomainName + "format=json&action=query&prop=extracts&exintro=&formatversion=2&explaintext=&titles="+searchCriterias;
        final String url = requestURL+ searchCriterias+ wikiSearch;
        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<WikipediaObject>() {}.getType(),
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
    public static GsonGetRequest<ArrayList<WikipediaObject>> getWikipediaObjectArray
    (
            @NonNull final Response.Listener<ArrayList<WikipediaObject>> listener,
            @NonNull final Response.ErrorListener errorListener,
            String searchCriteria
    )
    {
        final String url = BuildConfig.wikiDomainName + searchCriteria;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<WikipediaObject>>() {}.getType(),
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
