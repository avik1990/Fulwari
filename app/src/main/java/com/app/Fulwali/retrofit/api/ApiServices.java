package com.app.Fulwali.retrofit.api;


import com.app.Fulwali.model.AboutUsmodel;
import com.app.Fulwali.model.AddToCart;
import com.app.Fulwali.model.Banners;
import com.app.Fulwali.model.BaseResponse;
import com.app.Fulwali.model.CartDeleteAction;
import com.app.Fulwali.model.Category;
import com.app.Fulwali.model.ContactUsModel;
import com.app.Fulwali.model.MyCart;
import com.app.Fulwali.model.MyOrders;
import com.app.Fulwali.model.MyOrdersDetailsModel;
import com.app.Fulwali.model.MyProfile;
import com.app.Fulwali.model.Notes;
import com.app.Fulwali.model.OrderCancellationModel;
import com.app.Fulwali.model.Privacymodel;
import com.app.Fulwali.model.ProductList;
import com.app.Fulwali.model.ProductModel;
import com.app.Fulwali.model.LoginResponse;
import com.app.Fulwali.model.RegistrationResponse;
import com.app.Fulwali.model.SubCategoryDataResponse;
import com.app.Fulwali.model.TermsConditionmodel;
import com.app.Fulwali.model.ZipCodeVerify;
import com.app.Fulwali.model.ZipCodemodel;
import com.app.Fulwali.utils.Preferences;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("service.php?action=login")
    Call<LoginResponse> UserLogin(@Query("phone") String phone, @Query("password") String password, @Query("flag") String flag, @Query("unique_id") String unique_id);

    @GET("service.php?action=otp_validation")
    Call<LoginResponse> VerifyOTP(@Query("otp") String otp, @Query("user_id") String user_id, @Query("unique_id") String unique_id);


    @GET("service.php?action=registration")
    Call<RegistrationResponse> UserRegistration(@Query("fname") String fname,
                                                @Query("lname") String lname,
                                                @Query("phone") String phone,
                                                @Query("email") String email,
                                                @Query("password") String password,
                                                @Query("confirm_password") String confirm_password,
                                                @Query("address") String address,
                                                @Query("state") String state,
                                                @Query("city") String city,
                                                @Query("zip") String zip,
                                                @Query("flag") String flag, @Query("unique_id") String unique_id);

    @GET("service.php?action=edit_profile")
    Call<BaseResponse> UserUpdateDetails(@Query("user_id") String user_id,
                                         @Query("fname") String fname,
                                         @Query("lname") String lname,
                                         @Query("phone") String phone,
                                         @Query("email") String email,
                                         @Query("address") String address,
                                         @Query("state") String state,
                                         @Query("city") String city,
                                         @Query("zip") String zip);

    @GET("service.php?action=change_password")
    Call<BaseResponse> ChangePassword(@Query("old_password") String old_password,
                                      @Query("new_password") String new_password,
                                      @Query("confirm_password") String confirm_password,
                                      @Query("user_id") String user_id);


    @GET("service.php?action=forgot_password")
    Call<BaseResponse> UserForgotPassword(@Query("email") String email);

    @GET("service.php?action=my_account")
    Call<MyProfile> GetMYProfile(@Query("user_id") String user_id);


    @GET("service.php?action=category")
    Call<Category> GeCategory();

    @GET("service.php?action=delivery_time")
    Call<Notes> GetNotes();

    @GET("service.php?action=banners")
    Call<Banners> GetBanners();

    @GET("service.php?action=subcategory")
    Call<SubCategoryDataResponse>   GeSubCategoryDataResponse(@Query("category_id") String category_id);

    @GET("service.php?action=product_list")
    Call<ProductList> GetProductListResponse(@Query("category_id") String category_id, @Query("subcategory_id") String subcategory_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id);

