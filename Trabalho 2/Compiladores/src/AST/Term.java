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
public class Term extends Expr{
	private Factor factor;
	private ArrayList<String> muloplist;
	private ArrayList<Factor> factorlist;
        private boolean solo;

        public boolean getSolo(){
                return solo;
        }
        
	public Factor getFactor() {
		return factor;
	}
	
	public boolean onlyOneFactor(){
		return (factorlist == null)&&(factor != null) ? true: false;
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
                
                if(muloplist == null && factorlist == null && factor.getSolo() == true)
                    this.solo = true;
                else
                    this.solo = false;
	}

	public void genC(){
		factor.genC();
		
	}

	
	
}
