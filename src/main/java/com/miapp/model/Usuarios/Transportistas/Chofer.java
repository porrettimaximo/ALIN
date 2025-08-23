package com.miapp.model.Usuarios.Transportistas;

import com.miapp.model.Usuarios.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "chofer")
public class Chofer extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licencia;
    private String vehiculo;

    // Relación con cargas (opcional, si se quiere saber qué cargas transportó)
    // @OneToMany(mappedBy = "chofer")
    // private List<Carga> cargas;

    public Chofer() {
        super();
    }

    public Chofer(String nombre, String apellido, String email, String telefono, Integer dni, String password, String licencia, String vehiculo) {
        super(nombre, apellido, email, telefono, dni, password);
        this.licencia = licencia;
        this.vehiculo = vehiculo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }
}
