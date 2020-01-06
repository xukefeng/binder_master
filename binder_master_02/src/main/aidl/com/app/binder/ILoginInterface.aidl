// ILoginInterface.aidl
package com.app.binder;

// Declare any non-default types here with import statements

interface ILoginInterface {
   //登录
   void login();
   //登录回调

   void loginCallback(boolean loginStatus,String loginUser);
}
