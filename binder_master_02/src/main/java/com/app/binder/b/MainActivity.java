package com.app.binder.b;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.binder.ILoginInterface;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etPhone, etPwd;
    private ILoginInterface iLogin;//AIDL定义接口
    private boolean isStartRemote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPhone = findViewById(R.id.etPhone);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            login();
            }
        });
        binderService();
    }

    //双向绑定服务
    private void binderService() {
        Intent intent = new Intent();
        intent.setAction("Binder02_to_Binder01");
        intent.setPackage("com.app.binder.a");
        bindService(intent, conn, BIND_AUTO_CREATE);
        isStartRemote = true;
    }

    //连接服务
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iLogin = ILoginInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void login() {
        final String userPhone = etPhone.getText().toString();
        final String userPwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "用户名和密码为空", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("登录");
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean loginStatus = false;
                            if (userPhone.equals("123456") && userPwd.equals("123456")) {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                loginStatus = true;
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                                loginStatus = false;
                            }
                            progressDialog.dismiss();
                            //client，登录返回结果
                            iLogin.loginCallback(loginStatus, userPhone);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStartRemote) {
            unbindService(conn);
        }
    }
}
