package com.atakmap.app.rest.dataModel;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sam on 07-Nov-15.
 */
public class GeocodeObject implements Parcelable {
    private String mTitle;
    private String mBody;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }



    public GeocodeObject(Parcel in) {
        mTitle = in.readString();
        mBody = in.readString();
    }
    public GeocodeObject(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mBody);
    }
    public void readFromParcel(Parcel in){
        this.mBody=in.readString();
        this.mTitle=in.readString();
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GeocodeObject> CREATOR = new Parcelable.Creator<GeocodeObject>() {

        @Override
        public GeocodeObject createFromParcel(Parcel in) {
            return new GeocodeObject(in);
        }

        @Override
        public GeocodeObject[] newArray(int size) {
            return new GeocodeObject[size];
        }
    };
}

