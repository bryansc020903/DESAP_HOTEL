package com.example.proyecto_grupo5;

public class Usuario extends Persona {
    private String email;
    private String password;
    private String celular;

    // Constructor que incluye los atributos de Persona y Usuario
    public Usuario(String dni, String nombre, String apellido_p,
                   String apellido_m, String edad, String email,
                   String password, String celular) {
        super(dni, nombre, apellido_p, apellido_m, edad);  // Llamada al constructor de Persona
        this.email = email;
        this.password = password;
        this.celular = celular;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}