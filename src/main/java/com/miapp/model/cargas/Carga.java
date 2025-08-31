package com.miapp.model.cargas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "carga")
public class Carga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double peso;
    private Double altura;
    private Double ancho;
    private Double largo;
    private Integer pallets;

    public Carga() {}

    public Carga(Double peso, Double altura, Double ancho, Double largo) {
        this.peso = peso;
        this.altura = altura;
        this.ancho = ancho;
        this.largo = largo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }
    public Double getAncho() { return ancho; }
    public void setAncho(Double ancho) { this.ancho = ancho; }
    public Double getLargo() { return largo; }
    public void setLargo(Double largo) { this.largo = largo; }
    public Integer getPallets() {
        return pallets;
    }

    public void setPallets(Integer pallets) {
        this.pallets = pallets;
    }

    public Integer calculatePallets() {
        double volume = this.altura * this.ancho * this.largo;
        int calculatedPallets = (int) Math.ceil(volume / 1.44);
        return Math.max(calculatedPallets, 1);
    }

    public void validateAndSetPallets(Integer pallets) {
        int minimumPallets = calculatePallets();
        if (pallets < minimumPallets) {
            throw new IllegalArgumentException("El volumen de carga requiere un mínimo de " + minimumPallets + " pallets. Precio por pallet: $5.00");
        }
        this.pallets = pallets;
    }
}
