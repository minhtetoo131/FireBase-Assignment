package com.minhtetoo.firebase_assignment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.data.UserVO;
import com.minhtetoo.firebase_assignment.delegates.UserItemViewDelegate;
import com.minhtetoo.firebase_assignment.viewholders.UserListViewHolder;

import java.util.List;

/**
 * Created by min on 1/31/2018.
 */

public class UserListAdapter extends BaseRecyclerAdapter<UserListViewHolder,UserVO> {
    private LayoutInflater mLayoutInflater;

    UserItemViewDelegate mUserItemViewDelegate;

    public UserListAdapter(Context context, UserItemViewDelegate delegate) {
        super(context);

        mLayoutInflater = LayoutInflater.from(context);
        mUserItemViewDelegate = delegate;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.view_item_user_list,parent,false);
        UserListViewHolder userListViewHolder = new UserListViewHolder(itemView,mUserItemViewDelegate);

        return userListViewHolder;
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.setData(mData.get(position));

    }
}
