package com.kylehebert.fictionfodder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.NoteTable;

/**
 * Created by kylehebert on 11/14/15.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "noteDatabase.db";

    public NoteDatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + NoteTable.NAME + "(" +
                NoteTable.Columns.UUID + ", " +
                NoteTable.Columns.TYPE + ", " +
                NoteTable.Columns.TITLE + ", " +
                NoteTable.Columns.BODY + ", " +
                NoteTable.Columns.TAG + ", " +
                NoteTable.Columns.IMG_LOCATION + ", " +
                NoteTable.Columns.DATE + ", " +
                NoteTable.Columns.TRASH + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }



}
