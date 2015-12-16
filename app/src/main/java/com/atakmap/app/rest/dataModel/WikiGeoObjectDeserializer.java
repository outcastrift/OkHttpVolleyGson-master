package com.atakmap.app.rest.dataModel;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sam on 11/17/15.
 */
public class WikiGeoObjectDeserializer implements JsonDeserializer<WikiGeoObject> {

    @Override
    public WikiGeoObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        final WikiGeoObject wikiGeoObject = new WikiGeoObject();



        final JsonArray jsonArray = (JsonArray) json.getAsJsonObject().getAsJsonObject("query").getAsJsonArray("geosearch");

        Iterator iterator = jsonArray.iterator();

        List<List<String>> stackOfGeoCards = new ArrayList<>();
        while(iterator.hasNext()){
            int i = -1 +1;
            List<String> wikiGeoCard = new ArrayList<String>();
            //title
            wikiGeoCard.add(0,jsonArray.get(i).getAsJsonObject().get("title").getAsString());
            //lat
            wikiGeoCard.add(1,jsonArray.get(i).getAsJsonObject().get("lat").getAsString() );
            //lon
            wikiGeoCard.add(2,jsonArray.get(i).getAsJsonObject().get("lon").getAsString() );
            //distance from in meters
            wikiGeoCard.add(3,jsonArray.get(i).getAsJsonObject().get("dist").getAsString());
            //adding it to the stack
            stackOfGeoCards.add(i, wikiGeoCard);
        }
        wikiGeoObject.setStackOfGeoCards(stackOfGeoCards);


        return wikiGeoObject;
    }
}
