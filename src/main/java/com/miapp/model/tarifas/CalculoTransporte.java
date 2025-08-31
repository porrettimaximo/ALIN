package com.miapp.model.tarifas;

public class CalculoTransporte {

    private static final double FACTOR_CONSUMO = 2.5; // KM por litro de combustible
    private static final double IVA = 0.16; // Impuesto del 16%
    private static final double PROFIT = 0.05; // Profit del 5%

    public static double calcularLitrosNecesarios(double distanciaKm) {
        return distanciaKm / FACTOR_CONSUMO;
    }

    public static double calcularCostoTransporte(double distanciaKm, double precioCombustible) {
        double litrosNecesarios = calcularLitrosNecesarios(distanciaKm);
        return litrosNecesarios * precioCombustible;
    }

    public static double calcularTarifaCliente(double costoTransporte, double incrementoMin, double incrementoMax) {
        return costoTransporte * (1 + (incrementoMin + incrementoMax) / 2);
    }

    public static double calcularTotal(double tarifaCliente) {
        return tarifaCliente * (1 + PROFIT + IVA);
    }

    public static void mostrarCalculo(double distanciaKm, double precioCombustible, double incrementoMin, double incrementoMax) {
        double litrosNecesarios = calcularLitrosNecesarios(distanciaKm);
        double costoTransporte = calcularCostoTransporte(distanciaKm, precioCombustible);
        double tarifaCliente = calcularTarifaCliente(costoTransporte, incrementoMin, incrementoMax);
        double total = calcularTotal(tarifaCliente);

        System.out.println("Distancia: " + distanciaKm + " KM");
        System.out.println("Litros necesarios: " + litrosNecesarios);
        System.out.println("Costo transporte: " + costoTransporte);
        System.out.println("Tarifa cliente: " + tarifaCliente);
        System.out.println("Total con impuestos y profit: " + total);
    }
}