/*
    @GET("coSZpazTAO?indent=2")
    Call<ProductList> GetProductListResponse(@Query("category_id") String category_id, @Query("subcategory_id") String subcategory_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id);
*/

    @GET("service.php?action=zipcode_list")
    Call<ZipCodemodel> GetZipCodeList();

    @GET("service.php?action=pet_food_list")
    Call<ProductModel> Getpet_food_list(@Query("category_id") String category_id, @Query("user_id") String user_id);


    @GET("service.php?action=add_to_cart")
    Call<BaseResponse> AddtoCartService(@Query("user_id") String user_id,
                                        @Query("unique_id") String unique_id,
                                        @Query("product_id") String product_id,
                                        @Query("packet_id") String packet_id,
                                        @Query("quantity") String quantity, @Query("isCartAdd") String isCartAdd);


    @GET("service.php?action=about_us")
    Call<AboutUsmodel> GetAboutUS();

    @GET("service.php?action=our_contacts")
    Call<ContactUsModel> GetContactUs();

    @GET("service.php?action=order_cancellation")
    Call<OrderCancellationModel> Getorder_cancellation();

    @GET("service.php?action=replacement")
    Call<OrderCancellationModel> Getorder_Replacement();

    @GET("service.php?action=refund")
    Call<OrderCancellationModel> Getorder_Refund();

    @GET("service.php?action=terms")
    Call<TermsConditionmodel> GetTermData();

    @GET("service.php?action=privacy")
    Call<Privacymodel> GetPrivacyData();

    @GET("service.php?action=shipping")
    Call<Privacymodel> GetshippingData();


    @GET("service.php?action=zipcode_availability")
    Call<ZipCodeVerify> VerifyZipCode(@Query("zip") String zip);

    @GET("service.php?action=add_to_cart")
    Call<AddToCart> AddToCart(@Query("user_id") String user_id, @Query("product_id") String product_id, @Query("packet_id") String packet_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=view_cart")
    Call<MyCart> GetMyCart(@Query("user_id") String user_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=my_orders")
    Call<MyOrders> GetMyOrdes(@Query("user_id") String user_id);


    @GET("service.php?action=delete_item")
    Call<CartDeleteAction> GetCartDeleteAction(@Query("user_id") String user_id, @Query("cart_id") String cart_id, @Query("unique_id") String unique_id);

    //Preferences.get_userId(mContext), cart_id, Preferences.get_UniqueId(mContext), quantity
    @GET("service.php?action=update_item")
    Call<CartDeleteAction> UpdateMyCart(@Query("user_id") String user_id, @Query("cart_id") String cart_id, @Query("unique_id") String unique_id, @Query("quantity") String quantity, @Query("isCartAdd") String isCartAdd);


    @GET("service.php?action=checkout")
    Call<CartDeleteAction> PostShipping(@Query("fname") String fname, @Query("lname") String lname, @Query("email") String email, @Query("phone") String phone
            , @Query("address") String address
            , @Query("city") String city
            , @Query("state") String state
            , @Query("zip") String zip
            , @Query("user_id") String user_id
            , @Query("unique_id") String unique_id
            , @Query("quick_delivery") String quick_delivery);

    @GET("service.php?action=order_thankyou")
    Call<CartDeleteAction> GetCartThankyouMessage(@Query("user_id") String user_id, @Query("unique_id") String unique_id);


    @GET("service.php?action=post_feedback")
    Call<CartDeleteAction> PostFeedback(@Query("user_id") String user_id, @Query("name") String name, @Query("email") String email, @Query("phone") String phone
            , @Query("comment") String comment);


    @GET("service.php?action=search_result")
    Call<ProductList> Getsearch_food_list(@Query("category_id") String category_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id, @Query("search_string") String search_string);


    @GET("service.php?action=search_result")
    Call<ProductList> Getsearch_food_list1(@Query("category_id") String category_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id, @Query("subcategory_id") String subcat_id, @Query("search_string") String search_string);


    @GET("service.php?action=order_details")
    Call<MyOrdersDetailsModel> GetOrderedProductDetails(@Query("user_id") String user_id, @Query("order_id") String order_id);

    @GET("service.php?action=cancel_order")
    Call<BaseResponse> cancelOrder(@Query("user_id") String user_id, @Query("order_id") String order_id);


}
