package com.app.Fulwari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.Fulwari.adapter.CustomPacketAdapter;

public class FlowerCustomDetails extends AppCompatActivity {
    CustomPacketAdapter customPacketAdapter;
    RecyclerView rv_customizeList;
    ImageView iv_back;
    TextView title;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_custom_details);
        position=getIntent().getIntExtra("position",-1);
        rv_customizeList=findViewById(R.id.rv_customizeList);
        iv_back=findViewById(R.id.btn_back);
        title=findViewById(R.id.et_name);
        rv_customizeList.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        title.setText(FlowerListActivity.productModel.getProductData().get(position).getProductNameEnglish());
    }

    private void setAdapter() {
        customPacketAdapter =new CustomPacketAdapter(this,FlowerListActivity.productModel.getProductData().get(position).getPackets());
        rv_customizeList.setAdapter(customPacketAdapter);
    }
}
