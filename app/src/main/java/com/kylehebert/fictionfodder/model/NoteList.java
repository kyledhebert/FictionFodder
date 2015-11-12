package com.kylehebert.fictionfodder.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kylehebert on 11/12/15.
 * A singleton object used to store the list of Notes.
 */
public class NoteList {

    private static NoteList sNoteList;

    private List<Note> mNotes;

    public static NoteList get (Context context) {
        if (sNoteList == null) {
            sNoteList = new NoteList(context);

        }
        return sNoteList;
    }

    private NoteList(Context context) {
        mNotes = new ArrayList<>();
        //generate some test data for debug
        for (int i = 0; i < 100; i++) {
            Note note = new Note();
            note.setBodyText("This is a sample note");
            note.setIsInTrash(i % 2 == 0); //every other one
            mNotes.add(note);
        }

    }

    public List<Note> getNotes() {
        return mNotes;
    }

    public Note getNote(UUID id) {
        for (Note note : mNotes) {
            if (note.getId().equals(id)) {
                return note;
            }
        }
        return null;
    }


}
