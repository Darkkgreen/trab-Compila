package compiladores;

import AST.*;
import java.util.ArrayList;

public class Compiler {

	public Program compile(char[] p_input) {
		input = p_input;
		tokenPos = 0;
		nextToken();

		Program e = program();
		if (tokenPos != input.length) {
			error("compile");
		}
		return e;
	}

	//Program ::= Decl
	private Program program() {
		return decl();
	}

	//Decl ::= 'v' 'm' '(' ')' StmtBlock
	private Program decl() {
		if (token == 'v') {
			nextToken();
			if (token == 'm') {
				nextToken();
				if (token == '(') {
					nextToken();
					if (token == ')') {
						nextToken();
						return stmtBlock();
					} else {
						error("Decl");
					}
				} else {
					error("Decl");
				}
			} else {
				error("Decl");
			}
		} else {
			error("Decl");
		}
		return null;
	}

	//StmtBlock ::= '{' { VariableDecl } { Stmt } '}'
	private void stmtBlock() {
		ArrayList<Variable> ret = null;
		Variable aux = null;
		if (token == '{') {
			nextToken();
			while ((aux = variableDecl()) != null) {
			}
			while (stmt() == true);

		} else {
			error("stmtBlock");
		}

		if (token == '}') {
			nextToken();
		} else {
			System.out.println(token);
			error("stmtBlock");
		}

	}

	//VariableDecl ::= Variable ';'
	private Variable variableDecl() {
		Variable aux = variable();	
		if ((aux != null) && token == ';') {
			nextToken();
			return true;
		} else {
			return false;
		}
	}

	// Variable ::= Type Ident
	private Variable variable() {
		Variable aux = null;

		if (type() == true) {
			if (ident()) {
				return true;
			} else {
				error("variable");
			}
		}
		return false;

	}

	// Type ::= StdType | ArrayType
	private boolean type() {
		if (arrayType()) {
			return true;
		} else {
			return false;
		}
	}

	// StdType ::= 'i' | 'd' | 'c'
	private Type stdType() {
		switch (token) {
			case 'i':
			case 'd':
			case 'c':
				Type type = new Type(token, false);
				nextToken();
				return type;
			default:
				return null;
		}
	}

	// ArrayType ::= StdType '[' ']'
	private boolean arrayType() {
		if (stdType() == true) {
			if (token == '[') {
				nextToken();
				if (token == ']') {
					nextToken();
					return true;
				} else {
					return false;
				}
			}

			return true;
		}
		return false;
	}

	// Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt
	private boolean stmt() {
		if (((expr() == true) && (token == ';')) || ifStmt() || whileStmt() || breakStmt() || printStmt()) {
			if (token == ';') {
				nextToken();
			}
			return true;
		}

		return false;
	}

