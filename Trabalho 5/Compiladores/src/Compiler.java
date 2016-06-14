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

	private SymbolTable symbolTable;
	private Lexer lexer;
	private char[] input;
	private Stack whiles;
	private Stack exprValido;
	private Stack returnValido;
	private int pilha;
	private boolean relationOP = false;
	private String nomeArquivo;
	private boolean soloing = false;

	public ArrayList<Program> compile(char[] p_input, String nome) {
		this.nomeArquivo = nome;
		input = p_input;
		lexer = new Lexer(p_input, nome);
		lexer.nextToken();
		symbolTable = new SymbolTable();
		whiles = new Stack();
		exprValido = new Stack();
		returnValido = new Stack();
		pilha = 0;

		return program();
	}

	//Program ::= Decl
	private ArrayList<Program> program() {
		ArrayList<Program> listProgram = new ArrayList<Program>();
		boolean flag = false;
                Integer i = 0;

		Program aux = null;

		while ((aux = decl()) != null) {
			if (aux.getFdecl().getIdent().equals("main")) {
				flag = true;
			}
			listProgram.add(aux);
			aux = null;
			// variáveis podem tero mesmo nome mesmo em funções diferentes...
			symbolTable.removeLocalIdent();
		}

		if (flag == false) {
			error("You must declare a main function.");
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
		} else {
			returnValido.push(type);
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
					error("The name of function " + ident + " has already been declared!");
				}
				if (lexer.token == Symbol.LEFTPAR) {
					lexer.nextToken();
					formals = formals();

					if ((formals != null) && (ident.equals("main"))) {
						error("The function 'main' should not have parameters.");
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
				error("Expected a name for function");
			}

		}

		return null;

	}

	private Formals formals() {
		ArrayList<Variable> listV = null;
		boolean flag = false;

		Variable aux = variable(true);

		if (aux != null) {
			listV = new ArrayList<Variable>();
			listV.add(aux);
			aux = null;

			if (lexer.token == Symbol.COMMA) {
				lexer.nextToken();
				aux = variable(true);

				if (aux == null) {
					error("Expected a variable after a comma");
				}
				while (aux != null) {
					listV.add(aux);
					if (lexer.token == Symbol.COMMA) {
						lexer.nextToken();
						flag = true;
					} else {
						flag = false;
						break;
					}
					aux = variable(true);
				}
				if (flag == true) {
					error("Expected a variable after a comma");
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
				if (auxiliarStmt.getReturnFunc() != null) {
					if (returnValido.isEmpty() == false) {
						returnValido.pop();
					}
				}
				auxiliarStmt = null;
			}
		} else {
			error("Expected { to open the statement.");
		}

		if (returnValido.isEmpty() != true) {
			// quando essa pilha não esta vazia
			error("Expected a return for this function");
		}

		if (lexer.token == Symbol.RIGHTBRACKET) {
			StmtBlock smtmblock = new StmtBlock(variablesList, stmt);
			lexer.nextToken();
			return smtmblock;
		} else {
			error("Expected } to close the statement.");
		}
		return null;
	}

	//VariableDecl ::= Variable ';'
	private Variable variableDecl() {
		Variable aux = variable(false);
		if (aux != null) {
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				return aux;
			} else {
				error("Expected a ';' after the declaration's variable.");
				return null;
			}
		} else {
			return null;
		}
	}

	// Variable ::= Type Ident
	private Variable variable(Boolean formals) {
		Variable aux = null;
		Type type = null;
		String name = null;

		type = type();
		if (type != null) {
			name = ident();
			if (name != null) {
				if (symbolTable.getInLocal(name) != null) {
					error("Variable \"" + name + "\" already exists!");
				}
				aux = new Variable(name, type, formals);
				symbolTable.putInLocal(name, aux);
				return aux;
			} else {
				error("The name of variable is not set, because the name used \"" + lexer.getStringValue() + "\" is reserved"
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
			error("The type must be lowercase");
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
						if (type.getType() == Symbol.INTEGER) {
							type.setType(Symbol.INTEGERARRAY);
						} else if (type.getType() == Symbol.CHAR) {
							type.setType(Symbol.CHARARRAY);
						} else if (type.getType() == Symbol.DOUBLE) {
							type.setType(Symbol.DOUBLEARRAY);
						}
						return type;
					} else {
						error("Expected ]");
						return null;
					}
				} else if (lexer.token == Symbol.MINUS) {
					error("Array size must not be negative");
				} else {
					error("Missing array size");
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
			if ((exprValido.isEmpty() == true)) {
				if ((aux.getType() == Symbol.READCHAR) || (aux.getType() == Symbol.READINTEGER) || (aux.getType() == Symbol.READDOUBLE)) {
					error("Functions like readChar, readInteger and readDouble must be declared after a :=");
				} else {
					error("The expression declared is not valid.");
				}
			} else {
				exprValido.pop();
			}
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
			} else if (aux != null) {
				error("Expected \";\"");
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
										error("Expected '{' after the else statement");
									}
								}
							}
							ifstmt = new IfStmt(auxiliarExp, principal, opcional);
							return ifstmt;
						} else {
							error("Expected '{' in if statement");
						}
					} else {
						error("Expected ')' in if statement");
					}
				} else {
					error("Expected expression in if statement");
				}
			} else {
				error("Expected '(' in if statement");
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
								error("Expected '}' in while statement");
							}
						} else {
							error("Expected '{' in while statement");
						}
					} else {
						error("Expected ')' in while statement");
					}
				} else {
					error("Expected expression in while statement");
				}
			} else {
				error("Expected '(' in while statement");
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
					error("Expected a ';' after break");
				}
			} catch (EmptyStackException e) {
				error("Break out of a while");
			}
		}

		return false;
	}

	//PrintStmt ::= 'p' '(' Expr { ',' Expr }')'
	private PrintStmt printStmt() {
		ArrayList<Expr> listaExp = new ArrayList<Expr>();
		Expr aux;
                String escrita;

		if (lexer.token == Symbol.PRINT) {
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				lexer.nextToken();
                                if(!lexer.getStringValue().isEmpty())
                                    escrita = lexer.getStringValue();
                                else
                                    escrita = null;
				if ((aux = expr(true)) != null || escrita != null) {
                                        if(aux != null)
                                            listaExp.add(aux);
					aux = null;
					while (lexer.token == Symbol.COMMA) {
						lexer.nextToken();
						if ((aux = expr(true)) != null) {
							listaExp.add(aux);
							aux = null;
						} else {
							error("Expected expression after comma in print statement.");
						}
					}

					if (lexer.token == Symbol.RIGHTPAR) {
						lexer.nextToken();
						if (lexer.token == Symbol.SEMICOLON) {
							lexer.nextToken();
							PrintStmt imprime = new PrintStmt(listaExp, escrita);
							return imprime;
						} else {
							error("Expected ';' in print statement");
						}

					} else {
						error("Expected ')' in print statement");
					}
				} else {
					error("Expected a expression in print statement");
				}
			} else {
				error("Expected '(' in print statement");
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
					error("It is not possible to use the operator ! with a double");
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
								error("Operator % cannot be used between double");
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
				// testando validação de expr
				exprValido.push(1);

				lexer.nextToken();
				expr = expr(true);
				if (expr != null) {
					if ((lValue.getType().getType() == Symbol.READCHAR)
						|| (lValue.getType().getType() == Symbol.READDOUBLE)
						|| (lValue.getType().getType() == Symbol.READINTEGER)) {
						error("readChar, readInteger and readDouble should be declared after a ':=', not before");
					}

					if (lValue.getType().getType() == Symbol.INTEGER) {
//						System.out.println(expr.getType());
						if ((expr.getType() == Symbol.DOUBLE) || (expr.getType() == Symbol.READDOUBLE)) {
							error("You cannot set in a integer a double value");
						} else if ((expr.getType() == Symbol.CHAR) || (expr.getType() == Symbol.READCHAR)) {
							error("You cannot set in a integer a char value");
						} else if (expr.getType() == Symbol.VOID) {
							error("You cannot set a void value, because it is void");
						} else if ((expr.getType() == Symbol.INTEGERARRAY) || (expr.getType() == Symbol.CHARARRAY)
							|| expr.getType() == Symbol.DOUBLEARRAY) {
							error("You cannot set in a integer value a array");
						}
					} else if (lValue.getType().getType() == Symbol.CHAR) {
						simpleChar = lexer.getCharValue();
//						System.out.irintln(expr.getType());
						if ((expr.getType() == Symbol.DOUBLE) || (expr.getType() == Symbol.READDOUBLE)) {
							error("You cannot set in a char a double value");
						} else if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.READINTEGER)) {
							error("You cannot set in a char a integer value");
						} else if (expr.getType() == Symbol.VOID) {
							error("You cannot set a void value, because it is void");
						} else if (expr.getType() == Symbol.STRING) {
							if (lValue.getType().isArray() == true) {
								if (lValue.getType().getSize() >= expr.getSimexpr().getTerm().getFactor().getSingleChar().length()) {
									//tudo bem, da certo
								} else {
									error("The size of array isn't enough");
								}
							} else if (expr.getSimexpr().getTerm().getFactor().getSingleChar().length() > 1) {
								error("You cannot set into a char value a String more than 1 character");
							}
						} else if ((expr.getType() == Symbol.INTEGERARRAY) || (expr.getType() == Symbol.CHARARRAY)
							|| expr.getType() == Symbol.DOUBLEARRAY) {
							error("You cannot set in a char value a array");
						}

					} else if (lValue.getType().getType() == Symbol.DOUBLE) {
						doublee = lexer.getStringValue();
						if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.READINTEGER)) {
							error("You cannot set in a double a integer value");
						} else if ((expr.getType() == Symbol.CHAR) || (expr.getType() == Symbol.READCHAR)) {
							error("You cannot set in a double a char value");
						} else if (expr.getType() == Symbol.VOID) {
							error("You cannot set a void value, because it is void");
						} else if ((expr.getType() == Symbol.INTEGERARRAY) || (expr.getType() == Symbol.CHARARRAY)
							|| expr.getType() == Symbol.DOUBLEARRAY) {
							error("You cannot set in a double value a array");
						}
						// tratando os casos se for array
					} else if ((lValue.getType().getType() == Symbol.INTEGERARRAY) || (lValue.getType().getType() == Symbol.CHARARRAY)
						|| (lValue.getType().getType() == Symbol.DOUBLEARRAY)) {

						if (expr.getType() == Symbol.STRING) {
							if (lValue.getType().getSize() >= expr.getSimexpr().getTerm().getFactor().getSingleChar().length()) {
								if ((lValue.getType().getType() == Symbol.INTEGERARRAY) || (lValue.getType().getType() == Symbol.DOUBLEARRAY)) {
									error("You cannot set a String into a Integer Array or a Double Array");
								}
							} else {
								error("The size of array isn't enough");
							}
						} else if ((expr.getType() == Symbol.INTEGERARRAY) || (expr.getType() == Symbol.CHARARRAY)
							|| expr.getType() == Symbol.DOUBLEARRAY) {
							error("You cannot set into a char value a String more than 1 character");
						}
						if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.CHAR) || (expr.getType() == Symbol.DOUBLE)) {
							error("You cannot set a value to array");
						}
					}
				} else {
					error("There is no expression");
				}

			}
			return new Factor(lValue, expr, null, null, doublee, simpleChar, lValue.getType().getType(), null);
		} else if ((lexer.token == Symbol.NUMBER) || (lexer.token == Symbol.DOUBLE)) {
			Factor aux = null;
			if (lexer.token == Symbol.NUMBER) {
				aux = new Factor(null, null, null, lexer.getNumberValue(), null, simpleChar, Symbol.NUMBER, null);
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
					return new Factor(null, expr, null, null, null, simpleChar, expr.getType(), null);
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
				error("There is no variable called \"" + ident + "\" in this scope");
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
												error("There is no negative index");
											}
										}

										// ok não é número negativo, verifico o tamanho
										if (expr.getSimexpr().getTerm().getFactor().getNumber() >= aux.getType().getSize()) {
											error("The index surpass the array's size");
										}

									}
								}
							}
						}
						if (aux.getType().getType() == Symbol.INTEGERARRAY) {
							// dessa forma que esta comentado abaixo, ele esta mudando via referência
							// isso pode complicar quando é feito uma chamada de função, pois como
							// foi mudada durante execução, o compilador não consegue definir se é um array
							//auxType.setType(Symbol.INTEGER);
							auxType = new Type(Symbol.INTEGER, auxType.isArray(), auxType.getSize());
						} else if (aux.getType().getType() == Symbol.CHARARRAY) {
							//auxType.setType(Symbol.CHAR);
							auxType = new Type(Symbol.CHAR, auxType.isArray(), auxType.getSize());
						} else if (aux.getType().getType() == Symbol.DOUBLEARRAY) {
							//auxType.setType(Symbol.DOUBLE);
							auxType = new Type(Symbol.DOUBLE, auxType.isArray(), auxType.getSize());
						}
						if (lexer.token == Symbol.RIGHTSQUARE) {
							lexer.nextToken();
							return new LValue(ident, expr, auxType);
						}
					}
				} else {
					error("Variable \"" + aux.getName() + "\" is not a array");
				}

			} else {
				// essa verificação tem que ser feita no factor :/ infelizmente
				// inclusive no call e outras funções q tem atribuição
				if ((aux.getType().isArray() == true) && (auxType.getType() != Symbol.CHAR)) {
					if (aux.getType().getType() == Symbol.INTEGER) {
						auxType.setType(Symbol.INTEGERARRAY);
					} else if (aux.getType().getType() == Symbol.CHAR) {
						auxType.setType(Symbol.CHARARRAY);
					} else if (aux.getType().getType() == Symbol.DOUBLE) {
						auxType.setType(Symbol.DOUBLEARRAY);
					}
					//error("lValue : you must declare which index do you want to use");
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
		if (lexer.tokenPos == 0) {
			lexer.tokenPos = 1;
		} else if (lexer.tokenPos >= input.length) {
			lexer.tokenPos = input.length;
		}
		System.out.println();
		String strError = "\n" + nomeArquivo + " : " + lexer.getLineNumber() + " : " + function;
		System.out.println(strError);
		throw new RuntimeException(strError);

		//String strInput = new String(input, lexer.tokenPos - 1, input.length - lexer.tokenPos + 1);
		//	String strError = "Error at file " + nomeArquivo + " \"" + strInput + "\" in " + function + "";
		//	throw new RuntimeException(strError);
	}

	private Expr returnStmt(Symbol type) {
		if (lexer.token == Symbol.RETURN) {
			lexer.nextToken();
			CompositeExpr expr = expr(true);
			if (lexer.token == Symbol.SEMICOLON) {
				lexer.nextToken();
				if (expr == null) {
					return expr;
				} else if (type == Symbol.INTEGER) {
					if ((expr.getType() == Symbol.INTEGER) || (expr.getType() == Symbol.NUMBER)) {
						// vida louca
						return expr;
					} else {
						error("The return value is not the same as the declared function");
					}
				} else if (type == expr.getType()) {
					return expr;
				} else {
					error("The return value is not the same as the declared function");
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
				error("There is no function called " + ident);
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
						error("The numbers of parameters passed to function is not the same as declared");
					} else {
						// verificação de tipos de parâmetros
						CompositeExpr auxComp = null;
						for (Expr s : listExpr) {
							auxComp = (CompositeExpr) s;
							if (auxComp.getType() != listV.get(i).getType().getType()) {
								if ((auxComp.getType() == Symbol.NUMBER) && (listV.get(i).getType().getType() == Symbol.INTEGER)) {
									// ok
								} else {
									error("The parameter number " + (i + 1) + " is not the same type as declared in function" + auxComp.getType() + " " + listV.get(i).getType().getType());
								}
							}
							i++;
						}
					}
				} else if ((listExpr != null) && (listV == null)) {
					error("You are passing parameters to a function that dont have parameters");
				} else if ((listExpr == null) && (listV != null)) {
					error("The function that has been called should receive parameters");
				} else if ((listExpr == null) && (listV == null)) {
					// apenas pra facilitar a leitura do código
					// nessa situação o programa deve aceitar...
				}

				if (lexer.token == Symbol.RIGHTPAR) {
					lexer.nextToken();
					exprValido.push(1);
					return new Call(listExpr, ident);
				} else {
					error("Expected a ')' in call statement");
				}
			}
		}

		return null;

	}

}
