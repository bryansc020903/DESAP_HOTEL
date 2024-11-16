package com.example.proyecto_grupo5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {

    EditText email, password, celular;
    Button btnregister;
    String dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        celular = findViewById(R.id.celular);
        btnregister = findViewById(R.id.btnregister);

        Intent intent = getIntent();
        dni = intent.getStringExtra("dni");
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
                Intent intentregister = new Intent(registro.this, MainActivity.class);
                registro.this.startActivity(intentregister);
            }
        });



    }
    public void insert()
    {
        try
        {
            String p_email = email.getText().toString();
            String p_password = password.getText().toString();
            String p_celular = celular.getText().toString();

            ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setMessage("cargando");


            if (p_email.isEmpty()){
                Toast.makeText(this,"Ingrese email",Toast.LENGTH_SHORT).show();
            }else if (p_password.isEmpty()){
                Toast.makeText(this,"Ingrese password",Toast.LENGTH_SHORT).show();
            }else if (p_celular.isEmpty()){
                Toast.makeText(this,"Ingrese celular",Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.show();
                progressDialog.dismiss();
                StringRequest request =new StringRequest(Request.Method.POST,
                        "http://192.168.56.1:80/crud_usuario/insertar_.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(registro.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(registro.this,error.getMessage(),Toast.LENGTH_SHORT).show();progressDialog.dismiss();
                    }
                }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String>params=new HashMap<>();
                        params.put("dni", dni);
                        params.put("email",p_email);
                        params.put("clave",p_password);
                        params.put("celular",p_celular);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(registro.this);
                requestQueue.add(request);
            }
            Toast.makeText(this,"Usuario registrado",Toast.LENGTH_SHORT).show();
            email.setText("");
            password.setText("");
            celular.setText("");
            email.requestFocus();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Verificar datos a ingresar",Toast.LENGTH_SHORT).show();
        }
    }
}