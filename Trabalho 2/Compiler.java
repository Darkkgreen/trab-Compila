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

    //Program ::= Decl
    private Program program(){
      return null;
    }

    //Decl ::= 'v' 'm' '(' ')' StmtBlock
    private void decl(){
      if(token == 'v'){
        nextToken();
        if(token == 'm'){
          nextToken();
          if(token == '('){
            nextToken();
            if(token == ')'){
              nextToken();
              stmtBlock();
            }
            else
              error();  
          }else
            error();
        }else
          error();
      }else
        error();
    }

    //StmtBlock ::= '{' { VariableDecl } { Stmt } '{'
    private void stmtBlock(){
      if(token == '{'){
        nextToken();
        while(token == 'a') //TESTING
          variableDecl();
        stmt();
        if(token == '}'){
          nextToken();
        }else
          error();
      }else
        error();
    }

    //VariableDecl ::= Variable ';'
    private void variableDecl(){
      //variable();
      if(token == 'a')
        nextToken();
      if(token == ';')
        nextToken();
      else
        error();
    }

    //Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt
    private void stmt(){
      if(token == 'e'){
        nextToken();
        if(token == ';')
          System.out.println("I fucking love potatoes");
        else error();
      }else error();
    }

    //RelOp ::= '=' | '#' | '<' | '>'
    private char RelOp(){
      char ret = ' ';
      switch(token){
        case '=':
        case '#':
        case '<':
        case '>':
          ret = token;
          nextToken();
          break;
        default:
          error();
          break;
      }

      return ret;
    }

    //AddOp ::= '+' | '-'
    private char addOp(){
      char ret = ' ';
      switch(token){
        case '+':
        case '-':
          ret = token;
          nextToken();
          break;
        default:
          error();
          break;
      }

      return ret;
    }

    //MulOp ::= '*' | '/' | '%'
    private char mulOp(){
      char ret = ' ';
      switch(token){
        case '*':
        case '/':
        case '%':
          ret = token;
          nextToken();
          break;
        default:
          error();
          break;
      }

      return ret;
    }

    // Unary ::= '+' | '-' | '!' -- FEITO
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

    //number ::= '0'| '1' | ... | '9' -- FEITO
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

    // Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | 'b' | ... | 'z' -- FEITO
    private char letter(){
      char ret = ' ';
      switch(token){
        case 'v':
        case 'm':
        case 'i':
        case 'd':
        case 'c':
        case 'f':
        case 'e':
        case 'w':
        case 'b':
        case 'p':
        case 'r':
        case 's':
        case 't':
          error();
          break;
        default:
          if((token >= 'A' && token <= 'Z')||(token >= 'a' && token <= 'z')){
            ret = token;
            nextToken();
          }else{
            error();
          }
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
