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
public class Stmt {
    private IfStmt se;
    private WhileStmt enquanto;
    private boolean parada;
    private PrintStmt escrever;
    private Expr expressao;

    public Stmt(IfStmt se, WhileStmt enquanto, boolean parada, PrintStmt escrever, Expr expressao) {
        this.se = se;
        this.enquanto = enquanto;
        this.parada = parada;
        this.escrever = escrever;
        this.expressao = expressao;
    }

    public IfStmt getSe() {
        return se;
    }

    public void setSe(IfStmt se) {
        this.se = se;
    }

    public WhileStmt getEnquanto() {
        return enquanto;
    }

    public void setEnquanto(WhileStmt enquanto) {
        this.enquanto = enquanto;
    }

    public boolean isParada() {
        return parada;
    }

    public void setParada(boolean parada) {
        this.parada = parada;
    }

    public PrintStmt getEscrever() {
        return escrever;
    }

    public void setEscrever(PrintStmt escrever) {
        this.escrever = escrever;
    }
    
    public Expr getExpressao() {
        return expressao;
    }

    public void setExpressao(Expr expressao) {
        this.expressao = expressao;
    }
}
