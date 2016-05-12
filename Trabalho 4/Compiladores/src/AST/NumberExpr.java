package AST;

public class NumberExpr extends Expr {

	private char num;

	public NumberExpr(char n){
		this.num = n;
	}

	public StringBuffer genC(){
                StringBuffer aux;
		return aux = new StringBuffer(num);
	}
}
