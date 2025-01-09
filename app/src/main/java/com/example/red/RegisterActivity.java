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
    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etUsuario = super.findViewById(R.id.etUser);
        etContrasenia = super.findViewById(R.id.etContrasenia);
        etCorreo = super.findViewById(R.id.etCorreo);
        btnRegistro = super.findViewById(R.id.btnRegistro);

        dbHelper = new Datos(super.getBaseContext());
        db = dbHelper.getWritableDatabase();
        setContentView(R.layout.registro);
    }

    public void registrar(View view) {

        etUsuario = super.findViewById(R.id.etUser);
        etContrasenia = super.findViewById(R.id.etContrasenia);
        etCorreo = super.findViewById(R.id.etCorreo);
        btnRegistro = super.findViewById(R.id.btnRegistro);

        String correo = etCorreo.getText().toString();
        String contra = etContrasenia.getText().toString();
        String user = etUsuario.getText().toString();

        if(correo == null || contra == null || user == null){
            Toast.makeText(RegisterActivity.this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{

            ContentValues values = new ContentValues();
            values.put("correo", correo);
            values.put("contra", contra);
            values.put("Usuario", user);
            values.put("Imagen", "null");
            db.insert("usuarios", null, values);
            db.close();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

}
