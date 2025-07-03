package main.java.coloniaDeRobots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.util.ResultadoConectividad;
import main.java.coloniaDeRobots.util.ValidadorConectividad;
import main.java.coloniaDeRobots.util.ValidadorFactibilidad;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.SolicitudInviableException;
import main.java.logistica.excepciones.UbicacionDuplicadaException;

/**
 * Builder para configurar y validar SistemaLogistico antes de ejecutar la simulación.
 */
public class SistemaLogisticoBuilder {

    private double factorConsumo;
    private final List<Cofre> cofres = new ArrayList<>();
    private final List<Robopuerto> puertos = new ArrayList<>();
    private final List<RobotLogistico> robots = new ArrayList<>();

    /**
     * Establece el factor de consumo de energía por unidad de distancia.
     */
    public SistemaLogisticoBuilder withFactorConsumo(double factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Factor de consumo debe ser > 0");
        }
        this.factorConsumo = factor;
        return this;
    }

    /**
     * Agrega una colección de cofres al sistema.
     */
    public SistemaLogisticoBuilder addCofres(Collection<Cofre> listaCofres) {
        Objects.requireNonNull(listaCofres, "Lista de cofres no puede ser null");
        this.cofres.addAll(listaCofres);
        return this;
    }

    /**
     * Agrega una colección de robopuertos al sistema.
     */
    public SistemaLogisticoBuilder addRobopuertos(Collection<Robopuerto> listaPuertos) {
        Objects.requireNonNull(listaPuertos, "Lista de robopuertos no puede ser null");
        this.puertos.addAll(listaPuertos);
        return this;
    }

    /**
     * Agrega una colección de robots al sistema.
     */
    public SistemaLogisticoBuilder addRobots(Collection<RobotLogistico> listaRobots) {
        Objects.requireNonNull(listaRobots, "Lista de robots no puede ser null");
        this.robots.addAll(listaRobots);
        return this;
    }

    /**
     * Ejecuta todas las validaciones y construye el SistemaLogistico.
     * @throws ExcepcionLogistica en caso de error en validaciones
     */
    public SistemaLogistico build() throws ExcepcionLogistica {
        // 1. Validar ubicaciones duplicadas entre cofres y puertos
        validarUbicaciones();

        // 2. Validar conectividad y filtrar cofres inaccesibles
        ResultadoConectividad cr = ValidadorConectividad.validarConectividad(cofres, puertos);
        if (!cr.cofresInaccesibles.isEmpty()) {
            System.out.println(String.format(
                    "Se omitiran %d cofres fuera de cobertura: %s",
                    cr.cofresInaccesibles.size(), cr.cofresInaccesibles
                ));
        }
        List<Cofre> accesibles = cr.cofresAccesibles;

        // 3. Validar factibilidad de solicitudes
        try {
            ValidadorFactibilidad.validarFactibilidad(accesibles);
        } catch (SolicitudInviableException e) {
            throw e;
        }

        // 4. Construcción del sistema
        SistemaLogistico sistema = new SistemaLogistico(factorConsumo);
        accesibles.forEach(sistema::agregarCofre);
        puertos.forEach(sistema::agregarRobopuerto);
        robots.forEach(sistema::agregarRobot);
        System.out.println("Sistema Logistico construido con exito");
        return sistema;
    }

    private void validarUbicaciones() throws UbicacionDuplicadaException {
        Set<String> llaves = new HashSet<>();
        for (Cofre c : cofres) {
            String key = c.getUbicacion().toString();
            if (!llaves.add(key)) {
                throw new UbicacionDuplicadaException("Ubicacion duplicada de cofre: " + key);
            }
        }
        for (Robopuerto rp : puertos) {
            String key = rp.getUbicacion().toString();
            if (!llaves.add(key)) {
                throw new UbicacionDuplicadaException("Ubicacion duplicada de robopuerto: " + key);
            }
        }
    }
}
