/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author ricke
 */
public class FunctionDecl extends Expr {
	
	private Type type;
	private String ident;
	private StmtBlock stmt;
	private Formals formals;
	
	public FunctionDecl(Type type, String ident, StmtBlock stmt, Formals formals){
		this.type = type;
		this.ident = ident;
		this.stmt = stmt;
		this.formals = formals;
	}

	public Formals getFormals() {
		return formals;
	}

	public Type getType() {
		return type;
	}

	public String getIdent() {
		return ident;
	}

	public StmtBlock getStmt() {
		return stmt;
	}

	@Override
	public StringBuffer genC(Integer tabs) {
		StringBuffer aux = new StringBuffer();
                
                aux.append(type.genC(tabs));
                aux.append(" ");
                aux.append(ident);
                aux.append("(");
                aux.append(formals.genC(tabs));
                aux.append(")");
                aux.append(stmt.genC(tabs+1));
                
                return aux;
	}

	
}
