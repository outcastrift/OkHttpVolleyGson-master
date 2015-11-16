// IRestCallback.aidl
package com.atakmap.app.rest.restAidl;

// Declare any non-default types here with import statements
import com.atakmap.app.rest.dataModel.RestResponse;
interface IRestCallback {

   void returnResults(String response);
   void restResponse(inout RestResponse r);
}
