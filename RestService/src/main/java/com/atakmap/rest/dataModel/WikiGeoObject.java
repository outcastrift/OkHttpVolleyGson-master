package com.atakmap.rest.dataModel;

import java.util.List;

/**
 * Created by sam on 11/17/15.
 */
public class WikiGeoObject {

    private String mTitle;
    private String mBody;
    private List<List<String>> stackOfGeoCards;


    public List<List<String>> getStackOfGeoCards() {
        return stackOfGeoCards;
    }

    public void setStackOfGeoCards(List<List<String>> stackOfGeoCards) {
        this.stackOfGeoCards = stackOfGeoCards;
    }



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
    {
        mBody = body;
    }



}
