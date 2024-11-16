package com.example.proyecto_grupo5;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class Perfil extends AppCompatActivity {

    private TextView nombreCompletoTextView, emailTextView;
    private EditText celularTextView;
    private Button btnEditar, btnAceptar;
    private ImageView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        volver = findViewById(R.id.volver);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Regresar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hoteles.class);
                startActivity(i);
                finish();
                // Aquí puedes iniciar la actividad de registro
            }
        });



        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Perfil.this, "Regresando", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), hoteles.class);
                startActivity(i);
                finish();
            }
        });

        // Inicializar TextViews y botones
        nombreCompletoTextView = findViewById(R.id.detalleNom);
        emailTextView = findViewById(R.id.detalleEmail);
        celularTextView = findViewById(R.id.detalleCel);
        btnEditar = findViewById(R.id.btneditar);
        btnAceptar = findViewById(R.id.btnaceptar);

        // Obtener el email guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String emailUsuario = sharedPreferences.getString("email", null);

        if (emailUsuario != null) {
            // Llamar al método para obtener datos y llenar los campos
            obtenerDatos(emailUsuario);
        } else {
            Toast.makeText(this, "Email no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            // Opcional: redirigir a MainActivity si el email no está guardado
        }

        // Configurar los campos para que se vean "plomos" antes de editar
        configurarCamposDeshabilitados();

        // Hacer que el botón Aceptar esté opaco al inicio
        configurarBotonAceptarDeshabilitado();

        // Configurar el botón "Editar"
        btnEditar.setOnClickListener(v -> {
            // Desbloquear el campo celular y cambiar su color
            celularTextView.setFocusableInTouchMode(true);
            celularTextView.setClickable(true);
            celularTextView.setBackgroundColor(getResources().getColor(R.color.colorCelularEditable)); // Cambiar color

            // Hacer que los campos nombre y email permanezcan deshabilitados
            nombreCompletoTextView.setFocusable(false);
            emailTextView.setFocusable(false);

            // Hacer que el botón Aceptar se vea más claro (desbloquearlo)
            btnAceptar.setEnabled(true);
            btnAceptar.setAlpha(1.0f);  // Hacerlo más visible

        });

        // Configurar el botón "Aceptar"
        btnAceptar.setOnClickListener(v -> {
            String celular = celularTextView.getText().toString().trim();
            if (isValidCelular(celular)) {
                // Llamar a la función para actualizar el celular
                actualizarCelular(emailUsuario, celular);

                // Al aceptar, poner el botón Aceptar opaco nuevamente
                btnAceptar.setEnabled(false);
                btnAceptar.setAlpha(0.5f); // Hacerlo opaco de nuevo
            } else {
                Toast.makeText(Perfil.this, "Número de celular no válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar validación en tiempo real al campo celular
        celularTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No hacer nada antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Verificar si el texto tiene más de 9 caracteres o si contiene letras/caracteres no válidos
                String celular = celularTextView.getText().toString().trim();

                // Comprobar si tiene más de 9 caracteres o si contiene algo que no sea un número
                if (celular.length() > 9 || !TextUtils.isDigitsOnly(celular)) {
                    celularTextView.setError("El celular debe tener solo 9 dígitos y no puede contener letras o caracteres especiales.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Validar después de que el texto haya cambiado
                String celular = celularTextView.getText().toString().trim();

                // Validar que el número tenga exactamente 9 dígitos
                if (celular.length() == 9 && TextUtils.isDigitsOnly(celular)) {
                    celularTextView.setError(null); // Limpiar error si es válido
                }
            }
        });
    }

    private void obtenerDatos(String emailUsuario) {
        String url = "http://192.168.56.1:80/crud_perfil/obtener_perfil.php?email=" + emailUsuario;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject jsonObject = response.getJSONObject(0);

                        // Llenar los TextViews con los datos obtenidos
                        nombreCompletoTextView.setText(jsonObject.getString("nombre"));
                        emailTextView.setText(jsonObject.getString("email"));
                        celularTextView.setText(jsonObject.getString("celular"));
                    } else {
                        Toast.makeText(Perfil.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Perfil.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Perfil.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private boolean isValidCelular(String celular) {
        // Validar si el celular tiene exactamente 9 dígitos
        return celular.length() == 9 && TextUtils.isDigitsOnly(celular);
    }

    private void actualizarCelular(String emailUsuario, String celular) {
        String url = "http://192.168.56.1:80/crud_perfil/actualizar_celular.php";

        // Crear el objeto JSON para la actualización
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("email", emailUsuario);
            parametros.put("celular", celular);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar una solicitud POST para actualizar los datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Si la respuesta es exitosa, notificar al usuario
                    Toast.makeText(Perfil.this, "Número de celular actualizado", Toast.LENGTH_SHORT).show();

                    // Deshabilitar el campo celular y el botón Aceptar
                    celularTextView.setFocusable(false);
                    celularTextView.setClickable(false);
                    btnAceptar.setEnabled(false);
                    btnAceptar.setAlpha(0.5f); // Hacerlo opaco de nuevo

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Perfil.this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Perfil.this, "Error al realizar la actualización", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void configurarCamposDeshabilitados() {
        // Configurar los campos para que aparezcan "plomos" o deshabilitados
        nombreCompletoTextView.setFocusable(false);
        emailTextView.setFocusable(false);
        celularTextView.setFocusable(false);
        celularTextView.setClickable(false);

        // Cambiar el color de fondo para simular el estado deshabilitado
        nombreCompletoTextView.setBackgroundColor(getResources().getColor(R.color.colorCampoDeshabilitado));
        emailTextView.setBackgroundColor(getResources().getColor(R.color.colorCampoDeshabilitado));
        celularTextView.setBackgroundColor(getResources().getColor(R.color.colorCampoDeshabilitado));
    }

    private void configurarBotonAceptarDeshabilitado() {
        // Configurar el botón Aceptar como deshabilitado al inicio
        btnAceptar.setEnabled(false);
        btnAceptar.setAlpha(0.5f);  // Opaco
    }
}
