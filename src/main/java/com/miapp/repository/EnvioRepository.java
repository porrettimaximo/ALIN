package com.miapp.repository;

import com.miapp.model.envio.Envio;
import com.miapp.model.envio.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByEstado(EstadoEnvio estado);
    List<Envio> findByClienteId(Long clienteId);
    List<Envio> findByChoferId(Long choferId);
    Envio findByCodigoSeguimiento(String codigoSeguimiento);
} 
