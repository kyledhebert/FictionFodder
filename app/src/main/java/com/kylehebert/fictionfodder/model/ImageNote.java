package com.kylehebert.fictionfodder.model;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by kylehebert on 11/12/15.
 * A note object that holds a single image
 */
public class ImageNote extends Note {

    private String mCaption;

    public ImageNote(UUID uuid) {
        super(uuid);
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
