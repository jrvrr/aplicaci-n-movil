package com.example.red.ui.home;

import static android.app.Activity.RESULT_OK;
import static android.app.ProgressDialog.show;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import static androidx.core.app.ActivityCompat.recreate;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.red.MainActivity;
import android.Manifest;
import com.example.red.R;
import com.example.red.RegisterActivity;
import com.example.red.databinding.FragmentHomeBinding;
import com.example.red.ui.Datos;

public class HomeFragment extends Fragment {

    private Button btnActu;
    private EditText actCorreo, actUser, actContra;
    private Integer idUsuario;
    private Datos dbHelper;
    private SQLiteDatabase db;
    private ImageButton btnFoto;
    private Button btnTema;

    private View vista;

    private static final int PICK_IMAGE_REQUEST = 1;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vista = root;

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity() ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        }

        btnTema = root.findViewById(R.id.btnTema);

        // Detectar el estado actual del tema
        boolean isNightMode = (getActivity().getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;

        // Configurar el botón para cambiar el tema
        btnTema.setOnClickListener(v -> {
            // Cambiar entre temas claro y oscuro
            if (isNightMode) {
                // Activar el tema claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                // Activar el tema oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            // Reiniciar la actividad para aplicar el nuevo tema
            getActivity().recreate();
        });



        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        btnActu = root.findViewById(R.id.btnAct);
        actContra = root.findViewById(R.id.actPassword);
        actCorreo = root.findViewById(R.id.actCorreo);
        actUser = root.findViewById(R.id.actUser);

        btnActu.setOnClickListener(v -> {
            actualizarDatos(root);
        });

        dbHelper = new Datos(root.getContext());
        db = dbHelper.getWritableDatabase();

        btnFoto = root.findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    openGallery();
            }
        });

        SharedPreferences sharedPreferences =  requireActivity().getSharedPreferences("MiPreferencias", MODE_PRIVATE);


        idUsuario = sharedPreferences.getInt("numero_key", -1); // -1 es el valor por defecto si no se encuentra el número
        System.out.println(idUsuario);

        String query = "SELECT * FROM " + "usuarios" + " WHERE " + "id" + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});


        if (cursor != null && cursor.moveToFirst()) {
            String correoBd = cursor.getString(1);
            String contrasenia = cursor.getString(2);
            String nombre = cursor.getString(3);
            String imagen = cursor.getString(4);
            if(imagen.equals("null")){

            }else{
                System.out.println(imagen);
                //btnFoto.setImageURI(Uri.parse(imagen));
            }

            actCorreo.setText(correoBd.toString());
            actContra.setText(contrasenia.toString());
            actUser.setText(nombre.toString());


            cursor.close();
        }

        db.close();





        return root;
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData(); // Obtener la URI de la imagen seleccionada
            //ImageView imageView = vista.findViewById(R.id.);

            db = dbHelper.getWritableDatabase();

            String sql = "UPDATE " + "usuarios" + " SET "
                    + "Imagen" + " = ? "
                    + "WHERE " + "id" + " = ?";

            // Ejecuta el SQL
            db.execSQL(sql, new Object[]{imageUri, idUsuario});
            db.close();

            btnFoto.setImageURI(imageUri); // Mostrar la imagen en el ImageView

        }
    }

    public void actualizarDatos(View root) {
        //actContra = root.findViewById(R.id.actPassword);
        //actCorreo = root.findViewById(R.id.actCorreo);
        //actUser = root.findViewById(R.id.actUser);

        String correo = actCorreo.getText().toString();
        String contra = actContra.getText().toString();
        String user = actUser.getText().toString();

        if(correo == null || contra == null || user == null){
            Toast.makeText(root.getContext(), "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{

            db = dbHelper.getWritableDatabase();

            String sql = "UPDATE " + "usuarios" + " SET "
                    + "correo" + " = ?, "
                    + "contra" + " = ?, "
                    + "Usuario" + " = ? "
                    + "WHERE " + "id" + " = ?";

            // Ejecuta el SQL
            db.execSQL(sql, new Object[]{correo, contra, user, idUsuario});
            db.close();



            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("MI_FRAGMENTO");
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            if (fragment != null) {
                transaction.remove(fragment);  // Eliminar el fragmento actual
            }

            transaction.replace(R.id.nav_home, new HomeFragment(), "MI_FRAGMENTO");
            transaction.addToBackStack(null);  // Si deseas que puedas navegar hacia atrás
            transaction.commit();


        }

    }
}