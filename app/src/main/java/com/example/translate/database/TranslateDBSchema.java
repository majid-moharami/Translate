package com.example.translate.database;

public class TranslateDBSchema {
    public static final String NAME = "Translate.db";
    public static final int VERSION = 1;

    public static class WordTable{
        public static final String NAME = "wordTable";

        public static class COLS{
            public static final String PERSIAN = "persian";
            public static final String ENGLISH = "english";
            public static final String FRANCE = "france";
            public static final String ARABIAN = "arabian";
            public static final String UUID = "wordUUID";
            public static final String ID = "id";
        }
    }
}
