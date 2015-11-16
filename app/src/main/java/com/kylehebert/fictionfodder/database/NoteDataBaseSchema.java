package com.kylehebert.fictionfodder.database;

/**
 * Created by kylehebert on 11/14/15.
 * Defines the schema for the Notes database
 */
public class NoteDataBaseSchema {

    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TYPE = "type";
            public static final String TITLE = "title";
            public static final String BODY = "body";
            public static final String TAG = "tag";
            public static final String IMG_LOCATION = "img_location";
            public static final String DATE = "date";
            public static final String TRASH = "trash";
        }
    }
}
