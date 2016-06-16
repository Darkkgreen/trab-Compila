/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
package AST;

public class VariableExpr extends Expr {

	private char letter;

	public VariableExpr(char n){
		this.letter = n;
	}

	public StringBuffer genC(Integer tabs){
                StringBuffer aux;
		return aux = new StringBuffer(letter);
	}
}
