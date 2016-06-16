/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author Charizard
 */
public class WhileStmt {

	private ArrayList<Stmt> stmt;
	private Expr expr;

	public WhileStmt(ArrayList<Stmt> stmt, Expr expr) {
		this.stmt = stmt;
		this.expr = expr;
	}

	public ArrayList<Stmt> getStmt() {
		return stmt;
	}

	public void setStmt(ArrayList<Stmt> stmt) {
		this.stmt = stmt;
	}

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public StringBuffer genC(Integer tabs) {
		StringBuffer aux = new StringBuffer();
                StringBuffer tab = new StringBuffer();

		Integer i;
		for (i = 0; i < tabs; i++) {
			tab.append("\t");
		}
                
                aux.append(tab);
		aux.append("while(");
		aux.append(expr.genC(0));
		aux.append("){\n");

		for (Stmt s : stmt) {
			aux.append(s.genC(tabs + 1));
                        aux.append("\n");
		}
                
                aux.append(tab);
		aux.append("}");

		return aux;
	}

}
