/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
package AST;

public class NumberExpr extends Expr {

	private char num;

	public NumberExpr(char n){
		this.num = n;
	}

	public StringBuffer genC(Integer tabs){
                StringBuffer aux;
		return aux = new StringBuffer(num);
	}
}
