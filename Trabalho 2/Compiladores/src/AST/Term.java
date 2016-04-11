/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author ricke
 */
public class Term {
	private Factor factor;
	private ArrayList<String> muloplist;
	private ArrayList<Factor> factorlist;

	public Factor getFactor() {
		return factor;
	}

	public ArrayList<String> getMuloplist() {
		return muloplist;
	}

	public ArrayList<Factor> getFactorlist() {
		return factorlist;
	}

	public Term(Factor factor, ArrayList<String> muloplist, ArrayList<Factor> factorlist) {
		this.factor = factor;
		this.muloplist = muloplist;
		this.factorlist = factorlist;
	}


	
	
}
