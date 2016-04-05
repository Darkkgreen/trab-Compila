package AST;
import java.util.ArrayList;

public class Program{

	private Expr expr;
	private ArrayList<Variable> listV;

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
