/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 12:30 PM
 */

package xyz.leezoom.deerserverchan.api;

/**
 * @Author lee
 * @Time 10/24/17.
 */

public interface ApiProvider {
    ChanApi getChanApi();
    void setKey(String key);
}
