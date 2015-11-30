package com.kylehebert.fictionfodder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.kylehebert.fictionfodder.database.NoteDatabaseHelper;
import com.kylehebert.fictionfodder.database.NoteDatabaseSchema;
import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.TrashTable;
import com.kylehebert.fictionfodder.database.TrashNoteCursorWrapper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kylehebert on 11/30/15.
 * Provides the application with a view of the notes in the Trash
 * table of the database
 */

    /*
    There is a lot of code shared between this and NoteList
    TODO Look into ways of reusing CursorWrapper code for different tables
    */

public class TrashNoteList {

    private static TrashNoteList sTrashNoteList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TrashNoteList get (Context context) {
        if (sTrashNoteList == null) {
            sTrashNoteList = new TrashNoteList(context);
        }

        return sTrashNoteList;
    }

    private TrashNoteList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addNote(Note note) {

        ContentValues contentValues = getNoteValues(note);

        mDatabase.insert(TrashTable.NAME, null, contentValues);
    }

    public List<Note> getNotes() {
        List<Note> trashNoteList = new ArrayList<>();

        TrashNoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trashNoteList.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return trashNoteList;
    }

    public Note getNote(UUID id) {
        TrashNoteCursorWrapper cursor = queryNotes(
                TrashTable.Columns.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0 ) { //empty database
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();

        } finally {
            cursor.close();
        }
    }

    public TextNote getTextNote(UUID id) {
        TrashNoteCursorWrapper cursor = queryNotes(
                TrashTable.Columns.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTextNote();

        }finally {
            cursor.close();
        }
    }

    public void updateTextNote(TextNote textNote) {
        String uuidString = textNote.getId().toString();
        ContentValues contentValues = getTextNoteValues(textNote);

        mDatabase.update(TrashTable.NAME, contentValues, TrashTable.Columns.UUID + " = ?",
                new String[] {uuidString});

    }

    public ImageNote getImageNote(UUID id) {
        TrashNoteCursorWrapper cursor = queryNotes(
                TrashTable.Columns.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return  cursor.getImageNote();
        } finally {
            cursor.close();
        }
    }

    public void updateImageNote(ImageNote imageNote) {
        String uuidString = imageNote.getId().toString();
        ContentValues contentValues = getImageNoteValues(imageNote);

        mDatabase.update(TrashTable.NAME, contentValues,
                TrashTable.Columns.UUID + " = ?",
                new String []{uuidString}
        );
    }

    public File getPhotoFile(ImageNote imageNote) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, imageNote.getPhotoFilename());
    }

    public void permanentlyDeleteNote(Note note) {
        String uuidString = note.getId().toString();
        mDatabase.delete(TrashTable.NAME,
                TrashTable.Columns.UUID + " = ?",
                new String[] {uuidString}
        );
    }

    private TrashNoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TrashTable.NAME,
                null, //all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TrashNoteCursorWrapper(cursor);
    }

    private static ContentValues getNoteValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.UUID, note.getId().toString());
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.TYPE, note.getType());
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.TAG, note.getTag());
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.TRASH, note.isInTrash() ? 1 : 0);

        return contentValues;

    }

    private static ContentValues getTextNoteValues(TextNote textNote) {
        ContentValues contentValues = getNoteValues(textNote);
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.TITLE, textNote.getTitle());
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.BODY, textNote.getNoteBody());

        return contentValues;
    }

    private static ContentValues getImageNoteValues(ImageNote imageNote) {
        ContentValues contentValues = getNoteValues(imageNote);
        contentValues.put(NoteDatabaseSchema.TrashTable.Columns.CAPTION, imageNote.getCaption());

        return contentValues;
    }


}
