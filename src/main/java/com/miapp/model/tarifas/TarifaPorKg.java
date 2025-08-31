package com.miapp.model.tarifas;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("KG")
public class TarifaPorKg extends Tarifa {
    @Column
    private double tarifa;
    @Column
    private double peso;

    public TarifaPorKg() {}

    public TarifaPorKg(double tarifa, double peso) {
        this.tarifa = tarifa;
        this.peso = peso;
    }

    public TarifaPorKg(String nombre, Double valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public double calcularTarifaKg() {
        return this.tarifa * this.peso;
    }

    public double getTarifa() { return tarifa; }
    public void setTarifa(double tarifa) { this.tarifa = tarifa; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
}