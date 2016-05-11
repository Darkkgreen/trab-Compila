package AST;

import java.util.ArrayList;

public class Program {

	private ArrayList<Stmt> listS;
	private ArrayList<Variable> listV; // esquerda

	public Program(ArrayList<Variable> listV, ArrayList<Stmt> listS) {
		this.listV = listV;
		this.listS = listS;
	}

	public void genC(){
		System.out.println("void main () {");

		for(Variable v:listV){
                    v.genC();
                    System.out.println();
		}

		for(Stmt s:listS){
                    s.genC();
                    System.out.println();
                }
                
		System.out.println("\n }");
	}

}
