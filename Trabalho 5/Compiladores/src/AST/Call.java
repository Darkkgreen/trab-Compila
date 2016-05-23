/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author floss
 */
public class Call extends Expr{

	public Call(ArrayList<Expr> actuals, String ident){
		this.actuals = actuals;
		this.ident = ident;
	}

	public ArrayList<Expr> getActuals() {
		return actuals;
	}

	public String getIdent() {
		return ident;
	}

	private ArrayList<Expr> actuals;
	private String ident;

	@Override
	public StringBuffer genC(Integer tabs) {
		return null;
	}
	
}
