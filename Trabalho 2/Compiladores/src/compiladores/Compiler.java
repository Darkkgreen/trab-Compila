package compiladores;

import AST.*;
import java.util.ArrayList;
import Lexer.*;
import java.util.EmptyStackException;
import java.util.Stack;

public class Compiler {
	private boolean relationOP = false;
	private String nomeArquivo;

	public Program compile(char[] p_input, String nome) {
		this.nomeArquivo = nome;
		input = p_input;
		lexer = new Lexer(p_input);
		lexer.nextToken();
		variableNames = new ArrayList<String>();
		whiles = new Stack();
		pilha = 0;

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
		variablesList = new ArrayList<Variable>();
		ArrayList<Stmt> stmt = new ArrayList<Stmt>();
		Variable aux = null;
		Stmt auxiliarStmt = null;

		if (lexer.token == Symbol.LEFTBRACKET) {
			lexer.nextToken();
			while ((aux = variableDecl()) != null) {
				variablesList.add(aux);
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
			Program program = new Program(variablesList, stmt);
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
				for (Variable v : variablesList) {
					if (v.getName().equals(name) == true) {
						error("variable: Variable \"" + name + "\" already exists!");
					}
				}
				variableNames.add(name);
				aux = new Variable(name, type);
				return aux;
			} else {
				error("variable: The name of variable is not set, probably the name used \"" + lexer.getStringValue() + "\" is reserved"
					+ " or the first character is not a letter.");
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
			Type type = new Type(lexer.token, false, 0);
			lexer.nextToken();
			return type;
		} else if (lexer.getStringValue().toLowerCase().equals("char") || (lexer.getStringValue().toLowerCase().equals("int") || lexer.getStringValue().toLowerCase().equals("double"))) {
			error("stdType : The type must be lowercase");
		}

		return null;
	}

	// ArrayType ::= StdType '[' ']'
	private Type arrayType() {
		Type type = null;
		Integer aux = 0;
		type = stdType();
		if (type != null) {
			if (lexer.token == Symbol.LEFTSQUARE) {
				lexer.nextToken();
				if (lexer.token == Symbol.NUMBER) {
					aux = lexer.getNumberValue();
					lexer.nextToken();
					if (lexer.token == Symbol.RIGHTSQUARE) {
						type.setArray(true);
						type.setSize(aux);
						lexer.nextToken();
						return type;
					} else {
						error("arrayType: expected ]");
						return null;
					}
				} else if (lexer.token == Symbol.MINUS) {
					error("arrayType : Array size must not be negative");
				} else {
					error("arrayType: Missing array size");
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

		if ((aux = expr(false)) != null) {
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
			} else if (aux != null) {
				error("stmt: expected \";\"");
			}

		}

		se = ifStmt();
		enquanto = whileStmt();
		parada = breakStmt();
		imprime = printStmt();

		// sem essa verificação, o programa podera entrar em loop
		if ((se != null) || (enquanto != null) || (parada == true) || (imprime != null) || (aux != null)) {
			stmt = new Stmt(se, enquanto, parada, imprime, aux);
		}

		return stmt;
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
				if ((auxiliarExp = expr(true)) != null) {
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.LEFTBRACKET) {
							lexer.nextToken();
							while ((auxiliarStmt = stmt()) != null) {
								principal.add(auxiliarStmt);
								auxiliarStmt = null;
							}
							if (lexer.token == Symbol.RIGHTBRACKET) {
								lexer.nextToken();
								if (lexer.token == Symbol.ELSE) { //aqui entra a parte opcional
									lexer.nextToken();
									if (lexer.token == Symbol.LEFTBRACKET) {
										lexer.nextToken();
										while ((auxiliarStmt = stmt()) != null) {
											opcional.add(auxiliarStmt);
											auxiliarStmt = null;
										}
										if (lexer.token == Symbol.RIGHTBRACKET) {
											lexer.nextToken();
										}
									} else {
										error("ifStmt: expected [");
									}
								}
							}
							ifstmt = new IfStmt(auxiliarExp, principal, opcional);
							return ifstmt;
						} else {
							error("ifStmt: expected {");
						}
					} else {
						error("ifStmt: expected )");
					}
				} else {
					error("ifStmt: expected expression");
				}
			} else {
				error("ifStmt: expected (");
			}
		}
		return null;
	}

	//WhileStmt ::= 'w' '(' Expr ')' '{' { Stmt } '}'
	private WhileStmt whileStmt() {
		Expr auxiliarExp = null;
		ArrayList<Stmt> arrayPrinc = new ArrayList<Stmt>();
		Stmt auxiliarSt = null;
		Integer myPilha = -1;

		if (lexer.token == Symbol.WHILE) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if ((auxiliarExp = expr(true)) != null) {
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.LEFTBRACKET) {
							myPilha = pilha;
							whiles.push(pilha);

							lexer.nextToken();
							while ((auxiliarSt = stmt()) != null) {
								arrayPrinc.add(auxiliarSt);
								auxiliarSt = null;
							}
							if (lexer.token == Symbol.RIGHTBRACKET) {
								if (whiles.contains(myPilha) == true) {
									whiles.remove(myPilha);
								}
								lexer.nextToken();
								WhileStmt enquanto = new WhileStmt(arrayPrinc, auxiliarExp);
								return enquanto;
							} else {
								error("whileStmt: expected }");
							}
						} else {
							error("whileStmt: expected {");
						}
					} else {
						error("whileStmt: expected )");
					}
				} else {
					error("whileStmt: expected expression");
				}
			} else {
				error("whileStmt: expected (");
			}
		}
		return null;
	}

	//BreakStmt ::= 'b' ';'
	private boolean breakStmt() {
		if (lexer.token == Symbol.BREAK) {
			try {
				whiles.pop();
				lexer.nextToken();
				if (lexer.token == Symbol.SEMICOLON) {
					lexer.nextToken();
					return true;
				} else {
					error("breakStmt: expected ;");
				}
			} catch (EmptyStackException e) {
				error("breakStmt: break out of a while");
			}
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
				if ((aux = expr(true)) != null) {
					listaExp.add(aux);
					aux = null;
					while (lexer.token == Symbol.COMMA) {
						lexer.nextToken();
						if ((aux = expr(true)) != null) {
							listaExp.add(aux);
							aux = null;
						} else {
							error("printStmt: expected expression after comma");
						}
					}

					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.SEMICOLON) {
							lexer.nextToken();
							PrintStmt imprime = new PrintStmt(listaExp);
							return imprime;
						}else
							error("printStmt : expected ;");

					} else {
						error("printStmt: expected )");
					}
				} else {
					error("printStmt: expected print expression");
				}
			} else {
				error("printStmt: expected (");
			}
		}

		return null;
	}

	// Expr ::= SimExpr [ RelOp Expr ]
	private CompositeExpr expr(boolean possible) {
		CompositeExpr expr = null;
		SimExpr aux = null;
		String relop = null;

		aux = simExpr();
		if (aux != null) {
			if (aux.getSolo() == false && possible == false) {
				error("Expression not in the actual format");
			}
			if ((lexer.token == Symbol.ASSIGN) || (lexer.token == Symbol.NEQ) || (lexer.token == Symbol.LT)
				|| (lexer.token == Symbol.LE) || (lexer.token == Symbol.GT) || (lexer.token == Symbol.GE)) {
				String mulop = aux.getLastMulOp();
				String addop = aux.getLastAddOp();
				if ((relationOP == true) && (mulop == null) && (addop == null)) {
					error("Not possible many instances of relationship comparison");
				} else if ((relationOP == false) || ((mulop != null) && (mulop.equals("&&"))) || ((addop != null) && (addop.equals("||")))) {
					relationOP = true;
					relop = lexer.token.toString();
					lexer.nextToken();
					expr = expr(true);
					if (expr == null) {
						error("SimExpr");
					}
					relationOP = false;

				} else {
					error("Invalid operand for comparison because was expected before the operand && or ||");
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
		char simpleChar = '\0';
		if (lValue != null) {
			if (lexer.token == Symbol.DEFINITION) {
				lexer.nextToken();
				// aqui tem que fazer a verificação de TIPOS
				if (lexer.token == Symbol.QUOTE) {
					lexer.nextToken();
					simpleChar = lexer.getCharValue();
					return new Factor(lValue, null, null, null, null, simpleChar);
				}else if(lexer.token == Symbol.READCHAR || lexer.token == Symbol.READDOUBLE || lexer.token == Symbol.READINTEGER){ 
                                        error("Impossible to execute " + lexer.token + " operation in this scope");
                                }else {
					expr = expr(true);
					if (expr != null) {
						return new Factor(lValue, expr, null, null, null, simpleChar);
					} else {
						error("factor: There is no expression");
					}
				}
			}
			return new Factor(lValue, null, null, null, null, '\0');
		} else if ((lexer.token == Symbol.NUMBER) || (lexer.token == Symbol.DOUBLE)) {
			Factor aux = null;
			if (lexer.token == Symbol.NUMBER) {
				aux = new Factor(null, null, null, lexer.getNumberValue(), null, simpleChar);
			} else {
				aux = new Factor(null, null, null, null, lexer.getStringValue(), simpleChar);
			}
			lexer.nextToken();
			return aux;

		} else if (lexer.token == Symbol.LEFTPAR) {
			lexer.nextToken();
			expr = expr(true);
			if (expr != null) {
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(null, expr, null, null, null, simpleChar);
				}
			}

		} else if (lexer.token == Symbol.READINTEGER) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readInteger()".toString(), null, null, simpleChar);
				}
			}

		} else if (lexer.token == Symbol.READDOUBLE) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readDouble()".toString(), null, null, simpleChar);
				}
			}

		} else if (lexer.token == Symbol.READCHAR) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readChar()".toString(), null, null, simpleChar);
				}
			}

		}

		return null;
	}

	// LValue ::= Ident | Ident '[' Expr ']'
	private LValue lValue() {
		String ident = null;
		CompositeExpr expr = null;
		// verificar se a variável existe

		ident = ident();
		if (ident != null) {
			// valida se existe a variável
			boolean flag = false;
			Variable aux = null;
			for (Variable v : variablesList) {
				if (v.getName().equals(ident) == true) {
					flag = true;
					aux = v;
					break;
				}

			}
			if (flag == false) {
				error("lValeu : There is no variable called \"" + ident + "\" in this scope");
			}
			if (lexer.token == Symbol.LEFTSQUARE) {
				if (aux.getType().isArray() == true) {
					lexer.nextToken();
					expr = expr(true);
					if (expr != null) {
						// M O N S T R O
						// tem que verificar se o tamanho do vetor é compatível....
						if (expr.onlySimExpr() == true) {
							if (expr.getSimexpr().onlyOneTerm() == true) {
								if (expr.getSimexpr().getTerm().onlyOneFactor() == true) {
									if (expr.getSimexpr().getTerm().getFactor().isOnlyNumber() == true) {
										if (expr.getSimexpr().getUnary() != null) {
											// então é só número!
											if (expr.getSimexpr().getUnary().equals("-") == true) {
												error("lValue : there is no negative index");
											}
										}

										// ok não é número negativo, verifico o tamanho
										if (expr.getSimexpr().getTerm().getFactor().getNumber() >= aux.getType().getSize()) {
											error("lValue : the index surpass the array's size");
										}

									}
								}
							}
						}

						if (lexer.token == Symbol.RIGHTSQUARE) {
							lexer.nextToken();
							return new LValue(ident, expr);
						}
					}
				} else {
					error("lValue : Variable \"" + aux.getName() + "\" is not a array");
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
                        aux = name.toLowerCase();
                        if(aux.equals(Symbol.IF.toString()) || aux.equals(Symbol.ELSE.toString()) || aux.equals(Symbol.WHILE.toString()) ||
                                aux.equals(Symbol.PRINT.toString()) || aux.equals(Symbol.BREAK.toString()) || aux.equals(Symbol.DOUBLE.toString()) || 
                                aux.equals(Symbol.CHAR.toString()) || aux.equals(Symbol.INTEGER.toString()))
                            error("Invalid variable name");
			return name;
		}
//		else {
//			error("ident: Variable must begin with a letter");
//		}
		return null;
	}

	private void error(String function) {
		if (lexer.tokenPos == 0) {
			lexer.tokenPos = 1;
		} else if (lexer.tokenPos >= input.length) {
			lexer.tokenPos = input.length;
		}
		System.out.println();
		String strInput = new String(input, lexer.tokenPos - 1, input.length - lexer.tokenPos + 1);
		String strError = "Error at file "+nomeArquivo+" \"" + strInput + "\" in " + function + "";
		System.out.println(strError);
		throw new RuntimeException(strError);
	}

	private Lexer lexer;
	public ArrayList<String> variableNames;
	private char[] input;
	private ArrayList<Variable> variablesList;

	private Stack whiles;
	private int pilha;

}
