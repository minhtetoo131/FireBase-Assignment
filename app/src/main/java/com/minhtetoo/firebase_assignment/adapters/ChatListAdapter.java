package com.minhtetoo.firebase_assignment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.data.MessageVo;
import com.minhtetoo.firebase_assignment.viewholders.ChatMessageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by min on 2/1/2018.
 */

public class ChatListAdapter extends BaseRecyclerAdapter<ChatMessageViewHolder,MessageVo> {
    private LayoutInflater mLayoutInflater;
    private List<MessageVo> messageVos;
    private String msenderId;

    public ChatListAdapter(Context context,String senderId) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
        messageVos = new ArrayList<>();
        msenderId = senderId;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.view_item_message,parent,false);
        ChatMessageViewHolder chatMessageViewHolder = new ChatMessageViewHolder(itemView,msenderId);
        return chatMessageViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {

        holder.setData(mData.get(position));
    }
}
