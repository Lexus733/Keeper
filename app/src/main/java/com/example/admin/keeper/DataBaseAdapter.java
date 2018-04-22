package com.example.admin.keeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;



public class DataBaseAdapter {

    Context context;
    SQLiteDatabase sqLiteDatabase;
    TaskDatabaseHelper taskDatabaseHelper;

    public DataBaseAdapter(Context ctx)
    {
        this.context =ctx;
        taskDatabaseHelper =new TaskDatabaseHelper(context);
    }


    public DataBaseAdapter openDB()
    {
        try
        {
            sqLiteDatabase = taskDatabaseHelper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    public void close()
    {
        try
        {
            taskDatabaseHelper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public long add(String name, int type, String content)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(TaskDatabaseHelper.COLUMN_TITLE, name);
            cv.put(TaskDatabaseHelper.COLUMN_TYPE, type);
            cv.put(TaskDatabaseHelper.COLUMN_CONTENT, content);

            return sqLiteDatabase.insert(TaskDatabaseHelper.TABLE,TaskDatabaseHelper.COLUMN_ID,cv);

        }catch (SQLException e)
        {
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        }

        return 0;
    }


    public Cursor getAllTasks()
    {
        String[] columns={TaskDatabaseHelper.COLUMN_ID, TaskDatabaseHelper.COLUMN_TITLE,
                TaskDatabaseHelper.COLUMN_TYPE, TaskDatabaseHelper.COLUMN_CONTENT};

        return sqLiteDatabase.query(TaskDatabaseHelper.TABLE,columns,null,null,
                null, null,null);
    }

    public long UPdate(int id, String name, int type, String content)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(TaskDatabaseHelper.COLUMN_TITLE, name);
            cv.put(TaskDatabaseHelper.COLUMN_TYPE, type);
            cv.put(TaskDatabaseHelper.COLUMN_CONTENT, content);

            return sqLiteDatabase.update(TaskDatabaseHelper.TABLE, cv,
                    TaskDatabaseHelper.COLUMN_ID + " =?",
                    new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        }

        return 0;
    }

    public long Delete(int id)
    {
        try
        {
            return sqLiteDatabase.delete(TaskDatabaseHelper.TABLE,TaskDatabaseHelper.COLUMN_ID +
                    " =?",new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

}
