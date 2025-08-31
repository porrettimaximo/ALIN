package com.miapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.model.tarifas.TarifaPorEspacio;
import com.miapp.model.tarifas.TarifaPorKg;
import com.miapp.model.tarifas.TarifaPorKm;
import com.miapp.model.tarifas.TarifaPorPallet;
import com.miapp.repository.CotizacionRepository;

@Service
public class CotizacionService {
    
    @Autowired
    private DistanceService distanceService;
    @Autowired
    private CotizacionRepository cotizacionRepository;
    
    public Cotizacion calcularYCotizar(String origen, String destino,
                                       double peso, double largo, double ancho, double alto,
                                       boolean urgente, int pallets) throws Exception {

        double distancia = distanceService.getDistanceInKm(origen, destino);

        // Crear tarifas
        TarifaPorKg tarifaKg = new TarifaPorKg(0.50, peso);
        tarifaKg.setNombre("Tarifa por Kg"); // Asignar nombre
        tarifaKg.setValor(tarifaKg.calcularTarifaKg());
        TarifaPorEspacio tarifaEspacio = new TarifaPorEspacio(0.80, new com.miapp.model.cargas.Carga(largo, ancho, alto, peso));
        tarifaEspacio.setNombre("Tarifa por Espacio"); // Asignar nombre
        tarifaEspacio.setValor(tarifaEspacio.calcularTarifaEspacio());
        TarifaPorKm tarifaKm = new TarifaPorKm(0.80, distancia);
        tarifaKm.setNombre("Tarifa por Km"); // Asignar nombre
        tarifaKm.setValor(tarifaKm.calcularTarifaKm());
        
        if (pallets <= 0) {
            pallets = 1; // Valor predeterminado
        }

        TarifaPorPallet tarifaPallet = new TarifaPorPallet("Tarifa por Pallets", 5.00);
        // Asignar directamente el valor de la tarifa
        tarifaPallet.setValor(tarifaPallet.getValor());

        // Validar tarifas
        if (tarifaKg.getValor() <= 0 || tarifaEspacio.getValor() <= 0 || tarifaKm.getValor() <= 0 || tarifaPallet.getValor() <= 0) {
            throw new IllegalArgumentException("Las tarifas calculadas deben ser mayores a 0.");
        }

        // Crear cotización y asociar tarifas
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.getTarifas().add(tarifaKg);
        cotizacion.getTarifas().add(tarifaEspacio);
        cotizacion.getTarifas().add(tarifaKm);
        cotizacion.getTarifas().add(tarifaPallet);
        cotizacion.setDistancia(distancia);

        // Validar cotización
        if (cotizacion.getTarifas().isEmpty() || cotizacion.getDistancia() <= 0) {
            throw new IllegalArgumentException("La cotización debe tener tarifas válidas y una distancia mayor a 0.");
        }


        return cotizacion;
    }
    
    public Map<String, Object> calcularNuevaCotizacion(String origen, String destino, double precioCombustible) throws Exception {
        double distancia = distanceService.getDistanceInKm(origen, destino);

        // Calcular litros necesarios
        double litrosNecesarios = distancia / 2.5;

        // Calcular costo del transporte
        double costoTransporte = litrosNecesarios * precioCombustible;

        // Aplicar incremento del 205% al 225%
        double tarifaCliente = costoTransporte * 2.15; // Promedio del rango

        // Añadir profit del 5% y el IVA (16%)
        double profit = tarifaCliente * 0.05;
        double iva = (tarifaCliente + profit) * 0.16;
        double total = tarifaCliente + profit + iva;

        // Crear desglose
        Map<String, Object> desglose = new HashMap<>();
        desglose.put("distancia", distancia);
        desglose.put("litrosNecesarios", litrosNecesarios);
        desglose.put("costoTransporte", costoTransporte);
        desglose.put("tarifaCliente", tarifaCliente);
        desglose.put("profit", profit);
        desglose.put("iva", iva);
        desglose.put("total", total);

        return desglose;
    }
}