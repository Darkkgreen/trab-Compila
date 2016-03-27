import AST.*;
import java.util.ArrayList;

public class Compiler {

    public Program compile( char []p_input ) {
        input = p_input;
        tokenPos = 0;
        nextToken();


        Program e = program();
        if (tokenPos != input.length)
          error();

        return e;
    }

    //Program ::= VarDecList ':' Expr
    private Program program(){
      return null;
    }

    // Unary ::= '+' | '-' | '!'
    private char unary(){
      char ret = ' ';
      switch(token){
        case '+':
        case '-':
        case '!':
          ret = token;
          nextToken();
          break;
        default:
          error();
          break;
      }

      return ret;
    }

    //number ::= '0'| '1' | ... | '9'
    private NumberExpr digit() {
      NumberExpr ret = null;
      if(token >= '0' && token <= '9'){
        ret = new NumberExpr(token);
        nextToken();
      }else{
        error();
      }
      return ret;
    }

    // Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | 'b' | ... | 'z'
    private char letter(){
      char ret = ' ';
      if((token >= 'A' && token <= 'Z')||(token >= 'a' && token <= 'z')){
        ret = token;
        nextToken();
      }else{
        error();
      }
      return ret;
    }

    private void nextToken() {
      while(tokenPos < input.length && input[tokenPos] == ' '){
        tokenPos++;
      }
      if(tokenPos >= input.length)
        token = '\0';
      else {
        token = input[tokenPos];
        tokenPos++;
      }

      System.out.print(" " + token + " ");
    }

    private void error() {
        if ( tokenPos == 0 )
          tokenPos = 1;
        else
          if ( tokenPos >= input.length )
            tokenPos = input.length;

        String strInput = new String( input, tokenPos - 1, input.length - tokenPos + 1 );
        String strError = "Error at \"" + strInput + "\"";
        System.out.println( strError );
        throw new RuntimeException(strError);
    }

    private char token;
    private int  tokenPos;
    private char []input;

}
