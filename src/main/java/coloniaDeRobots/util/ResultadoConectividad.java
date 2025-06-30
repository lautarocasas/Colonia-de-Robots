package main.java.coloniaDeRobots.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.coloniaDeRobots.cofres.Cofre;

public class ResultadoConectividad {
	public final List<Cofre> cofresAccesibles;
	public final List<Cofre> cofresInaccesibles;

	public ResultadoConectividad(List<Cofre> cofresAccesibles, List<Cofre> cofresInaccesibles) {
		this.cofresAccesibles = Collections.unmodifiableList(new ArrayList<>(cofresAccesibles));
		this.cofresInaccesibles = Collections.unmodifiableList(new ArrayList<>(cofresInaccesibles));
	}
}
