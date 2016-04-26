/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import AST.Program;

/**
 *
 * @author ricke
 */
public class Compiladores {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String entrada = new String();
		//entrada = "void main () { /*ehuheue*/ int[] v_lokura321; //lokuraaaaa\nint[] v; int v_321; int v_; int v321; int [] vo____o;}";
//		entrada = "void main(){readChar();}";
		entrada = "/*\n" +
"	ERRLEX05: Nao deve aceitar operadores invalidos\n" +
"	Linha: 12;\n" +
"*/\n" +
"\n" +
"void main () {\n" +
"\n" +
"	int a;\n" +
"	int b;\n" +
"	\n" +
"	//Correto e a = b\n" +
"	if (a == b) {}\n" +
"}";
		entrada = entrada.concat(" ");

		char[] input = entrada.toCharArray();
		Compiler compiler = new Compiler();

		Program program = compiler.compile(input);
		program.genC();
	}

}
