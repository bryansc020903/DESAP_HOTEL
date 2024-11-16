package com.example.proyecto_grupo5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class reservas extends AppCompatActivity {

    private TextView numero, tipo, estado, nombreHotel;
    private ImageView volver;
    private Button btnpagar, btncancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        // Inicializar los elementos de la interfaz
        volver = findViewById(R.id.volver);
        btnpagar = findViewById(R.id.btnpagar);
        btncancelar = findViewById(R.id.btncancelar);
        nombreHotel = findViewById(R.id.hotel);
        tipo = findViewById(R.id.tipo);
        numero = findViewById(R.id.numero);
        estado = findViewById(R.id.estado);

        // Configurar el botón de regreso
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(reservas.this, "Regresar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hoteles.class);
                startActivity(i);
                finish();
            }
        });

        // Recuperar el número de habitación desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String numeroHabitacion = sharedPreferences.getString("numero", null);

        // Mostrar mensaje de verificación del número recuperado
        if (numeroHabitacion != null) {
            Toast.makeText(this, "Número recuperado: " + numeroHabitacion, Toast.LENGTH_SHORT).show();
            obtenerDatos(numeroHabitacion);
        } else {
            Toast.makeText(this, "No se encontraron datos de habitación. Por favor, selecciona una habitación primero.", Toast.LENGTH_SHORT).show();
        }

        // Acción de pagar
        btnpagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pagar = new Intent(reservas.this, Pagar.class);
                reservas.this.startActivity(pagar);
            }
        });

        // Acción de cancelar la reserva
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar cuadro de diálogo de confirmación
                new AlertDialog.Builder(reservas.this)
                        .setTitle("Confirmación")
                        .setMessage("¿Seguro que desea anular su proceso de reserva?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Actualizar estado en la base de datos
                                actualizarEstadoHabitacion(numeroHabitacion);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Cerrar el diálogo sin hacer nada
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    // Método para obtener los datos de la habitación desde el servidor
    private void obtenerDatos(String numeros) {
        String url = "http://192.168.56.1:80/crud_hotelitos/obtener_datos.php?numero=" + numeros;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Asignar los valores a los TextViews
                            nombreHotel.setText(response.getString("nombre"));
                            tipo.setText(response.getString("tipo"));
                            numero.setText(response.getString("numero"));
                            estado.setText(response.getString("estado"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(reservas.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(reservas.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    // Método para actualizar el estado de la habitación a "Disponible"
    private void actualizarEstadoHabitacion(String numeroHabitacion) {
        String url = "http://192.168.56.1:80/crud_reservas/actualiza_estado.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log para verificar la respuesta
                        Log.d("Respuesta del servidor", response);

                        // Verificar si la actualización fue exitosa
                        if (response != null && response.trim().contains("success")) {
                            // Mostrar mensaje de éxito
                            Toast.makeText(reservas.this, "Reserva cancelada exitosamente", Toast.LENGTH_SHORT).show();

                            // Limpiar los campos de datos en la interfaz
                            nombreHotel.setText("");
                            tipo.setText("");
                            numero.setText("");
                            estado.setText("");

                            // Eliminar el número de habitación de SharedPreferences
                            SharedPreferences.Editor editor = getSharedPreferences("MisPreferencias", MODE_PRIVATE).edit();
                            editor.remove("numero"); // Elimina el número de habitación
                            editor.apply();

                            // Limpiar todas las preferencias relacionadas con la habitación
                            SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                            preferences.edit().clear().apply(); // Borra todas las preferencias

                            // Redirigir a la pantalla principal (o donde sea necesario)
                            Intent i = new Intent(reservas.this, hoteles.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Si la respuesta no es "success", mostrar error
                            Toast.makeText(reservas.this, "Error al cancelar la reserva: " + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(reservas.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error de conexión", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros para la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("numero", numeroHabitacion);
                params.put("estado", "Disponible"); // Cambiar el estado a Disponible
                return params;
            }
        };

        queue.add(request);
    }



}
