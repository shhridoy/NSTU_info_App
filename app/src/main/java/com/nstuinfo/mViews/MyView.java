package com.nstuinfo.mViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nstuinfo.R;
import com.nstuinfo.mRecyclerView.MyAdapter;

import java.util.List;

/**
 * Created by whoami on 10/26/2018.
 */

public class MyView {

    public static void setTitleView(Context context, String text, LinearLayout linearLayout) {
        CardView cardView = new CardView(context);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        cardView.setRadius(5);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(12, 0, 12, 12);
        cardView.setLayoutParams(params1);

        TextView textView = new TextView(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }
        textView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(5, 15, 5, 15);
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setLinksClickable(true);

        cardView.addView(textView);
        linearLayout.addView(cardView);
    }

    public static void setSubtitleView (Context context, String content, LinearLayout linearLayout) {
        TextView textView = new TextView(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(content));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 15, 15);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.START);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);

        linearLayout.addView(textView);
    }

    public static void setContentView (Context context, String content, LinearLayout linearLayout) {
        CardView cardView = new CardView(context);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.md_white_1000));
        cardView.setRadius(7);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(15, 0, 15, 15);
        cardView.setLayoutParams(params1);

        TextView textView = new TextView(context);
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
        linearLayout.addView(cardView);
    }

    public static void setContentViewWithImage (Context context, String content, LinearLayout linearLayout) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.json_test,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.testTV);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(content));
        }
        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);


        linearLayout.addView(layout);
    }

    public static void setRecyclerView (Context context, List<String> itemsList, String title, LinearLayout linearLayout) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(0, 0, 0, 8);
        recyclerView.setLayoutParams(params1);

        MyAdapter myAdapter = new MyAdapter(context, itemsList, title, "second");
        recyclerView.setAdapter(myAdapter);

        linearLayout.addView(recyclerView);
    }

}
