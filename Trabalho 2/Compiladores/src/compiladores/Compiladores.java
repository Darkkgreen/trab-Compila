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
		entrada = "void main() { \n"
                        + "  n := 'a'; \n"
//			+ "  while (! a) { \n"
//			+ "    if(g){ \n"
//			+ "      a := g; \n"
//			+ "      if (a) { \n"
//			+ "        a := h; \n"
//			+ "      } \n"
//			+ "      g := a; \n"
//			+ "    }else{ \n"
//			+ "      if (k[n]) { \n"
//			+ "        while (k[n]) { \n"
//			+ "          while (a) { \n"
//			+ "            readChar(); \n"
//			+ "          } \n"
//			+ "        } \n"
//			+ "      } else { \n"
//			+ "        if (readInteger()) { \n"
//			+ "          readDouble(); \n"
//			+ "          a := h; \n"
//			+ "          k[n] := j; \n"
//			+ "        } \n"
//			+ "      } \n"
//			+ "    } \n"
//			+ "    while (k[n]) { \n"
//			+ "      readInteger(); \n"
//			+ "    } \n"
//			+ "  } \n"
//			+ "  readInteger(); \n"
//			+ "  readDouble(); \n"
//			+ "  readChar(); \n"
			+ "}";
		//		entrada = "void main(){\n"
		//			+ "	a := a + j\n"
		//			+ "}";
		entrada = entrada.concat(" ");

		char[] input = entrada.toCharArray();
		Compiler compiler = new Compiler();

		Program program = compiler.compile(input);
		program.genC();
	}

}
