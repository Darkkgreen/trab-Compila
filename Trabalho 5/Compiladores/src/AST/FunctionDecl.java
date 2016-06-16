/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
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
                
                aux.append(type.genC(0));
                aux.append(" ");
                aux.append(ident);
                aux.append("(");
                if(formals != null)
                    aux.append(formals.genC(0));
                aux.append(")");
                aux.append(stmt.genC(0));
                
                return aux;
	}

	
}
