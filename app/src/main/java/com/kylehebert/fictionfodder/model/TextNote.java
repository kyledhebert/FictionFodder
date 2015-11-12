package com.kylehebert.fictionfodder.model;

/**
 * Created by kylehebert on 11/12/15.
 * A model object that represents a plain text note.
 */
public class TextNote extends Note {

    private String mTitle;
    private String mNoteBody;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNoteBody() {
        return mNoteBody;
    }

    public void setNoteBody(String noteBody) {
        mNoteBody = noteBody;
    }
}
