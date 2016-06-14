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
                StringBuffer aux = new StringBuffer();
                StringBuffer tab = new StringBuffer();
                
                Integer i;
		for (i = 0; i < tabs; i++) {
			tab.append("\t");
		}
                
                aux.append(tab);
                aux.append("{\n");
                
                for (Variable var : listV){
			aux.append(var.genC(tabs+1));
			aux.append("\n");
                }
                
                aux.append("\n");
                
                for (Stmt princ : listS) {
			aux.append(princ.genC(tabs+1));
			aux.append("\n");
                }
                
                aux.append(tab);
                aux.append("}\n");
                
		return aux;
	}

}
