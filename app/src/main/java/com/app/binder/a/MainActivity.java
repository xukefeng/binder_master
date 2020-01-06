package com.app.binder.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.app.binder.ILoginInterface;


public class MainActivity extends AppCompatActivity {
    private boolean isStartRemote;
    private ILoginInterface iLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定服务
        binderService();
    }

    public void login(View view) {
        if (iLogin != null) {
            try {
                //调用Service提供的登录
                iLogin.login();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "请先安装应用", Toast.LENGTH_LONG).show();
        }
    }

    private void binderService() {
        Intent intent = new Intent();
        //设置Service应用Action（服务的唯一标识）
        intent.setAction("Binder01_to_Binder02");
        //设置Service的包名
        intent.setPackage("com.app.binder.b");
        //开启绑定服务
        bindService(intent, conn, BIND_AUTO_CREATE);
        //标识跨进程绑定
        isStartRemote = true;
    }

    //连接服务
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接成功（使用Service提供的方法）
            iLogin = ILoginInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //没有连接成功
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务，不然会出现服务连接资源异常
        if (isStartRemote) {
            unbindService(conn);
        }
    }
}
