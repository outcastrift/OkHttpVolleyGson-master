package com.atakmap.app.rest.restAidl;
import com.atakmap.app.rest.restAidl.IRestCallback;
interface IRestService {
      //void restRequest(IRestCallback callback);
      void restRequest(in String url, in String queryParams, in String geocodeSearch, in String routeBegin, in String routeEnd, in String wikiSearch, in IRestCallback callback);
      void googleRoute(in String origin, in String destination,in IRestCallback callback);
      void mapquestRoute(in String origin, in String destination,in IRestCallback callback);
      void osmRoute(in String origin, in String destination,in IRestCallback callback);


}
