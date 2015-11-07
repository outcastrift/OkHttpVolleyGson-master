package com.sottocorp.okhttpvolleygson.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sottocorp.okhttpvolleygson.R;
import com.sottocorp.okhttpvolleygson.base.App;
import com.sottocorp.okhttpvolleygson.dataModel.WikipediaObject;
import com.sottocorp.okhttpvolleygson.network.WikipediaRequests;
import com.sottocorp.okhttpvolleygson.network.GsonGetRequest;

/**
 * Demonstrates how to make a JSON Object request
 */
public class WikiObjectActivity extends AppCompatActivity
{
    private static final String sTag = "tagOne";

    private TextView mTitle, mBody;
    private ProgressBar mProgressBar;
    private LinearLayout mContent, mErrorView;
    private Button mButton;
    private EditText mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_object_request);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mButton=(Button)findViewById(R.id.search_button);
        mSearch=(EditText)findViewById(R.id.search_box);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateAndPerformSearch();
            }
        };

        mButton.setOnClickListener(clickListener);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTitle = (TextView) findViewById(R.id.my_title);
        mBody = (TextView) findViewById(R.id.my_body);
        mBody.setMovementMethod(new ScrollingMovementMethod());
        mErrorView = (LinearLayout) findViewById(R.id.error_view);
        mContent = (LinearLayout) findViewById(R.id.content);
        mProgressBar.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);



    }
    void validateAndPerformSearch(){
        String searchCriteria = null;
        searchCriteria=mSearch.getText().toString();
        if(searchCriteria.equalsIgnoreCase("")||searchCriteria==null){
            //do nothing
        }else{
            mProgressBar.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);

            final GsonGetRequest<WikipediaObject> gsonGetRequest =
                    WikipediaRequests.getWikipediaObject
                            (
                                    new Response.Listener<WikipediaObject>() {
                                        @Override
                                        public void onResponse(WikipediaObject dummyObject) {
                                            // Deal with the WikipediaObject here
                                            mProgressBar.setVisibility(View.GONE);
                                            mContent.setVisibility(View.VISIBLE);
                                            setData(dummyObject);
                                        }
                                    }
                                    ,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Deal with the error here
                                            mProgressBar.setVisibility(View.GONE);
                                            mErrorView.setVisibility(View.VISIBLE);
                                        }
                                    },
                                    searchCriteria
                            );


            App.addRequest(gsonGetRequest, sTag);
        }

    }
    @Override
    protected void onStop()
    {
        App.cancelAllRequests(sTag);

        super.onStop();
    }

    /**
     * Sets the data in the UI
     *
     * @param wikipediaObject is the object to get the data from
     */
    private void setData(@NonNull final WikipediaObject wikipediaObject)
    {
        mTitle.setText(wikipediaObject.getTitle());
        mBody.setText(wikipediaObject.getBody());
    }
}
