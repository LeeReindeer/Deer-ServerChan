package xyz.leezoom.deerserverchan

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import xyz.leezoom.deerserverchan.module.Message
import xyz.leezoom.java.util.http.HttpConsumer
import xyz.leezoom.java.util.http.HttpObservable
import xyz.leezoom.java.util.http.Scheduler

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var networkInfo: NetworkInfo? = null

    private var mTitle: EditText? = null
    private var mContent: EditText? = null
    private var mKey: EditText? = null
    private var mSend: Button? = null
    private var actionButton: FloatingActionButton? = null
    private var data: SharedPreferences? = null
    private var KEY = ""

    private var httpConsumer: HttpConsumer = object : HttpConsumer {
        @Throws(Exception::class)
        override fun succeed(s: String) {
            runOnUiThread {
                Toast.makeText(applicationContext, "Succeed", Toast.LENGTH_SHORT).show()
                android.util.Log.d("success: ", s)
            }
        }

        @Throws(Exception::class)
        override fun failed(s: String) {
            runOnUiThread {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                android.util.Log.d("failed: ", s)
            }
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

    fun sendMessage(message: Message) {
        val observer = HttpObservable()
        observer.get("https://sc.ftqq.com/$KEY.send?text=${message.title}&desp=${message.content}")
                .setConnectTimeout(10000)
                .setReadTimeout(10000)
                .observeOn(Scheduler.Main_Thread)
                .subscribe(httpConsumer)
                .execute()
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
        val builder = AlertDialog.Builder(this)
        mKey = EditText(this)
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
                Toast.makeText(applicationContext, "Title is empty!", Toast.LENGTH_SHORT).show()
                return
            }
            val message = Message()
            message.title = title
            message.content = content
            //sendMsg(message)
            sendMessage(message)
        } else if (v.id == R.id.float_button) {
            //enter SCKEY
            buildSCKEYDialog().show()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: MainActivity? = null
            private set
    }

}
