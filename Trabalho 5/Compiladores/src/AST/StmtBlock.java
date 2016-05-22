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
public class StmtBlock extends Expr{

	private ArrayList<Stmt> listS;
	private ArrayList<Variable> listV; // esquerda

	public ArrayList<Stmt> getListS() {
		return listS;
	}

	public ArrayList<Variable> getListV() {
		return listV;
	}
	
	public StmtBlock(ArrayList<Variable> listV, ArrayList<Stmt> listS){
		this.listS = listS;
		this.listV = listV;
	}

	@Override
	public StringBuffer genC(Integer tabs) {
		return null;
	}

}
