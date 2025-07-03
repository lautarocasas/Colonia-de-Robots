package main.java.coloniaDeRobots;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.RobotEvent;
import main.java.coloniaDeRobots.util.BuscadorCaminos;
import main.java.coloniaDeRobots.util.CaminoEsperado;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class RobotLogistico extends ElementoLogistico {
	//private static final Logger LOGGER = Logger.getLogger(RobotLogistico.class.getName());
	private final int capacidadCarga;
	private final double bateriaMaxima;
	private double bateriaActual;
	private double bateriaSimulacion;
	private List<Robopuerto> robopuertos; // para detectar recarga
	private List<Cofre> cofres;
	private final Deque<Ubicacion> ruta = new ArrayDeque<>();

	/**
	 * @param ubicacion      Posición inicial
	 * @param capacidadCarga Unidades máximas que puede transportar
	 * @param bateriaMaxima  Células máximas de batería
	 */
	public RobotLogistico(Ubicacion ubicacion, int capacidadCarga, double capacidadBateria) {
		super(ubicacion);
		if (capacidadCarga <= 0 || capacidadBateria <= 0)
			throw new IllegalArgumentException("Capacidad invalida");
		this.capacidadCarga = capacidadCarga;
		this.bateriaMaxima = capacidadBateria;
		this.setBateriaActual(capacidadBateria);
		this.bateriaSimulacion = bateriaActual;
		// LOGGER.info(() -> String.format("Robot en %s: carga %d, batería %.2f",
		// ubicacion, capacidadCarga, capacidadBateria));
		System.out.println(String.format("Robot en %s: carga %d, bateria %.2f",
		 ubicacion, capacidadCarga, capacidadBateria));
	}

	/**
	 * Inyecta la lista de robopuertos y cofres para planificación.
	 */
	public void setEntornoLogistico(List<Robopuerto> puertos, List<Cofre> cofres) {
		this.robopuertos = puertos;
		this.cofres = cofres;
	}

	/**
	 * @return true si hay ruta pendiente
	 */
	public boolean tieneTarea() {
		return !ruta.isEmpty();
	}

	/**
	 * Planifica la ruta óptima hasta destino, pasando por consecución de
	 * ubicaciones.
	 * @return TODO
	 * @throws ExcepcionLogistica 
	 */
	public boolean planificarRuta(Ubicacion origen, Ubicacion destino, double factorConsumo) throws ExcepcionLogistica {
		Robopuerto ultimoRobopuerto;
		// Cargar la batería de simulación
		this.bateriaSimulacion = this.bateriaActual; 
		// Determinar la ruta más corta hacia la el cofre que proveera los elementos de la solicitud
		CaminoEsperado caminoBusqueda = BuscadorCaminos.calcularCaminoMasCorto(this.ubicacion, destino, cofres, robopuertos);
		
		// Validar si puedo volver al robopuerto
		if(!haybateriaSuficiente(caminoBusqueda, factorConsumo, true)) {
			this.bateriaSimulacion = this.bateriaActual;
			return false;
		}
		
		// Determinar la ruta desde el cofre que provee hasta el de la solicitud
		CaminoEsperado caminoEntrega = BuscadorCaminos.calcularCaminoMasCorto(destino, origen, cofres, robopuertos);
		if(!haybateriaSuficiente(caminoEntrega, factorConsumo, false)) {
			this.bateriaSimulacion = this.bateriaActual;
			return false;
		}
		
		ultimoRobopuerto = caminoEntrega.getEstacionDeCarga().get(caminoEntrega.getEstacionDeCarga().size() - 1);
		CaminoEsperado retornoRobopuerto = BuscadorCaminos.calcularCaminoMasCorto(origen, ultimoRobopuerto.getUbicacion(), cofres, robopuertos);
		if(!haybateriaSuficiente(retornoRobopuerto, factorConsumo, false)) {
			this.bateriaSimulacion = this.bateriaActual;
			return false;
		}
		//CaminoEsperado caminoRobopuerto = BuscadorCaminos.calcularCaminoMasCorto(destino, , cofres, robopuertos);
		ruta.clear();
		return true;
		
	}

	private boolean haybateriaSuficiente(CaminoEsperado caminoBusqueda
			, double factorConsumo
			, boolean idavuelta) {
		double consumoBateria = 0, distanciaInicial = 0, distanciaNueva = 0;
		
		List<ElementoLogistico> recorrido = caminoBusqueda.getCamino();
		
		for(int i = 0; i < recorrido.size() - 1; i++) {
			ElementoLogistico ubicacionVieja = recorrido.get(i);
			ElementoLogistico ubicacionNueva = recorrido.get(i+1);
			
			distanciaInicial = caminoBusqueda.getDistancia().get(ubicacionVieja);
			distanciaNueva = caminoBusqueda.getDistancia().get(ubicacionNueva);
			
			consumoBateria = (distanciaNueva - distanciaInicial)*factorConsumo;
			
			if(bateriaSimulacion < consumoBateria)
				return false;
			
			bateriaSimulacion -= consumoBateria;
			if(ubicacionNueva instanceof Robopuerto ) {
				bateriaSimulacion = this.bateriaMaxima;
			}		
		}
		
		// Parametro para calcular si tengo que hacer un ida y vuelta.
		// boca boca boca
		if(!idavuelta)
			return true;
		
		for(int i = recorrido.size() - 1; i >= 1; i--) {
			ElementoLogistico ubicacionVieja = recorrido.get(i);
			ElementoLogistico ubicacionNueva = recorrido.get(i-1);
			
			distanciaInicial = caminoBusqueda.getDistancia().get(ubicacionVieja);
			distanciaNueva = caminoBusqueda.getDistancia().get(ubicacionNueva);
			
			consumoBateria = (distanciaInicial - distanciaNueva)*factorConsumo;
			
			if(bateriaSimulacion < consumoBateria)
				return false;
			
			bateriaSimulacion -= consumoBateria;
			if(ubicacionNueva instanceof Robopuerto ) {
				bateriaSimulacion = this.bateriaMaxima;
			}		
		}
		
		return true;
	}

	/**
	 * Avanza al siguiente punto de la ruta, consumiendo batería.
	 */
	public void avanzar() {
		if (ruta.isEmpty())
			return;
		Ubicacion siguiente = ruta.poll();
		double distancia = getUbicacion().calcularDistanciaA(siguiente);
		double consumo = distancia * 1.0;//factor de consumofijo
		if (getBateriaActual() < consumo) {
			// No puede avanzar sin recarga; cancelar ruta
			ruta.clear();
			return;
		}
		setBateriaActual(getBateriaActual() - consumo);
		// Actualizar posición
		this.ubicacion = siguiente;
	}

	public boolean puedeTransportar(int cantidad) {
		return cantidad <= capacidadCarga;
	}

	public boolean tieneBateriaPara(double distancia, double factor) {
		return getBateriaActual() >= distancia * factor;
	}

	public void consumirBateria(double cantidad) {
		setBateriaActual(Math.max(0, getBateriaActual() - cantidad));
		//LOGGER.fine(() -> String.format("Robot en %s consumió %.2f batería (resta %.2f)", ubicacion, cantidad,
				//getBateriaActual()));
		System.out.println(String.format("Robot en %s consumio %.2f batería (resta %.2f)", ubicacion, cantidad,getBateriaActual()));
	}

	/**
	 * Si el robot está dentro del alcance de algún robopuerto, retorna ese
	 * robopuerto.
	 */
	public Robopuerto getRobopuertoActual() {
		if (robopuertos == null)
			return null;
		for (Robopuerto rp : robopuertos) {
			if (rp.cubre(getUbicacion())) {
				return rp;
			}
		}
		return null;
	}

	/**
	 * Recarga completamente la batería.
	 */
	public void recargar() {
		Robopuerto rp = getRobopuertoActual();
		if (rp != null) {
			setBateriaActual(bateriaMaxima);
			//EventBus.getDefault().post(new RobotEvent(this, rp));
			System.out.println("Recargando bateria del robot en robopuerto");
		}
	}

	/**
	 * @return la capacidad máxima de carga
	 */
	public int getCapacidadCarga() {
		return capacidadCarga;
	}

	/**
	 * @return nivel actual de batería
	 */
	public double getBateriaActual() {
		return bateriaActual;
	}

	public void setBateriaActual(double bateriaActual) {
		this.bateriaActual = bateriaActual;
	}
}
