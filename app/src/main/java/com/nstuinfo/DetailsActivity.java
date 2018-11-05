package com.nstuinfo;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mRecyclerView.MyAdapter;
import com.nstuinfo.mRecyclerView.SpacesItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private String title = null;

    private LinearLayout ll, rootLL;
    private ScrollView scrollView;

    private Toolbar toolbar;
    private TextView appBarTitleTV;
    private TextView footerDateTV;

    private ExtractJson extractJson;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details);

        invalidateOptionsMenu();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        footerDateTV = findViewById(R.id.dateTV);

        scrollView = findViewById(R.id.detailsScroll);
        rootLL = findViewById(R.id.detailsMainLL);
        ll = findViewById(R.id.mainLL);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        title = getIntent().getStringExtra("TITLE");

        extractJson = new ExtractJson(this, ReadWriteJson.readFile(this), ll);

        if (title != null) {
            appBarTitleTV.setText(title);
            if (extractJson.hasContents(title)) {
                mRecyclerView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                itemsList = extractJson.getSecondaryItemsList(title);
                loadRecyclerView(true);
            } else {
                extractJson.getView(title);
            }

            if (!extractJson.getUpdatedDate(title).equalsIgnoreCase("")) {
                footerDateTV.setVisibility(View.VISIBLE);
                footerDateTV.setText(extractJson.getUpdatedDate(title));
            }

        } else {
            appBarTitleTV.setText(getResources().getString(R.string.app_name));
        }

        setTheme();

    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            rootLL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
            footerDateTV.setTextColor(Color.WHITE);
            footerDateTV.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
        }
    }

    private void loadRecyclerView(boolean isList) {
        myAdapter = new MyAdapter(this, itemsList, title, "second");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (extractJson.hasContents(title)) {
            getMenuInflater().inflate(R.menu.menu_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
        } else if (id == R.id.menu_item_search){
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    String query = newText.toLowerCase();

                    final List<String> filteredList = new ArrayList<>();

                    for (int i = 0; i < itemsList.size(); i++) {
                        final String text = itemsList.get(i).toLowerCase();
                        if (text.contains(query)) {
                            filteredList.add(itemsList.get(i));
                        }
                    }

                    myAdapter = new MyAdapter(DetailsActivity.this, filteredList, title, "second");
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                    mRecyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                    return false;
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
