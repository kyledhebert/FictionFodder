package com.kylehebert.fictionfodder.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * Model object that represents and individual note.
 */
public class Note {

    private UUID mId;
    private String mTag;
    private String mType;
    private boolean mIsInTrash;
    private Date mDate;


    public Note () {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isInTrash() {
        return mIsInTrash;
    }

    public void setIsInTrash(boolean isInTrash) {
        this.mIsInTrash = isInTrash;
    }
}
