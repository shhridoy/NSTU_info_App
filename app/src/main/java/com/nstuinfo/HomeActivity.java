package com.nstuinfo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.CompoundButton;
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
import com.nstuinfo.mJsonUtils.ExtractJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mOtherUtils.ExtraUtils;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mRecyclerView.MyAdapter;
import com.nstuinfo.mViews.FontAppearance;

import org.w3c.dom.Text;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout navLL;

    private ConstraintLayout mConstraintLayout;

    private static final String URL = "https://jsonblob.com/api/6a1a5234-d30f-11e8-9c58-b1987dc5c254";

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;
    private ExtractJson jsonExtract;

    private int jsonVersion = 0;

    private PopupWindow mPopUpWindow;

    private String initialFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        setTheme();

        initialFont = Preferences.getFontAppearance(this);

        if (ReadWriteJson.readFile(this).equals("")) {
            if (isInternetOn()) {
                parseJson(true);
            } else {
                Toast.makeText(getApplicationContext(), "Check connection to load data for the first time!!", Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!initialFont.equals(Preferences.getFontAppearance(this))) {
                jsonExtract = new ExtractJson(this, ReadWriteJson.readFile(this));
                if (itemsList != null) {
                    itemsList.clear();
                }
                itemsList = jsonExtract.getMainItemsList();
                loadRecyclerView();
                initialFont = Preferences.getFontAppearance(this);
            }
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseJson(true);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        int rand = ExtraUtils.getRandomNumber(1,4);
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
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_4));
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
        }
    }

    private void loadRecyclerView() {
        myAdapter = new MyAdapter(this, itemsList, "main");
        mRecyclerView.setAdapter(myAdapter);
    }

    private void parseJson(final boolean pdVisibility) {

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
                            ReadWriteJson.saveFile(HomeActivity.this, response);
                            jsonExtract = new ExtractJson(HomeActivity.this, response);
                            if (itemsList != null) {
                                itemsList.clear();
                            }
                            itemsList = jsonExtract.getMainItemsList();
                            loadRecyclerView();
                        } else {
                            ReadWriteJson.saveFile(HomeActivity.this, response);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
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
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

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
                            Toast.makeText(getApplicationContext(), "Activating Light Theme...", Toast.LENGTH_LONG).show();
                        } else if (index == 1) {
                            Preferences.setDarkTheme(HomeActivity.this, true);
                            Toast.makeText(getApplicationContext(), "Activating Dark Theme...", Toast.LENGTH_LONG).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(getIntent());
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
