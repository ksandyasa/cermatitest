package com.cermati.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.cermati.myapplication.adapters.MainListAdapter;
import com.cermati.myapplication.models.User;
import com.cermati.myapplication.presenters.MainPresenter;
import com.cermati.myapplication.views.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements MainView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.rvMain)
    RecyclerView rvMain;

    private final String TAG = MainActivity.class.getSimpleName();
    private LinearLayoutManager linearLayoutManager;
    private MainListAdapter rvAdapter;
    private MainPresenter mainPresenter;
    private List<User> users = new ArrayList<>();
    private String searchQuery = "";
    private SearchView searchView;
    private SearchManager searchManager;
    private MenuItem searchMenuItem;
    private int page = 1;
    private boolean isLoadMore = false;
    private boolean isMaxSize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        initViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
        }

        isLoadMore = false;
        isMaxSize = false;
        users.clear();
        page = 1;
        mainPresenter.loadGithubUsersData(page, 20, searchQuery.replace(" ", "%20"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.searchMenuItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) this.searchMenuItem.getActionView();
        this.searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        this.searchView.setSearchableInfo(this.searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        this.searchView.setOnQueryTextListener(new SearchQueryListener());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_search) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishedLoadGithubUsersData(List<User> userList) {
        if (userList.size() > 0) {
            this.users.addAll(userList);
            rvAdapter.notifyDataSetChanged();
        }else {
            Snackbar.make(rvMain, "No result found of " + searchQuery, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishedLoadMoreGithubUsersData(List<User> userList) {
        if (userList.size() > 0) {
            this.users.addAll(userList);
            rvAdapter.notifyDataSetChanged();
        }
        isLoadMore = false;
    }

    @Override
    public void failedLoadGithubUsersData(int status) {
        if (status == 500) {
            Snackbar.make(rvMain, "No Internet connection", Snackbar.LENGTH_SHORT).show();
        }else if (status == 401) {
            Snackbar.make(rvMain, "API has limit of 10 hits per minute.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failedLoadMoreGithubUsersData(int status) {
        if (status == 500) {
            Snackbar.make(rvMain, "No Internet connection", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(linearLayoutManager);

        rvAdapter = new MainListAdapter(MainActivity.this, this.users);
        rvMain.setAdapter(rvAdapter);
        rvMain.addOnScrollListener(new LoadMoreGithubUsersData());

        mainPresenter = new MainPresenter(MainActivity.this, this);
    }

    private class LoadMoreGithubUsersData extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int totalItemCount = linearLayoutManager.getItemCount();
            int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();

            if(lastVisibleItemCount == totalItemCount - 1 && totalItemCount!=0)
            {
                if(isLoadMore == false && isMaxSize == false)
                {
                    isLoadMore = true;
                    page++;
                    mainPresenter.loadMoreGithubUsersData(page, 20, searchQuery.replace(" ", "%20"));
                }
            }

        }
    }

    private class SearchQueryListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "searchQuery2 " + query);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d(TAG, "searchQuery2 " + newText);

            return false;
        }
    }

}
