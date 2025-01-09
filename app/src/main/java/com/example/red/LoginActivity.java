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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.red.ui.Datos;

public class LoginActivity extends AppCompatActivity{

    private EditText etLogin1, etLogin2;
    private Button btnLogin;
    private Datos dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        etLogin1 = super.findViewById(R.id.etLogin1);
        etLogin2 = super.findViewById(R.id.etLogin2);
        btnLogin = super.findViewById(R.id.btnLogin);

        dbHelper = new Datos(super.getBaseContext());
        db = dbHelper.getWritableDatabase();
        setContentView(R.layout.login);
    }


    public void iniciarSesion(View view) {
        etLogin1 = super.findViewById(R.id.etLogin1);
        etLogin2 = super.findViewById(R.id.etLogin2);
        btnLogin = super.findViewById(R.id.btnLogin);

        String correo = etLogin1.getText().toString();
        String contra = etLogin2.getText().toString();

        if(correo == null || contra == null){
            Toast.makeText(LoginActivity.this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("usuarios", new String[]{"id", "correo", "contra", "Usuario"}, null, null, null, null, null);
            int i = 0;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String correoBd = cursor.getString(1);
                    String contrasenia = cursor.getString(2);
                    String nombre = cursor.getString(3);

                    if(contra.equals(contrasenia) && correo.equals(correoBd)){

                        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencias", MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        System.out.println(id);

                        editor.putInt("numero_key", id);

                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }else{

                        if(i < 1){
                            Toast.makeText(LoginActivity.this, "¡Error de los datos ingresados!", Toast.LENGTH_SHORT).show();
                            i++;
                        }
                    }

                    Log.d("Usuario", "ID: " + id + ", Nombre: " + nombre + ", correo: " + correoBd);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();


        }

    }


    public void irRegistro(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
