package com.nstuinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mRecyclerView.MyAdapter;
import com.nstuinfo.mJsonUtils.ReadWriteJson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;
    private ExtractJson jsonExtract;

    private int jsonVersion = 0;


    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniViews();


        if (ReadWriteJson.readFile(this).equals("")) {
            if (isInternetOn()) {
                parseJson(true);
            } else {
                Toast.makeText(getApplicationContext(), "Check connection to laod data for the first time.", Toast.LENGTH_LONG).show();
            }
        } else {
            if (isInternetOn()) {
                jsonExtract = new ExtractJson(this, ReadWriteJson.readFile(this));
                itemsList = jsonExtract.getMainItemsList();
                loadRecyclerView();
                parseJson(false);
            } else {
                jsonExtract = new ExtractJson(this, ReadWriteJson.readFile(this));
                itemsList = jsonExtract.getMainItemsList();
                loadRecyclerView();
            }
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void iniViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadRecyclerView() {
        myAdapter = new MyAdapter(MainActivity.this, itemsList);
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SecondActivity.class).putExtra("TITLE", "test"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void parseJson(final boolean pdVisibility) {
        final String URL = "https://jsonblob.com/api/6a1a5234-d30f-11e8-9c58-b1987dc5c254";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.setCancelable(true);
        if (pdVisibility) {
            progressDialog.show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (pdVisibility) {
                            progressDialog.dismiss();
                            ReadWriteJson.saveFile(MainActivity.this, response);
                            jsonExtract = new ExtractJson(MainActivity.this, response);
                            itemsList = jsonExtract.getMainItemsList();
                            loadRecyclerView();
                        } else {
                            ReadWriteJson.saveFile(MainActivity.this, response);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.i("ERROR", error.getMessage());
                        //Toast.makeText(getApplicationContext(), "Error Occurs!!", Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        getBaseContext();
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return false;
    }

}
