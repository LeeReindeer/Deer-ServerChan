/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 6:48 PM
 */

package xyz.leezoom.deerserverchan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.leezoom.deerserverchan.module.Message;

/**
 * @Author lee
 * @Time 10/24/17.
 */

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    for (Object pdu : pdus) {
                        msg = SmsMessage.createFromPdu((byte[]) pdu);
                        String body = msg.getMessageBody();
                        String address = msg.getOriginatingAddress();
                        //Log.d("SMS ", address + "\n" + body);
                        //System.out.println(address + "\n" + body);
                        Message message = new Message();
                        message.setTitle(address);
                        message.setContent(body);
                        //send message to server
                        MainActivity mainActivity = MainActivity.getInstance();
                        mainActivity.sendMsg(message);
                    }
                } catch (Exception e) {
                    Log.d("Exception: ", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
