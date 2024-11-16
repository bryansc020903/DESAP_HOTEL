package com.example.proyecto_grupo5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class soporte extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        ImageView boton_imagen = findViewById(R.id.volver);

        boton_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(soporte.this, "Regresar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hoteles.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });





    }
}