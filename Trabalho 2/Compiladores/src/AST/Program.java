package AST;
import java.util.ArrayList;

public class Program{

	private ArrayList<Stmt> listS;
	private ArrayList<Variable> listV; // esquerda

	public Program(ArrayList<Variable> listV, ArrayList<Stmt> listS){
		this.listV = listV;
		this.listS = listS;
	}

	public void genC(){
		System.out.println();
		System.out.println("void main () {");
		System.out.print("\t");


		for(Variable v:listV){
			v.genC();
			System.out.print("\t");
		}

//		expr.genC();
		System.out.println("\n}");
	}

}
