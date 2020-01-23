package com.app.Fulwari;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.Fulwari.adapter.FlowerProductAdapter;
import com.app.Fulwari.adapter.MonthlyPacketAdapter;
import com.app.Fulwari.model.CustomPackData;
import com.app.Fulwari.model.PredefinedPackCategoryData;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonthlyCustompack extends AppCompatActivity {
    RecyclerView rv_recyclerview;
    TextView tv_notFound;
    ProgressDialog pDialog;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_custompack);
        rv_recyclerview=findViewById(R.id.rv_recyclerview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_recyclerview.setLayoutManager(layoutManager);
        tv_notFound=findViewById(R.id.not_found);
        context=this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        getPredefineCategoryData();
    }


    private void getPredefineCategoryData() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);

        Call<CustomPackData> call = redditAPI.getMonthlyCustomPacks( Preferences.get_userId(context), Preferences.get_UniqueId(context));
        call.enqueue(new Callback<CustomPackData>() {

            @Override
            public void onResponse(Call<CustomPackData> call, retrofit2.Response<CustomPackData> response) {
                Log.d("String", "" + response);
                pDialog.dismiss();
                CustomPackData customPackData=response.body();
                if(customPackData.getAck()==1){
                    tv_notFound.setVisibility(View.GONE);
                    rv_recyclerview.setVisibility(View.VISIBLE);
                    setDataToAdapter(customPackData);
                }
                else {
                    tv_notFound.setVisibility(View.VISIBLE);
                    rv_recyclerview.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<CustomPackData> call, Throwable t) {
                pDialog.dismiss();
                tv_notFound.setVisibility(View.VISIBLE);
                rv_recyclerview.setVisibility(View.GONE);
            }
        });
    }
  void setDataToAdapter(CustomPackData customPackData){
      MonthlyPacketAdapter ca = new MonthlyPacketAdapter(context, customPackData.getCustomPackDataModels());
      rv_recyclerview.setAdapter(ca);
  }
}
