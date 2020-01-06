package com.app.binder.a.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.app.binder.ILoginInterface;


public class LoginService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new ILoginInterface.Stub() {
            @Override
            public void login() throws RemoteException {

            }

            @Override
            public void loginCallback(boolean loginStatus, String loginUser) throws RemoteException {
                //接受服务端的回调
                Log.e("xkf", "======LoginStatus: " + loginStatus + "======LoginUser: " + loginUser);
            }
        };
    }
}
