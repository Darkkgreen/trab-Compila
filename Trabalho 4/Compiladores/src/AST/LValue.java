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
	Expr expr;
	Type type;
	
	public String getIdent() {
		return ident;
	}

	public Expr getExpr() {
		return expr;
	}

	public LValue(String ident, Expr expr, Type type) {
		this.ident = ident;
		this.expr = expr;
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public void genC(){
            System.out.print(ident);
            if(expr != null)
            {
                System.out.print("[");
                expr.genC();
                System.out.print("]");
            }
	}

}
