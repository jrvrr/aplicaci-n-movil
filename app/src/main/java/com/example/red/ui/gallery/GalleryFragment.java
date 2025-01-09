package com.example.red.ui.gallery;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.red.LoginActivity;
import com.example.red.R;
import com.example.red.RegisterActivity;
import com.example.red.databinding.FragmentGalleryBinding;
import com.example.red.ui.Datos;
import com.example.red.ui.slideshow.SlideshowFragment;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private TextView tvPubli, tvSecreto;
    private Button btnPubli, btnPubliImg;

    private Datos dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvPubli = root.findViewById(R.id.tvPubli);
        tvSecreto = root.findViewById(R.id.tvSecreto);
        btnPubli = root.findViewById(R.id.btnSubirPubli);
        btnPubliImg = root.findViewById(R.id.btnPubliImg);

        btnPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subirPubli();
            }
        });

        dbHelper = new Datos(root.getContext());
        db = dbHelper.getWritableDatabase();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void subirPubli(){
        String texto = tvPubli.getText().toString();
        String uri = tvSecreto.getText().toString();

        if(texto == null || uri == null){
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }else{

                ContentValues values = new ContentValues();
                values.put("texto", texto);
                values.put("Imagen", uri);
                db.insert("publi", null, values);


            Toast.makeText(requireContext(), "Publicación realizada", Toast.LENGTH_SHORT).show();
            tvPubli.setText("");

            //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //ft.replace(R.layout.fragment_slideshow, new SlideshowFragment()); // Cambia el fragmento
            //ft.addToBackStack(null);  // Añade la transacción a la pila de retroceso si lo necesitas
            //ft.commit();

            //Navigation.findNavController(requireView()).navigate(R.id.action_fragmentA_to_fragmentB);

        }


    }

}