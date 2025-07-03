package main.java.coloniaDeRobots;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.util.BuscadorCaminos;
import main.java.coloniaDeRobots.util.CaminoEsperado;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class RobotLogistico extends ElementoLogistico {
	private final int capacidadCarga;
	private final double bateriaMaxima;
	private double bateriaActual;
	private double bateriaSimulacion;
	private List<Robopuerto> robopuertos; // para detectar recarga
	private List<Cofre> cofres;
	private final Deque<ElementoLogistico> ruta = new ArrayDeque<>();
	private Solicitud pedido;

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
	public boolean planificarRuta(Ubicacion origen, Ubicacion destino, double factorConsumo, Solicitud sol) throws ExcepcionLogistica {
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
		
		// Luego de la entrega, volver al último robopuerto.
		ultimoRobopuerto = caminoEntrega.getEstacionDeCarga().get(caminoEntrega.getEstacionDeCarga().size() - 1);
		CaminoEsperado retornoRobopuerto = BuscadorCaminos.calcularCaminoMasCorto(origen, ultimoRobopuerto.getUbicacion(), cofres, robopuertos);
		if(!haybateriaSuficiente(retornoRobopuerto, factorConsumo, false)) {
			this.bateriaSimulacion = this.bateriaActual;
			return false;
		}
		
		List<ElementoLogistico> caminoBusquedaColl = caminoBusqueda.getCamino();
		List<ElementoLogistico> caminoEntregaColl = caminoEntrega.getCamino();
		List<ElementoLogistico> caminoRobopuertoColl = retornoRobopuerto.getCamino();
		
		caminoBusquedaColl.remove(0);
		caminoEntregaColl.remove(0);
		caminoRobopuertoColl.remove(0);
		
		// Limpiar la ruta actual
		ruta.clear();
		
		// Agregar los caminos
		ruta.addAll(caminoBusquedaColl);
		ruta.addAll(caminoEntregaColl);
		ruta.addAll(caminoRobopuertoColl);
		
		// Asignar solicitud al robot
		this.pedido = sol;
		
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
	public void avanzar(double factorConsumo) {
		if (ruta.isEmpty())
			return;
		
		// Ubicación actual del robot
		ElementoLogistico siguiente = ruta.poll();
		double distancia = getUbicacion().calcularDistanciaA(siguiente.getUbicacion());
		double consumo = distancia * factorConsumo;
		
		if (getBateriaActual() < consumo) {
			// No puede avanzar sin recarga; cancelar ruta
			ruta.clear();
			return;
		}
		
		setBateriaActual(getBateriaActual() - consumo);
		
		// Actualizar posición
		this.ubicacion = siguiente.getUbicacion();
		
		// ACTIVAR POSICIÓN
		if(siguiente instanceof Robopuerto)
			this.recargar();
		else if (siguiente instanceof Cofre) {
			if(siguiente.getUbicacion().equals(pedido.getCofreDestino().getUbicacion())) {
				((Cofre) siguiente).retirarItem(pedido.getItem(), capacidadCarga);
			}
			
			if(siguiente.getUbicacion().equals(pedido.getCofreOrigen().getUbicacion())) {
				((Cofre) siguiente).agregarItem(pedido.getItem(), capacidadCarga);
				pedido.registrarEntrega(capacidadCarga);
			}
		}
		
	}

	public boolean puedeTransportar(int cantidad) {
		return cantidad <= capacidadCarga;
	}

	public boolean tieneBateriaPara(double distancia, double factor) {
		return getBateriaActual() >= distancia * factor;
	}

	public void consumirBateria(double cantidad) {
		setBateriaActual(Math.max(0, getBateriaActual() - cantidad));
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
	
	public boolean tareaCompleta() {
		if(this.pedido == null)
			return false;
		
		return this.pedido.estaCompletada();
	}
	
	public Solicitud devolverSolicitud() {
		return this.pedido;
	}
	
	public void finalizarSolicitud() {
		if(!tareaCompleta())
			return;
		
		this.pedido = null;
			
	}
}
