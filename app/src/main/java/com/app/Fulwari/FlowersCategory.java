package com.app.Fulwari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.app.Fulwari.utils.CircularTextView;

public class FlowersCategory extends AppCompatActivity  implements View.OnClickListener {
    CardView predefineCardView,customCardView;
    ImageView btn_menu, btn_back;
    Context mContext;
    ImageView iv_cart;
    ImageView iv_search;
    CircularTextView tv_cartcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers_category);
        mContext=this;
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        predefineCardView = findViewById(R.id.predefined_cardview);
        customCardView = findViewById(R.id.custom_cardView);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        customCardView.setOnClickListener(this);
        predefineCardView.setOnClickListener(this);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.GONE);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        tv_cartcount.setVisibility(View.GONE);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setVisibility(View.GONE);
        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }else if(v==customCardView){
            Intent intent=new Intent(mContext,FlowerListActivity.class);
            intent.putExtra("tag", 2);
            intent.putExtra("sub_category_id", "0");
            startActivity(intent);
        }else if(v==predefineCardView){
                Intent intent=new Intent(mContext,MonthlyCustompack.class);
            intent.putExtra("tag", 1);
            intent.putExtra("sub_category_id", "0");
            startActivity(intent);
        }
    }
}
