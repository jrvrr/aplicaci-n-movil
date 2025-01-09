package com.example.red;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.red.ui.Datos;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasenia, etCorreo;
    private Button btnRegistro;
    private Datos dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Vincular vistas
        etUsuario = findViewById(R.id.etUser);
        etContrasenia = findViewById(R.id.etContrasenia);
        etCorreo = findViewById(R.id.etCorreo);
        btnRegistro = findViewById(R.id.btnRegistro);

        dbHelper = new Datos(this);

        btnRegistro.setOnClickListener(this::registrar);
    }

    public void registrar(View view) {
        String user = etUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contra = etContrasenia.getText().toString().trim();

        // Validaciones
        if (user.isEmpty() || correo.isEmpty() || contra.isEmpty()) {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!correo.contains("@")) {
            Toast.makeText(this, "El correo debe contener '@'", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contra.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insertar en la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Usuario", user);
        values.put("correo", correo);
        values.put("contra", contra);
        values.put("Imagen", "null");

        long newRowId = db.insert("usuarios", null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show();
        }
    }
}
