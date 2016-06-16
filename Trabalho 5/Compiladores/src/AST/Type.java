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
public class Type extends Expr{
	private boolean array;
	private Symbol type;
        private Integer size;

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public Symbol getType() {
		return type;
	}

	public void setType(Symbol type) {
		this.type = type;
	}

	public Type(Symbol type, boolean array, Integer sizeArray){
		this.array = array;
		this.type = type;
                this.size = sizeArray;
	}
	
	public StringBuffer genC(Integer tabs){
                StringBuffer aux = new StringBuffer();
                
                if(type.toString() == Symbol.INTEGERARRAY.toString()){
                    aux.append("int");
                }else if(type.toString() == Symbol.DOUBLEARRAY.toString()){
                    aux.append("double");
                }else if(type.toString() == Symbol.CHARARRAY.toString()){
                    aux.append("char");
                }else if(type.toString() ==  Symbol.VOID.toString()){
                    aux.append("void");
                }else
                    aux.append(type.toString());
                
                return aux;
	}
}
