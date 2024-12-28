package com.example.projetfinal.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projetfinal.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }


    public void insertTask(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", task.getTask());
        values.put("status", task.getStatus());
        db.insert(TODO_TABLE, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TODO_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                ToDoModel task = new ToDoModel();
                task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                task.setTask(cursor.getString(cursor.getColumnIndex("task")));
                task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return taskList;
    }


    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void updateTask(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();  // Ouvre la base de données en mode écriture

        ContentValues values = new ContentValues();
        values.put(TASK, task.getTask());  // Met à jour le texte de la tâche
        values.put(STATUS, task.getStatus());  // Met à jour le statut de la tâche

        // Met à jour la ligne dans la table ToDo en fonction de l'ID de la tâche
        db.update(TODO_TABLE, values, ID + " = ?", new String[]{String.valueOf(task.getId())});

        db.close();  // Ferme la base de données après l'opération
    }


   /* public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }*/

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
