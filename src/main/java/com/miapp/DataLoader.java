package com.miapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.Usuarios.Transportistas.Chofer;
import com.miapp.model.cargas.Carga;
import com.miapp.model.envio.Envio;
import com.miapp.model.envio.EstadoEnvio;
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
            // Limpiar datos previos
            envioRepo.deleteAll();
            tarifaRepo.deleteAll();
            choferRepo.deleteAll();
            clienteRepo.deleteAll();

            // Clientes y choferes de prueba
            Cliente cliente1 = new Cliente("Juan Perez", "juan.perez@example.com");
            cliente1.setTelefono("+54 9 11 1111-1111");
            Cliente cliente2 = new Cliente("Maria Lopez", "maria.lopez@example.com");
            cliente2.setTelefono("+54 9 11 2222-2222");
            clienteRepo.save(cliente1);
            clienteRepo.save(cliente2);

            Chofer chofer1 = new Chofer("Carlos Gomez", "ABC123");
            chofer1.setTelefono("+54 9 11 3333-3333");
            Chofer chofer2 = new Chofer("Luis Fernandez", "XYZ789");
            chofer2.setTelefono("+54 9 11 4444-4444");
            choferRepo.save(chofer1);
            choferRepo.save(chofer2);

            // Tarifas base
            tarifaRepo.save(new TarifaPorKg("Tarifa Basica", 10.0));
            tarifaRepo.save(new TarifaPorKg("Tarifa Premium", 20.0));
            tarifaRepo.save(new TarifaPorKg("Tarifa por Kg", 0.50));
            tarifaRepo.save(new TarifaPorEspacio("Tarifa por m3", 0.80));
            tarifaRepo.save(new TarifaPorKm("Tarifa por Km", 26));
            tarifaRepo.save(new TarifaPorPallet("Tarifa por Pallets", 50.0));
            // Porcentaje de urgencia (por ejemplo 15% = 0.15)
            tarifaRepo.save(new com.miapp.model.tarifas.TarifaFija("Tarifa Urgencia", 0.15));

            // Envíos iniciales genéricos
            Carga c1 = new Carga(200.0, 1.2, 0.8, 1.0);
            Carga c2 = new Carga(350.0, 1.4, 1.0, 1.2);
            Envio e1 = new Envio("ENV-1", cliente1, chofer1, c1);
            e1.setOrigen("Zatti 1495, Viedma, Argentina");
            e1.setDestino("Jose Garibaldi 522, Carmen de Patagones");
            e1.setEstado(EstadoEnvio.EN_TRANSITO);
            e1.setCostoTotal(25000.0);
            Envio e2 = new Envio("ENV-2", cliente2, chofer2, c2);
            e2.setOrigen("Zatti 1495, Viedma, Argentina");
            e2.setDestino("Jose Garibaldi 522, Carmen de Patagones");
            e2.setEstado(EstadoEnvio.ENTREGADO);
            e2.setCostoTotal(18000.0);
            envioRepo.save(e1);
            envioRepo.save(e2);

            // Usuarios de ejemplo con login
            Cliente clienteLogin = new Cliente("Usuario", "usuario@example.com", "password123");
            clienteLogin.setTelefono("+54 9 11 5555-5555");
            clienteRepo.save(clienteLogin);
            Chofer choferLogin = new Chofer("Transportista", "transportista@example.com", "password123", "ABC123", "Camion Mercedes-Benz");
            choferLogin.setTelefono("+54 9 11 6666-6666");
            choferLogin.setEstado(Chofer.Estado.ACEPTADO);
            choferRepo.save(choferLogin);

            // Envíos del usuario de ejemplo
            Carga c3 = new Carga(120.0, 1.2, 0.8, 1.1);
            c3.setPallets(2);
            Carga c4 = new Carga(300.0, 1.6, 1.0, 1.2);
            c4.setPallets(3);

            // Disponible (sin chofer) para que lo acepte un chofer
            Envio eDisponible = new Envio();
            eDisponible.setCodigoSeguimiento("ALI-" + System.currentTimeMillis());
            eDisponible.setCliente(clienteLogin);
            eDisponible.setCarga(c3);
            eDisponible.setOrigen("Buenos Aires, Argentina");
            eDisponible.setDestino("Rosario, Santa Fe, Argentina");
            eDisponible.setCostoTotal(200.0);
            eDisponible.setEstado(EstadoEnvio.EN_ESPERA);
            envioRepo.save(eDisponible);

            // Asignado al chofer de ejemplo
            Envio eAsignado = new Envio();
            eAsignado.setCodigoSeguimiento("ALI-" + (System.currentTimeMillis() + 1));
            eAsignado.setCliente(clienteLogin);
            eAsignado.setChofer(choferLogin);
            eAsignado.setCarga(c4);
            eAsignado.setOrigen("Cordoba, Argentina");
            eAsignado.setDestino("Mendoza, Argentina");
            eAsignado.setCostoTotal(500.0);
            eAsignado.setEstado(EstadoEnvio.EN_TRANSITO);
            envioRepo.save(eAsignado);
        };
    }
}
