package com.kylehebert.fictionfodder.model;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * Model object that represents and individual note.
 */
public class Note {

    private UUID mId;
    private String mBodyText;
    private boolean isInTrash;

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        this.mId = id;
    }

    public String getBodyText() {
        return mBodyText;
    }

    public void setBodyText(String bodyText) {
        mBodyText = bodyText;
    }

    public boolean isInTrash() {
        return isInTrash;
    }

    public void setIsInTrash(boolean isInTrash) {
        this.isInTrash = isInTrash;
    }
}
