/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */

import AST.*;
import AuxComp.SymbolTable;
import java.util.ArrayList;
import Lexer.*;
import java.util.EmptyStackException;
import java.util.Stack;

public class Compiler {

	private boolean relationOP = false;
	private String nomeArquivo;
	private boolean soloing = false;

	public ArrayList<Program> compile(char[] p_input, String nome) {
		this.nomeArquivo = nome;
		input = p_input;
		lexer = new Lexer(p_input);
		lexer.nextToken();
		symbolTable = new SymbolTable();
		whiles = new Stack();
		pilha = 0;

		return program();

		// ver isso daqui pode estar errado
		//		if (tokenPos != input.length) {
		//			error("compile");
		//		}
	}

	//Program ::= Decl
	private ArrayList<Program> program() {
		ArrayList<Program> listProgram = new ArrayList<Program>();
		boolean flag = false;

		Program aux = null;

		while ((aux = decl()) != null) {
			if (aux.getFdecl().getIdent().equals("main")) {
				flag = true;
			}
			listProgram.add(aux);
			aux = null;
		}

		if (flag == false) {
			error("Não há nenhuma função main declarada!!!!!!!!!");
		}

		// verifcação semântica se tem o void main
		//
		//
		//
		return listProgram;
	}

	private FunctionDecl fdecl() {
		String ident = null;
		StmtBlock stmt = null;
		Formals formals = null;
		Type type = null;

		// aqui deduzo que se for void, vai ser null
		// portanto, não deve ter outro if else e talz...
		type = type();
		if (type == null) {
			if (lexer.token == Symbol.VOID) {
				type = new Type(Symbol.VOID, false, 0);
				lexer.nextToken();
			}
		}

		if (type != null) {
			ident = ident();
			if (ident == null) {
				// tratando o erro
				if (lexer.token == Symbol.MAIN) {
					ident = "main";
					lexer.nextToken();
				}
			}
			if (ident != null) {
				// verificando se não existe uma função com o mesmo nome
				if (symbolTable.getInGlobal(ident) != null) {
					error(": the name " + ident + " has already been declared!");
				}
				if (lexer.token == Symbol.LEFTPAR) {
					lexer.nextToken();
					formals = formals();

					if ((formals != null) && (ident.equals("main"))) {
						error("A função main não deve ter parâmetros!!");
					}
					//if (formals != null) {
					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						stmt = stmtBlock(type.getType());

						// adicionando a função no symbolTable
						FunctionDecl aux = new FunctionDecl(type, ident, stmt, formals);
						symbolTable.putInGlobal(ident, aux);

						return aux;
					}
					//} else {
					//	error("fdecl2");
					//}
				}
			} else {
				error("fdecl : expected a name for function");
			}

		}

