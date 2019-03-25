/**
 * MainActivity to show the YouTube channel data from API calling
 */
package com.tones.frnotifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tones.frnotifications.adapter.MovieAdapter;
import com.tones.frnotifications.database.AppDatabase;
import com.tones.frnotifications.database.DatabaseClient;
import com.tones.frnotifications.database.RoomModelDAO;
import com.tones.frnotifications.database.RoomModelData;
import com.tones.frnotifications.helper.Utils;
import com.tones.frnotifications.notification.ForegroundNotificationService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressDialog mDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_CHANNEL = "https://www.googleapis.com/youtube/v3/search?part=id,snippet&maxResults=20&channelId=UCCq1xDJMBRF61kiOgU90_kw&key=AIzaSyAovyHhltYPsmodQR9Vqe940oUo6LfTK-o";
    private List<RoomModelData> channelList = new ArrayList<>();
    private MovieAdapter mAdapter;
    private RoomModelData mChannel = null;
    private RoomModelDAO rtContentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize view
        initView();
        // Set data into adapter
        setDataIntoAdapter();
        //send request to get feed
        DatabaseClient databaseClient = DatabaseClient.getInstance(MainActivity.this);
        AppDatabase appDatabase = databaseClient.getAppDatabase();
        rtContentDAO = appDatabase.getContentDAO();
        sendRequest();
        startService();

    }


    private void startService() {
        Intent intent = new Intent(this, ForegroundNotificationService.class);
        intent.setAction(ForegroundNotificationService.Action.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);

    }

    private void setDataIntoAdapter() {
        mAdapter = new MovieAdapter(this, channelList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, linearLayout.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void sendRequest() {
        mDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        mDialog.setMessage(getString(R.string.loading));
        mDialog.show();

        if (getNetworkAvailability()) {
            getFeed();
        } else {
            // Toast.makeText(getApplicationContext(), getString(R.string.network) + mDatabase.numberOfRows(), Toast.LENGTH_LONG).show();
            getFeedFromDatabase();
            mDialog.dismiss();
        }
    }

    private void getFeed() {
        // Creating volley request obj
        JsonObjectRequest mchannelReq = new JsonObjectRequest(Request.Method.GET, URL_CHANNEL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Hide dialog();
                        mDialog.dismiss();

                        try {
                            JSONObject responseJson = new JSONObject(response.toString());
                            JSONArray list = responseJson.getJSONArray(getString(R.string.items));
                            if (list.length() > 0) {
                                if (rtContentDAO.getAllContent().size() > 0) {
                                    rtContentDAO.deleteTable();
                                }
                                // Parsing response
                                for (int i = 0; i < list.length(); i++) {
                                    try {
                                        JSONObject obj = list.getJSONObject(i);
                                        mChannel = new RoomModelData();
                                        mChannel.setTitle(obj.getJSONObject("snippet").getString("title"));
                                        JSONObject snippetJSONObject = obj.getJSONObject("snippet");
                                        mChannel.setDescription(snippetJSONObject.getString("description"));
                                        mChannel.setDatetime(snippetJSONObject.getString("publishedAt"));
                                        String img = snippetJSONObject.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                                        mChannel.setThumbnailurl(img);
                                        mChannel.setId("" + i);
                                        channelList.add(mChannel);

                                        RunInBackground runInBackground = new RunInBackground();
                                        runInBackground.execute(mChannel);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error.getMessage());
                // hidePDialog();
                mDialog.dismiss();
            }
        });

        RequestQueue requestueue = Volley.newRequestQueue(this);
        requestueue.add(mchannelReq);
    }

    private void getFeedFromDatabase() {
        channelList.clear();
        channelList = rtContentDAO.getAllContent();
        mAdapter = new MovieAdapter(this, channelList);
        mRecyclerView.setAdapter(mAdapter);
    }


    // To check network availability
    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }

    // AsyncTask for background operation
    private class RunInBackground extends AsyncTask<RoomModelData, Void, Void> {
        private final String TAG = RunInBackground.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // isDataLoaded = true;
        }

        @Override
        protected Void doInBackground(RoomModelData... params) {
            RoomModelData channel = params[0];
            try {
                rtContentDAO.insert(channel);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

    }
}
