package com.atakmap.rest.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sam on 07-Nov-15.
 */
public class GeocodeObject implements Parcelable {
    public String mTitle;
    public String mBody;

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



    public GeocodeObject(String title, String body) {
        this.mTitle = title;
        this.mBody = body;
    }
    public GeocodeObject(){

    }

   /* @Override
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
            return new GeocodeObject(in.readString(),in.readString());
        }

        @Override
        public GeocodeObject[] newArray(int size) {
            return new GeocodeObject[size];
        }
    };*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mBody);
    }

    protected GeocodeObject(Parcel in) {
        this.mTitle = in.readString();
        this.mBody = in.readString();
    }

    public static final Creator<GeocodeObject> CREATOR = new Creator<GeocodeObject>() {
        public GeocodeObject createFromParcel(Parcel source) {
            return new GeocodeObject(source);
        }

        public GeocodeObject[] newArray(int size) {
            return new GeocodeObject[size];
        }
    };
    public void readFromParcel(Parcel in){
        this.mBody=in.readString();
        this.mTitle=in.readString();
    }
}

