/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author Charizard
 */
public class IfStmt {
    private Expr expr;
    private Stmt stmt;
    private Stmt opcional; //aqui entra o e

    public IfStmt(Expr expr, Stmt stmt, Stmt opcional) {
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

    public Stmt getStmt() {
        return stmt;
    }

    public void setStmt(Stmt stmt) {
        this.stmt = stmt;
    }

    public Stmt getOpcional() {
        return opcional;
    }

    public void setOpcional(Stmt opcional) {
        this.opcional = opcional;
    }
    
    
}
