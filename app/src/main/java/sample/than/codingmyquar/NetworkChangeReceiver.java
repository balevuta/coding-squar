package sample.than.codingmyquar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context pContext, Intent pIntent) {
        final ConnectivityManager connMgr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // Do something
            EventBus.getDefault().post(new EventResumeDownload("Resume"));
            Log.d("Network Available ", "Flag No 1");
        }
    }

}
