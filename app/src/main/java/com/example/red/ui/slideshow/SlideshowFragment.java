package com.example.red.ui.slideshow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.red.LoginActivity;
import com.example.red.MainActivity;
import com.example.red.R;
import com.example.red.databinding.FragmentSlideshowBinding;
import com.example.red.ui.Datos;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private Datos dbHelper;
    private SQLiteDatabase db;
    private LinearLayout contenedor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        contenedor = root.findViewById(R.id.lytContenedor);

        dbHelper = new Datos(root.getContext());
        db = dbHelper.getWritableDatabase();



        Cursor cursor = db.query("publi", new String[]{"id", "texto", "imagen"}, null, null, null, null, null);
        int i = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String texto = cursor.getString(1);
                String uri = cursor.getString(2);
                System.out.println(texto);

                LinearLayout caja = new LinearLayout(requireContext());
                caja.setOrientation(LinearLayout.VERTICAL);
                caja.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) caja.getLayoutParams();
                params.setMargins(0, 8, 0, 8);
                caja.setLayoutParams(params);

                TextView texto1 = new TextView(root.getContext());
                texto1.setText(texto);
                texto1.setGravity(Gravity.CENTER);
                texto1.setTextSize(24);
                caja.addView(texto1);
                contenedor.addView(caja);

            } while (cursor.moveToNext());
        }




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}