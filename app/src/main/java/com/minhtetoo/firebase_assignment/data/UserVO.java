package com.minhtetoo.firebase_assignment.data;

import java.io.Serializable;

/**
 * Created by min on 1/31/2018.
 */

public class UserVO implements Serializable {
    public String profileImgUrl;
    public String name;
    public String message;
    public String uid;
    public String email;

    public UserVO() {
    }

    public UserVO(String profileImgUrl, String userName, String message, String localuid, String localemail) {
        this.profileImgUrl = profileImgUrl;
        this.name = userName;
        this.message = message;
        uid = localuid;
        email = localemail;

    }


}
