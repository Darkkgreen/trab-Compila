/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author ricke
 */
public class LValue extends Expr{

	String ident;
	String exprIdent;
	Expr expr;
	
	public String getIdent() {
		return ident;
	}

	public Expr getExpr() {
		return expr;
	}

	public LValue(String ident, Expr expr) {
		this.ident = ident;
		this.expr = expr;
	}
	
	public void genC(){
		if(ident != null){
			System.out.print(ident);
			if(expr != null){
				System.out.print('[');
				expr.genC();
				System.out.print(']');				
			}
		}
	}

}
