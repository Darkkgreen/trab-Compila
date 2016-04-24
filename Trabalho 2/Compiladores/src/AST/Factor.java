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
public class Factor extends Expr
{
	private LValue lvalue;
	private Expr expr;	
	private String function;
	private String numberDouble;

	public String getNumberDouble() {
		return numberDouble;
	}

	public void setNumberDouble(String numberDouble) {
		this.numberDouble = numberDouble;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	private Integer number;

	public Factor(LValue lvalue, Expr expr, String function, Integer number, String numberDouble) {
		this.lvalue = lvalue;
		this.expr = expr;
		this.function = function;
		this.number = number;
		this.numberDouble = numberDouble;
	}
	
	public LValue getLvalue() {
		return lvalue;
	}

	public Expr getExpr() {
		return expr;
	}

	public String getFunction() {
		return function;
	}

	public void genC(){
		if((lvalue != null)&&(expr != null)){
			lvalue.genC();
			System.out.print(" := ");
			expr.genC();
		}else if(lvalue != null){
			lvalue.genC();
		}else if(number != null){
			System.out.print(number);
		}else if(numberDouble != null){
			System.out.print(numberDouble);
		}else if(expr != null){
			System.out.print('(');
			expr.genC();
			System.out.print(')');
		}else if(function != null){
			System.out.print(function);
		}
	}
}
