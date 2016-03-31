import AST.*;
import java.util.ArrayList;

public class Compiler {

    public Program compile( char []p_input ) {
        input = p_input;
        tokenPos = 0;
        nextToken();

        Program e = program();
        if (tokenPos != input.length)
          error("compile");

        return e;
    }

    //Program ::= Decl
    private Program program(){
      decl();
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
              error("decl");
          }else
            error("decl");
        }else
          error("decl");
      }else
        error("decl");
    }

    //StmtBlock ::= '{' { VariableDecl } { Stmt } '}'
    private void stmtBlock(){
      if(token == '{'){
        nextToken();
        while(variableDecl() == true);
        while(stmt() == true);
        if(token == '}'){
          nextToken();
        }else{
          System.out.println(token);
          error("stmtBlock");
        }
      }else
        error("stmtBlock");
    }

    //VariableDecl ::= Variable ';'
    private boolean variableDecl(){
      if(variable() && token == ';'){
        nextToken();
        return true;
      }else
        error("variableDecl");
      return false;
    }

    // Variable ::= Type Ident
    private boolean variable(){
      if((type() != true)||(ident()!=true))
        error("variable");
      else
        return true;
      return false;
    }

    // Type ::= StdType | ArrayType
    private boolean type(){
      if(stdType() || arrayType()){
        return true;
      }else
        error("type");
      return false;
    }

    // StdType ::= 'i' | 'd' | 'e'
    private boolean stdType(){
      switch(token){
        case 'i':
        case 'd':
        case 'c':
          nextToken();
          return true;
        default:
          break;
      }

      return false;
    }

    // ArrayType ::= StdType '[' ']'
    private boolean arrayType(){
      if(stdType() != true)
        error("arrayType 1");

      if(token=='['){
        nextToken();
        if(token == ']'){
          nextToken();
          return true;
        }else
          error("arrayType 2");
      }else
        error("arrayType 3");

      return false;
    }

    // Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt
    //
    //
    //
    //
    //

    // Ident ::= Letter { Letter | Digit}
    private boolean ident() {
      if(letter() != ' '){
        while(token != ';'){
          // solução pra que tenha numeros e letras misturados
          if(token >= '0' && token <= '9'){
            digit();
          }
          if((token >= 'A' && token <= 'Z')||(token >= 'a' && token <= 'z')){
            letter();
          }

        } // pode exibir erro se não tiver mais variaveis
        return true;
      }else
        error("ident");

      return false;
    }


    //Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt
    private boolean stmt(){
      return false;
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
          error("relOp");
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
          error("addOp");
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
          error("mulOp");
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
          error("unary");
          break;
      }

      return ret;
    }

    //number ::= '0'| '1' | ... | '9' -- FEITO
    private char digit() {
      char ret = ' ';
      if(token >= '0' && token <= '9'){
        ret = token;
        nextToken();
      }else{
        error("digit");
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
          System.out.println(token+" is reserved");
          error("letter 1");
          break;
        default:
          if((token >= 'A' && token <= 'Z')||(token >= 'a' && token <= 'z')){
            ret = token;
            nextToken();
          }else{
            error("letter 2");
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

    private void error(String function) {
        if ( tokenPos == 0 )
          tokenPos = 1;
        else
          if ( tokenPos >= input.length )
            tokenPos = input.length;

        String strInput = new String( input, tokenPos - 1, input.length - tokenPos + 1 );
        String strError = "Error at \"" + strInput + "\" in "+function+"";
        System.out.println( strError );
        throw new RuntimeException(strError);
    }

    private char token;
    private int  tokenPos;
    private char []input;

}
