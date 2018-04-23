package com.example.admin.keeper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqlDB.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "table_text";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CONTENT = "content";

    public static final int TYPE_ITEM_TEXT = 0;
    public static final int TYPE_ITEM_IMAGE = 1;

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLUMN_TITLE
                + " TEXT, " + COLUMN_TYPE + " INTEGER, "+ COLUMN_CONTENT
                + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE); // тут написано, что при обновлении версии
        // базы, надо удалить таблицу. Как бы на данном этапе ничего страшного этот код не
        // сработает, но зачем он вообще?
    }
}