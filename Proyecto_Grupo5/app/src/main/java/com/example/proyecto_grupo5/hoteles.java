package com.example.proyecto_grupo5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class hoteles extends AppCompatActivity {

    // Declarar los TextViews para los 4 hoteles
    TextView hotelito1, ubi1, contacto1;
    TextView hotelito2, ubi2, contacto2;
    TextView hotelito3, ubi3, contacto3;
    TextView hotelito4, ubi4, contacto4;

    ImageView cali1, cali2, cali3, cali4;

    Button btnver1, btnver2, btnver3, btnver4;


    private RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles);

        rq = Volley.newRequestQueue(this);

        ImageView boton_imagen = findViewById(R.id.opciones);
        // Enlazar las vistas con los TextView del layout
        hotelito1 = findViewById(R.id.hotelito1);
        ubi1 = findViewById(R.id.ubi1);
        contacto1 = findViewById(R.id.contacto1);
        cali1 = findViewById(R.id.cali1);

        hotelito2 = findViewById(R.id.hotelito2);
        ubi2 = findViewById(R.id.ubi2);
        contacto2 = findViewById(R.id.contacto2);
        cali2 = findViewById(R.id.cali2);

        hotelito3 = findViewById(R.id.hotelito3);
        ubi3 = findViewById(R.id.ubi3);
        contacto3 = findViewById(R.id.contacto3);
        cali3 = findViewById(R.id.cali3);

        hotelito4 = findViewById(R.id.hotelito4);
        ubi4 = findViewById(R.id.ubi4);
        contacto4 = findViewById(R.id.contacto4);
        cali4 = findViewById(R.id.cali4);

        btnver1 = findViewById(R.id.btnver1);
        btnver2 = findViewById(R.id.btnver2);
        btnver3 = findViewById(R.id.btnver3);
        btnver4 = findViewById(R.id.btnver4);

        btnver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hotel1.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }

        });

        btnver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hotel2.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });

        btnver3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hotel3.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });

        btnver4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hotel4.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });

        boton_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Perfil.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });

        // Llamada a la función para obtener los datos automáticamente
        obtenerDatosHoteles();
    }

    private void obtenerDatosHoteles() {
        // La URL del archivo PHP en tu servidor
        String url = "http://192.168.56.1/crud_hoteles/obtener.php";

        // Crear una solicitud JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Verificar que la respuesta tenga 4 hoteles
                            if (response.length() >= 4) {
                                JSONObject hotel1 = response.getJSONObject(0);
                                JSONObject hotel2 = response.getJSONObject(1);
                                JSONObject hotel3 = response.getJSONObject(2);
                                JSONObject hotel4 = response.getJSONObject(3);

                                // Asignar los datos a los TextView correspondientes
                                hotelito1.setText(hotel1.getString("nombre"));
                                ubi1.setText(hotel1.getString("ubicacion"));
                                contacto1.setText(hotel1.getString("contacto"));
                                recuperarImagen(hotel1.getString("urlcali"), cali1);

                                hotelito2.setText(hotel2.getString("nombre"));
                                ubi2.setText(hotel2.getString("ubicacion"));
                                contacto2.setText(hotel2.getString("contacto"));
                                recuperarImagen(hotel2.getString("urlcali"), cali2);

                                hotelito3.setText(hotel3.getString("nombre"));
                                ubi3.setText(hotel3.getString("ubicacion"));
                                contacto3.setText(hotel3.getString("contacto"));
                                recuperarImagen(hotel3.getString("urlcali"), cali3);

                                hotelito4.setText(hotel4.getString("nombre"));
                                ubi4.setText(hotel4.getString("ubicacion"));
                                contacto4.setText(hotel4.getString("contacto"));
                                recuperarImagen(hotel4.getString("urlcali"), cali4);
                            } else {
                                Toast.makeText(hoteles.this, "No hay suficientes datos de hoteles.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(hoteles.this, "Error al procesar la respuesta JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(hoteles.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);

        rq.add(jsonArrayRequest);
    }

    private void recuperarImagen(String fotoUrl, ImageView imageView) {
        ImageRequest peticion = new ImageRequest(fotoUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },
                0,
                0,
                null,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(hoteles.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de Volley
        rq.add(peticion);
    }
}
