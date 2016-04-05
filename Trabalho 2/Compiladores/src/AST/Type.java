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
public class Type {
	private boolean array;
	private char type;

	Type(char type, boolean array){
		this.array = array;
		this.type = type;
	}
		
	
}
