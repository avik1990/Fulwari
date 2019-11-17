package com.app.Fulwari;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.Fulwari.model.LoginResponse;
import com.app.Fulwari.model.ZipCodemodel;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    EditText et_phoneno;
    EditText et_password;
    Button btn_register, btn_login, btn_forgotpassword;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_phone, user_password;
    LoginResponse registration;
    ZipCodemodel zipCodemodel;
    List<String> list_text = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        et_phoneno = findViewById(R.id.et_phoneno);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        btn_forgotpassword = findViewById(R.id.btn_forgotpassword);

        btn_forgotpassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_login) {
            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Your Phone No.");
                return;
            }

            if (et_password.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Password");
                return;
            }

            user_phone = et_phoneno.getText().toString().trim();
            user_password = et_password.getText().toString().trim();

            verifyUser();

        } else if (v == btn_register) {
            fetchZipCode();
        } else if (v == btn_forgotpassword) {
            Intent i = new Intent(mContext, ForgotPasswordActivity.class);
            startActivity(i);
        }

    }

    private void verifyUser() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<LoginResponse> call = redditAPI.UserLogin(user_phone, user_password, Preferences.get_checkClicked(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    Log.d("Stringjwgerje", "" + registration);
                    if (registration.getAck() == 1) {
                        if (registration.getLoginData().size() > 0) {
                            Preferences.setisVerified(mContext, true);
                            Preferences.set_firstuserName(mContext, registration.getLoginData().get(0).getFname());
                            Preferences.set_lastName(mContext, registration.getLoginData().get(0).getLname());
                            Preferences.set_userEmail(mContext, registration.getLoginData().get(0).getEmail());
                            Preferences.set_userPhone(mContext, registration.getLoginData().get(0).getPhone());
                            Preferences.set_userId(mContext, registration.getLoginData().get(0).getUserId());
                            Preferences.set_address(mContext, registration.getLoginData().get(0).getAddress());
                            Preferences.set_city(mContext, registration.getLoginData().get(0).getCity());
                            Preferences.set_state(mContext, registration.getLoginData().get(0).getState());
                            Preferences.set_Zip(mContext, registration.getLoginData().get(0).getZip());
                            Intent intent = new Intent(mContext, Dashboard.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
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


}
