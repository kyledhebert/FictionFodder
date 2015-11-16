package com.kylehebert.fictionfodder.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kylehebert.fictionfodder.database.NoteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kylehebert on 11/12/15.
 * A singleton object used to store the list of Notes.
 */
public class NoteList {

    private static NoteList sNoteList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteList get (Context context) {
        if (sNoteList == null) {
            sNoteList = new NoteList(context);

        }
        return sNoteList;
    }

    private NoteList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteDatabaseHelper(mContext).getWritableDatabase();

    }

    public List<Note> getNotes() {
        return new ArrayList<>();
    }

    public Note getNote(UUID id) {
        return null;
    }


}
