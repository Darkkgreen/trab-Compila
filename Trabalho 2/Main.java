import AST.*;

public class Main {
    public static void main( String []args ) {
        //char []input = "a = 1  b = 3 : (- (+ a 2) b)".toCharArray();
        //char []input = "vm(){ i a; d a; c a; c u;}".toCharArray();
        //char []input = "vm(){ i[] a; d a; f(a=z){ a; } e{ z; } }".toCharArray();
        char []input = "vm(){ i[] a; d a; p(a,z)}".toCharArray();
        //caso com letra reservada
        //char []input = "vm(){ i AbzZa3; }".toCharArray();
        //char []input = "vm(){ i AzZa3; }".toCharArray();

        Compiler compiler = new Compiler();

        Program program  = compiler.compile(input);
        //program.genC();
    }
}

