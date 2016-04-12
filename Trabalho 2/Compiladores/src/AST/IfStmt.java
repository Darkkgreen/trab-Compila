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
}
