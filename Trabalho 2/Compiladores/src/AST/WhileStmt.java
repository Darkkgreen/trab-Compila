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
public class WhileStmt {
    private Stmt stmt;
    private Expr expr;

    public WhileStmt(Stmt stmt, Expr expr) {
        this.stmt = stmt;
        this.expr = expr;
    }
    
    public Stmt getStmt() {
        return stmt;
    }

    public void setStmt(Stmt stmt) {
        this.stmt = stmt;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
    
}
