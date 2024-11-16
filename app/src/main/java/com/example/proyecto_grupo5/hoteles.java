package com.example.proyecto_grupo5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class hoteles extends AppCompatActivity {

    private RequestQueue rq;
    private RecyclerView lsthoteles;
    private AdaptadorUsuario adaptadorUsuario;
    private String[] nombres, ubicaciones, contactos, calificaciones, urlImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles);

        ImageButton boton_imagen = findViewById(R.id.volver);
        lsthoteles = findViewById(R.id.lsthoteles);

        boton_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(hoteles.this, "Regresar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });

        ImageView menuIcon = findViewById(R.id.menu);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarMenu(v);
            }
        });

        rq = Volley.newRequestQueue(this);
        cargarHabitaciones();
    }

    private void mostrarMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_opciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId(); // Obtener el ID del elemento seleccionado
                if (id == R.id.perfil) {
                    startActivity(new Intent(hoteles.this, Perfil.class)); // Iniciar la actividad de perfil
                    return true;
                } else if (id == R.id.reservas) {
                    startActivity(new Intent(hoteles.this, reservas.class)); // Iniciar la actividad de reservas
                    return true;
                } else if (id == R.id.soporte) {
                    startActivity(new Intent(hoteles.this, soporte.class)); // Iniciar la actividad de soporte
                    return true;
                } else if (id == R.id.salir) {
                    startActivity(new Intent(hoteles.this, cerrar_sesion.class));
                    return true;
                }
                return false; // Si no coincide con ninguna opción
            }
        });

        popupMenu.show(); // Mostrar el menú
    }

    private void cargarHabitaciones() {
        String url = "http://192.168.56.1:80/crud_hoteles/hotel1.php"; // Cambia a tu URL de habitaciones
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hotel"); // Asegúrate que este sea el nombre correcto
                            int count = jsonArray.length();
                            nombres = new String[count];
                            ubicaciones = new String[count];
                            contactos = new String[count];
                            calificaciones = new String[count];
                            urlImagenes = new String[count];

                            for (int i = 0; i < count; i++) {
                                JSONObject objeto = jsonArray.getJSONObject(i);
                                // Asignar datos a los arrays
                                nombres[i] = objeto.getString("nombre");
                                ubicaciones[i] = objeto.getString("ubicacion");
                                contactos[i] = objeto.getString("contacto");
                                calificaciones[i] = objeto.getString("urlcali");
                                urlImagenes[i] = objeto.getString("urlhotel");
                            }

                            // Configurar el adaptador
                            adaptadorUsuario = new AdaptadorUsuario(count);
                            lsthoteles.setLayoutManager(new LinearLayoutManager(hoteles.this));
                            lsthoteles.setAdapter(adaptadorUsuario);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(hoteles.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(hoteles.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
            return new AdaptadorUsuarioHolder(getLayoutInflater().inflate(R.layout.list_item_hoteles, parent, false));
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
            TextView nombre, ubicacion, contacto;
            ImageView urlcali, urlhotel;

            public AdaptadorUsuarioHolder(@NonNull View itemView) {
                super(itemView);
                nombre = itemView.findViewById(R.id.nombre);
                ubicacion = itemView.findViewById(R.id.ubicacion);
                contacto = itemView.findViewById(R.id.contacto);
                urlcali = itemView.findViewById(R.id.urlcali);
                urlhotel = itemView.findViewById(R.id.urlimagen);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) return;

                        Intent intent = null;
                        try {
                            switch (position) {
                                case 0:
                                    intent = new Intent(itemView.getContext(), hotel1.class);
                                    break;
                                case 1:
                                    intent = new Intent(itemView.getContext(), hotel2.class);
                                    break;
                                case 2:
                                    intent = new Intent(itemView.getContext(), hotel3.class);
                                    break;
                                case 3:
                                    intent = new Intent(itemView.getContext(), hotel4.class);
                                    break;
                                default:
                                    Toast.makeText(itemView.getContext(), "No hay clase asignada para este hotel", Toast.LENGTH_SHORT).show();
                                    return;
                            }

                            if (intent != null) {
                                itemView.getContext().startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(itemView.getContext(), "Error al abrir la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    }
                });

            }

            public void imprimir(int position) {
                nombre.setText(nombres[position]);
                ubicacion.setText(ubicaciones[position]);
                contacto.setText(contactos[position]);
                recuperarImagen(calificaciones[position], urlcali);
                recuperarImagen(urlImagenes[position], urlhotel);
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

