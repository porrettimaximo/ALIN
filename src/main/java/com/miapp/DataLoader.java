package com.miapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.Usuarios.Transportistas.Chofer;
import com.miapp.model.cargas.Carga;
import com.miapp.model.envio.Envio;
import com.miapp.model.tarifas.TarifaPorEspacio;
import com.miapp.model.tarifas.TarifaPorKg;
import com.miapp.model.tarifas.TarifaPorKm;
import com.miapp.model.tarifas.TarifaPorPallet;
import com.miapp.repository.ChoferRepository;
import com.miapp.repository.ClienteRepository;
import com.miapp.repository.EnvioRepository;
import com.miapp.repository.TarifaRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ClienteRepository clienteRepo, ChoferRepository choferRepo, TarifaRepository tarifaRepo, EnvioRepository envioRepo) {
        return args -> {
            // Eliminar datos existentes
            envioRepo.deleteAll();
            tarifaRepo.deleteAll();
            choferRepo.deleteAll();
            clienteRepo.deleteAll();

            // Cargar clientes
            Cliente cliente1 = new Cliente("Juan Perez", "juan.perez@example.com");
            Cliente cliente2 = new Cliente("Maria Lopez", "maria.lopez@example.com");
            clienteRepo.save(cliente1);
            clienteRepo.save(cliente2);

            // Cargar choferes
            Chofer chofer1 = new Chofer("Carlos Gomez", "ABC123");
            Chofer chofer2 = new Chofer("Luis Fernandez", "XYZ789");
            choferRepo.save(chofer1);
            choferRepo.save(chofer2);

            // Cargar tarifas usando TarifaPorKg
            TarifaPorKg tarifa1 = new TarifaPorKg("Tarifa Básica", 10.0);
            TarifaPorKg tarifa2 = new TarifaPorKg("Tarifa Premium", 20.0);
            tarifaRepo.save(tarifa1);
            tarifaRepo.save(tarifa2);

            // Cargar tarifas fijas
            TarifaPorKg tarifaKg = new TarifaPorKg("Tarifa por Kg", 0.50);
            TarifaPorEspacio tarifaM3 = new TarifaPorEspacio("Tarifa por m³", 0.80);
            TarifaPorKm tarifaKm = new TarifaPorKm("Tarifa por Km", 26);
            TarifaPorPallet tarifaPallet = new TarifaPorPallet("Tarifa por Pallets", 50.0);

            tarifaRepo.save(tarifaKg);
            tarifaRepo.save(tarifaM3);
            tarifaRepo.save(tarifaKm);
            tarifaRepo.save(tarifaPallet);


            // Crear cargas ficticias para los envíos
            Carga carga1 = new Carga();
            Carga carga2 = new Carga();

            // Cargar envíos
            Envio envio1 = new Envio("Envio 1", cliente1, chofer1, carga1);
            envio1.setOrigen("Zatti 1495, Viedma, Argentina");
            envio1.setDestino("Jose Garibaldi 522, Carmen de Patagones");

            Envio envio2 = new Envio("Envio 2", cliente2, chofer2, carga2);
            envio2.setOrigen("Zatti 1495, Viedma, Argentina");
            envio2.setDestino("Jose Garibaldi 522, Carmen de Patagones");

            envioRepo.save(envio1);
            envioRepo.save(envio2);

            // Cargar usuarios para iniciar sesión
            Cliente clienteLogin = new Cliente("Usuario", "usuario@example.com", "password123");
            clienteRepo.save(clienteLogin);

            Chofer choferLogin = new Chofer("Transportista", "transportista@example.com", "password123", "ABC123", "Camión Mercedes-Benz");
            choferRepo.save(choferLogin);
        };
    }
}
