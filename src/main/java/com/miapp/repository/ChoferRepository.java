package com.miapp.repository;

import com.miapp.model.Usuarios.Transportistas.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {
    Chofer findByEmail(String email);
} 