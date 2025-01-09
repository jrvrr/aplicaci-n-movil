package com.example.red;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.red.ui.Datos;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin1, etLogin2;
    private Button btnLogin, btnIrRegistro;
    private Datos dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Vincular vistas
        etLogin1 = findViewById(R.id.etLogin1);
        etLogin2 = findViewById(R.id.etLogin2);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        dbHelper = new Datos(this);

        btnLogin.setOnClickListener(this::iniciarSesion);
        btnIrRegistro.setOnClickListener(view -> irRegistro(view));
    }

    public void iniciarSesion(View view) {
        String correo = etLogin1.getText().toString().trim();
        String contra = etLogin2.getText().toString().trim();

        if (correo.isEmpty() || contra.isEmpty()) {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("usuarios", new String[]{"id", "correo", "contra", "Usuario"}, "correo = ? AND contra = ?", new String[]{correo, contra}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String nombre = cursor.getString(3);

            SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("numero_key", id);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "¡Error de los datos ingresados!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
    }

    public void irRegistro(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
