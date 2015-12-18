package com.atakmap.app.rest.restAidl;

import android.os.RemoteException;
import com.android.volley.VolleyError;
import com.atakmap.app.rest.base.App;
import com.atakmap.app.rest.dataModel.Response;
import com.atakmap.app.rest.network.GsonGetRequest;
import com.atakmap.app.rest.network.RestRequests;



/**
 * Created by sam on 12/17/15.
 */
public class RestResponseImpl extends IRestService.Stub {
    @Override
    public void restRequest(String url, String queryParams, String geocodeSearch, String routeBegin, String routeEnd, String wikiSearch, IRestCallback callback) throws RemoteException {
        throw new RemoteException("Wrong Method");
    }

    @Override
    public void googleRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        throw new RemoteException("Wrong Method");
    }

    @Override
    public void mapquestRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        throw new RemoteException("Wrong Method");
    }

    @Override
    public void osmRoute(String origin, String destination, IRestCallback callback) throws RemoteException {
        throw new RemoteException("Wrong Method");
    }

    @Override
    public void getRestReponse(String url, String queryParams, String queryParams2, String searchString1, String searchString2, String jsonKey, IRestCallback callback) throws RemoteException {
            final IRestCallback cb = callback;
            if (queryParams == null
                    || url == null
                    || searchString1 == null) {
                //do nothing
            } else {



                final String jsonResultsKey = jsonKey;
                final GsonGetRequest<Response> gsonGetRequest;
                gsonGetRequest = RestRequests.getResponseObject
                        (new com.android.volley.Response.Listener<com.atakmap.app.rest.dataModel.Response>() {
                             @Override
                             public void onResponse(Response response) {

                                 String results = response.getKey(jsonResultsKey);
                                 try {
                                     cb.returnResults(results);
                                 } catch (RemoteException e) {
                                     e.printStackTrace();
                                 }
                                 try {
                                     cb.restResponse(response.convertToRestResponse());
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                             }
                         }
                                ,
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        try {
                                            cb.returnResults("Network or transmission error has occurred.");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                          //  cb.restResponse(new RestResponse("Network or transmission error has occurred."+"Empty"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                url,
                                queryParams,
                                queryParams2,
                                searchString1,
                                searchString2
                        );


                App.addRequest(gsonGetRequest, "RestResponseImpl");

            }

        }

}
