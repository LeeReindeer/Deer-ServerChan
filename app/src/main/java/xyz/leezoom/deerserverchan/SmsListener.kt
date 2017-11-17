/*
 * Created by Lee.
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/24/17 6:48 PM
 */

package xyz.leezoom.deerserverchan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

import xyz.leezoom.deerserverchan.module.Message

/**
 * @Author lee
 * *
 * @Time 10/24/17.
 */

class SmsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            var msg: SmsMessage? = null
            if (bundle != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<Any>
                    for (pdu in pdus) {
                        msg = SmsMessage.createFromPdu(pdu as ByteArray)
                        val body = msg!!.messageBody
                        val address = msg.originatingAddress
                        //Log.d("SMS ", address + "\n" + body);
                        //System.out.println(address + "\n" + body);
                        val message = Message()
                        message.title = address
                        message.content = body
                        //send message to server
                        MainActivity.instance?.sendMessage(message)
                    }
                } catch (e: Exception) {
                    Log.d("Exception: ", e.message)
                    e.printStackTrace()
                }

            }
        }
    }
}
