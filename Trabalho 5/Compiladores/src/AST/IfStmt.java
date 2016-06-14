/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author Charizard
 */
public class IfStmt {

	private Expr expr;
	private ArrayList<Stmt> stmt;
	private ArrayList<Stmt> opcional; //aqui entra o e

	public IfStmt(Expr expr, ArrayList<Stmt> stmt, ArrayList<Stmt> opcional) {
		this.expr = expr;
		this.stmt = stmt;
		this.opcional = opcional;
	}

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public ArrayList<Stmt> getStmt() {
		return stmt;
	}

	public void setStmt(ArrayList<Stmt> stmt) {
		this.stmt = stmt;
	}

	public ArrayList<Stmt> getOpcional() {
		return opcional;
	}

	public void setOpcional(ArrayList<Stmt> opcional) {
		this.opcional = opcional;
	}

	public StringBuffer genC(Integer tabs) {
		StringBuffer aux = new StringBuffer();
                StringBuffer tab = new StringBuffer();
                Integer i;
                
                for (i = 0; i < tabs; i++) {
                    tab.append("\t");
                }
                
                aux.append(tab);
		aux.append("if(");
		aux.append(expr.genC(0));
		aux.append("){\n");

		for (Stmt princ : stmt) {
			aux.append(princ.genC(tabs+1));
			aux.append("\n");
		}
                aux.append(tab);
		aux.append("}");

		if (!opcional.isEmpty()) {
			aux.append("else{\n");
			for (Stmt op : opcional) {
				aux.append(op.genC(tabs+1));
				aux.append("\n");
			}
                        aux.append(tab);
			aux.append("}\n");
		}

		return aux;
	}
}
