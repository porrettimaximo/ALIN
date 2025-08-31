package com.miapp.model.tarifas;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("KM")
public class TarifaPorKm extends Tarifa {
    @Column
    private double tarifa;
    @Column
    private double distancia;

    public TarifaPorKm() {}

    public TarifaPorKm(double tarifa, double distancia) {
        this.tarifa = tarifa;
        this.distancia = distancia;
    }

    public TarifaPorKm(String nombre, double tarifa) {
        this.nombre = nombre;
        this.tarifa = tarifa;
        this.valor = tarifa; // Asignar un valor predeterminado
    }

    public double calcularTarifaKm() {
        return this.tarifa * this.distancia;
    }

    public double getTarifa() { return tarifa; }
    public void setTarifa(double tarifa) { this.tarifa = tarifa; }
    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
}