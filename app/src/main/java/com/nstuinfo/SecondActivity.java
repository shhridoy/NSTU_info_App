package com.nstuinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.nstuinfo.mJsonUtils.ReadWriteJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {

    String title = null;

    LinearLayout ll;
    StringBuilder s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_details1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll = findViewById(R.id.mainLL);

        title = getIntent().getStringExtra("TITLE");

        ExtractJson extractJson = new ExtractJson(this, ReadWriteJson.readFile(this), ll);

        if (title != null) {
            extractJson.getView(title);
        }

    }

    private void jsonValues() {
        ll = findViewById(R.id.mainLL);
        s = new StringBuilder();
        final String URL = "https://jsonblob.com/api/6a1a5234-d30f-11e8-9c58-b1987dc5c254";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.setCancelable(true);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = (JSONObject) jsonArray.get(i);

                                if (object.has("data_version")) {
                                    int data_version = object.getInt("data_version");
                                }

                                if (object.has("data")) {
                                    JSONArray dataArray = object.getJSONArray("data");
                                    for (int j=0; j<dataArray.length(); j++) {

                                        JSONObject dataObject = (JSONObject) dataArray.get(j);
                                        String item = dataObject.getString("item");

                                        if (title != null && title.equalsIgnoreCase(item)) {

                                            setTitleView(item);

                                            JSONArray detailsArray = dataObject.getJSONArray("details");
                                            for (int k=0; k<detailsArray.length(); k++) {
                                                JSONObject contentObject = (JSONObject) detailsArray.get(k);

                                                String ttle = "";
                                                String hint = "";
                                                String content = "";

                                                ttle = contentObject.getString("title");
                                                hint = contentObject.getString("hint");
                                                content = contentObject.getString("content");

                                                if (ttle != null && ttle.length() > 0) {
                                                    setTitleView(ttle);
                                                }

                                                if (hint != null && hint.length() > 0) {
                                                    setHintView(hint);
                                                }

                                                if (content != null && content.length() > 0) {
                                                    setContentsView(content);
                                                }

                                            }
                                        }
                                    }
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Exception arises!!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.i("ERROR", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error Occurs!!", Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void introTestInitialize() {
        LinearLayout ll = findViewById(R.id.mainLL);
        for (int i=0; i<5; i++) {
            CardView cardView = new CardView(this);
            cardView.setCardElevation(0);
            cardView.setCardBackgroundColor(getResources().getColor(R.color.md_white_1000));
            cardView.setRadius(2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 5);
            cardView.setLayoutParams(params);

            TextView textView = new TextView(this);
            textView.setText(getResources().getString(R.string.introduction_info_1));
            textView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.START);
            textView.setTextSize(14);
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.NORMAL);
            textView.setPadding(5, 5, 5, 5);

            Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
            textView.setLinksClickable(true);

            cardView.addView(textView);

            ll.addView(cardView);
        }
    }

    private void introductionLayoutInitialize() {
        TextView detailsTV = findViewById(R.id.introduction_details_tv);
        TextView info_tv_1 = findViewById(R.id.introduction_info_1_tv);
        TextView info_tv_2 = findViewById(R.id.introduction_info_2_tv);
        TextView info_tv_3 = findViewById(R.id.introduction_info_3_tv);

        detailsTV.setMovementMethod(LinkMovementMethod.getInstance());
        info_tv_1.setMovementMethod(LinkMovementMethod.getInstance());
        info_tv_2.setMovementMethod(LinkMovementMethod.getInstance());
        info_tv_3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void regentBoardLayoutInitialize() {
        TextView chairman_details_tv = findViewById(R.id.regent_chairman_details_tv);
        TextView member_1 = findViewById(R.id.regent_member_1_tv);
        TextView member_2 = findViewById(R.id.regent_member_2_tv);
        TextView member_3 = findViewById(R.id.regent_member_3_tv);
        TextView member_4 = findViewById(R.id.regent_member_4_tv);
        TextView member_5 = findViewById(R.id.regent_member_5_tv);
        TextView member_6 = findViewById(R.id.regent_member_6_tv);
        TextView member_7 = findViewById(R.id.regent_member_7_tv);
        TextView member_8 = findViewById(R.id.regent_member_8_tv);
        TextView member_9 = findViewById(R.id.regent_member_9_tv);
        TextView member_10 = findViewById(R.id.regent_member_10_tv);
        TextView member_11 = findViewById(R.id.regent_member_11_tv);
        TextView member_12 = findViewById(R.id.regent_member_12_tv);
        TextView member_13 = findViewById(R.id.regent_member_13_tv);
        TextView member_14 = findViewById(R.id.regent_member_14_tv);
        TextView member_15 = findViewById(R.id.regent_member_15_tv);

        chairman_details_tv.setMovementMethod(LinkMovementMethod.getInstance());
        member_1.setMovementMethod(LinkMovementMethod.getInstance());
        member_2.setMovementMethod(LinkMovementMethod.getInstance());
        member_3.setMovementMethod(LinkMovementMethod.getInstance());
        member_4.setMovementMethod(LinkMovementMethod.getInstance());
        member_5.setMovementMethod(LinkMovementMethod.getInstance());
        member_6.setMovementMethod(LinkMovementMethod.getInstance());
        member_7.setMovementMethod(LinkMovementMethod.getInstance());
        member_8.setMovementMethod(LinkMovementMethod.getInstance());
        member_9.setMovementMethod(LinkMovementMethod.getInstance());
        member_10.setMovementMethod(LinkMovementMethod.getInstance());
        member_11.setMovementMethod(LinkMovementMethod.getInstance());
        member_12.setMovementMethod(LinkMovementMethod.getInstance());
        member_13.setMovementMethod(LinkMovementMethod.getInstance());
        member_14.setMovementMethod(LinkMovementMethod.getInstance());
        member_15.setMovementMethod(LinkMovementMethod.getInstance());
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
            return true;
        } else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTitleView(String content) {
        CardView cardView = new CardView(this);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        cardView.setRadius(2);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(5, 0, 5, 5);
        cardView.setLayoutParams(params1);

        TextView textView = new TextView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(content));
        }
        textView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);

        cardView.addView(textView);
        ll.addView(cardView);
    }

    private void setHintView (String content) {
        TextView textView = new TextView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(content));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 5);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.START);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);

        ll.addView(textView);
    }

    private void setContentsView(String content) {
        CardView cardView = new CardView(this);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.md_white_1000));
        cardView.setRadius(2);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(8, 0, 8, 5);
        cardView.setLayoutParams(params1);

        TextView textView = new TextView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(content));
        }
        textView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.START);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);


        cardView.addView(textView);
        ll.addView(cardView);
    }

}
