package AST;

public class NumberExpr extends Expr {

	private char num;

	public NumberExpr(char n){
		this.num = n;
	}

	public void genC(){
		System.out.print(num);
	}
}
