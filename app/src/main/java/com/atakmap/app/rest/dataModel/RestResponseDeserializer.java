package com.atakmap.app.rest.dataModel;

/**
 * Created by Sam on 07-Nov-15.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Deserializer for a geocode object
 *
 * Convert a JsonObject into a Dummy object.
 */
public class RestResponseDeserializer implements JsonDeserializer<Response>
{
    @SuppressLint("LongLogTag")
    @Override
    public Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        /*
        Map<String, Object> attributes = new HashMap<String, Object>();
        Object o = json;
       if(o.equals(json.getAsJsonObject())){
           JsonObject jsonObject = new JsonObject();
          attributes = new HashMap<String, Object>();
           Set<Map.Entry<String, JsonElement>> entrySet=jsonObject.entrySet();
           for(Map.Entry<String, JsonElement> entry : entrySet){
               attributes.put(entry.getKey(), jsonObject.get(entry.getKey()));
           }




       }else if(o.equals(json.getAsJsonArray())){
           JsonArray jsonArray = new JsonArray();
*/
        Gson gson =new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> attributes = gson.fromJson(json, type);








        Response response =new Response(attributes);


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

        return response;
    }
}