package com.example.red.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Datos extends SQLiteOpenHelper {

    public Datos(Context context) {
        super(context, "datos.db", null, 2);  // Cambié la versión de la base de datos para reflejar el cambio
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String query = "CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT, contra TEXT, Usuario TEXT, Imagen TEXT)";
        db.execSQL(query);

        // Crear tabla de publicaciones con el nuevo campo 'hora'
        query = "CREATE TABLE publi (id INTEGER PRIMARY KEY AUTOINCREMENT, texto TEXT, imagen TEXT, hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verifica si la versión de la base de datos ha cambiado
        if (oldVersion < 2) {
            // Agregar el campo 'hora' a la tabla 'publi' si la versión es anterior a la 2
            String query = "ALTER TABLE publi ADD COLUMN hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
            db.execSQL(query);
        }
    }
}
