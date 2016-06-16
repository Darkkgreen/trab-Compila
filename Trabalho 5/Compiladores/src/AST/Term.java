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
 * @author ricke
 */
public class Term extends Expr{
	private Factor factor;
	private ArrayList<String> muloplist;
	private ArrayList<Factor> factorlist;
        private boolean solo;

        public boolean getSolo(){
                return solo;
        }
        
	public Factor getFactor() {
		return factor;
	}
	
	public boolean onlyOneFactor(){
		return (factorlist == null)&&(factor != null) ? true: false;
	}

	public ArrayList<String> getMuloplist() {
		return muloplist;
	}

	public ArrayList<Factor> getFactorlist() {
		return factorlist;
	}

	public Term(Factor factor, ArrayList<String> muloplist, ArrayList<Factor> factorlist, Symbol type) {
		this.factor = factor;
		this.muloplist = muloplist;
		this.factorlist = factorlist;
		this.type = type;
                
                if(muloplist == null && factorlist == null && factor.getSolo() == true)
                    this.solo = true;
                else
                    this.solo = false;
	}

	public StringBuffer genC(Integer tabs){
            StringBuffer aux = new StringBuffer();
            Integer contador = 0;
	    
            aux.append(factor.genC(tabs));
            
            if(muloplist != null && !muloplist.isEmpty()){
                for(String s: muloplist){
                    aux.append(" " + s);
                    aux.append(" " + factorlist.get(contador).genC(0));
                    contador++;
                }
            }
            
            return aux;
        }
	
	private Symbol type;

	public Symbol getType() {
		return type;
	}

	public void setType(Symbol type) {
		this.type = type;
	}
	

	
	
}
