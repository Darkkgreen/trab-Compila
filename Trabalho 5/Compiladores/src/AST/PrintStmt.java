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
    private String escrita;
    
    public ArrayList<Expr> getLista() {
        return lista;
    }

    public void setLista(Expr expr) {
        this.lista.add(expr);
    }

    public PrintStmt(ArrayList<Expr> lista, String escrita) {
        this.lista = lista;
        this.escrita = escrita;
    }
    
     public StringBuffer genC(Integer tabs){
        StringBuffer aux = new StringBuffer();
        StringBuffer tab = new StringBuffer();
        Integer i;
        
        for (i = 0; i < tabs; i++) {
            tab.append("\t");
        }       
        
        aux.append(tab);
        aux.append("printf(");
        
        if(escrita != null){
            aux.append("\"" + escrita + "\"");
            if(lista.size() != 0)
                aux.append(", ");
        }
        
        if(lista.size() != 0){
            if(lista.size() == 1){
                aux.append(lista.get(0).genC(tabs));
            }else{
                for(Expr e:lista){
                    aux.append(e.genC(tabs));
                    if(lista.size() != (lista.lastIndexOf(e) - 1)){
                        aux.append(", ");
                    }
                }
            }
        }
        aux.append(");\n");
        
        return aux;
    }
    
}
