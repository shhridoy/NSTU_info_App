package com.nstuinfo.mJsonUtils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nstuinfo.mViews.MyView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Created by whoami on 10/24/2018.
 */

public class ExtractJson {

    private Context context;
    private String text;
    private LinearLayout linearLayout;

    public ExtractJson (Context context, String text) {
        this.context  = context;
        this.text = text;
    }

    public ExtractJson(Context context, String text, LinearLayout linearLayout) {
        this.context = context;
        this.text = text;
        this.linearLayout = linearLayout;
    }

    public List<String> getMainItemsList() {
        List<String> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_version")) {
                    int data_version = object.getInt("data_version");
                }

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item");
                            list.add(item);
                        }
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Execption Arise", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Execption Arise", Toast.LENGTH_SHORT).show();
        }

        return list;
    }

    public void getView(String itemTag) {
        try {
            JSONArray jsonArray = new JSONArray(text);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = (JSONObject) jsonArray.get(i);

                if (object.has("data_version")) {
                    int data_version = object.getInt("data_version");
                }

                if (object.has("data")) {

                    JSONArray dataArray = object.getJSONArray("data");

                    for (int j=0; j<dataArray.length(); j++) {

                        JSONObject dataObject = (JSONObject) dataArray.get(j);

                        if (dataObject.has("item")) {
                            String item = dataObject.getString("item");

                            if (item.equalsIgnoreCase(itemTag)) {
                                if (dataObject.has("details")) {

                                    JSONArray detailsArray = dataObject.getJSONArray("details");

                                    for (int k=0; k<detailsArray.length(); k++) {

                                        JSONObject detailsObject = (JSONObject) detailsArray.get(k);

                                        if (detailsObject.has("title_hint")) {
                                            String title_hint = detailsObject.getString("title_hint");
                                            if (!title_hint.equalsIgnoreCase("")) {
                                                MyView.setTitleView(context, title_hint, linearLayout);
                                            }
                                        }

                                        if (detailsObject.has("title")) {
                                            String title = detailsObject.getString("title");
                                            if (!title.equalsIgnoreCase("")) {
                                                if (detailsObject.has("contents")) {

                                                } else {
                                                    MyView.setTitleView(context, title, linearLayout);
                                                }
                                            }
                                        }

                                        if (detailsObject.has("hint")) {
                                            String hint = detailsObject.getString("hint");
                                            if (!hint.equalsIgnoreCase("")) {
                                                MyView.setSubtitleView(context, hint, linearLayout);
                                            }
                                        }

                                        if (detailsObject.has("content")) {
                                            String content = detailsObject.getString("content");
                                            if (!content.equalsIgnoreCase("")) {
                                                MyView.setContentView(context, content, linearLayout);
                                            }
                                        }

                                        if (detailsObject.has("contents")) {
                                            JSONArray contentsArray = dataObject.getJSONArray("contents");
                                        }

                                    }
                                }
                            }
                        }



                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Execption Arise", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Execption Arise", Toast.LENGTH_SHORT).show();
        }
    }

}
