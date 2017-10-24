/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:40 PM
 */

package xyz.leezoom.deerserverchan.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author lee
 * @Time 10/24/17.
 */

public enum ApiHelper implements ApiProvider {

    CHANAPI {
        String k = "";
        @Override
        public ChanApi getChanApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://sc.ftqq.com/" + k + ".send/")
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(ChanApi.class);
        }

        @Override
        public void setKey(String key) {
            this.k = key;
        }
    },
}
