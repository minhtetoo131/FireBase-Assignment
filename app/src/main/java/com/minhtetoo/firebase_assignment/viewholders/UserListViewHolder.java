package com.minhtetoo.firebase_assignment.viewholders;

import android.view.View;
import android.widget.TextView;

import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.data.UserVO;
import com.minhtetoo.firebase_assignment.delegates.UserItemViewDelegate;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by min on 1/31/2018.
 */

public class UserListViewHolder extends BaseViewHolder<UserVO> {
    View mItemView;
//    CircleImageView mCircleImageView;
    TextView tvUserName;
    TextView tvMessage;
    UserItemViewDelegate mUserItemViewDelegate;
    UserVO currentVO;


    public UserListViewHolder(View itemView, UserItemViewDelegate delegate) {
        super(itemView);
        mItemView = itemView;
//        mCircleImageView = mItemView.findViewById(R.id.profile_image);
        tvUserName = mItemView.findViewById(R.id.tv_user_name);
        tvMessage = mItemView.findViewById(R.id.tv_message);
        mUserItemViewDelegate = delegate;


    }

    @Override
    public void setData(UserVO data) {
        tvUserName.setText(data.name);
        currentVO = data;


    }

    @Override
    public void onClick(View v) {
        mUserItemViewDelegate.onTapItemView(currentVO);


    }
}
