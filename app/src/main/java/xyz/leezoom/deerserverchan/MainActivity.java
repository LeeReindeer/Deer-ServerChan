package xyz.leezoom.deerserverchan;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.leezoom.deerserverchan.api.ApiHelper;
import xyz.leezoom.deerserverchan.api.ChanApi;
import xyz.leezoom.deerserverchan.module.*;
import xyz.leezoom.deerserverchan.module.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static MainActivity instance;

    private EditText mTitle;
    private EditText mContent;
    private EditText mKey;
    private Button mSend;
    private FloatingActionButton actionButton;
    private SharedPreferences data;
    private String KEY = "";
    private ChanApi chanApi;
    private SmsListener smsListener;

    Consumer<Status> consumer = new Consumer<Status>() {
        @Override
        public void accept(Status status) throws Exception {
            if (status.getError().equals("0")) {
                Toast.makeText(MainActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        KEY = getSharedPreferences("data", MODE_PRIVATE).getString("key", "");
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void sendMsg(Message message) {
        //set key
        ApiHelper.CHANAPI.setKey(KEY);
        chanApi = ApiHelper.CHANAPI.getChanApi();
        chanApi.sendToChan(message.getTitle(), message.getContent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mTitle = (EditText) findViewById(R.id.edit_title);
        mContent = (EditText) findViewById(R.id.edit_content);
        mSend =(Button) findViewById(R.id.button_send);
        actionButton =(FloatingActionButton) findViewById(R.id.float_button);
        mSend.setOnClickListener(this);
        actionButton.setOnClickListener(this);
    }

    private AlertDialog buildSCKEYDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog dialog = builder
                .setTitle("SCKEY")
                .setView(mKey = new EditText(MainActivity.this))
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save to SharedPreferences
                        String key;
                        if (!(key = mKey.getText().toString()).isEmpty()) {
                            data = getSharedPreferences("data", MODE_PRIVATE);
                            KEY = key;
                            SharedPreferences.Editor editor = data.edit();
                            editor.putString("key", key);
                            editor.apply();
                            Log.d("Main", KEY);
                            System.out.println(KEY);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_send) {
            //send msg
            String title = mTitle.getText().toString();
            String content = mContent.getText().toString();
            //title can't be empty
            if (title.isEmpty()) {
                Toast.makeText(this, "Title is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (KEY.equals("")) {
                Toast.makeText(this, "Key is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            Message message = new Message();
            message.setTitle(title);
            message.setContent(content);
            sendMsg(message);
        } else if (v.getId() == R.id.float_button){
            //enter SCKEY
            buildSCKEYDialog().show();
        }
    }


}