	//IfStmt ::= 'f' '(' Expr ')' '{' { Stmt } '}' [ 'e' '{' { Stmt } '}' ]
	private boolean ifStmt() {
		if (token == 'f') {
			nextToken();
			if (token == '(') {
				nextToken();
				if (expr()) {
					if (token == ')') {
						nextToken();
						if (token == '{') {
							nextToken();
							while (stmt() == true) {
								if (token == '}') {
									nextToken();
									if (token == 'e') { //aqui entra a parte opcional
										nextToken();
										if (token == '{') {
											nextToken();
											while (stmt() == true) {
												if (token == '}') {
													nextToken();
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	//WhileStmt ::= 'w' '(' Expr ')' '{' { Stmt } '}'
	private boolean whileStmt() {
		if (token == 'w') {
			nextToken();
			if (token == '(') {
				nextToken();
				if (expr()) {
					if (token == ')') {
						nextToken();
						if (token == '{') {
							nextToken();
							while (stmt() == true) {
								if (token == '}') {
									nextToken();
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	//BreakStmt ::= 'b' ';'
	private boolean breakStmt() {
		if (token == 'b') {
			nextToken();
			if (token == ';') {
				nextToken();
				return true;
			}
		}

		return false;
	}

	//PrintStmt ::= 'p' '(' Expr { ',' Expr }')'
	private boolean printStmt() {
		if (token == 'p') {
			nextToken();
			if (token == '(') {
				nextToken();
				if (expr()) {
					while (token == ',') {
						nextToken();
						if (expr()) {
							nextToken();
						}
					}

					if (token == ')') {
						nextToken();
						return true;
					}
				}
			}
		}

		return false;
	}

	// Expr ::= SimExpr [ RelOp Expr ]
	private boolean expr() {
		if (simExpr()) {
			if (relOp()) {
				if (!expr()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

	// SimExpr ::= [Unary] Term { AddOp Term }
	private boolean simExpr() {
		if (unary()) {
		}

		if (term()) {
			while (addOp()) {
				if (!term()) {
					error("SimExpr 2");
					return false;
				}
			}

			return true;
		}

		return false;
	}

	// Term ::= Factor { MulOp Factor }
	private boolean term() {
		if (factor()) {
			while (mulOp() == true) {
				if (!factor()) {
					error("Term");
				}
			}

			return true;
		}

		return false;
	}

	// Factor ::= LValue '=' Expr | LValue | '(' Expr ')' | 'r' '(' ')' | 's' '(' ')' | 't' '(' ')'
	private boolean factor() {
		if (lValue()) {
			if (token == ':') {
				if (expr()) {
					return true;
				}
			}
			return true;
		} else if (token == '(') {
			nextToken();
			if (expr()) {
				if (token == ')') {
					nextToken();
					return true;
				}
			}

		} else if (token == 'r') {
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return true;
				}
			}

		} else if (token == 's') {
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return true;
				}
			}

		} else if (token == 't') {
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return true;
				}
			}
		}

		return false;
	}

	// LValue ::= Ident | Ident '[' Expr ']'
	private boolean lValue() {
		if (ident()) {
			if (token == '[') {
				nextToken();
				if (expr()) {
					if (token == ']') {
						nextToken();
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	// Ident ::= Letter { Letter | Digit}
	private boolean ident() {
		if (letter() == true) {
			while (letter() || digit());
			return true;
		}
		return false;
	}

	//RelOp ::= '=' | '#' | '<' | '>'
	private boolean relOp() {
		char ret = ' ';
		switch (token) {
			case '=':
			case '#':
			case '<':
			case '>':
				ret = token;
				nextToken();
				return true;
			default:
				return false;
		}
	}

	//AddOp ::= '+' | '-'
	private boolean addOp() {
		char ret = ' ';
		switch (token) {
			case '+':
			case '-':
				ret = token;
				nextToken();
				return true;
			default:
				return false;
		}
	}

	//MulOp ::= '*' | '/' | '%'
	private boolean mulOp() {
		char ret = ' ';
		switch (token) {
			case '*':
			case '/':
			case '%':
				ret = token;
				nextToken();
				return true;
			default:
				return false;
		}
	}

	// Unary ::= '+' | '-' | '!'
	private boolean unary() {
		char ret = ' ';
		switch (token) {
			case '+':
			case '-':
			case '!':
				ret = token;
				nextToken();
				return true;
			default:
				return false;
		}
	}

	// Digit ::= '0'| '1' | ... | '9'
	private boolean digit() {
		char ret = ' ';

		if (token >= '0' && token <= '9') {
			ret = token;
			nextToken();
			return true;
		} else {
			return false;
		}
	}

	// Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | 'b' | ... | 'z'
	private boolean letter() {
		char ret = ' ';

		switch (token) {
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
				return false;
			default:
				if ((token >= 'A' && token <= 'Z') || (token >= 'a' && token <= 'z')) {
					ret = token;
					nextToken();
					return true;
				} else {
					return false;
				}
		}
	}

	private void nextToken() {
		while (tokenPos < input.length && input[tokenPos] == ' ') {
			tokenPos++;
		}
		if (tokenPos >= input.length) {
			token = '\0';
		} else {
			token = input[tokenPos];
			tokenPos++;
		}

		System.out.print(" " + token + " ");
	}

	private void error(String function) {
		if (tokenPos == 0) {
			tokenPos = 1;
		} else if (tokenPos >= input.length) {
			tokenPos = input.length;
		}

		String strInput = new String(input, tokenPos - 1, input.length - tokenPos + 1);
		String strError = "Error at \"" + strInput + "\" in " + function + "";
		System.out.println(strError);
		throw new RuntimeException(strError);
	}

	private char token;
	private int tokenPos;
	private char[] input;

}
