package AST;
import java.util.ArrayList;

public class Program{

	private Expr expr; // direita
	private ArrayList<Variable> listV; // esquerda

	public Program(ArrayList<Variable> listV, Expr expr){
		this.listV = listV;
		this.expr = expr;
	}

	public void genC(){
		System.out.println("void main () {");
		System.out.print("\t");


		for(Variable v:listV){
			v.genC();
		}

		expr.genC();
		System.out.println("\n }");
	}

}
