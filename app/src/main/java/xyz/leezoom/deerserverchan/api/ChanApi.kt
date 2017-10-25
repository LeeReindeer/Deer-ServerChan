/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:49 PM
 */

package xyz.leezoom.deerserverchan.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.leezoom.deerserverchan.module.Status

/**
 * @Author lee
 * *
 * @Time 10/24/17.
 */

interface ChanApi {

    @GET(".")
    fun sendToChan(@Query("text") title: String,
                   @Query("desp") content: String?): Observable<Status>
}
