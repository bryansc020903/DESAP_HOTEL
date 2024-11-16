package com.example.proyecto_grupo5;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class dni extends AppCompatActivity {

    EditText dni;
    Button btnverificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);

        dni = (EditText) findViewById(R.id.dni);
        btnverificar = (Button) findViewById(R.id.btnverificar);

        btnverificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarEdad();
            }
        });
    }

    public void verificarEdad() {
        try {
            String p_dni = dni.getText().toString();

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Verificando...");

            if (p_dni.isEmpty()) {
                Toast.makeText(this, "Ingrese DNI", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.56.1:80/crud_dni/verificar_edad.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                // Parsear la respuesta que contiene la edad (asumimos que la API devuelve solo la edad en texto)
                                int edad = Integer.parseInt(response.trim());

                                if (edad >= 18) {
                                    Toast.makeText(dni.this, "Edad verificada: " + edad + ". Redirigiendo a registro...", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(dni.this, registro.class);
                                    intent.putExtra("dni", p_dni);  // Pasamos el DNI como extra
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(dni.this, "Edad verificada: " + edad + ". No cumple con la mayoría de edad.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(dni.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("dni", p_dni);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(dni.this);
                requestQueue.add(request);
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Verificar datos a ingresar", Toast.LENGTH_SHORT).show();
        }
    }
}
