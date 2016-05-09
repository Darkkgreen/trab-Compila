package AST;

import Lexer.Symbol;
import java.util.ArrayList;

public class SimExpr extends Expr {

	private String unary;
	private Term term;
	private ArrayList<String> addop;
	private ArrayList<Term> termList;
	
	public boolean onlyOneTerm(){
		return ((termList == null)&& (term != null)) ? true : false;
	}
        private boolean solo;

	public void genC(){

	}

        public boolean getSolo(){
                return solo;
        }
        
	public String getUnary() {
		return unary;
	}

	public Term getTerm() {
		return term;
	}

	public ArrayList<String> getAddop() {
		return addop;
	}

	public ArrayList<Term> getTermList() {
		return termList;
	}

	public SimExpr(String unary, Term term, ArrayList<String> addop, ArrayList<Term> termList, Symbol type) {
		this.unary = unary;
		this.term = term;
		this.addop = addop;
		this.termList = termList;
		this.type = type;
                
                if(addop == null && termList == null && term.getSolo() == true)
                    this.solo = true;
                else
                    this.solo = false;
                    
	}
	
	public String getLastAddOp(){
		if(addop != null){
			Integer last = addop.size();
			return addop.get(last-1);
		}
		return null;
	}
	
	public String getLastMulOp(){
		if(term != null){
			ArrayList<String> aux = term.getMuloplist();
			if(aux != null){
				Integer last;
				last = aux.size();
				return aux.get(last-1);
			}
		}

		return null;
	}
	
	private Symbol type;

	public Symbol getType() {
		return type;
	}

	public void setType(Symbol type) {
		this.type = type;
	}
	

}
