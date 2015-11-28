package com.kylehebert.fictionfodder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Environment;

import com.kylehebert.fictionfodder.database.NoteCursorWrapper;
import com.kylehebert.fictionfodder.database.NoteDatabaseHelper;
import com.kylehebert.fictionfodder.database.NoteDatabaseSchema;
import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.NoteTable;
import com.kylehebert.fictionfodder.utility.Constants;

import java.io.File;
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

    private final static String TYPE_IMAGE_NOTE = "imageNote";
    private final static String TYPE_TEXT_NOTE = "textNote";


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

    public void addNote(Note note) {
        ContentValues contentValues = getNoteValues(note);

        mDatabase.insert(NoteTable.NAME, null, contentValues);

    }

    public void addTextNote(TextNote textNote) {
        ContentValues contentValues = getTextNoteValues(textNote);

        mDatabase.insert(NoteTable.NAME, null, contentValues);

    }

    public void addImageNote(ImageNote imageNote) {
        ContentValues contentValues = getImageNoteValues(imageNote);

        mDatabase.insert(NoteTable.NAME, null, contentValues);
    }

    public List<Note> getNotes(int queryType) {
        List<Note> noteList = new ArrayList<>();
        NoteCursorWrapper cursor;

        switch (queryType) {
            case Constants.QUERY_ALL_NOTES:
                cursor = queryNotes(null, null);
                break;
            case Constants.QUERY_TEXT_NOTES:
                cursor = queryNotes(NoteTable.Columns.TYPE + " = ?", new String[] {Constants
                        .TYPE_TEXT_NOTE});
                break;
            case Constants.QUERY_IMAGE_NOTES:
                cursor = queryNotes(NoteTable.Columns.TYPE + " = ?", new String[] {Constants
                        .TYPE_IMAGE_NOTE});
                break;
//            case Constants.QUERY_TRASH_NOTES:
//                // there is no trash query at the moment
//                break;
            default:
                cursor = queryNotes(NoteTable.Columns.TYPE + " = ?", new String[] {Constants
                        .TYPE_TEXT_NOTE});

        }



        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                noteList.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return noteList;
    }



    public Note getNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Columns.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) { //empty database
                return null;
            }

            cursor.moveToFirst();
            return  cursor.getNote();
        } finally {
            cursor.close();
        }
    }

    public TextNote getTextNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Columns.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) { //empty database
                return null;
            }

            cursor.moveToFirst();
            return  cursor.getTextNote();
        } finally {
            cursor.close();
        }
    }

    public void updateTextNote(TextNote textNote) {
        String uuidString = textNote.getId().toString();
        ContentValues contentValues = getTextNoteValues(textNote);

        mDatabase.update(NoteTable.NAME, contentValues,
                NoteTable.Columns.UUID + " =?",
                new String[] {uuidString});
    }

    public void updateImageNote(ImageNote imageNote) {
        String uuidString = imageNote.getId().toString();
        ContentValues contentValues = getImageNoteValues(imageNote);

        mDatabase.update(NoteTable.NAME, contentValues,
                NoteTable.Columns.UUID + " =?",
                new String[] {uuidString});
    }

    public ImageNote getImageNote(UUID id) {
        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Columns.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) { //empty database
                return null;
            }

            cursor.moveToFirst();
            return  cursor.getImageNote();
        } finally {
            cursor.close();
        }
    }


    public File getPhotoFile(ImageNote imageNote) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, imageNote.getPhotoFilename());
    }

    public void deleteNote(Note note) {
        String uuidString = note.getId().toString();
        mDatabase.delete(NoteTable.NAME,
                NoteTable.Columns.UUID + " = ?",
                new String[] {uuidString});
    }

    public void updateNote(Note note) {
        ContentValues contentValues;
        String uuidString = note.getId().toString();
        //figure out what type of note is being updated
        if (note.getType().equals(TYPE_TEXT_NOTE)) {
            TextNote textNote = new TextNote(note.getId());
            contentValues = getTextNoteValues(textNote);
        } else {
            ImageNote imageNote = new ImageNote(note.getId());
            contentValues = getImageNoteValues(imageNote);
        }

        //update the row where UUID = uuidString
        mDatabase.update(NoteTable.NAME, contentValues,
                NoteTable.Columns.UUID + " = ?",
                new  String[] {uuidString});
    }

    private static ContentValues getNoteValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteTable.Columns.UUID, note.getId().toString());
        contentValues.put(NoteTable.Columns.TYPE, note.getType());
        contentValues.put(NoteTable.Columns.TAG, note.getTag());
        contentValues.put(NoteTable.Columns.TRASH, note.isInTrash() ? 1 : 0);

        return contentValues;

    }

    private static ContentValues getTextNoteValues(TextNote textNote) {
        ContentValues contentValues = getNoteValues(textNote);
        contentValues.put(NoteTable.Columns.TITLE, textNote.getTitle());
        contentValues.put(NoteTable.Columns.BODY, textNote.getNoteBody());

        return contentValues;
    }

    private static ContentValues getImageNoteValues(ImageNote imageNote) {
        ContentValues contentValues = getNoteValues(imageNote);
        contentValues.put(NoteTable.Columns.CAPTION, imageNote.getCaption());

        return contentValues;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, //select all Columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new NoteCursorWrapper(cursor);
    }


}
