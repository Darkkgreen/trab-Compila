package AST;

public class Variable{
	private char letra;
	private NumberExpr num;

	public Variable(char letra, NumberExpr num){
		this.letra = letra;
		this.num = num;
	}

	public void genC(){
		System.out.print("\t int "+letra+" = ");
		num.genC();
		System.out.println(";");
	}
}
