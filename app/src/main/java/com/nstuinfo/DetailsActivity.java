package com.nstuinfo;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mOtherUtils.Preferences;

public class DetailsActivity extends AppCompatActivity {

    private String title = null;

    private LinearLayout ll, rootLL;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLL = findViewById(R.id.detailsMainLL);
        ll = findViewById(R.id.mainLL);

        title = getIntent().getStringExtra("TITLE");

        ExtractJson extractJson = new ExtractJson(this, ReadWriteJson.readFile(this), ll);

        if (title != null) {
            getSupportActionBar().setTitle(title);
            extractJson.getView(title);
        }

        setTheme();

    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            rootLL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

}
