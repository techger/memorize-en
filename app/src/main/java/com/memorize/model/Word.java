package com.memorize.model;

/**
 * Created by Tortuvshin on 10/26/2015.
 */
public class Word {
    private String id;
    private String english;
    private String mongolia;

    public Word(String id, String english, String mongolia) {
        this.id = id;
        this.english = english;
        this.mongolia = mongolia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getMongolia() {
        return mongolia;
    }

    public void setMongolia(String mongolia) {
        this.mongolia = mongolia;
    }
}
