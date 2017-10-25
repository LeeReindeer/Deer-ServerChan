/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:40 PM
 */

package xyz.leezoom.deerserverchan.module

import com.google.gson.annotations.SerializedName

/**
 * @Author lee
 * *
 * @Time 10/24/17.
 */

class Status {

    @SerializedName("errno")
    var error: String? = null
    @SerializedName("errmsg")
    var errorMessage: String? = null
}
