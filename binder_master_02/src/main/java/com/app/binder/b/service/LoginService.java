package com.app.binder.b.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.app.binder.ILoginInterface;
import com.app.binder.b.MainActivity;

public class LoginService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new ILoginInterface.Stub() {
            @Override
            public void login() throws RemoteException {
                Log.e("xkf", "======BinderB_Start_Callback");
                serviceStartActivity();
            }

            @Override
            public void loginCallback(boolean loginStatus, String loginUser) throws RemoteException {

            }
        };
    }

    //启动界面
    private void serviceStartActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
