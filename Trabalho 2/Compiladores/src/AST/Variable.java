package AST;

public class Variable extends Expr{
	private Type type;
	private String name; // nome da variável no caso feita pelo indent

	public Variable(String name, Type type){
		this.type = type;
		this.name = name;
	}

	public Type getType(){
		return this.type;
	}

	public void genC(){
		// NÃO IMPLEMENTADO
	}
}
