/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:40 PM
 */

package xyz.leezoom.deerserverchan.module;

import com.google.gson.annotations.SerializedName;

/**
 * @Author lee
 * @Time 10/24/17.
 */

public class Status {

    @SerializedName("errno")
    private String error;
    @SerializedName("errmsg")
    private String errorMessage;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
