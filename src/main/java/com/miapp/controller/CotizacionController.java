package com.miapp.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.model.tarifas.Tarifa;
import com.miapp.repository.CotizacionRepository;
import com.miapp.repository.TarifaRepository;
import com.miapp.service.CotizacionService;
import com.miapp.service.DistanceService;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {
    @Autowired
    private CotizacionRepository cotizacionRepository;
    @Autowired
    private CotizacionService cotizacionService;
    @Autowired
    private DistanceService distanceService;
    @Autowired
    private TarifaRepository tarifaRepository;

    @GetMapping
    public List<Cotizacion> getAll() {
        return cotizacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cotizacion> getById(@PathVariable Long id) {
        return cotizacionRepository.findById(id);
    }

    @PostMapping
    public Cotizacion create(@RequestBody Cotizacion cotizacion) {
        return cotizacionRepository.save(cotizacion);
    }

    @PutMapping("/{id}")
    public Cotizacion update(@PathVariable Long id, @RequestBody Cotizacion cotizacion) {
        cotizacion.setId(id);
        return cotizacionRepository.save(cotizacion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cotizacionRepository.deleteById(id);
    }

    @PostMapping("/calcular")
    public Cotizacion calcularCotizacion(@RequestBody Map<String, Object> datos) throws Exception {
        String origen = (String) datos.get("origin");
        String destino = (String) datos.get("destination");

        double peso = Double.parseDouble(datos.get("weight").toString());
        double largo = Double.parseDouble(datos.get("length").toString());
        double ancho = Double.parseDouble(datos.get("width").toString());
        double alto = Double.parseDouble(datos.get("height").toString());
        int pallets = datos.get("pallets") != null ? Integer.parseInt(datos.get("pallets").toString()) : 0;
        boolean urgente = datos.get("urgency") != null && Boolean.parseBoolean(datos.get("urgency").toString());

        // Crear la cotización con la distancia incluida
        Cotizacion cotizacion = cotizacionService.calcularYCotizar(origen, destino, peso, largo, ancho, alto, urgente, pallets);


        return cotizacion;
    }

    @PostMapping("/calcular-nueva")
    public ResponseEntity<Map<String, Object>> calcularNuevaCotizacion(@RequestBody Map<String, Object> datos) throws Exception {
        String origen = (String) datos.get("origen");
        String destino = (String) datos.get("destino");
        double precioCombustible = Double.parseDouble(datos.get("precioCombustible").toString());

        Map<String, Object> resultado = cotizacionService.calcularNuevaCotizacion(origen, destino, precioCombustible);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/api/places/autocomplete")
    public ResponseEntity<?> autocompletePlaces(@RequestParam("input") String input) {
        try {
            JSONArray predictions = distanceService.getPlaceSuggestions(input);
            return ResponseEntity.ok(predictions.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener sugerencias: " + e.getMessage());
        }
    }

    @GetMapping("/tarifas-y-iva")
    public ResponseEntity<Map<String, Object>> obtenerTarifasYIVA() {
        List<Tarifa> tarifas = tarifaRepository.findAll();
        double iva = 0.16; // Valor fijo del IVA

        Map<String, Object> response = new HashMap<>();
        response.put("tarifas", tarifas);
        response.put("iva", iva);

        return ResponseEntity.ok(response);
    }
}