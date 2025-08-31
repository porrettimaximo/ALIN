package com.miapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/cliente-dashboard")
    public String clienteDashboard() {
        return "cliente-dashboard";
    }

    @GetMapping("/chofer-dashboard")
    public String choferDashboard() {
        return "chofer-dashboard";
    }
}
