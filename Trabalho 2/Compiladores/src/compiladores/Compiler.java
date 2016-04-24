package compiladores;

import AST.*;
import java.util.ArrayList;
import Lexer.*;

public class Compiler {
    
        private boolean aninhado = false;

	public Program compile(char[] p_input) {
		input = p_input;
		lexer = new Lexer(p_input);
		lexer.nextToken();
		variableNames = new ArrayList<String>();

		Program e = program();

		// ver isso daqui pode estar errado
//		if (tokenPos != input.length) {
//			error("compile");
//		}
		return e;
	}

	//Program ::= Decl
	private Program program() {
		return decl();
	}

	//Decl ::= 'v' 'm' '(' ')' StmtBlock
	private Program decl() {
		Program ret = null;
		if (lexer.token == Symbol.VOID) {
			lexer.nextToken();
			if (lexer.token == Symbol.MAIN) {
				lexer.nextToken();
				if (lexer.token == Symbol.LEFTPAR) {
					lexer.nextToken();
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						ret = stmtBlock();
					} else {
						error("decl: expected )");
					}
				} else {
					error("decl: expected (");
				}
			} else {
				error("decl: expected main");
			}
		} else {
			error("decl: expected void");
		}

		return ret;
	}

	//StmtBlock ::= '{' { VariableDecl } { Stmt } '}'
	private Program stmtBlock() {
		ArrayList<Variable> ret = new ArrayList<Variable>();
		ArrayList<Stmt> stmt = new ArrayList<Stmt>();
		Variable aux = null;
		Stmt auxiliarStmt = null;

		if (lexer.token == Symbol.LEFTBRACKET) {
			lexer.nextToken();
			while ((aux = variableDecl()) != null) {
				ret.add(aux);
				aux = null;
			}
			while ((auxiliarStmt = stmt()) != null) {
				stmt.add(auxiliarStmt);
				auxiliarStmt = null;
			}
		} else {
			error("stmtBlock: expected {");
		}

		if (lexer.token == Symbol.RIGHTBRACKET) {
			Program program = new Program(ret, stmt);
			lexer.nextToken();
			return program;
		} else {
			error("stmtBlock: expected }");
		}
		return null;
	}

	//VariableDecl ::= Variable ';'
	private Variable variableDecl() {
		Variable aux = variable();
		if (aux != null) {
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				return aux;
			} else {
				error("variableDecl: expected ;");
				return null;
			}
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
			if (name != null) {
				if (variableNames.contains(name) == true) {
					error("variable: Variable " + name + " already exists!");
				} else {
					variableNames.add(name);
				}
				aux = new Variable(name, type);
				return aux;
			} else {
				error("variable: The name of variable is not set");
			}
		}
		return null;
	}

	// Type ::= StdType | ArrayType
	private Type type() {
		return arrayType();
	}

	// StdType ::= 'i' | 'd' | 'c'
	private Type stdType() {
		if ((lexer.token == Symbol.INTEGER) || (lexer.token == Symbol.CHAR) || (lexer.token == Symbol.DOUBLE)) {
			Type type = new Type(lexer.token, false);
			lexer.nextToken();
			return type;
		}

		return null;
	}

	// ArrayType ::= StdType '[' ']'
	private Type arrayType() {
		Type type = null;
		type = stdType();
		if (type != null) {
			if (lexer.token == Symbol.LEFTSQUARE) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTSQUARE) {
					type.setArray(true);
					lexer.nextToken();
					return type;
				} else {
					error("arrayType: expected ]");
					return null;
				}
			}
			// como o construtor de type ja define false o array,
			// nada é feito
			return type;
		}
		return type;
	}

	// Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt
	private Stmt stmt() {
		IfStmt se = null;
		WhileStmt enquanto = null;
		boolean parada = false;
		PrintStmt imprime = null;
		CompositeExpr aux = null;
		Stmt stmt = null;

		if (((aux = expr()) != null)&& (lexer.token == Symbol.SEMICOLON) || (se = ifStmt()) != null || (enquanto = whileStmt()) != null || (parada = breakStmt()) || (imprime = printStmt()) != null) {
			stmt = new Stmt(se, enquanto, parada, imprime, aux);
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
			}
			return stmt;

		}
		return null;
	}

	//IfStmt ::= 'f' '(' Expr ')' '{' { Stmt } '}' [ 'e' '{' { Stmt } '}' ]
	private IfStmt ifStmt() {
            Expr auxiliarExp = null;
            ArrayList<Stmt> principal = new ArrayList<Stmt>();
            ArrayList<Stmt> opcional = new ArrayList<Stmt>();
            Stmt auxiliarStmt = null;
            IfStmt ifstmt = null;
            
		if (lexer.token == Symbol.IF) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if ((auxiliarExp = expr()) != null) {
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.LEFTBRACKET) {
							lexer.nextToken();
							while ((auxiliarStmt = stmt()) != null) {
                                                                principal.add(auxiliarStmt);
                                                                auxiliarStmt = null;
								if (lexer.token == Symbol.RIGHTBRACKET) {
									lexer.nextToken();
									if (lexer.token == Symbol.ELSE) { //aqui entra a parte opcional
										lexer.nextToken();
										if (lexer.token == Symbol.LEFTBRACKET) {
											lexer.nextToken();
											while ((auxiliarStmt = stmt()) != null) {
                                                                                            opcional.add(auxiliarStmt);
                                                                                            auxiliarStmt = null;
												if (lexer.token == Symbol.RIGHTBRACKET) {
													lexer.nextToken();
												}
											}
										}else
                                                                                    error("ifStmt: expected [");
									}       
								}
							}
                                                        ifstmt = new IfStmt(auxiliarExp, principal, opcional);
                                                        return ifstmt;
						}else
                                                    error("ifStmt: expected {");
					}else
                                            error("ifStmt: expected )");
				}else
                                    error("ifStmt: expected expression");
			}else
                            error("ifStmt: expected (");
		}
		return null;
	}

	//WhileStmt ::= 'w' '(' Expr ')' '{' { Stmt } '}'
	private WhileStmt whileStmt() {
            Expr auxiliarExp = null;
            ArrayList<Stmt> arrayPrinc = new ArrayList<Stmt>();
            Stmt auxiliarSt = null;
            
            aninhado = true;
            
		if (lexer.token == Symbol.WHILE) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if ((auxiliarExp = expr()) != null) {
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.LEFTBRACKET) {
							lexer.nextToken();
							while ((auxiliarSt = stmt()) != null) {
                                                            arrayPrinc.add(auxiliarSt);
                                                            auxiliarSt = null;
							}
                                                        if (lexer.token == Symbol.RIGHTBRACKET) {
                                                            lexer.nextToken();
                                                            WhileStmt enquanto = new WhileStmt(arrayPrinc, auxiliarExp);
                                                            
                                                            aninhado = false;
                                                            return enquanto;
                                                        }else
                                                            error("whileStmt: expected }");
						}else
                                                    error("whileStmt: expected {");
					}else
                                            error("whileStmt: expected )");
				}else
                                    error("whileStmt: expected expression");
			}else
                            error("whileStmt: expected (");
		}
            
            aninhado = false;
            return null;
	}

	//BreakStmt ::= 'b' ';'
	private boolean breakStmt() {
		if (lexer.token == Symbol.BREAK) {
                    if(aninhado == true){
			lexer.nextToken();
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				return true;
			}else
                            error("breakStmt: expected ;");
                    }else
                        error("breakStmt: break out of a while");
		}

		return false;
	}

	//PrintStmt ::= 'p' '(' Expr { ',' Expr }')'
	private PrintStmt printStmt() {
            ArrayList<Expr> listaExp = new ArrayList<Expr>();
            Expr aux;
            
		if (lexer.token == Symbol.PRINT) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if ((aux = expr()) != null) {
                                    listaExp.add(aux);
                                    aux = null;
					while (lexer.token == Symbol.COMMA) {
						lexer.nextToken();
						if ((aux = expr()) != null) {
                                                    listaExp.add(aux);
                                                    aux = null;
						}else
                                                    error("printStmt: expected expression after comma");
					}

					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
                                                PrintStmt imprime = new PrintStmt(listaExp);
						return imprime;
					}else
                                            error("printStmt: expected )");
				}else
                                    error("printStmt: expected print expression");
			}else
                            error("printStmt: expected (");
		}

		return null;
	}

	// Expr ::= SimExpr [ RelOp Expr ]

	private CompositeExpr expr() {
		CompositeExpr expr = null;
		SimExpr aux = null;
		String relop = null;

		aux = simExpr();
		if (aux != null) {
			if ((lexer.token == Symbol.ASSIGN) || (lexer.token == Symbol.NEQ) || (lexer.token == Symbol.LT)
				|| (lexer.token == Symbol.LE) || (lexer.token == Symbol.GT) || (lexer.token == Symbol.GE)) {
				expr = expr();
				if (expr == null) {
					error("SimExpr");
				}
			}
			return new CompositeExpr(aux, relop, expr);
		} else {
			return null;
		}

	}

	// SimExpr ::= [Unary] Term { AddOp Term }
	private SimExpr simExpr() {
		String unary = null;
		String aux2 = null;
		Term termAux = null;
		Term termAux2 = null;
		ArrayList<String> addop = null;
		ArrayList<Term> termList = null;

		if ((lexer.token == Symbol.MINUS) || (lexer.token == Symbol.NOT) || (lexer.token == Symbol.PLUS)) {
			unary = lexer.token.toString();
			lexer.nextToken();
		}

		termAux = term();
		if (termAux != null) {
			while (true) {
				if ((lexer.token == Symbol.PLUS) || (lexer.token == Symbol.OR) || (lexer.token == Symbol.MINUS)) {
					if (addop == null) {
						addop = new ArrayList<String>();
					}
					addop.add(lexer.token.toString());
					lexer.nextToken();

					termAux2 = term();
					if (termAux2 != null) {
						if (termList == null) {
							termList = new ArrayList<Term>();
						}
						termList.add(termAux);
					} else {
						break;
					}
				} else {
					break;
				}
			}
			return new SimExpr(unary, termAux, addop, termList);
		}

		return null;
	}

	// Term ::= Factor { MulOp Factor }
	private Term term() {
		Factor aux = null;
		Factor aux3 = null;
		String aux2 = null;
		ArrayList<String> mulop = null;
		ArrayList<Factor> factorList = null;

		aux = factor();
		if (aux != null) {
			while (true) {
				if ((lexer.token == Symbol.MULT) || (lexer.token == Symbol.DIV) || (lexer.token == Symbol.REMAINDER) || (lexer.token == Symbol.AND)) {
					if (mulop == null) {
						mulop = new ArrayList<String>();
					}
					mulop.add(lexer.token.toString());
					lexer.nextToken();

					aux3 = factor();
					if (aux3 != null) {
						if (factorList == null) {
							factorList = new ArrayList<Factor>();
						}
						factorList.add(aux3);
					} else {
						error("Term");
					}
				} else {
					break;
				}
			}

			return new Term(aux, mulop, factorList);
		}

		return null;
	}

	// Factor ::= LValue ':' Expr | LValue | '(' Expr ')' | 'r' '(' ')' | 's' '(' ')' | 't' '(' ')'
	private Factor factor() {
		LValue lValue = null;
		lValue = lValue();
		CompositeExpr expr = null;
		if (lValue != null) {
			if (lexer.token == Symbol.DEFINITION) {
				lexer.nextToken();
				expr = expr();
				if (expr != null) {
					return new Factor(lValue, expr, null, null, null);
				}else
					error("factor: There is no expression");
			}
			return new Factor(lValue, null, null, null, null);
		} else if ((lexer.token == Symbol.NUMBER) || (lexer.token == Symbol.DOUBLE)) {
			Factor aux = null;
			if (lexer.token == Symbol.NUMBER) {
				aux = new Factor(null, null, null, lexer.getNumberValue(), null);
			} else {
				aux = new Factor(null, null, null, null, lexer.getStringValue());
			}
			lexer.nextToken();
			return aux;

		} else if (lexer.token == Symbol.LEFTPAR) {
			lexer.nextToken();
			expr = expr();
			if (expr != null) {
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(null, expr, null, null, null);
				}
			}

		} else if (lexer.token == Symbol.READINTEGER) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readInteger()".toString(), null, null);
				}
			}

		} else if (lexer.token == Symbol.READDOUBLE) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readDouble()".toString(), null, null);
				}
			}

		} else if (lexer.token == Symbol.READCHAR) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readChar()".toString(), null, null);
				}
			}

		}

		return null;
	}

	// LValue ::= Ident | Ident '[' Expr ']'
	private LValue lValue() {
		String ident = null;
		Expr expr = null;
		// verificar se a variável existe

		ident = ident();
		if (ident != null) {
			if (lexer.token == Symbol.LEFTSQUARE) {
				lexer.nextToken();
				expr = expr();
				if (expr != null) {
					if (lexer.token == Symbol.RIGHTSQUARE) {
						lexer.nextToken();
						return new LValue(ident, expr);
					}
				}
			} else {
				return new LValue(ident, null);
			}
		}
		return null;
	}

	// Ident ::= Letter { Letter | Digit}
	private String ident() {
		String name = new String();
		String aux;
		boolean flag1 = false, flag2 = false, flag3 = false;

		if (lexer.token == Symbol.IDENT) {
			name = name.concat(lexer.getStringValue());
			lexer.nextToken();

			do {
				if (lexer.token == Symbol.UNDERSCORE) {
					name = name.concat("_");
					lexer.nextToken();
					flag1 = true;
				} else {
					flag1 = false;
				}
				if (lexer.token == Symbol.IDENT) {
					name = name.concat(lexer.getStringValue());
					lexer.nextToken();
					flag2 = true;
				} else {
					flag2 = false;
				}
				if (lexer.token == Symbol.NUMBER) {
					name = name.concat(lexer.getStringValue());
					lexer.nextToken();
					flag3 = true;
				} else {
					flag3 = false;
				}
			} while (!((flag1 == false) && (flag2 == false) && (flag3 == false)));
			return name;
		}
//		else {
//			error("ident: Variable must begin with a letter");
//		}
		return null;
	}
