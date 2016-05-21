package AST;

public class VariableExpr extends Expr {

	private char letter;

	public VariableExpr(char n){
		this.letter = n;
	}

	public StringBuffer genC(){
                StringBuffer aux;
		return aux = new StringBuffer(letter);
	}
}
