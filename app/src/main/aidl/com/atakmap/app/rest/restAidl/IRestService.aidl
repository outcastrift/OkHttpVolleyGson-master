package com.atakmap.app.rest.restAidl;
import com.atakmap.app.rest.restAidl.RestResponse;

interface IRestService {

     String restRequest(int typeOfRequest, String requestURL, String queryParameters, String routeStart, String routeEnd, String geocode, String wikiSearch);
}
