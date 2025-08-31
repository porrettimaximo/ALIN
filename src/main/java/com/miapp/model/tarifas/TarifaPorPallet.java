package com.miapp.model.tarifas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PALLET")
public class TarifaPorPallet extends Tarifa {

    private static final Logger logger = LoggerFactory.getLogger(TarifaPorPallet.class);

    public TarifaPorPallet() {
        super("Tarifa por Pallets", 0.0); // Llamada al constructor de la clase base
        logger.info("Constructor por defecto inicializado correctamente.");
    }

    public TarifaPorPallet(String nombre, double tarifa) {
        super(nombre != null && !nombre.isEmpty() ? nombre : "Tarifa por Pallets", tarifa > 0 ? tarifa : 1.0);
        logger.info("Constructor parametrizado inicializado correctamente con nombre={} y tarifa={}", nombre, tarifa);
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
        logger.info("SetNombre: nombre={}", nombre);
    }
}
