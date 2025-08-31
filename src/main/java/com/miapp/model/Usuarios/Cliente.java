package com.miapp.model.Usuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Cliente() {
        super();
    }

    public Cliente(String nombre, String apellido, String email, String telefono, Integer dni, String password) {
        super(nombre, apellido, email, telefono, dni, password);
    }

    public Cliente(String nombre, String email) {
        super();
        this.setNombre(nombre);
        this.setEmail(email);
    }

    public Cliente(String nombre, String email, String password) {
        super();
        this.setNombre(nombre);
        this.setEmail(email);
        this.setPassword(password);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
