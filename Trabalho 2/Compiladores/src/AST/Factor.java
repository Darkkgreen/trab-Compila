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
public class Factor extends Expr
{
	private LValue lvalue;
	private Expr expr;	
	private String function;
	private String numberDouble;
        private char singleChar;
	private Integer number;
        private boolean solo;
	private Symbol type;

	public Symbol getType() {
		return type;
	}

        public boolean getSolo(){
            return solo;
        }
        
        public char getSingleChar() {
            return singleChar;
        }

        public void setSingleChar(char singleChar) {
            this.singleChar = singleChar;
        }
	
	public boolean isLValueExist(){
		return lvalue == null ? false : true;
	}
	
	public boolean isOnlyNumber(){
		return (lvalue == null)&&(expr == null)&&(function == null)&&(singleChar == '\0')&&(numberDouble == null)&&(number != null) ? true : false;
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
	

	public Factor(LValue lvalue, Expr expr, String function, Integer number, String numberDouble, char singleChar, Symbol type) {
		this.lvalue = lvalue;
		this.expr = expr;
		this.function = function;
		this.number = number;
		this.numberDouble = numberDouble;
                this.singleChar = singleChar;
		this.type = type;
		
		if(type == null){
			if(expr == null && lvalue == null){
				// nothing to do
			}else if(expr != null){
//				type = expr.getType();								
			}else if(lvalue != null){
				lvalue.getType();
			}
		}
//                System.out.println(type+"DENTDO ROD FACTOE");
                if(expr == null && function == null && number == null && numberDouble == null && singleChar == '\0')
                    this.solo = true;
                else
                    this.solo = false;
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
