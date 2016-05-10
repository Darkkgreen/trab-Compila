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

	public String getName() {
		return name;
	}

	public void genC(){
		type.genC();
		System.out.print(" ");
		System.out.print(name);
		System.out.print(";");
	}
}
