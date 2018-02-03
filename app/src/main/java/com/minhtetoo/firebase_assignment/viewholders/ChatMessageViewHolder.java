package com.minhtetoo.firebase_assignment.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.data.MessageVo;

import butterknife.BindView;


/**
 * Created by min on 2/1/2018.
 */

public class ChatMessageViewHolder extends BaseViewHolder<MessageVo> {
    @BindView(R.id.iv_recieve_image_message)ImageView ivRecieve;
    @BindView(R.id.iv_send_image_message)ImageView ivSend;
    @BindView(R.id.tv_receive_message)TextView tvRecieve;
    @BindView(R.id.tv_send_message)TextView tvSend;

    private String msenderId;

    public ChatMessageViewHolder(View itemView,String senderId) {
        super(itemView);

        msenderId = senderId;
    }

    @Override
    public void setData(MessageVo data) {
        populateWithView(data);


    }

    private void populateWithView(MessageVo data) {

        if (data.getSenderId().equals(msenderId)){
            ivRecieve.setVisibility(View.GONE);
            tvRecieve.setVisibility(View.GONE);
            if (data.getSendPicUrl() != null){
                Glide.with(ivSend.getContext())
                        .load(data.getSendPicUrl())
                        .into(ivSend);
                tvSend.setVisibility(View.GONE);
            }else{
                tvSend.setText(data.getSendMessage());
                ivSend.setVisibility(View.GONE);
            }
        }else{
            ivSend.setVisibility(View.GONE);
            tvSend.setVisibility(View.GONE);

            if (data.getSendPicUrl() !=null){
                Glide.with(ivRecieve.getContext())
                        .load(data.getSendPicUrl())
                        .into(ivRecieve);
                tvRecieve.setVisibility(View.GONE);
            }else{
                tvRecieve.setText(data.getSendMessage());
                ivRecieve.setVisibility(View.GONE);
            }
        }

        //TODO
    }

    @Override
    public void onClick(View view) {

    }
}
