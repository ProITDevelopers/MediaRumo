package com.mediarumo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiClient {

//    @POST("login")
//    Call<Usuario> login(@Body LoginRequest loginRequest);
//
//    @POST("register")
//    Call<User> register(@Body RegisterRequest registerRequest);
//
//    @GET("appConfig")
//    Call<AppConfig> getAppConfig();
//
//    @FormUrlEncoded
//    @POST("getChecksum")
//    Call<ChecksumResponse> getCheckSum(@FieldMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST("transactionStatus")
//    Call<Order> checkTransactionStatus(@Field("order_gateway_id") String orderId);
//
    @GET("/LojaOnline/listarprodutos")
    Call<List<Product>> getProducts();
//
//    @POST("prepareOrder")
//    Call<PrepareOrderResponse> prepareOrder(@Body PrepareOrderRequest request);
//
//    @GET("transactions")
//    Call<List<Transaction>> getTransactions();
}
