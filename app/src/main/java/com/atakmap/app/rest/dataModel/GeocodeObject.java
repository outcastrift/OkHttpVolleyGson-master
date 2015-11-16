package com.atakmap.app.rest.dataModel;


import auto.parcelgson.AutoParcelGson;

/**
 * Created by Sam on 07-Nov-15.
 */
@AutoParcelGson
public abstract class GeocodeObject  {
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

    static GeocodeObject create(String mTitle, String mBody) {
        return builder().title(mTitle).body(mBody).build();
    }

    public abstract String title();
    public abstract String body();

    @AutoParcelGson.Builder
    public abstract static class Builder {
        public abstract Builder title(String s);
        public abstract Builder body(String s);
        public abstract GeocodeObject build();
    }



    public static AutoParcelGson.Builder builder() {
        return new AutoParcelGson_GeocodeObject.Builder();
    }

    public abstract Builder toBuilder();

}

