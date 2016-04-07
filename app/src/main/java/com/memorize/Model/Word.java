package com.memorize.Model;

/**
 * Created by Byambaa on 10/26/2015.
 */
public class Word {
    private String english;
    private String type;
    private String mongolia;

    public Word(String english, String type, String mongolia) {
        this.english = english;
        this.type = type;
        this.mongolia = mongolia;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMongolia() {
        return mongolia;
    }

    public void setMongolia(String mongolia) {
        this.mongolia = mongolia;
    }
}
