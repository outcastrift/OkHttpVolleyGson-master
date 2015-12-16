package com.atakmap.rest.dataModel;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializer for a dummy object
 *
 * Convert a JsonObject into a Dummy object.
 */
public class OsmRouteDeserializer implements JsonDeserializer<RouteObject>
{
    @Override
    public RouteObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        final RouteObject routeObject = new RouteObject();
        final JsonObject jsonObject =json.getAsJsonObject().getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonArray("legs").get(0).getAsJsonObject();
        String distanceToTravel = jsonObject.getAsJsonObject("distance").get("text").getAsString();
        String estimatedTravelDuration = jsonObject.getAsJsonObject("duration").get("text").getAsString();
        String startAddress =jsonObject.get("start_address").getAsString();
        String endAddress = jsonObject.get("end_address").getAsString();
        String startGeopoint=jsonObject.getAsJsonObject("start_location").get("lat").getAsString()
                + ", "+jsonObject.getAsJsonObject("start_location").get("lng").getAsString();
        String endGeopoint=jsonObject.getAsJsonObject("end_location").get("lat").getAsString()
                +", "+ jsonObject.getAsJsonObject("end_location").get("lng").getAsString();









        routeObject.setTitle(startAddress +" to "+ endAddress);
        routeObject.setDirections(jsonObject.getAsJsonArray("steps"));
      //  wikipediaObject.setTitle(jsonObject.get("query").getAsString());
        routeObject.setBody(" Estimated time to travel = "+estimatedTravelDuration+
        "\n Estimated distance to travel = "+ distanceToTravel+
        "\n Start Location Latitude and Longitude = "+ startGeopoint+
        "\n End Location Latitude and Longitude = "+ endGeopoint);

        return routeObject;
    }
}
