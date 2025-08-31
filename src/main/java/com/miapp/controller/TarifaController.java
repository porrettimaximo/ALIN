package com.miapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miapp.model.tarifas.Tarifa;
import com.miapp.repository.TarifaRepository;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    @Autowired
    private TarifaRepository tarifaRepository;

    @GetMapping
    public List<Tarifa> getAll() {
        return tarifaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Tarifa> getById(@PathVariable Long id) {
        return tarifaRepository.findById(id);
    }

    @PostMapping
    public Tarifa create(@RequestBody Tarifa tarifa) {
        if (!StringUtils.hasText(tarifa.getNombre())) {
            throw new IllegalArgumentException("El campo 'nombre' no puede estar vacío.");
        }
        return tarifaRepository.save(tarifa);
    }

    @PutMapping("/{id}")
    public Tarifa update(@PathVariable Long id, @RequestBody Tarifa tarifa) {
        if (!StringUtils.hasText(tarifa.getNombre())) {
            throw new IllegalArgumentException("El campo 'nombre' no puede estar vacío.");
        }
        tarifa.setId(id);
        return tarifaRepository.save(tarifa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tarifaRepository.deleteById(id);
    }
}