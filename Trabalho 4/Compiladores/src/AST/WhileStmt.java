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
    
    public StringBuffer genC(){
        StringBuffer aux =  new StringBuffer("while(");
        StringBuffer exp;
        StringBuffer st;
        
        exp = expr.genC();
        aux.append(exp);
        aux.append("){\n");
        
        for(Stmt s:stmt){
            st = s.genC();
            aux.append(st);
            aux.append("\n");
        }
        
        aux.append("}\n");
        
        return aux;
    }
    
}
