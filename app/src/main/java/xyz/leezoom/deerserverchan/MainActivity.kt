package xyz.leezoom.deerserverchan

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import xyz.leezoom.deerserverchan.api.ApiHelper
import xyz.leezoom.deerserverchan.api.ChanApi
import xyz.leezoom.deerserverchan.module.Message
import xyz.leezoom.deerserverchan.module.Status

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var networkInfo: NetworkInfo? = null

    private var mTitle: EditText? = null
    private var mContent: EditText? = null
    private var mKey: EditText? = null
    private var mSend: Button? = null
    private var actionButton: FloatingActionButton? = null
    private var data: SharedPreferences? = null
    private var KEY = ""
    private var chanApi: ChanApi? = null

    internal var consumer: Consumer<Status> = Consumer { status ->
        if (status.error == "0") {
            Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        KEY = getSharedPreferences("data", Context.MODE_PRIVATE).getString("key", "")
    }

    fun sendMsg(message: Message) {
        //check key
        if (KEY == "") {
            Toast.makeText(this, "Key is empty!", Toast.LENGTH_SHORT).show()
            return
        }
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkInfo = cm.activeNetworkInfo
        if (networkInfo == null || !networkInfo!!.isConnectedOrConnecting) {
            Toast.makeText(this, "Check Network, failed to send", Toast.LENGTH_SHORT).show()
            return
        }
        //set key
        ApiHelper.CHANAPI.setKey(KEY)
        chanApi = ApiHelper.CHANAPI.getChanAPi()
        chanApi!!.sendToChan(message.title, message.content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        mTitle = findViewById(R.id.edit_title) as EditText
        mContent = findViewById(R.id.edit_content) as EditText
        mSend = findViewById(R.id.button_send) as Button
        actionButton = findViewById(R.id.float_button) as FloatingActionButton
        mSend!!.setOnClickListener(this)
        actionButton!!.setOnClickListener(this)
    }

    private fun buildSCKEYDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this@MainActivity)
        mKey = EditText(this@MainActivity)
        val dialog = builder
                .setTitle("SCKEY")
                .setView(mKey)
                .setCancelable(true)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    //save to SharedPreferences
                    val key: String = mKey?.text.toString();
                    if (key != "") {
                        data = getSharedPreferences("data", Context.MODE_PRIVATE)
                        KEY = key
                        val editor = data!!.edit()
                        editor.putString("key", key)
                        editor.apply()
                        Log.d("Main", KEY)
                        System.out.print(KEY)
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
        return dialog
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_send) {
            //send msg
            val title = mTitle!!.text.toString()
            val content = mContent!!.text.toString()
            //title can't be empty
            if (title == "") {
                Toast.makeText(this, "Title is empty!", Toast.LENGTH_SHORT).show()
                return
            }
            val message = Message()
            message.title = title
            message.content = content
            sendMsg(message)
        } else if (v.id == R.id.float_button) {
            //enter SCKEY
            buildSCKEYDialog().show()
        }
    }

    companion object {

        var instance: MainActivity? = null
            private set
    }


}
