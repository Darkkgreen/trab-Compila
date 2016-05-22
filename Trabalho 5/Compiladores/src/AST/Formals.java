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
public class Formals extends Expr{
	public Formals(ArrayList<Variable> listV){
		this.listV = listV;
	}
	
	private ArrayList<Variable> listV;
	
	public void add(Variable aux){
		listV.add(aux);
	}

	public ArrayList<Variable> getListV() {
		return listV;
	}

	public void setListV(ArrayList<Variable> listV) {
		this.listV = listV;
	}	

	@Override
	public StringBuffer genC(Integer tabs) {
		return null;
	}
	
}
