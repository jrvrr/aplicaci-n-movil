package com.example.red.ui.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.red.R;
import com.example.red.databinding.FragmentHomeBinding;
import com.example.red.ui.Datos;

public class HomeFragment extends Fragment {

    private Button btnActu, btnEdit;
    private EditText actCorreo, actUser, actContra;
    private Integer idUsuario;
    private Datos dbHelper;
    private SQLiteDatabase db;
    private ImageButton btnFoto;
    private Button btnTema;
    private TextView tvFotoLeyenda;

    private View vista;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vista = root;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        }

        btnTema = root.findViewById(R.id.btnTema);
        btnFoto = root.findViewById(R.id.btnFoto);
        tvFotoLeyenda = root.findViewById(R.id.tvFotoLeyenda);
        actContra = root.findViewById(R.id.actPassword);
        actCorreo = root.findViewById(R.id.actCorreo);
        actUser = root.findViewById(R.id.actUser);
        btnActu = root.findViewById(R.id.btnAct);
        btnEdit = root.findViewById(R.id.btnEdit);

        disableEditing();

        btnEdit.setOnClickListener(v -> enableEditing());

        btnActu.setOnClickListener(v -> actualizarDatos(root));
        btnFoto.setOnClickListener(v -> openGallery());

        // Cambiar tema
        boolean isNightMode = (getActivity().getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        btnTema.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
            getActivity().recreate();
        });

        dbHelper = new Datos(root.getContext());
        db = dbHelper.getWritableDatabase();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MiPreferencias", MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("numero_key", -1);
        cargarDatosUsuario();
        return root;
    }

    private void disableEditing() {
        actUser.setEnabled(false);
        actCorreo.setEnabled(false);
        actContra.setEnabled(false);
        btnActu.setEnabled(false);
    }

    private void enableEditing() {
        actUser.setEnabled(true);
        actCorreo.setEnabled(true);
        actContra.setEnabled(true);
        btnActu.setEnabled(true);
    }

    private void cargarDatosUsuario() {
        String query = "SELECT * FROM usuarios WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});
        if (cursor != null && cursor.moveToFirst()) {
            actCorreo.setText(cursor.getString(1));
            actContra.setText(cursor.getString(2));
            actUser.setText(cursor.getString(3));
            cursor.close();
        }
        db.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PICK_IMAGE_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            db = dbHelper.getWritableDatabase();
            String sql = "UPDATE usuarios SET Imagen = ? WHERE id = ?";
            db.execSQL(sql, new Object[]{imageUri, idUsuario});
            db.close();
            btnFoto.setImageURI(imageUri);
        }
    }

    public void actualizarDatos(View root) {
        String correo = actCorreo.getText().toString();
        String contra = actContra.getText().toString();
        String user = actUser.getText().toString();

        if (correo.isEmpty() || contra.isEmpty() || user.isEmpty()) {
            Toast.makeText(root.getContext(), "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            db = dbHelper.getWritableDatabase();
            String sql = "UPDATE usuarios SET correo = ?, contra = ?, Usuario = ? WHERE id = ?";
            db.execSQL(sql, new Object[]{correo, contra, user, idUsuario});
            db.close();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_home, new HomeFragment()).addToBackStack(null).commit();
        }
    }
}