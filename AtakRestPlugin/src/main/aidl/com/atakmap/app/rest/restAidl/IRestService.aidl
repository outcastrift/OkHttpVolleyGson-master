package com.atakmap.app.rest.restAidl;
import com.atakmap.app.rest.restAidl.IRestCallback;
interface IRestService {
      //void restRequest(IRestCallback callback);
      void restRequest(in int requestType,in String url, in String queryParams, in String geocodeSearch, in String routeBegin, in String routeEnd, in String wikiSearch, in IRestCallback callback);

}
