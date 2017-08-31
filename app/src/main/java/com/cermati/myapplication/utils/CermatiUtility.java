package com.cermati.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cermati.myapplication.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 8/31/17.
 */

public class CermatiUtility {

    public static class JSONParseUtility {

        public static List<User> getUserListDataFromJSON(String json) throws JSONException {
            List<User> userList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    User user = new User();
                    user.setLogin(jsonArray.getJSONObject(i).getString("login"));
                    user.setId(jsonArray.getJSONObject(i).getInt("id"));
                    user.setAvatar_url(jsonArray.getJSONObject(i).getString("avatar_url"));
                    user.setGravatar_id(jsonArray.getJSONObject(i).getString("gravatar_id"));
                    user.setUrl(jsonArray.getJSONObject(i).getString("url"));
                    user.setHtml_url(jsonArray.getJSONObject(i).getString("html_url"));
                    user.setFollowers_url(jsonArray.getJSONObject(i).getString("followers_url"));
                    user.setFollowing_url(jsonArray.getJSONObject(i).getString("following_url"));
                    user.setGists_url(jsonArray.getJSONObject(i).getString("gists_url"));
                    user.setStarred_url(jsonArray.getJSONObject(i).getString("starred_url"));
                    user.setSubscriptions_url(jsonArray.getJSONObject(i).getString("subscriptions_url"));
                    user.setOrganizations_url(jsonArray.getJSONObject(i).getString("organizations_url"));
                    user.setRepos_url(jsonArray.getJSONObject(i).getString("repos_url"));
                    user.setEvents_url(jsonArray.getJSONObject(i).getString("events_url"));
                    user.setReceived_events_url(jsonArray.getJSONObject(i).getString("received_events_url"));
                    user.setType(jsonArray.getJSONObject(i).getString("type"));
                    user.setSite_admin(jsonArray.getJSONObject(i).getBoolean("site_admin"));
                    user.setScore(jsonArray.getJSONObject(i).getDouble("score"));
                    userList.add(user);
                }
            }

            return userList;
        }

    }

    public static class ConnectionUtility {

        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

    }

}
