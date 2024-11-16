package com.example.proyecto_grupo5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tarjeta extends AppCompatActivity {

    private ImageView volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tarjeta);

        volver = findViewById(R.id.volver);

        volver.setOnClickListener(v -> {
            Toast.makeText(Tarjeta.this, "Regresar", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Pagar.class);
            startActivity(i);
            finish();
        });
    }
}