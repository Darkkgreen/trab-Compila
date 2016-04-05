package AST;

public class CompositeExpr extends Expr {

	private char op;
	private Expr esq, dir;

	public CompositeExpr(char o, Expr e, Expr d){
		this.op = o;
		this.esq = e;
		this.dir = d;
	}

	public void genC(){
		System.out.print('(');
		esq.genC();
		System.out.print(op);
		dir.genC();
		System.out.print(')');
	}


}
