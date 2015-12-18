package com.atakmap.app.rest.dataModel;

/**
 * Created by Sam on 16-Nov-15.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Sam on 07-Nov-15.
 */
public class RestResponse implements Parcelable {
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

    public List<String> getStepDirections() {
        return stepDirections;
    }

    public void setStepDirections(List<String> stepDirections) {
        this.stepDirections = stepDirections;
    }

    public List<String> stepNumber;
    public List<String> stepDirections;
    public List<String> startPoint;
    public List<String> endPoint;
    public List<String> distance;
    public List<String> duration;
    public List<String> keys;
    public List<String> valueForKey;

    public RestResponse(List<String> keys,List<String> valueForKey){
        this.keys=keys;
        this.valueForKey = valueForKey;
    }

    public RestResponse(String title, List<String> stepNumber,List<String> stepDirections,List<String> startPoint,List<String> endPoint,List<String> distance,List<String> duration) {
        this.mTitle = title;
        this.stepNumber = stepNumber;
        this.stepDirections = stepDirections;
        this.startPoint = startPoint;
        this.endPoint=endPoint;
        this.distance=distance;
        this.duration=duration;
    }
    public RestResponse(String title, String body) {
        this.mTitle = title;
        this.mBody = body;
    }
    public RestResponse(){

    }


    public List<String> getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(List<String> endPoint) {
        this.endPoint = endPoint;
    }

    public List<String> getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(List<String> stepNumber) {
        this.stepNumber = stepNumber;
    }

    public List<String> getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(List<String> startPoint) {
        this.startPoint = startPoint;
    }

    public List<String> getDistance() {
        return distance;
    }

    public void setDistance(List<String> distance) {
        this.distance = distance;
    }

    public List<String> getDuration() {
        return duration;
    }

    public void setDuration(List<String> duration) {
        this.duration = duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mBody);
        dest.writeStringList(this.stepNumber);
        dest.writeStringList(this.stepDirections);
        dest.writeStringList(this.startPoint);
        dest.writeStringList(this.endPoint);
        dest.writeStringList(this.distance);
        dest.writeStringList(this.duration);
        dest.writeStringList(this.keys);
        dest.writeStringList(this.valueForKey);
    }

    protected RestResponse(Parcel in) {
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.stepNumber = in.createStringArrayList();
        this.stepDirections = in.createStringArrayList();
        this.startPoint = in.createStringArrayList();
        this.endPoint = in.createStringArrayList();
        this.distance = in.createStringArrayList();
        this.duration = in.createStringArrayList();
        this.keys = in.createStringArrayList();
        this.valueForKey = in.createStringArrayList();
    }

    public static final Parcelable.Creator<RestResponse> CREATOR = new Parcelable.Creator<RestResponse>() {
        public RestResponse createFromParcel(Parcel source) {
            return new RestResponse(source);
        }

        public RestResponse[] newArray(int size) {
            return new RestResponse[size];
        }
    };

    public void readFromParcel(Parcel in){
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.stepNumber = in.createStringArrayList();
        this.stepDirections = in.createStringArrayList();
        this.startPoint = in.createStringArrayList();
        this.endPoint = in.createStringArrayList();
        this.distance = in.createStringArrayList();
        this.duration = in.createStringArrayList();
        this.keys = in.createStringArrayList();
        this.valueForKey = in.createStringArrayList();
    }
}

