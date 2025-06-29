package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.UbicacionDuplicadaException;
import main.java.logistica.excepciones.ValorInvalidoException;
import main.java.logistica.io.GestorArchivos;

class GestorArchivosTest {
    private static GestorArchivos loader;

    @BeforeAll
    static void setup() {
        loader = new GestorArchivos();
    }

    @Test
    void testValidSimple() {
        assertDoesNotThrow(() ->
            loader.cargarDesdeArchivo("src/main/resources/valid_simple.json")
        );
    }

    @ParameterizedTest
    @CsvSource({
        "src/main/resources/missing_robots.json, EstructuraInvalidaException",
        "src/main/resources/negative_alcance.json, ValorInvalidoException",
        "src/main/resources/duplicate_location.json, UbicacionDuplicadaException"
    })
    void testInvalidCases(String path, String exceptionName) {
        Class<? extends ExcepcionLogistica> expected;
        switch (exceptionName) {
            case "EstructuraInvalidaException": expected = EstructuraInvalidaException.class; break;
            case "ValorInvalidoException":     expected = ValorInvalidoException.class;     break;
            case "UbicacionDuplicadaException":expected = UbicacionDuplicadaException.class;break;
            default: throw new IllegalArgumentException("Excepción desconocida");
        }
        assertThrows(expected, () ->
            loader.cargarDesdeArchivo(path)
        );
    }

    @Test
    void testComplex() {
        assertDoesNotThrow(() ->
            loader.cargarDesdeArchivo("src/main/resources/complex.json")
        );
    }
}
