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
public class CompositeExpr extends Expr{
	private SimExpr simexpr;
	private String relOp;
	private CompositeExpr expr;

	public CompositeExpr(SimExpr simexpr, String relOp, CompositeExpr expr) {
		this.simexpr = simexpr;
		this.relOp = relOp;
		this.expr = expr;
	}

	public SimExpr getSimexpr() {
		return simexpr;
	}

	public String getRelOp() {
		return relOp;
	}

	public CompositeExpr getExpr() {
		return expr;
	}

	public void genC() {
		// não implementado
	}
}
