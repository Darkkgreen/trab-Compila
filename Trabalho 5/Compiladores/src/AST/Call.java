/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author floss
 */
public class Call extends Expr{

	public Call(ArrayList<Expr> actuals, String ident){
		this.actuals = actuals;
		this.ident = ident;
	}

	public ArrayList<Expr> getActuals() {
		return actuals;
	}

	public String getIdent() {
		return ident;
	}

	private ArrayList<Expr> actuals;
	private String ident;

	@Override
	public StringBuffer genC(Integer tabs) {
            StringBuffer aux = new StringBuffer();
            StringBuffer tab = new StringBuffer();
            Integer i;
            
            for (i = 0; i < tabs; i++) {
                tab.append("\t");
            }
            
            aux.append(ident);
            aux.append("(");
            
            if(actuals.size() == 1){
                aux.append(actuals.get(0).genC(0));
            }else{
                for(Expr e:actuals){
                    aux.append(e.genC(0));
                    if(actuals.size() != (actuals.lastIndexOf(e) - 1)){
                        aux.append(", ");
                    }
                }
            }
            
            aux.append(")\n");
            
            return aux;
	}
	
}
