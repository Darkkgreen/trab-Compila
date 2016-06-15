/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;

/**
 *
 * @author ricke
 */
public class Factor extends Expr {

	private Call call;
	private LValue lvalue;
	private CompositeExpr expr;
	private String function;
	private String numberDouble;
	private String singleChar;
	private Integer number;
	private boolean solo;
	private Symbol type;

	public Symbol getType() {
		return type;
	}

	public boolean getSolo() {
		return solo;
	}

	public String getSingleChar() {
		return singleChar;
	}


	public boolean isLValueExist() {
		return lvalue == null ? false : true;
	}

	public boolean isOnlyNumber() {
		return (lvalue == null) && (expr == null) && (function == null) && (singleChar.equals("\0")) && (numberDouble == null) && (number != null) ? true : false;
	}

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

	public Factor(LValue lvalue, CompositeExpr expr, String function, Integer number, String numberDouble, String singleChar, Symbol type, Call call) {
		this.lvalue = lvalue;
		this.expr = expr;
		this.function = function;
		this.number = number;
		this.numberDouble = numberDouble;
		this.singleChar = singleChar;
		this.type = type;
		this.call = call;

		if (type == null) {
			if (expr == null && lvalue == null) {
				// nothing to do
			} else if (expr != null) {
//				type = expr.getType();								
			} else if (lvalue != null) {
				lvalue.getType();
			}
		}
//                System.out.println(type+"DENTDO ROD FACTOE");

		if (lvalue != null && expr == null && function == null && number == null && numberDouble == null && singleChar.equals('\0')) {
			this.solo = true;
		} else {
			this.solo = false;
		}
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

	public StringBuffer genC(Integer tabs) {
		StringBuffer aux = new StringBuffer();

		Integer i;
		if ((lvalue != null) && (expr != null)) {
			if ((expr.getType() == Symbol.READDOUBLE) || (expr.getType() == Symbol.READCHAR) || (expr.getType() == Symbol.READINTEGER)) {
				for (i = 0; i < tabs; i++) {
					aux.append("\t");
				}
				aux.append(expr.genC(tabs));
				aux.append(lvalue.genC(tabs) + ")");
			} else {
				for (i = 0; i < tabs; i++) {
					aux.append("\t");
				}
				aux.append(lvalue.genC(tabs));
				aux.append(" = ");
                                if(expr.getType() == Symbol.CHAR){
                                    aux.append("\'");
                                    aux.append(expr.getSimexpr().getTerm().getFactor().getSingleChar());
                                    aux.append("\'");
                                }else{
                                    aux.append(expr.genC(tabs));
                                }
			}
		} else if (lvalue != null) {
			aux.append(lvalue.genC(0));
                } else if (call != null){
                        aux.append(call.genC(0));
		} else if (number != null) {
			aux.append(number);
		} else if (numberDouble != null) {
			aux.append(numberDouble);
		} else if (expr != null) {
			aux.append('(');
			aux.append(expr.genC(0));
			aux.append(')');
		} else if (function != null) {
			if (function.equals("readChar()")) {
				aux.append("scanf(\"%c\", &");
			} else if (function.equals("readDouble()")) {
				aux.append("scanf(\"%lf\", &");
			} else {
				aux.append("scanf(\"%d\", &");
			}
		} else if (singleChar.equals('\0')) {
			aux.append("\'" + singleChar + "\'");
		}

		return aux;
	}
}
