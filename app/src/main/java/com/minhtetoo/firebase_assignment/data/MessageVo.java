package com.minhtetoo.firebase_assignment.data;

/**
 * Created by min on 2/1/2018.
 */

public class MessageVo {
    private String sendMessage;
    private String sendPicUrl;
    private String senderId;


    public MessageVo() {
    }

    public MessageVo(String sendMessage, String sendPicUrl, String senderId) {
        this.sendMessage = sendMessage;
        this.sendPicUrl = sendPicUrl;
        this.senderId = senderId;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public String getSendPicUrl() {
        return sendPicUrl;
    }

    public String getSenderId() {
        return senderId;
    }
}
