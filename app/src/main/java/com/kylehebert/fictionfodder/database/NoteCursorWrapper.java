package com.kylehebert.fictionfodder.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.NoteTable;
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

    private final static String TYPE_IMAGE_NOTE = "imageNote";
    private final static String TYPE_TEXT_NOTE = "textNote";


    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public TextNote getTextNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Columns.UUID));
        long date = getLong(getColumnIndex(NoteTable.Columns.DATE));
        String type = getString(getColumnIndex(NoteTable.Columns.TYPE));
        String tag = getString(getColumnIndex(NoteTable.Columns.TAG));
        int isInTrash = getInt(getColumnIndex(NoteTable.Columns.TRASH));
        String title = getString(getColumnIndex(NoteTable.Columns.TITLE));
        String body = getString(getColumnIndex(NoteTable.Columns.BODY));

        TextNote textNote = new TextNote(UUID.fromString(uuidString));
        textNote.setDate(new Date(date));
        textNote.setType(type);
        textNote.setTag(tag);
        textNote.setIsInTrash(isInTrash != 0);
        textNote.setTitle(title);
        textNote.setNoteBody(body);

        return textNote;

    }

    public Note getNote(){

        String uuidString = getString(getColumnIndex(NoteTable.Columns.UUID));
        long date = getLong(getColumnIndex(NoteTable.Columns.DATE));
        String type = getString(getColumnIndex(NoteTable.Columns.TYPE));
        String tag = getString(getColumnIndex(NoteTable.Columns.TAG));
        int isInTrash = getInt(getColumnIndex(NoteTable.Columns.TRASH));
        String title = getString(getColumnIndex(NoteTable.Columns.TITLE));
        String body = getString(getColumnIndex(NoteTable.Columns.BODY));
        String imageLocation = getString(getColumnIndex(NoteTable.Columns.IMG_LOCATION));
        String caption = getString(getColumnIndex(NoteTable.Columns.CAPTION));

        Note note = new Note(UUID.fromString(uuidString));
        note.setDate(new Date(date));
        note.setType(type);
        note.setTag(tag);
        note.setIsInTrash(isInTrash != 0);



        //before adding remaining note details determine the type of note
        if (type.equals(TYPE_TEXT_NOTE)) {
            TextNote textNote = new TextNote(note.getId());
            textNote.setTitle(title);
            textNote.setNoteBody(body);
            return textNote;
        } else {
            ImageNote imageNote = new ImageNote(note.getId());
            imageNote.setUri(Uri.parse(imageLocation));
            imageNote.setCaption(caption);
            return imageNote;
        }


    }

}
