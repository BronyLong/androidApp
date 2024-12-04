package com.example.laba1;
import android.app.Application;
import android.provider.ContactsContract;

public class Laba1Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.createInstance(this);
    }
}
