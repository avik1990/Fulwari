package com.app.Fulwari.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.Fulwari.MyOrderActivity;
import com.app.Fulwari.ProductOrderDetails;
import com.app.Fulwari.R;
import com.app.Fulwari.model.BaseResponse;
import com.app.Fulwari.model.MyOrders;
import com.app.Fulwari.model.ReviewResponse;
import com.app.Fulwari.retrofit.api.ApiServices;
import com.app.Fulwari.utils.ConnectionDetector;
import com.app.Fulwari.utils.Preferences;
import com.app.Fulwari.utils.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {
    private List<MyOrders.OrderDatum> moviesList;
    Context mContext;
    MyOrders.OrderDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    ConnectionDetector cd;
    TextView tv_date;
    ProgressDialog pDialog;
    BaseResponse baseResponse;
    ReviewResponse reviewResponse;
    String stComments;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_totalitems, tv_totalprice, tv_paymentstatus, tv_deliverystatus, tv_orderid;
        Button btnDetails, btnCancel, btnreview;

        public MyViewHolder(View view) {
            super(view);
            tv_date = view.findViewById(R.id.tv_date);
            tv_totalitems = view.findViewById(R.id.tv_totalitems);
            tv_totalprice = view.findViewById(R.id.tv_totalprice);
            tv_paymentstatus = view.findViewById(R.id.tv_paymentstatus);
            tv_deliverystatus = view.findViewById(R.id.tv_deliverystatus);
            tv_orderid = view.findViewById(R.id.tv_orderid);
            tv_date = view.findViewById(R.id.tv_date);
            btnDetails = view.findViewById(R.id.btnDetails);
            btnCancel = view.findViewById(R.id.btnCancel);
            btnreview = view.findViewById(R.id.btnreview);
        }
    }

    public MyOrdersAdapter(List<MyOrders.OrderDatum> moviesList, Context mContext) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        cd = new ConnectionDetector(mContext);

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tv_date.setText(Utility.getFormasssttedDate(movie.getOrderDate()));
        holder.tv_totalitems.setText("Total Items : " + movie.getTotalItems());
        holder.tv_totalprice.setText("Total Price : " + "\u20B9" + ". " + movie.getTotalPrice());
        holder.tv_paymentstatus.setText("Payment Status : " + movie.getPaymentStatus());
        holder.tv_deliverystatus.setText("Delivery Status : " + movie.getDeliveryStatus());
        holder.tv_orderid.setText("Order Id : " + movie.getOrderId());


        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductOrderDetails.class);
                i.putExtra("order_id", holder.tv_orderid.getText().toString().replace("Order Id :", "").trim());
                mContext.startActivity(i);
            }
        });

        if (movie.getCancel_request_sent().equalsIgnoreCase("No")) {
            holder.btnCancel.setEnabled(true);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setText(" Cancel ");
        } else if (movie.getCancel_request_sent().equalsIgnoreCase("Yes")) {
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setText(" Cancel Request Sent ");
            holder.btnCancel.setEnabled(false);
        }else{
            holder.btnCancel.setVisibility(View.GONE);
        }

        /*if (movie.getSuggestion_posted() == null) {
            holder.btnreview.setText("Review");
            postReview(holder);
        } else*/

        if (movie.getSuggestion_posted().equalsIgnoreCase("No")) {
            postReview(holder);
            holder.btnreview.setText("Post Review");
        } else if (movie.getSuggestion_posted().equalsIgnoreCase("Yes")) {
            holder.btnreview.setText(" View Review ");
            holder.btnreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewReview(holder, holder.tv_orderid.getText().toString().replace("Order Id :", "").trim());
                }
            });
        }


        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);
                LayoutInflater inflater = ((MyOrderActivity) mContext).getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.ordercancel_dialog, null);
                dialogBuilder.setView(dialogView);
                final EditText edt = dialogView.findViewById(R.id.edit1);
                dialogBuilder.setTitle("Cancel Reason");
                //dialogBuilder.setMessage("Enter text below");
                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        stComments = edt.getText().toString().trim();
                        if (stComments.isEmpty()) {
                            Toast.makeText(mContext, "Please Enter Comment", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cancelOrder(holder.tv_orderid.getText().toString().replace("Order Id :", "").trim(), stComments);
                    }
                });
                dialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
    }

    private void viewReview(final MyViewHolder holder, String orderId) {
        pDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<ReviewResponse> call = redditAPI.viewreviewOrder(Preferences.get_userId(mContext), orderId);
        call.enqueue(new Callback<ReviewResponse>() {

            @Override
            public void onResponse(Call<ReviewResponse> call, retrofit2.Response<ReviewResponse> response) {
                Log.d("ResponseOTP", "" + response);
                if (response.isSuccessful()) {
                    //Log.d("Response",response.body().toString());
                    reviewResponse = response.body();
                    if (reviewResponse.getReviewData().getAck() == 1) {
                        // Utility.showToastShort(mContext, baseResponse.getMsg());
                        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);
                        LayoutInflater inflater = ((MyOrderActivity) mContext).getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.orderviewreview_dialog, null);
                        dialogBuilder.setView(dialogView);
                        final TextView edt = dialogView.findViewById(R.id.edit1);
                        dialogBuilder.setTitle("Your Review");
                        edt.setText(reviewResponse.getReviewData().getComment());
                        //dialogBuilder.setMessage("Enter text below");
                        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        android.app.AlertDialog b = dialogBuilder.create();
                        b.show();
                    } else {
                        Utility.showToastShort(mContext, reviewResponse.getReviewData().getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void postReview(final MyViewHolder holder) {
        holder.btnreview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);
                LayoutInflater inflater = ((MyOrderActivity) mContext).getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.orderreview_dialog, null);
                dialogBuilder.setView(dialogView);
                final EditText edt = dialogView.findViewById(R.id.edit1);
                dialogBuilder.setTitle("Review Your Order");
                //dialogBuilder.setMessage("Enter text below");
                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        stComments = edt.getText().toString().trim();
                        if (stComments.isEmpty()) {
                            Toast.makeText(mContext, "Please Enter Review", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Toast.makeText(mContext, holder.tv_orderid.getText().toString().replace("Order Id :", "").trim(), Toast.LENGTH_SHORT).show();
                        ReviewOrder(holder.tv_orderid.getText().toString().replace("Order Id :", "").trim(), stComments);
                    }
                });
                dialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private void cancelOrder(String OrderID, String comments) {
        pDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<BaseResponse> call = redditAPI.cancelOrder(Preferences.get_userId(mContext), OrderID, comments);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d("ResponseOTP", "" + response);
                if (response.isSuccessful()) {
                    //Log.d("Response",response.body().toString());
                    baseResponse = response.body();
                    if (baseResponse.getAck() == 1) {
                        stComments = "";
                        Utility.showToastShort(mContext, baseResponse.getMsg());
                        ((MyOrderActivity) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastLong(mContext, baseResponse.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void ReviewOrder(String OrderID, String comments) {
        pDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<BaseResponse> call = redditAPI.reviewOrder(Preferences.get_userId(mContext), OrderID, comments);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d("ResponseOTP", "" + response);
                if (response.isSuccessful()) {
                    //Log.d("Response",response.body().toString());
                    baseResponse = response.body();
                    if (baseResponse.getAck() == 1) {
                        stComments = "";
                        Utility.showToastShort(mContext, baseResponse.getMsg());
                        ((MyOrderActivity) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastShort(mContext, baseResponse.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
