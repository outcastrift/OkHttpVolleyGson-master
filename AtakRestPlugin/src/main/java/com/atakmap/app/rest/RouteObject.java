package com.atakmap.app.rest;

import com.google.gson.JsonArray;

/**
 * Represents a dummy object
 */
public class RouteObject
{
    private String mTitle;
    private String mBody;
    private  JsonArray mDirections;


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getBody()
    {
        return mBody;
    }

    public void setBody(String body)
    {mBody = body;
    }
    public JsonArray getDirections() {
        return mDirections;
    }
    public void setDirections(JsonArray directions){
        mDirections =directions;
    }
}
