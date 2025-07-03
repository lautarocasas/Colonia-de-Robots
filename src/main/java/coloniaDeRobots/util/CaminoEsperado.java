package main.java.coloniaDeRobots.util;

import java.util.List;
import java.util.Map;

import main.java.coloniaDeRobots.ElementoLogistico;
import main.java.coloniaDeRobots.Robopuerto;

public class CaminoEsperado {
	private List<ElementoLogistico> camino;
	private Map<ElementoLogistico, ElementoLogistico> precedencia;
	private Map<ElementoLogistico, Double> distancia;
	private List<Robopuerto> estacionDeCarga;
	
	
	public CaminoEsperado(List<ElementoLogistico> camino, Map<ElementoLogistico, ElementoLogistico> precedencia,
			Map<ElementoLogistico, Double> distancia, List<Robopuerto> estacionDeCarga) {
		super();
		this.camino = camino;
		this.precedencia = precedencia;
		this.distancia = distancia;
		this.estacionDeCarga = estacionDeCarga;
	}
	
	public List<Robopuerto> getEstacionDeCarga() {
		return estacionDeCarga;
	}

	public void setEstacionDeCarga(List<Robopuerto> estacionDeCarga) {
		this.estacionDeCarga = estacionDeCarga;
	}

	public List<ElementoLogistico> getCamino() {
		return camino;
	}
	public Map<ElementoLogistico, ElementoLogistico> getPrecedencia() {
		return precedencia;
	}
	public Map<ElementoLogistico, Double> getDistancia() {
		return distancia;
	}

	public void setCamino(List<ElementoLogistico> camino) {
		this.camino = camino;
	}

	public void setPrecedencia(Map<ElementoLogistico, ElementoLogistico> precedencia) {
		this.precedencia = precedencia;
	}

	public void setDistancia(Map<ElementoLogistico, Double> distancia) {
		this.distancia = distancia;
	}
	
	
}
