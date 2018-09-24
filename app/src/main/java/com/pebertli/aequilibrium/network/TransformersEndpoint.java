/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:38 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 10:58 PM
 *
 */

package com.pebertli.aequilibrium.network;

import com.pebertli.aequilibrium.model.TransformerListModel;
import com.pebertli.aequilibrium.model.TransformerModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TransformersEndpoint
{

    //@Headers({"Content-Type: application/json","Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0cmFuc2Zvcm1lcnNJZCI6Ii1MTWRSX29rbUsxNVVra2hhSU1xIiwiaWF0IjoxNTM3MjE5OTEwfQ.ESPnCSXKvdvF5V1d_KvWHiWoVnt4WHv0sWxfh3k1fSM"})
    @Headers("Content-Type: application/json")
    @GET("/transformers")
    Call<TransformerListModel> getAll();

    @POST("/transformers")
    Call<TransformerModel> createTransformer(@Body TransformerModel transformer);

    @GET("/allspark/")
    Call<String> getToken();

    @DELETE("transformers/{id}")
    Call<ResponseBody> deleteTransformer(@Path("id") String id);

    @PUT("/transformers")
    Call<ResponseBody> updateTransFormer(@Body TransformerModel transformer);


}
