package com.example.proyecto_grupo5;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Pagar extends AppCompatActivity {

    private TextView precio;
    private ImageView volver;
    private double precioPorDia; // Variable para almacenar el precio por día
    private RadioButton rdbyape, rdbplin, rdbtarjeta;
    private Button pagarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagar);

        volver = findViewById(R.id.volver);

        volver.setOnClickListener(v -> {
            Toast.makeText(Pagar.this, "Regresar", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), reservas.class);
            startActivity(i);
            finish();
        });

        precio = findViewById(R.id.precio);

        // Inicializar los RadioButtons
        rdbyape = findViewById(R.id.rbtnyape);
        rdbplin = findViewById(R.id.rbtnplin);
        rdbtarjeta = findViewById(R.id.rbtntarjeta);

        // Inicializar el botón de pagar
        pagarButton = findViewById(R.id.pagar);

        // Establecer el comportamiento al hacer clic en el botón de pagar
        pagarButton.setOnClickListener(v -> {
            // Comprobar cuál RadioButton está seleccionado
            if (rdbyape.isChecked()) {
                // Navegar al layout_yape
                Intent intent = new Intent(Pagar.this, Yape.class);
                startActivity(intent);
            } else if (rdbplin.isChecked()) {
                // Navegar al layout_plin
                Intent intent = new Intent(Pagar.this, Plin.class);
                startActivity(intent);
            } else if (rdbtarjeta.isChecked()) {
                // Navegar al layout_tarjeta
                Intent intent = new Intent(Pagar.this, Tarjeta.class);
                startActivity(intent);
            } else {
                // Si ningún RadioButton está seleccionado
                Toast.makeText(Pagar.this, "Por favor, seleccione un método de pago", Toast.LENGTH_SHORT).show();
            }
        });

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the current date to the Check-In field
        EditText checkinEditText = findViewById(R.id.checkin);

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());

        // Set the current date in the checkin EditText
        checkinEditText.setText(currentDate);

        // Set up the DatePicker for Checkout
        EditText checkoutEditText = findViewById(R.id.checkout);

        checkoutEditText.setOnClickListener(v -> {
            // Get current date for DatePicker dialog
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Create and show the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Pagar.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Set the selected date in the checkout EditText
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        checkoutEditText.setText(selectedDate);

                        // Calculate the price after selecting Check-out date
                        calculateTotalPrice(checkinEditText.getText().toString(), selectedDate, precioPorDia);
                    },
                    year, month, dayOfMonth
            );

            // Show the date picker
            datePickerDialog.show();
        });

        // Recuperar el número de habitación desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String numero= sharedPreferences.getString("numero", null);
        obtenerDatos(numero);
    }

    private void obtenerDatos(String numero) {
        String url = "http://192.168.56.1:80/crud_pago/obtener_precio.php?numero=" + numero;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener el precio por día desde la respuesta de la base de datos
                            precioPorDia = response.getDouble("precio");

                            // Llamar a la función para calcular el precio total, pasando el precio por día
                            EditText checkinEditText = findViewById(R.id.checkin);
                            EditText checkoutEditText = findViewById(R.id.checkout);

                            // Verificar que ambas fechas estén establecidas antes de calcular
                            if (!checkinEditText.getText().toString().isEmpty() && !checkoutEditText.getText().toString().isEmpty()) {
                                calculateTotalPrice(checkinEditText.getText().toString(), checkoutEditText.getText().toString(), precioPorDia);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Pagar.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Pagar.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    private void calculateTotalPrice(String checkinDate, String checkoutDate, double precioPorDia) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date checkin = sdf.parse(checkinDate);
            Date checkout = sdf.parse(checkoutDate);

            // Calcular la diferencia en milisegundos
            long differenceInMillis = checkout.getTime() - checkin.getTime();

            // Convertir milisegundos a días
            long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

            // Si la diferencia es cero, significa que el check-in y el check-out son el mismo día, así que asignamos 1 día
            if (differenceInDays == 0) {
                differenceInDays = 1;
            }

            // Calcular el precio total
            double totalPrice = precioPorDia * differenceInDays;

            // Mostrar el precio total en el TextView "precio" (solo el valor numérico)
            precio.setText(String.valueOf(totalPrice));  // Solo el precio total como número

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Pagar.this, "Error al calcular la diferencia de fechas", Toast.LENGTH_SHORT).show();
        }
    }
}