		return null;

	}

	private Formals formals() {
		ArrayList<Variable> listV = null;

		Variable aux = variable();

		if (aux != null) {
			listV = new ArrayList<Variable>();
			listV.add(aux);
			aux = null;

			if (lexer.token == Symbol.COMMA) {
				lexer.nextToken();
				while ((aux = variable()) != null) {
					listV.add(aux);
					if (lexer.token == Symbol.COMMA) {
						lexer.nextToken();
					} else {
						break;
					}
				}
			}

		}

		if (listV != null) {
			return new Formals(listV);
		} else {
			return null;
		}
	}

	//Decl ::= FunctionDecl
	private Program decl() {
		FunctionDecl ret = fdecl();

		if (ret != null) {
			return new Program(ret);
		}
		return null;
	}

	//StmtBlock ::= '{' { VariableDecl } { Stmt } '}'
	private StmtBlock stmtBlock(Symbol type) {
		ArrayList<Variable> variablesList = new ArrayList<Variable>();
		ArrayList<Stmt> stmt = new ArrayList<Stmt>();
		Variable aux = null;
		Stmt auxiliarStmt = null;

		if (lexer.token == Symbol.LEFTBRACKET) {
			lexer.nextToken();
			while ((aux = variableDecl()) != null) {
				variablesList.add(aux);
				aux = null;
			}
			while ((auxiliarStmt = stmt(type)) != null) {
				stmt.add(auxiliarStmt);
				auxiliarStmt = null;
			}
		} else {
			error("stmtBlock: expected {");
		}

		if (lexer.token == Symbol.RIGHTBRACKET) {
			StmtBlock smtmblock = new StmtBlock(variablesList, stmt);
			lexer.nextToken();
			return smtmblock;
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
				if (symbolTable.getInLocal(name) != null) {
					error("variable: Variable \"" + name + "\" already exists!");
				}
				aux = new Variable(name, type);
				symbolTable.putInLocal(name, aux);
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
	private Stmt stmt(Symbol type) {
		IfStmt se = null;
		WhileStmt enquanto = null;
		boolean parada = false;
		PrintStmt imprime = null;
		CompositeExpr aux = null;
		Stmt stmt = null;

		soloing = true;
		if ((aux = expr(false)) != null) {
			if ((aux.getType() == Symbol.READCHAR) || (aux.getType() == Symbol.READINTEGER) || (aux.getType() == Symbol.READDOUBLE)) {
				error("Stmt : functions like readChar, readInteger and readDouble must be declared after a :=");
			}
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
			} else if (aux != null) {
				error("stmt: expected \";\"");
			}
			return new Stmt(null, null, false, null, aux, null);
		}
		soloing = false;

		se = ifStmt(type);
		if (se != null) {
			return new Stmt(se, null, false, null, null, null);
		}
		enquanto = whileStmt(type);
		if (enquanto != null) {
			return new Stmt(null, enquanto, false, null, null, null);
		}

		parada = breakStmt();
		if (parada == true) {
			return new Stmt(null, null, true, null, null, null);
		}
		imprime = printStmt();
		if (imprime != null) {
			return new Stmt(null, null, false, imprime, null, null);
		}

		Expr returnFunc = returnStmt(type);
		if (returnFunc != null) {
			return new Stmt(null, null, false, null, null, returnFunc);
		}

		return null;
	}

	//IfStmt ::= 'f' '(' Expr ')' '{' { Stmt } '}' [ 'e' '{' { Stmt } '}' ]
	private IfStmt ifStmt(Symbol type) {
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
							while ((auxiliarStmt = stmt(type)) != null) {
								principal.add(auxiliarStmt);
								auxiliarStmt = null;
							}
							if (lexer.token == Symbol.RIGHTBRACKET) {
								lexer.nextToken();
								if (lexer.token == Symbol.ELSE) { //aqui entra a parte opcional
									lexer.nextToken();
									if (lexer.token == Symbol.LEFTBRACKET) {
										lexer.nextToken();
										while ((auxiliarStmt = stmt(type)) != null) {
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
	private WhileStmt whileStmt(Symbol type) {
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
							while ((auxiliarSt = stmt(type)) != null) {
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
						} else {
							error("printStmt : expected ;");
						}

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
		Symbol auxType = null;

		aux = simExpr();
		if (aux != null) {
			auxType = aux.getType();
//			System.out.println(auxType+"COMPOSITE");
			//if (aux.getSolo() == false && possible == false) {
			//	error("Expression not in the actual format");
			//}
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
			//if(soloing == true){
			//    error("Expression not in the actual format");
			//}
			return new CompositeExpr(aux, relop, expr, auxType);
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
		Symbol auxType = null;

		if ((lexer.token == Symbol.MINUS) || (lexer.token == Symbol.NOT) || (lexer.token == Symbol.PLUS)) {
			unary = lexer.token.toString();
			lexer.nextToken();
		}

		termAux = term();
		if (termAux != null) {
			auxType = termAux.getType();
//			System.out.println(auxType+"TERM");
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

					// tratando se for int := double
					if (termAux2.getType() == Symbol.DOUBLE) {
						auxType = Symbol.DOUBLE;
					}
				} else {
					break;
				}
			}
			// tratando se for int := double
			if (termAux.getType() == Symbol.DOUBLE) {
				if (unary != null && unary.equals("!") == true) {
					error("simExpr : it is not possible to use the operator ! with a double");
				}
			}
			return new SimExpr(unary, termAux, addop, termList, auxType);
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
		Symbol auxType = null;

		aux = factor();

		if (aux != null) {
//			System.out.println(auxType);
			auxType = aux.getType();
//			System.out.println(auxType+"TERM");
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

					// verificando possibilidade do modulo
					if (aux.getType() == Symbol.DOUBLE) {
						auxType = Symbol.DOUBLE;
						if (aux3.getType() == Symbol.DOUBLE) {
							if (mulop.get(mulop.size() - 1).equals("%")) {
								error("Term : operator % cannot be used between double");
							}
						}
					}

				} else {
					break;
				}
			}

			return new Term(aux, mulop, factorList, auxType);
		}

		return null;
	}

	// Factor ::= LValue ':' Expr | LValue | '(' Expr ')' | 'r' '(' ')' | 's' '(' ')' | 't' '(' ')'
	private Factor factor() {
		LValue lValue = null;
		String ident = ident();
		lValue = lValue(ident);
		CompositeExpr expr = null;
		String doublee = null;
		Call callAux = null;
		String simpleChar = new String("\0");
		if (lValue != null) {
			if (lexer.token == Symbol.DEFINITION) {
				lexer.nextToken();
				expr = expr(true);
				if (expr != null) {
					if (lValue.getType().getType() == Symbol.INTEGER) {
//						System.out.println(expr.getType());
						if ((expr.getType() == Symbol.DOUBLE) || (expr.getType() == Symbol.READDOUBLE)) {
							error("Factor : you cannot set in a integer a double value");
						} else if ((expr.getType() == Symbol.CHAR) || (expr.getType() == Symbol.READCHAR)) {
							error("Factor : you cannot set in a integer a char value");
						} else if (expr.getType() == Symbol.VOID) {
							error("Factor : VOIDD VALUEEEEEE");
						}
					} else if (lValue.getType().getType() == Symbol.CHAR) {
						simpleChar = lexer.getCharValue();
//						System.out.println(expr.getType());
						if ((expr.getType() == Symbol.DOUBLE) || (expr.getType() == Symbol.READDOUBLE)) {
							error("Factor : you cannot set in a char a double value");
						} else if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.READINTEGER)) {
							error("Factor : you cannot set in a char a integer value");
						} else if (expr.getType() == Symbol.VOID) {
							error("Factor : VOIDD VALUEEEEEE");
						} else if (expr.getType() == Symbol.STRING) {
							if (lValue.getType().isArray() == true) {
								if (lValue.getType().getSize() >= expr.getSimexpr().getTerm().getFactor().getSingleChar().length()) {
									//tudo bem, da certo
								} else {
									error("O TAMANHO NAO DEU CERTO CARA");
								}
							} else if (expr.getSimexpr().getTerm().getFactor().getSingleChar().length() > 1) {
								error("O TAMANHO NÃO BATE CARA");
							}
						}

					} else if (lValue.getType().getType() == Symbol.DOUBLE) {
						doublee = lexer.getStringValue();
//						System.out.println(expr.getType());
						if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.READINTEGER)) {
							error("Factor : you cannot set in a double a integer value");
						} else if ((expr.getType() == Symbol.CHAR) || (expr.getType() == Symbol.READCHAR)) {
							error("Factor : you cannot set in a double a char value");
						} else if (expr.getType() == Symbol.VOID) {
							error("Factor : VOIDD VALUEEEEEE");
						}
					}
				} else {
					error("factor: There is no expression");
				}

			}
			return new Factor(lValue, expr, null, null, doublee, simpleChar, lValue.getType().getType(), null);
		} else if ((lexer.token == Symbol.NUMBER) || (lexer.token == Symbol.DOUBLE)) {
			Factor aux = null;
			if (lexer.token == Symbol.NUMBER) {
				aux = new Factor(null, null, null, lexer.getNumberValue(), null, simpleChar, Symbol.INTEGER, null);
			} else {
				aux = new Factor(null, null, null, null, lexer.getStringValue(), simpleChar, Symbol.DOUBLE, null);
			}
			lexer.nextToken();
//			System.out.println(aux.getType()+"FACTOR");
			return aux;

		} else if (ident == null && lexer.token == Symbol.LEFTPAR) {
			lexer.nextToken();
			expr = expr(true);
			if (expr != null) {
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(null, expr, null, null, null, simpleChar, null, null);
				}
			}

		} else if (lexer.token == Symbol.READINTEGER) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readInteger()".toString(), null, null, simpleChar, Symbol.READINTEGER, null);
				}
			}

		} else if (lexer.token == Symbol.READDOUBLE) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readDouble()".toString(), null, null, simpleChar, Symbol.READDOUBLE, null);
				}
			}

		} else if (lexer.token == Symbol.READCHAR) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Factor(lValue, null, "readChar()".toString(), null, null, simpleChar, Symbol.READCHAR, null);
				}
			}

		} else if (lexer.token == Symbol.STRING) {
			lexer.nextToken();
			simpleChar = lexer.getStringValue();
			return new Factor(lValue, null, null, null, null, simpleChar, Symbol.STRING, null);
		} else if (lexer.token == Symbol.QUOTE) {
			lexer.nextToken();
			simpleChar = lexer.getCharValue();
			return new Factor(lValue, null, null, null, null, simpleChar, Symbol.CHAR, null);
		} else if ((callAux = call(ident)) != null) {
			Symbol auxType = ((FunctionDecl) symbolTable.getInGlobal(callAux.getIdent())).getType().getType();
			return new Factor(lValue, null, null, null, null, simpleChar, auxType, callAux);
		}

		return null;
	}

	// LValue ::= Ident | Ident '[' Expr ']'
	private LValue lValue(String ident) {
		CompositeExpr expr = null;

		if (ident != null) {
			// valida se existe a variável
			boolean flag = false;
			Variable aux = null;
			Type auxType = null;

			if (symbolTable.getInLocal(ident) != null) {
				// existe a variável
				flag = true;
				auxType = ((Variable) symbolTable.getInLocal(ident)).getType();
				aux = (Variable) symbolTable.getInLocal(ident);
			} else if (symbolTable.getInGlobal(ident) != null) {
				// deixar o Call tratar
				return null;
			} else {
				error("lValue There is no variable called \"" + ident + "\" in this scope");
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
							return new LValue(ident, expr, auxType);
						}
					}
				} else {
					error("lValue : Variable \"" + aux.getName() + "\" is not a array");
				}

			} else {
				if ((aux.getType().isArray() == true) && (auxType.getType() != Symbol.CHAR)) {
					error("lValue : you must declare which index do you want to use");
				}
				return new LValue(ident, null, auxType);
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

	private void error(String function) {
		// if (lexer.tokenPos == 0) {
		// 	lexer.tokenPos = 1;
		// } else if (lexer.tokenPos >= input.length) {
		// 	lexer.tokenPos = input.length;
		// }
		// System.out.println();
		// String strInput = new String(input, lexer.tokenPos - 1, input.length - lexer.tokenPos + 1);
		// String strError = "Error at file " + nomeArquivo + " \"" + strInput + "\" in " + function + "";
		// System.out.println(strError);
		// throw new RuntimeException(strError);

		String strInput = new String(input, lexer.tokenPos - 1, input.length - lexer.tokenPos + 1);
		String strError = "Error at file " + nomeArquivo + " \"" + strInput + "\" in " + function + "";
		throw new RuntimeException(strError);
	}

	private SymbolTable symbolTable;

	private Lexer lexer;
	private char[] input;

	private Stack whiles;
	private int pilha;

	private Expr returnStmt(Symbol type) {
		if (lexer.token == Symbol.RETURN) {
			lexer.nextToken();
			CompositeExpr expr = expr(true);
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				if (expr == null) {
					return expr;
				} else if (type == expr.getType()) {
					return expr;
				} else {
					error("NÃO É DO MESMO TIPO O RETORNO");
				}
			}

		}
		return null;
	}

	private ArrayList<Expr> actuals() {
		ArrayList<Expr> ret = new ArrayList<Expr>();
		CompositeExpr aux = null;
		boolean flag = false;

		while ((aux = expr(true)) != null) {
			flag = true;
			ret.add(aux);
			aux = null;
			if (lexer.token == Symbol.COMMA) {
				lexer.nextToken();
			} else {
				break;
			}
		}

		if (flag == true) {
			return ret;
		} else {
			return null;
		}
	}

	private Call call(String ident) {
		if (ident != null) {
			// verificação semântica
			if (symbolTable.getInGlobal(ident) == null) {
				error("Call : there is no function called " + ident);
			}

			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();

				ArrayList<Expr> listExpr = actuals();

				// verificação semântica 
				FunctionDecl aux = (FunctionDecl) symbolTable.getInGlobal(ident);
				Formals auxForm = aux.getFormals();
				ArrayList<Variable> listV = null;
				try {
					listV = auxForm.getListV();
				} catch (RuntimeException e) {
					listV = null;
				}

				Integer i = 0;

				if ((listExpr != null) && (listV != null)) {
					// quando ambos tem parâmetros
					if (listExpr.size() != listV.size()) {
						error("Não é o mesmo número de parâmetros!!");
					} else {
						// verificação de tipos de parâmetros
						CompositeExpr auxComp = null;
						for (Expr s : listExpr) {
							auxComp = (CompositeExpr) s;
							if (auxComp.getType() != listV.get(i).getType().getType()) {
								error("O parâmetro número " + (i + 1) + " não é do mesmo tipo da função chamada");
							}
							i++;
						}
					}
				} else if ((listExpr != null) && (listV == null)) {
					error("Esta sendo passado parâmetros para uma função que não tem!!!");
				} else if ((listExpr == null) && (listV != null)) {
					error("A função que esta sendo invocada deve receber parâmetros!!");
				} else if ((listExpr == null) && (listV == null)) {
					// apenas pra facilitar a leitura do código
					// nessa situação o programa deve aceitar...
				}

				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					return new Call(listExpr, ident);
				} else {
					error("Call : expected a )");
				}
			}
		}

		return null;

	}

}
