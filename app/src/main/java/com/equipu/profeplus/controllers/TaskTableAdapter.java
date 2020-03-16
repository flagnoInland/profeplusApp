package com.equipu.profeplus.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Herbert Caller on 23/08/2016.
 */
public class TaskTableAdapter {

    public static final int TASK_FINISH_LESSON = 1;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_PENDING = 2;


    public static class TaskTableDBHelper extends SQLiteOpenHelper {

        private static final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + "Tasks" + " (" +
                "id" + " INTEGER PRIMARY KEY, " +
                "user" + " INT" + ", " +
                "lesson" + " INT" + ", " +
                "status" + " INT" + ", " +
                "data" + "STRING" +", " +
                "code" +  " INT" + " )";

        private static final String SQL_DELETE_TASK_TABLE = "DROP TABLE IF EXISTS " + "Tasks";

        String pathDB = "/data/data/com.equipu.profeplus/databases/profeplus.db";
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "profeplus.db";

        public TaskTableDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TASK_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TASK_TABLE);
            onCreate(db);
        }


        public void addTask(SQLiteDatabase db, String user, int lesson, String data, int code){
            ContentValues values = new ContentValues();
            values.put("t", user);
            values.put("user", user);
            values.put("lesson", lesson);
            values.put("status", 2);
            values.put("code", code);
            values.put("data", data);
            long isOK = db.insert("Tasks", null, values);
        }

        public void changeStatus(SQLiteDatabase db, int lessonid){
            ContentValues values = new ContentValues();
            values.put("status", 1);
            String whereClause = "lesson=?";
            String[] whereArgs = new String[]{String.valueOf(lessonid)};
            db.update("Tasks", values, whereClause, whereArgs);
        }


        public int[] getUnfinishedTasks(SQLiteDatabase db){
            String stat = "SELECT lesson FROM Tasks WHERE status=2";
            Cursor myQuery = db.rawQuery(stat, new String[]{});
            int[] tasks = new int[myQuery.getCount()];
            myQuery.moveToFirst();
            int i=0;
            if (myQuery.getCount() > 0) {
                tasks[i] = myQuery.getInt(myQuery.getColumnIndex("lesson"));
                myQuery.moveToNext();
                i++;
            }
            return tasks;
        }

    }

}
