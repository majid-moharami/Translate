package com.example.translate.model;

import java.io.Serializable;
import java.util.UUID;

public class WordTranslate implements Serializable {
    private String mPersian;
    private String mEnglish;
    private String mFrance;
    private String mArabian;
    private UUID mUUID;

    public WordTranslate(String s) {
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getPersian() {
        return mPersian;
    }

    public void setPersian(String persian) {
        mPersian = persian;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public void setEnglish(String english) {
        mEnglish = english;
    }

    public String getFrance() {
        return mFrance;
    }

    public void setFrance(String france) {
        mFrance = france;
    }

    public String getArabian() {
        return mArabian;
    }

    public void setArabian(String arabian) {
        mArabian = arabian;
    }

    public WordTranslate() {
        mUUID= UUID.randomUUID();
    }

    public WordTranslate(String persian, String english, String france, String arabian ) {
        mPersian = persian;
        mEnglish = english;
        mFrance = france;
        mArabian = arabian;
        mUUID = UUID.randomUUID();
    }
    public WordTranslate(String persian, String english, String france, String arabian,UUID uuid ) {
        mPersian = persian;
        mEnglish = english;
        mFrance = france;
        mArabian = arabian;
        mUUID = uuid;
    }
}
