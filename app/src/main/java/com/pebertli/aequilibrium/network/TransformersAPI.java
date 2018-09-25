/*
 * *
 *  * Created by Pebertli Barata on 9/24/18 1:19 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 9/23/18 11:43 PM
 *
 */

package com.pebertli.aequilibrium.network;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pebertli.aequilibrium.model.TransformerListModel;
import com.pebertli.aequilibrium.model.TransformerModel;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Wrapper for REST requests
 */
public class TransformersAPI
{
    public interface ApiListener
    {
        void onCreateResponse(TransformerModel model, int code);
        void onDeleteResponse(TransformerModel model, int code);
        void onUpdateResponse(TransformerModel originalModel, TransformerModel newModel, int code);
        void onGetResponse(TransformerModel model, int code);
        void onGetResponse(List<TransformerModel> list, int code);
    }

    private TransformersEndpoint mService;
    private ApiListener mListener;
    private final String BASE_URL = "https://transformers-api.firebaseapp.com";

    public TransformersAPI()
    {
        Retrofit mRetrofit;
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                //interceptor to fill Auth Bearer
                .addInterceptor(new Interceptor()
                {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException
                    {
                        Request originalRequest = chain.request();

                        String token = Session.getInstance().getSessionStringValue("token");
                        Request.Builder builder = originalRequest.newBuilder();
                        if (token != null)
                            builder.header("Authorization", "Bearer " + token);

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                ;
        //enables TLS 1.2
        OkHttpClient client = enableTls12OnPreLollipop(okHttpClient).build();

        //Using gson for JSon parser
        Gson gson = new GsonBuilder().setLenient().create();
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mService = mRetrofit.create(TransformersEndpoint.class);
    }

    public void setApiListener(ApiListener listener)
    {
        mListener = listener;
    }

    public void getAll()
    {
            Call<TransformerListModel> call = mService.getAll();

        call.enqueue(new Callback<TransformerListModel>()
        {
            @Override
            public void onResponse(Call<TransformerListModel> call, Response<TransformerListModel> response)
            {
                if(mListener != null)
                    mListener.onGetResponse(response.body().getTransformers(), response.code());
            }

            @Override
            public void onFailure(Call<TransformerListModel> call, Throwable t)
            {
                if(mListener != null)
                     mListener.onGetResponse((List<TransformerModel>) null, 500);
            }
        });
    }

    public void addTransformer(final TransformerModel model)
    {
        if(!model.isValid() && mListener != null)
        {
                mListener.onCreateResponse(null, 500);
        }
        Call<TransformerModel> callPost = mService.createTransformer(model);

        callPost.enqueue(new Callback<TransformerModel>()
        {
            @Override
            public void onResponse(Call<TransformerModel> call, Response<TransformerModel> response)
            {
                if(mListener != null)
                    mListener.onCreateResponse(response.body(), response.code());
            }

            @Override
            public void onFailure(Call<TransformerModel> call, Throwable t)
            {
                if(mListener != null)
                    mListener.onCreateResponse(null, 500);
            }
        });
    }

    public void deleteTransformer(final TransformerModel model)
    {
        Call<ResponseBody> callDelete = mService.deleteTransformer(model.getId());

        callDelete.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if(mListener != null)
                    mListener.onDeleteResponse(model, response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                if(mListener != null)
                    mListener.onDeleteResponse(model, 500);
            }
        });
    }

public void updateTransformer(final TransformerModel originalModel, final TransformerModel newModel)
{
    if(newModel.isValid())
    {
        Call<ResponseBody> callUpdate = mService.updateTransFormer(newModel);

        callUpdate.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if(mListener != null)
                    mListener.onUpdateResponse(originalModel, newModel, response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                if(mListener != null)
                    mListener.onUpdateResponse(originalModel, null, 500);
            }
        });
    }
}

    /**
     * From https://github.com/pelias/pelias-android-sdk
     */
    private OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
              TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
          throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), trustManager);

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

}
