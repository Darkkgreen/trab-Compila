/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
package AST;

import Lexer.Symbol;
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

    public StringBuffer genC(Integer tabs) {
        StringBuffer aux = new StringBuffer();
        StringBuffer aux2 = new StringBuffer();
        StringBuffer tab = new StringBuffer();

        Integer i;

        for (i = 0; i < tabs; i++) {
            tab.append("\t");
        }

        aux.append(tab);
        aux.append("printf(\"");

        for (i = 0; i < lista.size(); i++) {
            if(((CompositeExpr)lista.get(i)).getType() == Symbol.CHAR){
                aux.append("%c");
            }else if(((CompositeExpr)lista.get(i)).getType() == Symbol.STRING){
                aux.append("%s");
            }else if(((CompositeExpr)lista.get(i)).getType() == Symbol.DOUBLE){
                aux.append("%f");
            }else if((((CompositeExpr)lista.get(i)).getType() == Symbol.INTEGER)||(((CompositeExpr)lista.get(i)).getType() == Symbol.NUMBER)){
                aux.append("%d");
            }
            aux2.append(", ");
            aux2.append(lista.get(i).genC(0));
        }
        aux.append("\"");
        aux.append(aux2);        
        aux.append(");\n");

        return aux;
    }

}
