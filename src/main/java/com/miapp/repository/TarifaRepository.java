package com.miapp.repository;

import com.miapp.model.tarifas.Tarifa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findTopByNombreIgnoreCaseAndCotizacionIsNull(String nombre);
} 
