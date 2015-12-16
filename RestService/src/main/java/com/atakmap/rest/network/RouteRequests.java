package com.atakmap.rest.network;

import android.support.annotation.NonNull;
import com.android.volley.Response;
import com.atakmap.rest.dataModel.*;
import com.atakmap.rest.service.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Api requests
 */
public class RouteRequests
{
    private static Gson gson;

    /**
     * Returns a dummy object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<RouteObject> getRouteObject
    (
            @NonNull final Response.Listener<RouteObject> listener,
            @NonNull final Response.ErrorListener errorListener,
            String origin,
            String destination,
            String urlForRequest,
            String queryParams
    )
    {
        gson = new GsonBuilder().registerTypeAdapter(RouteObject.class, new RouteObjectDeserializer())
                .create();
        final String url =urlForRequest+queryParams+origin+destination;
        // final String url = BuildConfig.directionsDomainName + "/json?origin="+origin+"&destination="+destination;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<RouteObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }
    public static GsonGetRequest<RouteObject> getMapQuestRoute
            (
                    @NonNull final Response.Listener<RouteObject> listener,
                    @NonNull final Response.ErrorListener errorListener,
                    String origin,
                    String destination,
                    String urlForRequest,
                    String queryParams
            )
    {
        gson = new GsonBuilder().registerTypeAdapter(RouteObject.class, new MapQuestRouteDeserializer())
                .create();
        final String url =urlForRequest+queryParams+origin+destination;
        // final String url = BuildConfig.directionsDomainName + "/json?origin="+origin+"&destination="+destination;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<RouteObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }
    public static GsonGetRequest<RouteObject> getOsmRoute
        (
                @NonNull final Response.Listener<RouteObject> listener,
                @NonNull final Response.ErrorListener errorListener,
                String origin,
                String destination,
                String urlForRequest,
                String queryParams
        )
{
    gson = new GsonBuilder().registerTypeAdapter(RouteObject.class, new OsmRouteDeserializer())
            .create();
    final String url =urlForRequest+queryParams+origin+destination;
    // final String url = BuildConfig.directionsDomainName + "/json?origin="+origin+"&destination="+destination;

    return new GsonGetRequest<>
            (
                    url,
                    new TypeToken<RouteObject>() {}.getType(),
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
    public static GsonGetRequest<ArrayList<RouteObject>> getRouteObjectArray
    (
            @NonNull final Response.Listener<ArrayList<RouteObject>> listener,
            @NonNull final Response.ErrorListener errorListener,
            String origin,
            String destination
    )
    {
        final String url = BuildConfig.directionsDomainName+ "/json?origin="+origin+"&destination="+destination;

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<RouteObject>>() {}.getType(),
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
