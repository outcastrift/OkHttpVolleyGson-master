package com.atakmap.app.rest.dataModel;

import java.util.*;

/**
 * Created by sam on 12/17/15.
 */
public class Response {
    Map<String, String> attributes = new HashMap<String, String>();
    public List<String> keys;
    public List<String> valueForKey;
    public Response(Map<String, String> attributes){
        this.attributes = attributes;

    }
    public String getKey(String key){
        String verifiedResponse= attributes.get(key);
        return verifiedResponse;
    }

    public RestResponse convertToRestResponse() {

        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            keys.add(attributes.get(it.next()));
            valueForKey.add(attributes.get(it.next()));

        }
        RestResponse rr = new RestResponse(keys, valueForKey);
        return rr;
    }

}
