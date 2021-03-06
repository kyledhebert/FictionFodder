package com.kylehebert.fictionfodder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.kylehebert.fictionfodder.model.ImageNote;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.TextNote;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kylehebert on 11/18/15.
 * A CursorWrapper subclass for reading data from the Note database
 */
public class NoteCursorWrapper extends CursorWrapper {


    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public TextNote getTextNote() {
        String uuidString = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.UUID));
        long date = getLong(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.DATE));
        String type = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TYPE));
        String tag = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TAG));
        int isInTrash = getInt(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TRASH));
        String title = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TITLE));
        String body = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.BODY));

        TextNote textNote = new TextNote(UUID.fromString(uuidString));
        textNote.setDate(new Date(date));
        textNote.setType(type);
        textNote.setTag(tag);
        textNote.setIsInTrash(isInTrash != 0);
        textNote.setTitle(title);
        textNote.setNoteBody(body);

        return textNote;

    }
    public ImageNote getImageNote() {
        String uuidString = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.UUID));
        long date = getLong(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.DATE));
        String type = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TYPE));
        String tag = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TAG));
        int isInTrash = getInt(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TRASH));
        String caption = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.CAPTION));

        ImageNote imageNote = new ImageNote(UUID.fromString(uuidString));
        imageNote.setDate(new Date(date));
        imageNote.setType(type);
        imageNote.setTag(tag);
        imageNote.setIsInTrash(isInTrash != 0);
        imageNote.setCaption(caption);

        return imageNote;

    }

    public Note getNote(){

        String uuidString = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.UUID));
        long date = getLong(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.DATE));
        String type = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TYPE));
        String tag = getString(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TAG));
        int isInTrash = getInt(getColumnIndex(NoteDatabaseSchema.TrashTable.Columns.TRASH));

        Note note = new Note(UUID.fromString(uuidString));
        note.setDate(new Date(date));
        note.setType(type);
        note.setTag(tag);
        note.setIsInTrash(isInTrash != 0);

        return note;

    }

}
