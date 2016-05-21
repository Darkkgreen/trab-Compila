package AST;

import java.util.ArrayList;

public class Program {

	private ArrayList<Stmt> listS;
	private ArrayList<Variable> listV; // esquerda

	public Program(ArrayList<Variable> listV, ArrayList<Stmt> listS) {
		this.listV = listV;
		this.listS = listS;
	}

	public StringBuffer genC(){
                StringBuffer aux = new StringBuffer("#include <stdio.h>\n#include <stdlib.h>\n\nvoid main(){");
                StringBuffer variable;
                StringBuffer stmt;

		for(Variable v:listV){
                    variable = v.genC();
                    aux.append(variable);
                    aux.append("\n");
		}

		for(Stmt s:listS){
                    stmt = s.genC();
                    aux.append(stmt);
                    aux.append("\n");
                }
                
		aux.append("}");
                
                return aux;
	}

}
