package com.kylehebert.fictionfodder.model;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * Model object that represents and individual note.
 */
public class Note {

    private UUID mId;
    private String mTag;
    private boolean mIsInTrash;

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        this.mId = id;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public boolean isInTrash() {
        return mIsInTrash;
    }

    public void setIsInTrash(boolean isInTrash) {
        this.mIsInTrash = isInTrash;
    }
}
