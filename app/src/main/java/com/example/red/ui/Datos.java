package com.example.red.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Datos extends SQLiteOpenHelper {

    public Datos(Context context) {
        super(context, "datos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT, contra TEXT, Usuario TEXT, Imagen TEXT)";
        db.execSQL(query);
        query = "CREATE TABLE publi (id INTEGER PRIMARY KEY AUTOINCREMENT, texto TEXT, imagen TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejar la actualización de la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

}
