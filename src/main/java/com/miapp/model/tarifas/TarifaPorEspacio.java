package com.miapp.model.tarifas;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESPACIO")
public class TarifaPorEspacio extends Tarifa {
    @Column
    private double tarifa;
    @Column
    private double carga;

    public TarifaPorEspacio() {}

    public TarifaPorEspacio(double tarifa, com.miapp.model.cargas.Carga carga) {
        this.tarifa = tarifa;
        this.carga = carga.getAltura() * carga.getAncho() * carga.getLargo() / 1000000.0; // volumen en m3
    }
    public TarifaPorEspacio(String nombre,  double valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public double calcularTarifaEspacio() {
        return this.tarifa * this.carga;
    }

    public double getTarifa() { return tarifa; }
    public void setTarifa(double tarifa) { this.tarifa = tarifa; }
    public double getCarga() { return carga; }
    public void setCarga(double carga) { this.carga = carga; }
}