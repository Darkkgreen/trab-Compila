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
public class Factor
{
	private LValue lvalue;
	private Expr expr;	
	private String function;
	private Integer number;

	public Factor(LValue lvalue, Expr expr, String function, Integer number) {
		this.lvalue = lvalue;
		this.expr = expr;
		this.function = function;
		this.number = number;
	}
	
	public LValue getLvalue() {
		return lvalue;
	}

	public Expr getExpr() {
		return expr;
	}

	public String getFunction() {
		return function;
	}

}
