package com.cermati.myapplication.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.cermati.myapplication.models.User;
import com.cermati.myapplication.services.NetworkConnection;
import com.cermati.myapplication.utils.CermatiUtility;
import com.cermati.myapplication.utils.ConstantAPI;
import com.cermati.myapplication.views.MainView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 8/31/17.
 */

public class MainPresenter {

    private final String TAG = MainPresenter.class.getSimpleName();
    private Context context;
    private Messenger messenger;
    private Message message;
    private Bundle bundle;
    private Handler handler;
    private String[] stringResponse = {""};
    private List<User> users = new ArrayList<>();
    private MainView mainView;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this.context, NetworkConnection.class);
        this.messenger = new Messenger(this.handler);
        networkIntent.putExtra("messenger", this.messenger);
        networkIntent.putExtra("url", url);
        this.context.startService(networkIntent);
    }

    public void loadGithubUsersData(int page, int per_page, String query) {
        if (CermatiUtility.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainGithubUsersData(page, per_page, query);
        }else{
            this.mainView.failedLoadGithubUsersData(500);
        }
    }

    public void loadMoreGithubUsersData(int page, int per_page, String query) {
        if (CermatiUtility.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainMoreGithubUsersData(page, per_page, query);
        }else{
            this.mainView.failedLoadMoreGithubUsersData(500);
        }
    }

    private void obtainGithubUsersData(int page, int per_page, String query) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseGithubUsersData(msg);
            }

        };

        doNetworkService(ConstantAPI.GET_USERS + "?q=" + query + "&page=" + page + "&per_page=" + per_page);
    }

    private void parseGithubUsersData(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.stringResponse[0] = this.bundle.getString("network_response");
        Log.d(TAG, "responseString[0] " + this.stringResponse[0]);
        try {
            this.users = CermatiUtility.JSONParseUtility.getUserListDataFromJSON(this.stringResponse[0]);
            this.mainView.finishedLoadGithubUsersData(this.users);
        } catch (JSONException e) {
            this.mainView.failedLoadGithubUsersData(401);
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

    private void obtainMoreGithubUsersData(int page, int per_page, String query) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseMoreGithubUsersData(msg);
            }

        };

        doNetworkService(ConstantAPI.GET_USERS + "?q=" + query + "&page=" + page + "&per_page=" + per_page);
    }

    private void parseMoreGithubUsersData(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.stringResponse[0] = this.bundle.getString("network_response");
        Log.d(TAG, "responseString[0] " + this.stringResponse[0]);
        try {
            this.users = CermatiUtility.JSONParseUtility.getUserListDataFromJSON(this.stringResponse[0]);
            this.mainView.finishedLoadMoreGithubUsersData(this.users);
        } catch (JSONException e) {
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

}
