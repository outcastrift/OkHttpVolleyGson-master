package com.atakmap.app.rest.dataModel;

/**
 * Created by Sam on 07-Nov-15.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializer for a geocode object
 *
 * Convert a JsonObject into a Dummy object.
 */
public class GeocodeObjectDeserializer implements JsonDeserializer<GeocodeObject>
{
    @SuppressLint("LongLogTag")
    @Override
    public GeocodeObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        GeocodeObject geocodeObject = new GeocodeObject();
        final JsonObject jsonObject = json.getAsJsonObject().getAsJsonArray("results").get(0).getAsJsonObject();

      //  Log.e("JSON OUTPUT = ", jsonObject.toString());
       // final JsonArray jsonArray = jsonObject.get(0).getAsJsonArray();
       // final JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();


        //final  JsonArray jarray = jsonObject.getAsJsonArray("results");
        //final JsonObject elementObject =jarray.get(0).getAsJsonObject();
        String title = jsonObject.get("formatted_address").getAsString();
        Log.d("GeocodeObjectDeserializer title =",title);
        geocodeObject.setTitle(title);
       // Log.d("GeocodeObjectDeserializer title =",jsonObject.get("formatted_address").getAsString());
        String lat =jsonObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsString();
        String lng = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsString();
        String boundsNE = jsonObject.getAsJsonObject("geometry").getAsJsonObject("bounds").getAsJsonObject("northeast").get("lat").getAsString()+
                jsonObject.getAsJsonObject("geometry").getAsJsonObject("bounds").getAsJsonObject("northeast").get("lng").getAsString();
        String boundsSW = jsonObject.getAsJsonObject("geometry").getAsJsonObject("bounds").getAsJsonObject("southwest").get("lat").getAsString()+
                jsonObject.getAsJsonObject("geometry").getAsJsonObject("bounds").getAsJsonObject("southwest").get("lng").getAsString();

        String locationType = jsonObject.getAsJsonObject("geometry").get("location_type").getAsString();
        String longName=jsonObject.getAsJsonArray("address_components").get(0).getAsJsonObject().get("long_name").getAsString();
        String shortName=jsonObject.getAsJsonArray("address_components").get(0).getAsJsonObject().get("short_name").getAsString();
                String country=jsonObject.getAsJsonArray("address_components").get(1).getAsJsonObject().get("long_name").getAsString();


        geocodeObject.setBody("Latitude = " + lat +"\n Longitude = "+ lng+
                        "\n This location is " + locationType+
        "\n Bounds are as follows : "+
                "\n NorthEast = "+boundsNE+
                "\n SouthWest = "+boundsSW+
               "\n Location details are as follows : "+
        "\n Long Name = "+longName+
        "\n Short Name = "+shortName+
        "\n Country = "+country);

       /* for (int i = 0; i < jarray.size(); i++) {
             geocodeObject = new GeocodeObject();

            JsonObject keywordsObject = jarray.get(i).getAsJsonObject();
            geocodeObject.setTitle(keywordsObject.get("formatted_address").getAsString());
            Log.d("GeocodeObjectDeserializer title =",keywordsObject.get("formatted_address").getAsString());
           String lat =keywordsObject.get("geometry").getAsJsonObject().get("lat").getAsString();
            String lng =keywordsObject.get("geometry").getAsJsonObject().get("lng").getAsString();
            geocodeObject.setBody("Latitude = "+lat +"\n Longitude = "+ lng);
            Log.d("GeocodeObjectDeserializer Body =","Latitude = "+lat +"\n Longitude = "+ lng);

        }*/
       // geocodeObject.setTitle(jsonObject.get("title").getAsString());
        //  wikipediaObject.setTitle(jsonObject.get("query").getAsString());
        //geocodeObject.setBody(jsonObject.get("extract").getAsString());

        return geocodeObject;
    }
}