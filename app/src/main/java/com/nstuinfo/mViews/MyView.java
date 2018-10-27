package com.nstuinfo.mViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nstuinfo.R;

/**
 * Created by whoami on 10/26/2018.
 */

public class MyView {

    public static void setTitleView(Context context, String text, LinearLayout linearLayout) {
        CardView cardView = new CardView(context);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        cardView.setRadius(3);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(8, 0, 8, 8);
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
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);

        cardView.addView(textView);
        linearLayout.addView(cardView);
    }

    public static void setClickableTitleView(Context context, String text, LinearLayout linearLayout) {
        CardView cardView = new CardView(context);
        cardView.setCardElevation(0);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        cardView.setRadius(2);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(5, 0, 5, 5);
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
        textView.setPadding(5, 5, 5, 5);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        textView.setLinksClickable(true);

        cardView.addView(textView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        params.setMargins(10, 0, 10, 8);
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
        cardView.setRadius(3);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(10, 0, 10, 8);
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

}
