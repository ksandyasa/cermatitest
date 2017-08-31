package com.cermati.myapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by apridosandyasa on 6/19/16.
 */
public class NetworkConnection extends IntentService {

    private final String TAG = NetworkConnection.class.getSimpleName();
    private String[] responseString = {""};
    private Messenger messenger;
    private Message message;
    private String url;

    public NetworkConnection() {
        super("");
    }

    public void doObtainDataFromServer(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectionPool(new ConnectionPool(100,10000));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/vnd.github+json")
                .addHeader("Content-Encoding", "gzip")
                .addHeader("Connection", "Keep-Alive")
                .build();
        Log.d(TAG, url);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new NetworkConnectionCallback());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.messenger = (Messenger) intent.getParcelableExtra("messenger");
        this.url = intent.getStringExtra("url");
        this.message = Message.obtain();
        doObtainDataFromServer(this.url);
    }

    private class NetworkConnectionCallback implements Callback {

        @Override
        public void onFailure(Request request, IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, "failure " + e.getMessage());
            }
            onFailureInMainThread();
        }

        @Override
        public void onResponse(final Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    onResponseInMainThread(response);
                }else {
                    onFailureInMainThread();
                }
            } catch (IOException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    public void onFailureInMainThread() {
        Log.d(TAG, "failure connection");
        Bundle bundle = new Bundle();
        bundle.putString("network_response", this.responseString[0]);
        bundle.putString("network_failure", "yes");
        this.message.setData(bundle);
        try {
            this.messenger.send(this.message);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }

    public void onResponseInMainThread(Response response) throws IOException {
        this.responseString[0] = response.body().string();
        Log.d(TAG, "responseString[0] " + this.responseString[0]);
        Log.d(TAG, "message.what" + this.message.what);
        Bundle bundle = new Bundle();
        bundle.putString("network_response", this.responseString[0]);
        bundle.putString("network_failure", "no");
        this.message.setData(bundle);
        try {
            this.messenger.send(this.message);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }

}