//
//	//RelOp ::= '=' | '#' | '<' | '>'
//	private String relOp() {
//		char ret = ' ';
//		switch (token) {
//			case '=':
//			case '#':
//			case '<':
//			case '>':
//				ret = token;
//				nextToken();
//				return Character.toString(ret);
//			default:
//				return null;
//		}
//	}
//
//	//AddOp ::= '+' | '-'
//	private String addOp() {
//		char ret = ' ';
//		switch (token) {
//			case '+':
//			case '-':
//				ret = token;
//				nextToken();
//				return Character.toString(ret);
//			default:
//				return null;
//		}
//	}
//
//	//MulOp ::= '*' | '/' | '%'
//	private String mulOp() {
//		char ret = ' ';
//		switch (token) {
//			case '*':
//			case '/':
//			case '%':
//				ret = token;
//				nextToken();
//				return Character.toString(ret);
//			default:
//				return null;
//		}
//	}
//
//
//	// Digit ::= '0'| '1' | ... | '9'
//	private String digit() {
//		String ret;
//
//		if (token >= '0' && token <= '9') {
//			ret = Character.toString(token);
//			nextToken();
//			return ret;
//		} else {
//			return null;
//		}
//	}
//
//	// Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | 'b' | ... | 'z'
//	private String letter() {
//		String ret;
//
//		switch (token) {
//			case 'v':
//			case 'm':
//			case 'i':
//			case 'd':
//			case 'c':
//			case 'f':
//			case 'e':
//			case 'w':
//			case 'b':
//			case 'p':
//			case 'r':
//			case 's':
//			case 't':
//				return null;
//			default:
//				if ((token >= 'A' && token <= 'Z') || (token >= 'a' && token <= 'z')) {
//					ret = Character.toString(token);
//					nextToken();
//					return ret;
//				} else {
//					return null;
//				}
//		}
//	}

	private void error(String function) {
		if (lexer.tokenPos == 0) {
			lexer.tokenPos = 1;
		} else if (lexer.tokenPos >= input.length) {
			lexer.tokenPos = input.length;
		}

		String strInput = new String(input, lexer.tokenPos - 1, input.length - lexer.tokenPos + 1);
		String strError = "Error at \"" + strInput + "\" in " + function + "";
		System.out.println(strError);
		throw new RuntimeException(strError);
	}

	private Lexer lexer;
	public ArrayList<String> variableNames;
	private char[] input;

}
