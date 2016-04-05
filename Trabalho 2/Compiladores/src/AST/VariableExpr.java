package AST;

public class VariableExpr extends Expr {

	private char letter;

	public VariableExpr(char n){
		this.letter = n;
	}

	public void genC(){
		System.out.print(letter);
	}
}
