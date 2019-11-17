package com.app.Fulwari;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.Fulwari.adapter.CategoryAdapter;
import com.app.Fulwari.adapter.SliderPagerAdapter;
import com.app.Fulwari.model.Banners;
import com.app.Fulwari.model.Category;
import com.app.Fulwari.model.ContactUsModel;
import com.app.Fulwari.model.MyCart;
import com.app.Fulwari.model.Notes;
import com.app.Fulwari.model.ZipCodemodel;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.CircularTextView;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    ImageView btn_menu;
    NavigationView navigationView;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    Category getCategory;
    Notes notes;
    Banners banners;
    RecyclerView rv_cat;
    ImageView iv_cart;
    CircularTextView tv_cartcount;
    View headerView;
    RelativeLayout rl_image;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private ImageView[] dots;
    int page_position = 0;
    List<String> list_text = new ArrayList<>();
    ZipCodemodel zipCodemodel;
    MyCart myCart;
    TextView tv_cartprice, tv_note;
    boolean doubleBackToExitPressedOnce = false;
    ContactUsModel aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inc);
        mContext = this;
        Fresco.initialize(mContext);
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        slider_image_list = new ArrayList<>();
        initView();

        if (cd.isConnected()) {
            parsejson();
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));
        if (!Preferences.isgenerateUniqueKey(mContext)) {
            generateUniqueId();
            Preferences.setUniqueKey(mContext, true);
        }

        LoadCartProduct();
    }

    private void initslider() {
        try {
            vp_slider = (ViewPager) findViewById(R.id.vp_slider);
            ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

            sliderPagerAdapter = new SliderPagerAdapter(Dashboard.this, slider_image_list);
            vp_slider.setAdapter(sliderPagerAdapter);
            sliderPagerAdapter.notifyDataSetChanged();

            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                public void run() {
                    if (page_position == slider_image_list.size()) {
                        page_position = 0;
                    } else {
                        page_position = page_position + 1;
                    }
                    vp_slider.setCurrentItem(page_position, true);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(int currentPage) {
        try {
            dots = new ImageView[slider_image_list.size()];
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(7, 0, 7, 0);
            ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new ImageView(this);
                dots[i].setBackgroundResource(R.drawable.dot);
                dots[i].setLayoutParams(lp);
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setBackgroundResource(R.drawable.dot_selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void generateUniqueId() {
        Random rand = new Random();
        String uniqueId = System.currentTimeMillis() + "" + rand.nextInt(500);
        Log.d("uniqueId", uniqueId);
        Preferences.set_UniqueId(mContext, uniqueId);
    }

    private void initView() {
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setVisibility(View.GONE);
        tv_cartprice = findViewById(R.id.tv_cartprice);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        tv_note = findViewById(R.id.tv_note);
        tv_note.setSelected(true);
        rl_image = findViewById(R.id.rl_image);

        if (!Preferences.getisVerified(mContext)) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_myprofile).setVisible(false);
            nav_Menu.findItem(R.id.nav_changepass).setVisible(false);
            nav_Menu.findItem(R.id.nav_myprofile).setVisible(false);
            nav_Menu.findItem(R.id.nav_myorders).setVisible(false);
        } else {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_myprofile).setVisible(true);
            nav_Menu.findItem(R.id.nav_changepass).setVisible(true);
            nav_Menu.findItem(R.id.nav_myprofile).setVisible(true);
            nav_Menu.findItem(R.id.nav_myorders).setVisible(true);
        }


        rv_cat = findViewById(R.id.rv_cat);
        rv_cat.setLayoutManager(new GridLayoutManager(mContext, 2));

        if (Preferences.getisVerified(mContext)) {
            headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView txt_name = (TextView) headerView.findViewById(R.id.tv_user_name);
            ImageView ivNavDrawer = (ImageView) findViewById(R.id.iv_product_photo);
            txt_name.setText(Preferences.get_firstName(mContext));
        } else {
            headerView = navigationView.inflateHeaderView(R.layout.nav_header_main_before_login);
            Button btn_login = headerView.findViewById(R.id.btn_login);
            Button btn_register = headerView.findViewById(R.id.btn_register);
            btn_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Preferences.set_checkClicked(mContext, "0");
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    fetchZipCode();
                }
            });

        }


        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);

        ImageView iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Utility.CallContactNo(mContext);
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Utility.openNavDrawer(id, mContext);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_menu) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }
    }

    private void parsejson() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Category> call = redditAPI.GeCategory();
        call.enqueue(new Callback<Category>() {

            @Override
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getCategory = response.body();
                    if (getCategory.getCategoryData().size() > 0) {
                        setuplistview();
                    }
                }
                getBannerData();
                //pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void getBannerData() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Banners> call = redditAPI.GetBanners();
        call.enqueue(new Callback<Banners>() {

            @Override
            public void onResponse(Call<Banners> call, retrofit2.Response<Banners> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    banners = response.body();

                    if (banners.getAck() == 1) {
                        if (banners.getBannerData().size() > 0) {
                            for (int i = 0; i < banners.getBannerData().size(); i++) {
                                slider_image_list.add(banners.getBannerData().get(i).getBannerPhoto());
                            }

                            initslider();
                            addBottomDots(0);
                        } else {
                            rl_image.setVisibility(View.GONE);
                        }
                    } else {
                        rl_image.setVisibility(View.GONE);
                    }

                }
                //getNotes();
                //off by sakil
            }

            @Override
            public void onFailure(Call<Banners> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void fetchContactDetails() {
        //pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ContactUsModel> call = redditAPI.GetContactUs();
        call.enqueue(new Callback<ContactUsModel>() {

            @Override
            public void onResponse(Call<ContactUsModel> call, retrofit2.Response<ContactUsModel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    aboutUs = response.body();
                    if (aboutUs != null) {
                        //Utility.showToastShort(mContext,aboutUs.getContactData().contactNo1);
                        Preferences.set_contactNo(mContext, aboutUs.getContactData().contactNo1);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ContactUsModel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void setuplistview() {
        CategoryAdapter ca = new CategoryAdapter(mContext, getCategory.getCategoryData());
        rv_cat.setAdapter(ca);
    }


    private void fetchZipCode() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ZipCodemodel> call = redditAPI.GetZipCodeList();
        call.enqueue(new Callback<ZipCodemodel>() {

            @Override
            public void onResponse(Call<ZipCodemodel> call, retrofit2.Response<ZipCodemodel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    zipCodemodel = response.body();
                    if (zipCodemodel.getAck() == 1) {
                        if (zipCodemodel.getZipData().size() > 0) {
                            list_text.clear();
                            for (int i = 0; i < zipCodemodel.getZipData().size(); i++) {
                                list_text.add(zipCodemodel.getZipData().get(i).getAvailableZipcode());
                            }
                            showCustomDialog();
                        }
                    } else {
                        Utility.showToastShort(mContext, zipCodemodel.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ZipCodemodel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        FlexboxLayout container = dialogView.findViewById(R.id.v_container);
        Button btn_proceed = dialogView.findViewById(R.id.btn_proceed);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);


        inflatelayout(container);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Preferences.set_checkClicked(mContext, "0");
                Intent i = new Intent(mContext, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void inflatelayout(FlexboxLayout container) {
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(5, 5, 5, 5);
        for (int i = 0; i < list_text.size(); i++) {
            final TextView tv = new TextView(getApplicationContext());
            tv.setText(list_text.get(i));
            tv.setHeight(70);
            tv.setTextSize(16.0f);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setBackground(getResources().getDrawable(R.drawable.rounded_corner_flex));
            tv.setId(i + 1);
            tv.setLayoutParams(buttonLayoutParams);
            tv.setTag(i);
            tv.setPadding(20, 10, 20, 10);
            container.addView(tv);
        }

    }

    public void LoadCartProduct() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<MyCart> call = redditAPI.GetMyCart(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<MyCart>() {

            @Override
            public void onResponse(Call<MyCart> call, retrofit2.Response<MyCart> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    myCart = response.body();
                    if (myCart.getAck().equals("1")) {
                        tv_cartprice.setVisibility(View.VISIBLE);
                        tv_cartprice.setText("Total Price " + "\u20B9" + " " + myCart.getPriceData().grand_total);
                    } else {
                        tv_cartprice.setVisibility(View.GONE);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyCart> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void getNotes() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Notes> call = redditAPI.GetNotes();
        call.enqueue(new Callback<Notes>() {

            @Override
            public void onResponse(Call<Notes> call, retrofit2.Response<Notes> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    notes = response.body();
                    if (notes.deliveryData.ack == 1) {
                        tv_note.setVisibility(View.VISIBLE);
                        tv_note.setText(notes.deliveryData.content);
                    } else {
                        tv_note.setVisibility(View.VISIBLE);//changes
                    }


                }
                fetchContactDetails();
                //getBannerData();
                // pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Notes> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utility.showToastShort(mContext, "Press again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
