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
public class PrintStmt {
    private ArrayList<Expr> lista;
    
    public ArrayList<Expr> getLista() {
        return lista;
    }

    public void setLista(Expr expr) {
        this.lista.add(expr);
    }

    public PrintStmt(ArrayList<Expr> lista) {
        this.lista = lista;
    }
    
     public void genC(){
        System.out.println("printf(");
        if(!lista.isEmpty()){
            if(lista.size() == 1){
                lista.get(0).genC();
            }else{
                for(Expr e:lista){
                    e.genC();
                    System.out.print(", ");
                }
            }
        }
        System.out.println(");");
    }
    
}
