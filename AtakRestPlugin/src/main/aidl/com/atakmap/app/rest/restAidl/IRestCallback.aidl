// IRestCallback.aidl
package com.atakmap.app.rest.restAidl;

// Declare any non-default types here with import statements
import com.atakmap.app.rest.dataModel.RestResponse;

interface IRestCallback {
/**
Method to receive the entirety of a RestResponse within 1 string.
This is valuable in alot of scenarios, you need to make a request out and you know the return data is going to be small and easily managed or the calling code is already complex enough without introducing a new Object.

**/
  void returnResults(String response);
/**
This method returns a instance of the RestResponse class. Within the returned class all the information from the response will be locked to fields that are annotated appropriately.
**/
  void restResponse(inout RestResponse r);
   }