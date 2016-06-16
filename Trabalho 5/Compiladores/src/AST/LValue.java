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
public class LValue extends Expr{

	String ident;
	Expr expr;
	Type type;
	
	public String getIdent() {
		return ident;
	}

	public Expr getExpr() {
		return expr;
	}

	public LValue(String ident, Expr expr, Type type) {
		this.ident = ident;
		this.expr = expr;
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public StringBuffer genC(Integer tabs){
            StringBuffer aux = new StringBuffer();
            
            aux.append(ident);
            if(expr != null)
            {
                aux.append("[");
                aux.append(expr.genC(0));
                aux.append("]");
            }
            
            return aux;
	}

}
