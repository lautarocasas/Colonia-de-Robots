```markdown
# Sistema Logístico Automatizado

**Proyecto:** Trabajo Práctico – Programación Avanzada (UNLaM)  
**Grupo:** Beta  
**Integrantes:** Agustín García Riveros, Federico Martucci, Tomás Branchesi, Juan Bernardez, Lautaro Casas  
**Docentes:** Verónica Aubin, Lucas Videla  
**Fecha de Entrega:** 03/07/2025  

---

## Descripción

Simulación de una red logística automatizada donde robots transportan ítems entre cofres inteligentes dentro del alcance de robopuertos. Se contemplan:

- **Cinco tipos de cofres**:  
  - **Provisión Activa**: empuja ítems automáticamente a solicitudes.  
  - **Provisión Pasiva**: almacena ítems a la espera de extracción.  
  - **Solicitud**: genera pedidos de ítems.  
  - **Intermedio**: solicita y puede proveer (búfer).  
  - **Almacenamiento**: guarda excedentes.  

- **Robopuertos** como nodos de cobertura y estaciones de recarga.  
- **Robots logísticos** con capacidad de carga y batería limitada.  
- **Planificación de rutas** óptimas con Dijkstra.  
- **Módulo de eventos** (Event Bus) para logging y métricas — implementación básica disponible.  
- **Métricas**: ciclos de simulación, número de transportes, distancia recorrida y recargas realizadas.

---

## Características

- **Carga dinámica** de la red desde archivos JSON (`src/main/resources/*.json`)  
- **Validación exhaustiva** de la configuración inicial:  
  - estructura completa (`robopuertos`, `cofres`, `robots`)  
  - rangos y valores válidos  
  - detección de ubicaciones duplicadas  
  - cobertura mínima  
  - factibilidad de solicitudes  
- **Arquitectura modular** y basada en patrones de diseño (Factory, Strategy, Observer/EventBus, Template Method, Singleton).  
- **Simulación por ciclos** hasta alcanzar estado estable o límite de iteraciones.  
- **Pruebas unitarias** con JUnit5 cubriendo >90% de código.  

---

## Estructura de Carpetas

```

.
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── coloniaDeRobots
│   │   │   │   ├── SistemaLogistico.java
│   │   │   │   ├── RobotLogistico.java
│   │   │   │   ├── Robopuerto.java
│   │   │   │   ├── Item.java
│   │   │   │   ├── Ubicacion.java
│   │   │   │   └── Solicitud.java
│   │   │   ├── coloniaDeRobots/cofres
│   │   │   │   ├── Cofre.java
│   │   │   │   ├── CofreProvisionActiva.java
│   │   │   │   ├── CofreProvisionPasiva.java
│   │   │   │   ├── CofreSolicitud.java
│   │   │   │   ├── CofreIntermedio.java
│   │   │   │   └── CofreAlmacenamiento.java
│   │   │   ├── coloniaDeRobots/util
│   │   │   │   ├── BuscadorCaminos.java
│   │   │   │   └── MetricsCollector.java
│   │   │   ├── coloniaDeRobots/eventos
│   │   │   │   ├── EventBus.java        ← implementación básica
│   │   │   │   ├── Evento.java
│   │   │   │   ├── CofreAccionadoEvent.java
│   │   │   │   ├── SolicitudRegistradaEvent.java
│   │   │   │   ├── TransporteGeneradoEvent.java
│   │   │   │   └── RobotEvent.java
│   │   │   ├── logistica/io
│   │   │   │   └── GestorArchivos.java
│   │   │   └── logistica/factory
│   │   │       ├── CofreFactory.java
│   │   │       ├── RobotFactory.java
│   │   │       └── RobopuertoFactory.java
│   │   └── resources
│   │       ├── config\_simple.json
│   │       ├── config\_extenso.json
│   │       └── config\_stress.json
│   └── test
│       └── java
│           └── ... (JUnit5 tests por clase)
└── docs
└── UML.md   ← diagrama de clases en Mermaid

````

---

## Requisitos Previos

- **Java 23** (JDK 23) instalado  
- **Jackson Databind 2.19.1** (.jar) en el classpath  
- **JUnit 5** (.jar) en el classpath para ejecutar tests  

---

## Compilación y Ejecución

1. **Compilar** todo:
   ```bash
   javac -cp ".:lib/*" src/main/java/**/*.java
````

2. **Ejecutar** simulación con configuración de ejemplo:

   ```bash
   java -cp ".:lib/*:src/main/java" main.java.coloniaDeRobots.Main
   ```

3. **Ejecutar pruebas**:

   ```bash
   javac -cp ".:lib/*:src/main/java" src/test/java/**/*.java
   java -cp ".:lib/*:src/main/java:src/test/java" org.junit.platform.console.ConsoleLauncher \
     --scan-classpath
   ```

---

## Patrón EventBus (parcial)

El **EventBus** permite publicar y suscribir eventos de forma desacoplada. Actualmente cuenta con:

* Registro de listeners por tipo de evento.
* Publicación de eventos desde `SistemaLogistico`, `Cofre` y `RobotLogistico`.
* Listeners: `ConsoleLoggerListener`, `MetricsCollector`.

> **Nota:** Queda pendiente completar la gestión avanzada de suscripciones genéricas y la eliminación dinámica de listeners.

---

## Documentación y Bibliografía

* **Refactoring Guru** – [Design Patterns](https://refactoring.guru/es/design-patterns)
* Gamma, Helm, Johnson & Vlissides – *Design Patterns: Elements of Reusable Object-Oriented Software*
* Oracle Java API – *Jackson* y *Java Collections Framework*

---

### Licencia

Este código se entrega como parte del Trabajo Práctico de la materia y queda licenciado para uso académico en la UNLaM.
