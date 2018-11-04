package com.nstuinfo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.nstuinfo.mJsonUtils.Constants;
import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mOtherUtils.ExtraUtils;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mRecyclerView.MyAdapter;
import com.nstuinfo.mRecyclerView.SpacesItemDecoration;
import com.nstuinfo.mViews.FontAppearance;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView appBarTitleTV;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RelativeLayout navLL;

    private CoordinatorLayout coordinatorLayout;
    private ConstraintLayout mConstraintLayout;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;
    private ExtractJson jsonExtract;

    private int jsonOfflineVersion = 0;
    private int jsonOnlineVersion = 0;

    private PopupWindow mPopUpWindow;

    private String initialFont;
    private boolean isInitialThemeDark = false;
    private boolean isInitialViewGrid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        setTheme();

        initialFont = Preferences.getFontAppearance(this);
        isInitialThemeDark = Preferences.isDarkTheme(this);
        isInitialViewGrid = Preferences.isGridView(this);

        if (ReadWriteJson.readFile(this).equals("")) {
            if (isInternetOn()) {
                parseJson(true);
            } else {
                Toast.makeText(getApplicationContext(), "Need internet connection to load data for the first time!!", Toast.LENGTH_LONG).show();
            }
        } else {
            if (isInternetOn()) {
                jsonExtract = new ExtractJson(this, ReadWriteJson.readFile(this));
                if (itemsList != null) {
                    itemsList.clear();
                }
                itemsList = jsonExtract.getMainItemsList();
                loadRecyclerView();
                parseJson(false);
            } else {
                jsonExtract = new ExtractJson(this, ReadWriteJson.readFile(this));
                if (itemsList != null) {
                    itemsList.clear();
                }
                itemsList = jsonExtract.getMainItemsList();
                loadRecyclerView();
            }
        }

        if (!ReadWriteJson.readFile(this).equals("")){
            jsonOfflineVersion = jsonExtract.getJsonVersion();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            if (isInitialViewGrid != Preferences.isGridView(this)) {
                loadRecyclerView();
                isInitialViewGrid = Preferences.isGridView(this);
            }

            if (isInitialThemeDark != Preferences.isDarkTheme(this)) {
                setTheme();
                loadRecyclerView();
                isInitialThemeDark = Preferences.isDarkTheme(this);
            }

            if (!initialFont.equals(Preferences.getFontAppearance(this))) {
                loadRecyclerView();
                initialFont = Preferences.getFontAppearance(this);
            }

        }
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        appBarTitleTV.setText(getResources().getString(R.string.app_name));

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetOn()) {
                    parseJson(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your data connection!!", Toast.LENGTH_LONG).show();
                }
                navViewImageAlteration();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_UP||
                        event.getAction() == MotionEvent.ACTION_SCROLL) {

                    fab.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.hide();
                        }
                    }, 6000);

                }

                return false;
            }
        });

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        mConstraintLayout = findViewById(R.id.homeConstraintLayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navLL = navigationView.getHeaderView(0).findViewById(R.id.navLL);

        navViewImageAlteration();
    }

    private void navViewImageAlteration() {
        int rand = ExtraUtils.getRandomNumber(1,8);
        if (rand == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_1));
            }
        } else if (rand == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_2));
            }
        } else if (rand == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_3));
            }
        } else if (rand == 4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_4));
            }
        } else if (rand == 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_5));
            }
        } else if (rand == 6) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_6));
            }
        } else if (rand == 7) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_7));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_8));
            }
        }
    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
            navigationView.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setPopupTheme(R.style.PopupMenuLight);
            navigationView.setBackgroundColor(Color.WHITE);
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        }
    }

    private void loadRecyclerView() {
        myAdapter = new MyAdapter(this, itemsList, "main");

        if (Preferences.isGridView(this)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecyclerView.setAdapter(myAdapter);
    }

    private void parseJson(final boolean pdVisibility) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.setCancelable(true);
        if (pdVisibility) {
            progressDialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.JSON_URL_1,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (pdVisibility) {
                            progressDialog.dismiss();
                            jsonExtract = new ExtractJson(HomeActivity.this, response);
                            if (itemsList != null) {
                                itemsList.clear();
                            }
                            itemsList = jsonExtract.getMainItemsList();
                            loadRecyclerView();
                            jsonExtract.setPopupNotificationDialog();
                            ReadWriteJson.saveFile(HomeActivity.this, response);
                        } else {

                            jsonExtract = new ExtractJson(HomeActivity.this, response);
                            jsonOnlineVersion =  jsonExtract.getJsonVersion();

                            jsonExtract.setPopupNotificationDialog();

                            ReadWriteJson.saveFile(HomeActivity.this, response);

                            if (jsonOfflineVersion < jsonOnlineVersion) {
                                updateNoticeDialog();
                            }
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

    boolean backButtonPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(backButtonPressedOnce){
                super.onBackPressed();
                return;
            }

            this.backButtonPressedOnce = true;
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please press back again to exit!!", Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backButtonPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_search) {
            navViewImageAlteration();
            searchPopupWindow();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_theme) {
            popupThemeWindow();
        } else if (id == R.id.nav_font_appearance) {
            fontAppearanceDialog();
        } else if (id == R.id.nav_itemview) {
            itemViewDialog();
        } else if (id == R.id.nav_web_site) {
            Uri uri = Uri.parse("http://nstu.edu.bd/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("InflateParams")
    private void popupThemeWindow(){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.theme_popup_window,null);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        TextView titleTV = null;
        CircleMenu circleMenu = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            titleTV = layout.findViewById(R.id.popUpTitleTV);
            circleMenu = layout.findViewById(R.id.circleMenu);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.8) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        titleTV.setText("Theme");

        FontAppearance.setPrimaryTextSize(this, titleTV);

        if (Preferences.isDarkTheme(HomeActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                titleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                mainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                titleTV.setBackgroundColor(Color.BLACK);
            }
            backDimRL.setBackgroundColor(getResources().getColor(R.color.dim_white));
        }

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        circleMenu.setMainMenu(
                getResources().getColor(R.color.colorPrimary),
                R.mipmap.ic_add,
                R.mipmap.ic_remove)
                .addSubMenu(Color.WHITE, R.mipmap.ic_sun)
                .addSubMenu(Color.BLACK, R.mipmap.ic_cloud)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if (index == 0) {
                            Preferences.setDarkTheme(HomeActivity.this, false);
                            //Toast.makeText(getApplicationContext(), "Activating Light Theme...", Toast.LENGTH_LONG).show();
                        } else if (index == 1) {
                            Preferences.setDarkTheme(HomeActivity.this, true);
                            //Toast.makeText(getApplicationContext(), "Activating Dark Theme...", Toast.LENGTH_LONG).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPopUpWindow.dismiss();
                            }
                        }, 1500);
                    }
                });

        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void fontAppearanceDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.font_appearance_dialog);
        dialog.setCancelable(true);

        RelativeLayout dlgMainRL = dialog.findViewById(R.id.dialogMainRL);
        TextView dlgTitleTV = dialog.findViewById(R.id.fontDialogTitleTV);
        final RadioButton smallRB = dialog.findViewById(R.id.smallFontRB);
        final RadioButton mediumRB = dialog.findViewById(R.id.mediumFontRB);
        final RadioButton largeRB = dialog.findViewById(R.id.largeFontRB);

        FontAppearance.setPrimaryTextSize(this, dlgTitleTV);

        if (Preferences.isDarkTheme(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                dlgMainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                dlgTitleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                dlgMainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                dlgTitleTV.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            }
            dlgTitleTV.setTextColor(Color.WHITE);
            smallRB.setTextColor(Color.WHITE);
            mediumRB.setTextColor(Color.WHITE);
            largeRB.setTextColor(Color.WHITE);
        }

        if (Preferences.getFontAppearance(HomeActivity.this).equals(Preferences.MEDIUM_FONT)){
            mediumRB.setChecked(true);
        } else if (Preferences.getFontAppearance(HomeActivity.this).equals(Preferences.LARGE_FONT)){
            largeRB.setChecked(true);
        } else {
            smallRB.setChecked(true);
        }

        smallRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(true);
                mediumRB.setChecked(false);
                largeRB.setChecked(false);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.SMALL_FONT);
            }
        });

        mediumRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(false);
                mediumRB.setChecked(true);
                largeRB.setChecked(false);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.MEDIUM_FONT);
            }
        });

        largeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(false);
                mediumRB.setChecked(false);
                largeRB.setChecked(true);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.LARGE_FONT);
            }
        });

        dialog.show();
    }

    private void itemViewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_view_dialog);
        dialog.setCancelable(true);

        RelativeLayout dlgMainRL = dialog.findViewById(R.id.dialogMainRL);
        TextView dlgTitleTV = dialog.findViewById(R.id.viewDialogTitleTV);
        final RadioButton listRB = dialog.findViewById(R.id.ListRB);
        final RadioButton gridRB = dialog.findViewById(R.id.GridRB);

        FontAppearance.setPrimaryTextSize(this, dlgTitleTV);

        if (Preferences.isDarkTheme(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                dlgMainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                dlgTitleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                dlgMainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                dlgTitleTV.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            }
            dlgTitleTV.setTextColor(Color.WHITE);
            listRB.setTextColor(Color.WHITE);
            gridRB.setTextColor(Color.WHITE);
        }

        if (Preferences.isGridView(this)){
            gridRB.setChecked(true);
        } else {
            listRB.setChecked(true);
        }

        listRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRB.setChecked(true);
                gridRB.setChecked(false);
                Preferences.setGridView(HomeActivity.this, false);
            }
        });

        gridRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRB.setChecked(false);
                gridRB.setChecked(true);
                Preferences.setGridView(HomeActivity.this, true);
            }
        });

        dialog.show();
    }

    private void updateNoticeDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        // builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color='#0D47A1'>Notice!!</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>Data has been updated.</font>"));
        builder.setPositiveButton(Html.fromHtml("OK"),new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void searchPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.search_popup_window,null);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        RelativeLayout etBGRL = null;
        EditText editText = null;
        RecyclerView recyclerView = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            etBGRL = layout.findViewById(R.id.ETRL);
            editText = layout.findViewById(R.id.popUpSearchET);
            recyclerView = layout.findViewById(R.id.RecyclerVIew);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.9) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));

        if (Preferences.isDarkTheme(HomeActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                etBGRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                mainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                etBGRL.setBackgroundColor(Color.BLACK);
            }
            backDimRL.setBackgroundColor(getResources().getColor(R.color.dim_white));
        }

        searchContent(editText, recyclerView);

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void searchContent(EditText editText, final RecyclerView rcView) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().toLowerCase();

                List<String> contentList = jsonExtract.getAllContents();

                final List<String> filteredList = new ArrayList<>();

                for (int i = 0; i < contentList.size(); i++) {
                    final String text = contentList.get(i).toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(contentList.get(i));
                    }
                }

                MyAdapter adapter = new MyAdapter(HomeActivity.this, filteredList, "content");
                rcView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                rcView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navViewImageAlteration();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
    }
}
