package com.miapp.controller;

import com.miapp.repository.ClienteRepository;
import com.miapp.repository.ChoferRepository;
import com.miapp.repository.CargaRepository;
import com.miapp.repository.EnvioRepository;
import com.miapp.repository.CotizacionRepository;
import com.miapp.repository.TarifaRepository;
import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.Usuarios.Transportistas.Chofer;
import com.miapp.model.cargas.Carga;
import com.miapp.model.envio.Envio;
import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.model.envio.EstadoEnvio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/setup")
@CrossOrigin(origins = "*")
public class SetupController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ChoferRepository choferRepository;

    @Autowired
    private CargaRepository cargaRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initializeData() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Crear clientes de ejemplo
            Cliente cliente1 = new Cliente("Juan", "Pérez", "juan.perez@email.com", "11-1234-5678", 12345678, "1234");
            Cliente cliente2 = new Cliente("María", "González", "maria.gonzalez@email.com", "11-2345-6789", 23456789, "1234");
            Cliente cliente3 = new Cliente("Carlos", "López", "carlos.lopez@email.com", "11-3456-7890", 34567890, "1234");

            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
            clienteRepository.save(cliente3);

            // Crear choferes de ejemplo
            Chofer chofer1 = new Chofer("Roberto", "Martínez", "roberto.martinez@email.com", "11-4567-8901", 45678901, "1234", "B123456", "Camión Mercedes-Benz 710");
            Chofer chofer2 = new Chofer("Ana", "Rodríguez", "ana.rodriguez@email.com", "11-5678-9012", 56789012, "1234", "B234567", "Camión Iveco Daily");
            Chofer chofer3 = new Chofer("Luis", "Fernández", "luis.fernandez@email.com", "11-6789-0123", 67890123, "1234", "B345678", "Camión Ford F-4000");

            choferRepository.save(chofer1);
            choferRepository.save(chofer2);
            choferRepository.save(chofer3);

            // Crear tarifas de ejemplo - Comentado por ahora para evitar errores de BD
            // TarifaPorKm tarifaKm = new TarifaPorKm(50.0,00            // tarifaRepository.save(tarifaKm);

            // TarifaPorKg tarifaKg = new TarifaPorKg(0.5,00            // tarifaRepository.save(tarifaKg);

            // TarifaPorEspacio tarifaEspacio = new TarifaPorEspacio(0.8, null);
            // tarifaRepository.save(tarifaEspacio);

            // Crear cargas de ejemplo
            Carga carga1 = new Carga(3500.0, 100.0, 150.0, 200.0);
            cargaRepository.save(carga1);

            Carga carga2 = new Carga(2500.0, 80.0, 120.0, 180.0);
            cargaRepository.save(carga2);

            // Crear envíos de ejemplo
            Envio envio1 = new Envio();
            envio1.setOrigen("Buenos Aires, CABA");
            envio1.setDestino("Rosario, Santa Fe");
            envio1.setCarga(carga1);
            envio1.setCliente(cliente1);
            envio1.setChofer(chofer1);
            envio1.setEstado(EstadoEnvio.EN_CAMINO_A);
            envio1.setCodigoSeguimiento("ALI-ABC123XYZ");
            envioRepository.save(envio1);

            Envio envio2 = new Envio();
            envio2.setOrigen("Córdoba, Córdoba");
            envio2.setDestino("Mendoza, Mendoza");
            envio2.setCarga(carga2);
            envio2.setCliente(cliente2);
            envio2.setChofer(chofer2);
            envio2.setEstado(EstadoEnvio.ENTREGADO);
            envio2.setCodigoSeguimiento("ALI-DEF456UVW");
            envioRepository.save(envio2);

            // Crear cotizaciones de ejemplo
            Cotizacion cotizacion1 = new Cotizacion();
            cotizacionRepository.save(cotizacion1);

            response.put("success", true);
            response.put("message", "Datos de ejemplo creados exitosamente");
            response.put("clientes", 3);
            response.put("choferes", 3);
            response.put("cargas", 2);
            response.put("envios", 2);
            response.put("cotizaciones", 1);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear datos de ejemplo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSetupStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            long clientesCount = clienteRepository.count();
            long choferesCount = choferRepository.count();
            long cargasCount = cargaRepository.count();
            long enviosCount = envioRepository.count();
            long cotizacionesCount = cotizacionRepository.count();

            response.put("success", true);
            response.put("clientes", clientesCount);
            response.put("choferes", choferesCount);
            response.put("cargas", cargasCount);
            response.put("envios", enviosCount);
            response.put("cotizaciones", cotizacionesCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estado: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 