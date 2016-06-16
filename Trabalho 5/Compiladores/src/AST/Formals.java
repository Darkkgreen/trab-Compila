/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author ricke
 */
public class Formals extends Expr{
	public Formals(ArrayList<Variable> listV){
		this.listV = listV;
	}
	
	private ArrayList<Variable> listV;
	
	public void add(Variable aux){
		listV.add(aux);
	}

	public ArrayList<Variable> getListV() {
		return listV;
	}

	public void setListV(ArrayList<Variable> listV) {
		this.listV = listV;
	}	

	@Override
	public StringBuffer genC(Integer tabs) {
            StringBuffer aux = new StringBuffer();
            
            if(!listV.isEmpty()){
                if(listV.size() == 1){
                    aux.append(listV.get(0).genC(0));
                }else{
                    for(Variable v:listV){
                        aux.append(v.genC(0));
                        if(listV.size() != (listV.lastIndexOf(v) + 1)){
                            aux.append(", ");
                        }
                    }
                }
            }
            
            return aux;
	}
	
}
