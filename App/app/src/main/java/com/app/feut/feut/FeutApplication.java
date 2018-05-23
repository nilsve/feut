package com.app.feut.feut;

import android.app.Activity;
import android.app.Application;

import com.app.feut.feut.connection.Connection;

/**
 * Created by nils.van.eijk on 18-04-18.
 */

public class FeutApplication extends Application {
    private static Activity curActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(Connection.getInstance()).start();
    }

    public static Activity getCurrentActivity(){
        return curActivity;
    }
    public static void setCurrentActivity(Activity mCurrentActivity){
        curActivity = mCurrentActivity;
    }
}