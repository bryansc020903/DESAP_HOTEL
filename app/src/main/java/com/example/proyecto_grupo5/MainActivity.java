package com.example.proyecto_grupo5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnlogin, btnsingin;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar botones y campos de texto
        btnlogin = findViewById(R.id.btnlogin);
        btnsingin = findViewById(R.id.btnsingin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Acciones de los botones
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(); // Llamar al método de inicio de sesión
            }
        });

        btnsingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentsingin = new Intent(MainActivity.this, dni.class);
                MainActivity.this.startActivity(intentsingin);
            }
        });
    }

    private void login() {
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        // Verificar si los campos están vacíos
        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar diálogo de carga
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        // Realizar la solicitud POST para iniciar sesión
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.56.1:80/crud_Main/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.trim().equals("success")) {
                            // Guardar el email en SharedPreferences
                            guardarEmail(inputEmail);

                            // Redirigir a la actividad de hoteles
                            Intent intentlogin = new Intent(MainActivity.this, hoteles.class);
                            MainActivity.this.startActivity(intentlogin);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "El email o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", inputEmail);
                params.put("clave", inputPassword);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    // Método para guardar el email en SharedPreferences
    private void guardarEmail(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply(); // Guarda el email de forma persistente
    }
}
