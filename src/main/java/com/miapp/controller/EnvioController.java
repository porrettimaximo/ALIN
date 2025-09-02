package com.miapp.controller;

import com.miapp.model.Usuarios.Transportistas.Chofer;
import com.miapp.repository.EnvioRepository;
import com.miapp.repository.ClienteRepository;
import com.miapp.repository.ChoferRepository;
import com.miapp.repository.CargaRepository;
import com.miapp.model.cargas.Carga;
import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.envio.Envio;
import com.miapp.model.envio.EstadoEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/envio")
@CrossOrigin(origins = "*")
public class EnvioController {
    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ChoferRepository choferRepository;

    @Autowired
    private CargaRepository cargaRepository;

    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearEnvio(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Crear la carga
            Carga carga = new Carga();
            carga.setPeso(Double.parseDouble(request.get("weight").toString()));
            carga.setLargo(Double.parseDouble(request.get("length").toString()));
            carga.setAncho(Double.parseDouble(request.get("width").toString()));
            carga.setAltura(Double.parseDouble(request.get("height").toString()));
            cargaRepository.save(carga);

            // Obtener el cliente (asumiendo que está logueado)
            Long clienteId = Long.parseLong(request.get("clienteId").toString());
            Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

            if (cliente == null) {
                response.put("success", false);
                response.put("message", "Cliente no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear el envío
            Envio envio = new Envio();
            envio.setOrigen(request.get("origin").toString());
            envio.setDestino(request.get("destination").toString());
            envio.setCarga(carga);
            envio.setCliente(cliente);
            envio.setEstado(EstadoEnvio.EN_ESPERA);
            envio.setCodigoSeguimiento("ALI- " + System.currentTimeMillis());
            envio.setCostoTotal(Double.parseDouble(request.get("totalCost").toString()));
            envio.setCreadoEn(LocalDateTime.now());
            envio.setMetodoPago(request.get("metodoPago") != null ? request.get("metodoPago").toString() : null);
            if (request.get("detalleOrigen") != null) envio.setDetalleOrigen(request.get("detalleOrigen").toString());
            if (request.get("detalleDestino") != null) envio.setDetalleDestino(request.get("detalleDestino").toString());
            if (request.get("detalleCarga") != null) envio.setDetalleCarga(request.get("detalleCarga").toString());

            envioRepository.save(envio);

            response.put("success", true);
            response.put("message", "Envío creado exitosamente");
            response.put("codigoSeguimiento", envio.getCodigoSeguimiento());
            response.put("envioId", envio.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear envío: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Envio> envioOpt = envioRepository.findById(id);
        if (envioOpt.isPresent()) {
            return ResponseEntity.ok(envioOpt.get());
        } else {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "Envío no encontrado");
            return ResponseEntity.status(404).body(resp);
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Envio>> getEnviosDisponibles() {
        List<Envio> envios = envioRepository.findByEstado(EstadoEnvio.EN_ESPERA);
        return ResponseEntity.ok(envios);
    }

    @PostMapping("/aceptar/{envioId}")
    public ResponseEntity<Map<String, Object>> aceptarEnvio(@PathVariable Long envioId, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Envio> envioOpt = envioRepository.findById(envioId);
            if (!envioOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Envío no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            Envio envio = envioOpt.get();
            if (envio.getEstado() != EstadoEnvio.EN_ESPERA) {
                response.put("success", false);
                response.put("message", "El envío ya no está disponible");
                return ResponseEntity.badRequest().body(response);
            }

            // Obtener el chofer
            Long choferId = Long.parseLong(request.get("choferId").toString());
            Chofer chofer = choferRepository.findById(choferId).orElse(null);

            if (chofer == null) {
                response.put("success", false);
                response.put("message", "Chofer no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar que el chofer esté aceptado para trabajar
            if (chofer.getEstado() != com.miapp.model.Usuarios.Transportistas.Chofer.Estado.ACEPTADO) {
                response.put("success", false);
                response.put("message", "El chofer aún no está habilitado para trabajar");
                return ResponseEntity.badRequest().body(response);
            }

            // Asignar chofer y cambiar estado
            envio.setChofer(chofer);
            envio.setEstado(EstadoEnvio.EN_TRANSITO);
            envio.setActualizadoEn(LocalDateTime.now());
            envioRepository.save(envio);

            response.put("success", true);
            response.put("message", "Envío aceptado exitosamente");
            response.put("codigoSeguimiento", envio.getCodigoSeguimiento());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al aceptar envío: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Envio>> getEnviosCliente(@PathVariable Long clienteId) {
        List<Envio> envios = envioRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/chofer/{choferId}")
    public ResponseEntity<List<Envio>> getEnviosChofer(@PathVariable Long choferId) {
        List<Envio> envios = envioRepository.findByChoferId(choferId);
        return ResponseEntity.ok(envios);
    }

    @PostMapping("/entregar/{envioId}")
    public ResponseEntity<Map<String, Object>> entregarEnvio(@PathVariable Long envioId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Envio> envioOpt = envioRepository.findById(envioId);
            if (!envioOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Envío no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            Envio envio = envioOpt.get();
            envio.setEstado(EstadoEnvio.ENTREGADO);
            envio.setActualizadoEn(LocalDateTime.now());
            envioRepository.save(envio);

            response.put("success", true);
            response.put("message", "Envío marcado como entregado");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al entregar envío: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/{id}/ruta")
    public ResponseEntity<Map<String, Object>> actualizarRuta(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Optional<Envio> envioOpt = envioRepository.findById(id);
            if (envioOpt.isEmpty()) {
                resp.put("success", false);
                resp.put("message", "Envío no encontrado");
                return ResponseEntity.badRequest().body(resp);
            }
            Envio envio = envioOpt.get();
            String origen = body.get("origen");
            String destino = body.get("destino");
            if (origen == null || origen.isBlank() || destino == null || destino.isBlank()) {
                resp.put("success", false);
                resp.put("message", "Origen y destino son obligatorios");
                return ResponseEntity.badRequest().body(resp);
            }
            envio.setOrigen(origen);
            envio.setDestino(destino);
            envio.setActualizadoEn(LocalDateTime.now());
            envioRepository.save(envio);
            resp.put("success", true);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            resp.put("success", false);
            resp.put("message", "Error actualizando ruta: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(resp);
        }
    }

    @GetMapping("/by-codigo")
    public ResponseEntity<?> getByCodigo(@RequestParam("code") String code) {
        Envio e = envioRepository.findByCodigoSeguimiento(code);
        if (e != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("id", e.getId());
            return ResponseEntity.ok(resp);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", "Código no encontrado");
        return ResponseEntity.status(404).body(resp);
    }
}
