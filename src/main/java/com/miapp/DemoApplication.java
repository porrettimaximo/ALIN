package com.miapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.miapp.repository.ChoferRepository;
import com.miapp.repository.ClienteRepository;
import com.miapp.repository.EnvioRepository;
import com.miapp.repository.TarifaRepository;

@SpringBootApplication
@EntityScan(basePackages = {"com.miapp.model"})
@EnableJpaRepositories(basePackages = {"com.miapp.repository"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner runSetup(DataLoader dataLoader, ClienteRepository clienteRepo, ChoferRepository choferRepo, TarifaRepository tarifaRepo, EnvioRepository envioRepo) {
        return args -> {
            System.out.println("Inicializando datos de ejemplo...");
            dataLoader.initDatabase(clienteRepo, choferRepo, tarifaRepo, envioRepo);
            System.out.println("Datos inicializados correctamente.");
        };
    }
}