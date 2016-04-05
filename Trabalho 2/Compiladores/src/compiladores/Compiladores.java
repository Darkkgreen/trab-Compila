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
      char []input = "vm(){ia; ix; io; in; ij;}".toCharArray();
      //char []input = "vm(){ i a; d a; c a; c u;}".toCharArray();
      //char []input = "vm(){a + b + c = (!d + e);}".toCharArray();
      //char[] input = "vm(){ i[] a; d a; f(a=z){ a; } e{ z; } }".toCharArray();
      //char []input = "vm(){ i[] a; d a; p(a,z)}".toCharArray();
      //char []input = "vm(){ i aa12; d[] aa2; c aa;}".toCharArray();
      //caso com letra reservada
      //char []input = "vm(){ i AbzZa3; }".toCharArray();
      //char []input = "vm(){ i AzZa3; }".toCharArray();

      Compiler compiler = new Compiler();

      Program program = compiler.compile(input);
      //program.genC();
   }

}
