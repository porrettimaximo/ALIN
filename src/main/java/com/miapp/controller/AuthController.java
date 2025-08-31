package com.miapp.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.Usuarios.Transportistas.Chofer;
import com.miapp.repository.ChoferRepository;
import com.miapp.repository.ClienteRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ChoferRepository choferRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Map<String, Object> response = new HashMap<>();

        try {
            Cliente cliente = clienteRepository.findByEmail(email);
            if (cliente != null && cliente.getPassword() != null && cliente.getPassword().equals(password)) {
                response.put("success", true);
                response.put("user", cliente);
                response.put("userType", "cliente");
                response.put("redirectUrl", "/cliente-dashboard");
                return ResponseEntity.ok(response);
            }

            Chofer chofer = choferRepository.findByEmail(email);
            if (chofer != null && chofer.getPassword() != null && chofer.getPassword().equals(password)) {
                response.put("success", true);
                response.put("user", chofer);
                response.put("userType", "chofer");
                response.put("redirectUrl", "/chofer-dashboard");
                return ResponseEntity.ok(response);
            }

            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en el servidor");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> registerRequest) {
        String userType = (String) registerRequest.get("userType");
        Map<String, Object> response = new HashMap<>();

        try {
            String password = (String) registerRequest.get("password");
            if ("cliente".equals(userType)) {
                Cliente cliente = new Cliente(
                    (String) registerRequest.get("nombre"),
                    (String) registerRequest.get("apellido"),
                    (String) registerRequest.get("email"),
                    (String) registerRequest.get("telefono"),
                    Integer.parseInt(registerRequest.get("dni").toString()),
                    password
                );
                clienteRepository.save(cliente);
                response.put("success", true);
                response.put("user", cliente);
                response.put("userType", "cliente");
            } else if ("chofer".equals(userType)) {
                Chofer chofer = new Chofer(
                    (String) registerRequest.get("nombre"),
                    (String) registerRequest.get("apellido"),
                    (String) registerRequest.get("email"),
                    (String) registerRequest.get("telefono"),
                    Integer.parseInt(registerRequest.get("dni").toString()),
                    password,
                    (String) registerRequest.get("licencia"),
                    (String) registerRequest.get("vehiculo")
                );
                choferRepository.save(chofer);
                response.put("success", true);
                response.put("user", chofer);
                response.put("userType", "chofer");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}