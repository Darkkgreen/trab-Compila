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

	public void genC() {
		// não implementado
	}
	
}
