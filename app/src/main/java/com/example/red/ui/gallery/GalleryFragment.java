package com.example.red.ui.gallery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.red.R;
import com.example.red.databinding.FragmentGalleryBinding;
import com.example.red.ui.Datos;

public class GalleryFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FragmentGalleryBinding binding;
    private EditText tvPubli;
    private TextView tvHoraPubli;
    private ImageView imgPreview;
    private Button btnPubli, btnPubliImg;
    private Uri imgUri; // Uri para almacenar la imagen seleccionada
    private Datos dbHelper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar elementos del diseño
        tvPubli = root.findViewById(R.id.tvPubli);
        tvHoraPubli = root.findViewById(R.id.tvHoraPubli);
        imgPreview = root.findViewById(R.id.imgPreview);
        btnPubli = root.findViewById(R.id.btnSubirPubli);
        btnPubliImg = root.findViewById(R.id.btnPubliImg);

        // Inicializar base de datos
        dbHelper = new Datos(root.getContext());
        db = dbHelper.getWritableDatabase();

        // Configurar eventos de los botones
        btnPubliImg.setOnClickListener(v -> selectImage());
        btnPubli.setOnClickListener(v -> {
            String texto = tvPubli.getText().toString();
            if (texto.isEmpty() || imgUri == null) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                subirPubli(texto, imgUri.toString());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para abrir la galería y seleccionar una imagen
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Manejo del resultado de la selección de la imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imgUri = data.getData();
            imgPreview.setImageURI(imgUri);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }

    // Método para subir una publicación a la base de datos
    private void subirPubli(String texto, String uri) {
        ContentValues values = new ContentValues();
        values.put("texto", texto);
        values.put("imagen", uri);

        long id = db.insert("publi", null, values);
        if (id != -1) {
            Toast.makeText(getContext(), "Publicación realizada", Toast.LENGTH_SHORT).show();
            tvPubli.setText("");
            imgPreview.setVisibility(View.GONE);

            // Obtener y mostrar la hora de la publicación
            Cursor cursor = db.rawQuery("SELECT hora FROM publi WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                String hora = cursor.getString(0);
                tvHoraPubli.setText("Publicado el: " + hora);
                tvHoraPubli.setVisibility(View.VISIBLE);
            }
            cursor.close();
        } else {
            Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
        }
    }
}
