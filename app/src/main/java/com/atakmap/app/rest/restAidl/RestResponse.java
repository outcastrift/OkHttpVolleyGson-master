package com.atakmap.app.rest.restAidl;

import android.os.Parcel;
import android.os.Parcelable;

public class RestResponse implements Parcelable {

    private String mValue;

    public RestResponse(Parcel source) {
        mValue = source.readString();
    }


    public RestResponse(String value) {
        mValue = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
    }

    public static final Creator<RestResponse> CREATOR = new Creator<RestResponse>() {
        @Override
        public RestResponse[] newArray(int size) {
            return new RestResponse[size];
        }

        @Override
        public RestResponse createFromParcel(Parcel source) {
            return new RestResponse(source);
        }
    };
}
