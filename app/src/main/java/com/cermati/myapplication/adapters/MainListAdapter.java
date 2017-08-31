package com.cermati.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cermati.myapplication.R;
import com.cermati.myapplication.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by apridosandyasa on 8/31/17.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder> {

    private Context context;
    private List<User> userList;

    public MainListAdapter(Context context, List<User> objects) {
        this.context = context;
        this.userList = objects;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        MainListViewHolder holder = new MainListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainListViewHolder holder, int position) {
        Picasso.with(this.context)
                .load(userList.get(position).getAvatar_url())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivProfile);

        holder.tvUsername.setText(userList.get(position).getLogin());
        holder.tvId.setText("" + userList.get(position).getId());
        holder.tvScore.setText("Score : " + userList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.rlContainer)
        RelativeLayout rlContainer;

        @InjectView(R.id.ivProfile)
        AppCompatImageView ivProfile;

        @InjectView(R.id.tvUsername)
        AppCompatTextView tvUsername;

        @InjectView(R.id.tvId)
        AppCompatTextView tvId;

        @InjectView(R.id.tvScore)
        AppCompatTextView tvScore;

        public MainListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }

}
