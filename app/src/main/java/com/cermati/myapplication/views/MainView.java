package com.cermati.myapplication.views;

import com.cermati.myapplication.models.User;

import java.util.List;

/**
 * Created by apridosandyasa on 8/31/17.
 */

public interface MainView {
    void finishedLoadGithubUsersData(List<User> userList);
    void finishedLoadMoreGithubUsersData(List<User> userList);
    void failedLoadGithubUsersData(int status);
    void failedLoadMoreGithubUsersData(int status);
}
