package com.memorize.model;

/**
 * Created by Tortuvshin on 11/15/2015.
 */
public class RememberWord {
    String rememberEnglish;
    String rememberType;
    String rememberMongolia;

    public RememberWord(String rEnglish, String rType, String rMongolia) {
        this.rememberEnglish = rEnglish;
        this.rememberType = rType;
        this.rememberMongolia = rMongolia;
    }

    public String getRememberEnglish() {
        return rememberEnglish;
    }

    public void setRememberEnglish(String rememberEnglish) {
        this.rememberEnglish = rememberEnglish;
    }

    public String getRememberType() {
        return rememberType;
    }

    public void setRememberType(String rememberType) {
        this.rememberType = rememberType;
    }

    public String getRememberMongolia() {
        return rememberMongolia;
    }

    public void setRememberMongolia(String rememberMongolia) {
        this.rememberMongolia = rememberMongolia;
    }
}
