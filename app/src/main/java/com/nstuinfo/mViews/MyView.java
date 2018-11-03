package com.nstuinfo.mViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nstuinfo.R;
import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mOtherUtils.StringUtil;
import com.nstuinfo.mRecyclerView.MyAdapter;

import java.util.List;

/**
 * Created by whoami on 10/26/2018.
 */

public class MyView {

    @SuppressLint("InflateParams")
    public static void setTitleView(Context context, String text, LinearLayout linearLayout) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.title_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.titleTV);
        CardView cardView = layout.findViewById(R.id.title_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(Color.BLACK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(text));
        }

        FontAppearance.setPrimaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        linearLayout.addView(layout);
    }

    @SuppressLint("InflateParams")
    public static void setHintView(Context context, String content, LinearLayout linearLayout) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.hint_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.hintTV);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(content));
        }

        if (Preferences.isDarkTheme(context)) {
            tv.setTextColor(Color.WHITE);
        }

        FontAppearance.setSecondaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        linearLayout.addView(layout);
    }

    @SuppressLint("InflateParams")
    public static void setContentView (Context context, String content, LinearLayout linearLayout) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.content_view,null);
        }

        assert layout != null;
        TextView tv = layout.findViewById(R.id.contentTV);
        CardView cardView = layout.findViewById(R.id.content_item_view_card);

        if (Preferences.isDarkTheme(context)) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
            tv.setTextColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(content));
        }

        FontAppearance.setSecondaryTextSize(context, tv);

        Linkify.addLinks(tv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
        tv.setLinksClickable(true);

        ImageView callImg = layout.findViewById(R.id.phoneIMGV);
        ImageView mailImg = layout.findViewById(R.id.mailIMGV);
        callImg.setVisibility(View.GONE);
        mailImg.setVisibility(View.GONE);

        if ( content.contains("Phone") || content.contains("Telephone") || content.contains("Mobile") ||
                content.contains("phone:") || content.contains("telephone:") || content.contains("mobile:") ) {

            callImg.setVisibility(View.VISIBLE);
        }

        if (content.contains("Email") || content.contains("E-mail") || content.contains("Mail") ||
                content.contains("email:") || content.contains("e-mail:") || content.contains("mail:")) {

            mailImg.setVisibility(View.VISIBLE);
        }

        linearLayout.addView(layout);

        //Spannable p_Text2 = Spannable.Factory.getInstance().newSpannable(tv.getText());
        StringUtil.removeUnderlines(new SpannableString(tv.getText()));
    }

    public static void setPopupDialog(final Context context, String title, String message, String posBtn, String negBtn, final String posBtnUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title.trim().equalsIgnoreCase("")) {
            builder.setTitle(Html.fromHtml("<font color='#0D47A1'>Notice!!</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#0D47A1'>"+title+"</font>"));
        }

        if (!message.trim().equalsIgnoreCase("")) {
            builder.setMessage(Html.fromHtml("<font color='#000000'>"+message+"</font>"));
        }

        if (posBtn.trim().equalsIgnoreCase("")) {
            builder.setPositiveButton(Html.fromHtml("OK"),new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setPositiveButton(Html.fromHtml(posBtn),new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (posBtnUrl.trim().equalsIgnoreCase("")) {
                        dialog.cancel();
                    } else {
                        Uri uri = Uri.parse(posBtnUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                }
            });
        }

        if (!negBtn.trim().equalsIgnoreCase("")) {
            builder.setNegativeButton(Html.fromHtml(negBtn),new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog alert = builder.create();

        if (!ReadWriteJson.readFile(context).equalsIgnoreCase("")) {
            String prevmsg = new ExtractJson(context, ReadWriteJson.readFile(context)).getPopupMessage();
            if (!message.trim().equalsIgnoreCase("")){
                if (!prevmsg.equalsIgnoreCase(message)) {
                    alert.show();
                }
            }
        } else {
            if (!message.trim().equalsIgnoreCase("")){
                alert.show();
            }
        }
    }

    // VIEWs CREATED PROGRAMMATICALLY AND UNUSED
    public static void setTitleView2(Context context, String text, LinearLayout linearLayout) {
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

    public static void setHintView2 (Context context, String content, LinearLayout linearLayout) {
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

    public static void setContentView2 (Context context, String content, LinearLayout linearLayout) {
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
