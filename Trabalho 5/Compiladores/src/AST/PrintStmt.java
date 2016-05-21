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
    
     public StringBuffer genC(){
        StringBuffer aux = new StringBuffer("printf(");
        StringBuffer exp;
        
        if(!lista.isEmpty()){
            if(lista.size() == 1){
                exp = lista.get(0).genC();
                aux.append(exp);
            }else{
                for(Expr e:lista){
                    exp = e.genC();
                    aux.append(exp);
                    if(lista.size() != (lista.lastIndexOf(e) - 1)){
                        aux.append(", ");
                    }
                }
            }
        }
        aux.append(");");
        
        return aux;
    }
    
}
