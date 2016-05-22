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
	
	public StringBuffer genC(Integer tabs){
            StringBuffer aux = new StringBuffer();
            StringBuffer exp;
            
            aux.append(ident);
            if(expr != null)
            {
                aux.append("[");
                exp = expr.genC(tabs);
                aux.append(exp);
                aux.append("]");
            }
            
            return aux;
	}

}
