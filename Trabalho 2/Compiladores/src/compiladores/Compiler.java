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
	private Program stmtBlock() {
		Expr expr = null;
		ArrayList<Variable> ret = new ArrayList<Variable>();
		Variable aux = null;
                
		if (token == '{') {
			nextToken();
			while ((aux = variableDecl()) != null) {
<<<<<<< HEAD
				ret.add(aux);
=======
                            ret.add(aux);
                            aux = null;
>>>>>>> 4e13b41addf4ad008d272fd91b0c4e36b620aa82
			}
			//while (stmt() == true);

		} else {
			error("stmtBlock");
		}

		if (token == '}') {
			Program program = new Program(ret, expr);
			nextToken();
			return program;
		} else {
			error("stmtBlock");
		}
		return null;
	}

	//VariableDecl ::= Variable ';'
	private Variable variableDecl() {
		Variable aux = variable();	
		if ((aux != null) && token == ';') {
			nextToken();
			return aux;
		} else {
			return null;
		}
	}

	// Variable ::= Type Ident
	private Variable variable() {
		Variable aux = null;
		Type type = null;
		String name = null;

		type = type();
		if (type != null) {
			name = ident();
			if(name != null){
				aux = new Variable(name, type);
				return aux;
			}else
				error("variable");
		}
		return null;
	}

	// Type ::= StdType | ArrayType
	private Type type() {
		return arrayType();
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
	private Type arrayType() {
		Type type = null;
		type = stdType();
		if (type != null) {
			if (token == '[') {
				nextToken();
				if (token == ']') {
					type.setArray(true);
					nextToken();
					return type;
				} else {
					return type;
				}
			}
			// como o construtor de type ja define false,
			// nada é feito
			return type;
		}
		return type;
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
												}else
                                                                                                    error("IfStmt");
											}
										}else
                                                                                    error("IfStmt");
									}
								}else
                                                                    error("IfStmt");
							}
						}else
                                                    error("IfStmt");
					}else
                                            error("IfStmt");
				}else
                                    error("IfStmt");
			}else
                            error("IfStmt");
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
								}else
                                                                    error("WhileStmt");
							}
						}else
                                                    error("WhileStmt");
					}else
                                            error("WhileStmt");
				}else
                                    error("WhileStmt");
			}else
                            error("WhileStmt");
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
			}else
                            error("BreakStmt");
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
						}else
                                                    error("PrintStmt");
					}

					if (token == ')') {
						nextToken();
						return true;
					}else
                                            error("PrintStmt");
				}else
                                    error("PrintStmt");
			}else
                            error("PrintStmt");
		}

		return false;
	}

	// Expr ::= SimExpr [ RelOp Expr ]
	private boolean expr() {
		if (simExpr()) {
			if (relOp()) {
				if (!expr()) {
                                        error("SimExpr");
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

	// Factor ::= LValue ':' Expr | LValue | '(' Expr ')' | 'r' '(' ')' | 's' '(' ')' | 't' '(' ')'
	private boolean factor() {
		if (lValue()) {
			if (token == ':') {
                            nextToken();
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
                        nextToken();
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return true;
				}
			}

		} else if (token == 's') {
                        nextToken();
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return true;
				}
			}

		} else if (token == 't') {
                        nextToken();
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
	private String ident() {
		String name = null;
		String aux;
		boolean flag = false;


		name = letter();
		if (name != null) {
			while (!flag){
				aux = letter();
				if(aux != null)
					name.concat(aux);
				else
					flag = true;

				aux = digit();
				if(flag == true)
					if(aux == null)
						break;
				name.concat(aux);
			}
			return name;
		}
		return null;
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
	private String digit() {
		String ret;

		if (token >= '0' && token <= '9') {
			ret = Character.toString(token);
			nextToken();
			return ret;
		} else {
			return null;
		}
	}

	// Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | 'b' | ... | 'z'
	private String letter() {
		String ret;

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
				return null;
			default:
				if ((token >= 'A' && token <= 'Z') || (token >= 'a' && token <= 'z')) {
					ret = Character.toString(token);
					nextToken();
					return ret;
				} else {
					return null;
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
