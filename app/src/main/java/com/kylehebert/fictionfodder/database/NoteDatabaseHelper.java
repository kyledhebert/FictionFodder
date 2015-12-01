package com.kylehebert.fictionfodder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.NoteTable;
import com.kylehebert.fictionfodder.database.NoteDatabaseSchema.TrashTable;

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
                "_id integer primary key autoincrement, " +
                NoteTable.Columns.UUID + ", " +
                NoteTable.Columns.DATE + ", " +
                NoteTable.Columns.TYPE + ", " +
                NoteTable.Columns.TAG + ", " +
                NoteTable.Columns.TRASH +", " +
                NoteTable.Columns.TITLE + ", " +
                NoteTable.Columns.BODY + ", " +
                NoteTable.Columns.IMG_LOCATION + ", " +
                NoteTable.Columns.CAPTION + ") ");

        database.execSQL("create table " + TrashTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TrashTable.Columns.UUID + ", " +
                TrashTable.Columns.DATE + ", " +
                TrashTable.Columns.TYPE + ", " +
                TrashTable.Columns.TAG + ", " +
                TrashTable.Columns.TRASH +", " +
                TrashTable.Columns.TITLE + ", " +
                TrashTable.Columns.BODY + ", " +
                TrashTable.Columns.IMG_LOCATION + ", " +
                TrashTable.Columns.CAPTION + ") ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }



}
