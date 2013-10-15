package br.com.vale.controlevr.classes;

import java.io.Serializable;

public class Dia implements Serializable {
	
	private String dia;
	private String diaCompleto;
	private String position;
	private boolean selecionado;
	
	public Dia(String diaCompleto, String position, boolean selecionado) {
		setDiaCompleto(diaCompleto);
		setDia(diaCompleto.split("-")[2].replaceFirst("^0*", ""));
		setPosition(position);
		setSelecionado(selecionado);
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getDiaCompleto() {
		return diaCompleto;
	}

	public void setDiaCompleto(String diaCompleto) {
		this.diaCompleto = diaCompleto;
	}
}