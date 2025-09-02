package com.miapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Value("${google.api.key}")
    private String googleApiKey;

    @GetMapping("/cliente-dashboard")
    public String clienteDashboard(Model model) {
        model.addAttribute("googleApiKey", googleApiKey);
        return "dashboard-cliente";
    }

    @GetMapping("/chofer-dashboard")
    public String choferDashboard(Model model) {
        model.addAttribute("googleApiKey", googleApiKey);
        return "dashboard-chofer";
    }

    @GetMapping("/cliente/envios")
    public String clienteEnvios(Model model) {
        model.addAttribute("googleApiKey", googleApiKey);
        return "cliente-envios";
    }

    @GetMapping("/cliente/nuevo-envio")
    public String clienteNuevoEnvio(Model model) {
        // Página de nuevo envío unificada en el dashboard del cliente
        return "redirect:/cliente-dashboard";
    }
}
