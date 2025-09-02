package com.miapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.model.tarifas.TarifaFija;
import com.miapp.model.tarifas.TarifaPorEspacio;
import com.miapp.model.tarifas.TarifaPorKg;
import com.miapp.model.tarifas.TarifaPorKm;
import com.miapp.model.tarifas.TarifaPorPallet;
import com.miapp.repository.CotizacionRepository;
import com.miapp.repository.TarifaRepository;

@Service
public class CotizacionService {

    @Autowired
    private DistanceService distanceService;
    @Autowired
    private CotizacionRepository cotizacionRepository;
    @Autowired
    private TarifaRepository tarifaRepository;

    public Cotizacion calcularYCotizar(String origen, String destino,
                                       double peso, double largo, double ancho, double alto,
                                       boolean urgente, int pallets) throws Exception {
        // Distancia
        double distancia = distanceService.getDistanceInKm(origen, destino);

        // Tarifas base desde BD (nombre + valor, sin cotización asociada)
        double perKg      = tarifaRepository.findTopByNombreIgnoreCaseAndCotizacionIsNull("Tarifa por Kg")
                               .map(t -> t.getValor()).orElse(0.50);
        double perM3      = tarifaRepository.findTopByNombreIgnoreCaseAndCotizacionIsNull("Tarifa por m3")
                               .map(t -> t.getValor()).orElse(0.80);
        double perKm      = tarifaRepository.findTopByNombreIgnoreCaseAndCotizacionIsNull("Tarifa por Km")
                               .map(t -> t.getValor()).orElse(0.80);
        double perPallet  = tarifaRepository.findTopByNombreIgnoreCaseAndCotizacionIsNull("Tarifa por Pallets")
                               .map(t -> t.getValor()).orElse(5.00);
        double urgencyPct = tarifaRepository.findTopByNombreIgnoreCaseAndCotizacionIsNull("Tarifa Urgencia")
                               .map(t -> t.getValor()).orElse(0.15);

        // Cálculos (metros / kg)
        double volume = Math.max(0.0, largo * ancho * alto);
        double distVal = Math.max(0.0, perKm * distancia);
        double weightVal = Math.max(0.0, perKg * peso);
        double volVal = volume > 0 ? Math.max(0.0, perM3 * volume) : 0.0;
        double palVal = Math.max(0, pallets) * perPallet;
        double subtotal = distVal + weightVal + volVal + palVal;
        double urgVal = urgente ? subtotal * Math.max(0.0, urgencyPct) : 0.0;

        // Armar la cotización con desglose
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setDistancia(distancia);

        if (distVal > 0){
            TarifaPorKm t = new TarifaPorKm("Distancia", distVal);
            t.setCotizacion(cotizacion); cotizacion.getTarifas().add(t);
        }
        if (weightVal > 0){
            TarifaPorKg t = new TarifaPorKg("Peso", weightVal);
            t.setCotizacion(cotizacion); cotizacion.getTarifas().add(t);
        }
        if (volVal > 0){
            TarifaPorEspacio t = new TarifaPorEspacio("Volumen", volVal);
            t.setCotizacion(cotizacion); cotizacion.getTarifas().add(t);
        }
        if (palVal > 0){
            TarifaPorPallet t = new TarifaPorPallet("Pallets", palVal);
            t.setCotizacion(cotizacion); cotizacion.getTarifas().add(t);
        }
        if (urgVal > 0){
            TarifaFija t = new TarifaFija("Urgencia", urgVal);
            t.setCotizacion(cotizacion); cotizacion.getTarifas().add(t);
        }

        if (cotizacion.getTarifas().isEmpty() || cotizacion.getDistancia() <= 0) {
            throw new IllegalArgumentException("La cotización debe tener tarifas válidas y una distancia mayor a 0.");
        }

        return cotizacionRepository.save(cotizacion);
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

