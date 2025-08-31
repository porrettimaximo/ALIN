package com.miapp.model.tarifas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.miapp.model.cotizacion.Cotizacion;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_tarifa")
public abstract class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    @JsonBackReference
    private Cotizacion cotizacion;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    protected String nombre;

    @Column(nullable = false)
    protected Double valor;

    public Tarifa() {}

    public Tarifa(String nombre, Double valor) {
        this.nombre = nombre;
        this.valor = valor;
        System.out.println("Tarifa creada: nombre=" + nombre + ", valor=" + valor);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cotizacion getCotizacion() { return cotizacion; }
    public void setCotizacion(Cotizacion cotizacion) { this.cotizacion = cotizacion; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}