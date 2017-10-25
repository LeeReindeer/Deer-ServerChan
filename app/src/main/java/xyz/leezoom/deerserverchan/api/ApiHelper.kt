/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:40 PM
 */

package xyz.leezoom.deerserverchan.api

import android.util.Log

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author lee
 * *
 * @Time 10/24/17.
 */

enum class ApiHelper : ApiProvider {

    CHANAPI {

        var chanApi: ChanApi? = null

        override fun getChanAPi(): ChanApi {

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://sc.ftqq.com/$k.send/")
                    .client(OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            if (chanApi == null) {
                chanApi = retrofit.create<ChanApi>(ChanApi::class.java!!)
                Log.d("Api: ", chanApi!!.toString())
            }
            return chanApi!!
        }

        private var k = ""

        override fun setKey(key: String) {
            this.k = key
        }
    }
}
