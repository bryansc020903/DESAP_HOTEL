package com.example.proyecto_grupo5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class hotel1 extends AppCompatActivity {

    private RequestQueue rq;
    private RecyclerView lst1;
    private AdaptadorUsuario adaptadorUsuario;
    private String[] numeros, tipos, descripciones, estados, precios, urlImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel1);

        ImageButton boton_imagen = findViewById(R.id.volver);
        lst1 = findViewById(R.id.lst1);

        boton_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hotel1.this, "Regresar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hoteles.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });



        rq = Volley.newRequestQueue(this);
        cargarHabitaciones();
    }

    private void cargarHabitaciones() {
        String url = "http://192.168.56.1:80/crud_hotelitos/hotel1.php"; // Cambia a tu URL de habitaciones
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("habitacion"); // Asegúrate que este sea el nombre correcto
                            int count = jsonArray.length();
                            numeros = new String[count];
                            tipos = new String[count];
                            descripciones = new String[count];
                            estados = new String[count];
                            precios = new String[count];
                            urlImagenes = new String[count];

                            for (int i = 0; i < count; i++) {
                                JSONObject objeto = jsonArray.getJSONObject(i);
                                // Asignar datos a los arrays
                                numeros[i] = objeto.getString("numero");
                                tipos[i] = objeto.getString("tipo");
                                descripciones[i] = objeto.getString("descripcion");
                                estados[i] = objeto.getString("estado");
                                precios[i] = objeto.getString("precio");
                                urlImagenes[i] = objeto.getString("urlimagen");
                            }

                            // Configurar el adaptador
                            adaptadorUsuario = new AdaptadorUsuario(count);
                            lst1.setLayoutManager(new LinearLayoutManager(hotel1.this));
                            lst1.setAdapter(adaptadorUsuario);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(hotel1.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(hotel1.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        rq.add(requerimiento);
    }

    private class AdaptadorUsuario extends RecyclerView.Adapter<AdaptadorUsuario.AdaptadorUsuarioHolder> {
        private int itemCount;

        public AdaptadorUsuario(int itemCount) {
            this.itemCount = itemCount;
        }

        @NonNull
        @Override
        public AdaptadorUsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorUsuarioHolder(getLayoutInflater().inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorUsuarioHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        class AdaptadorUsuarioHolder extends RecyclerView.ViewHolder {
            TextView numero, tipo, descripcion, estado, precio;
            ImageView ivFoto;

            public AdaptadorUsuarioHolder(@NonNull View itemView) {
                super(itemView);
                numero = itemView.findViewById(R.id.numero);
                tipo = itemView.findViewById(R.id.tipo);
                descripcion = itemView.findViewById(R.id.descripcion);
                estado = itemView.findViewById(R.id.estado);
                precio = itemView.findViewById(R.id.precio);
                ivFoto = itemView.findViewById(R.id.urlimagen);
            }

            public void imprimir(int position) {
                // Asignar los valores a los TextView
                numero.setText(numeros[position]);
                tipo.setText(tipos[position]);
                descripcion.setText(descripciones[position]);
                estado.setText(estados[position]);
                precio.setText(precios[position]);
                recuperarImagen(urlImagenes[position], ivFoto);
            }

            public void recuperarImagen(String foto, ImageView iv) {
                ImageRequest peticion = new ImageRequest(foto,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                iv.setImageBitmap(response);
                            }
                        },
                        0,
                        0,
                        null,
                        null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejar el error de carga de imagen
                            }
                        });
                rq.add(peticion);
            }
        }
    }
}
