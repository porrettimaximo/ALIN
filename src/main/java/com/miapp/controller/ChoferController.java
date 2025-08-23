package com.miapp.controller;

import com.miapp.repository.ChoferRepository;
import com.miapp.model.Usuarios.Transportistas.Chofer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/choferes")
public class ChoferController {
    @Autowired
    private ChoferRepository choferRepository;

    @GetMapping
    public List<Chofer> getAll() {
        return choferRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Chofer> getById(@PathVariable Long id) {
        return choferRepository.findById(id);
    }

    @PostMapping
    public Chofer create(@RequestBody Chofer chofer) {
        return choferRepository.save(chofer);
    }

    @PutMapping("/{id}")
    public Chofer update(@PathVariable Long id, @RequestBody Chofer chofer) {
        chofer.setId(id);
        return choferRepository.save(chofer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        choferRepository.deleteById(id);
    }
} 