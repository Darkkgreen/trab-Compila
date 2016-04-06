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
		ArrayList<Variable> ret = new ArrayList<Variable>();
                ArrayList<Stmt> stmt = new ArrayList<Stmt>();
		Variable aux = null;
                Stmt auxiliarStmt = null;
                
		if (token == '{') {
			nextToken();
			while ((aux = variableDecl()) != null) {
                            ret.add(aux);
                            aux = null;
			}
			while ((auxiliarStmt = stmt()) != null){
                            stmt.add(auxiliarStmt);
                            auxiliarStmt = null;
                        }

		} else {
			error("stmtBlock");
		}

		if (token == '}') {
			Program program = new Program(ret, stmt);
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
	private Stmt stmt() {
            IfStmt se = null;
            WhileStmt enquanto= null;
            boolean parada = false;
            PrintStmt imprime = null;
            CompositeExpr aux = null;
            Stmt stmt = null;
            
		if ((((aux = expr()) != null) && (token == ';')) || (se = ifStmt()) != null || (enquanto = whileStmt()) != null || (parada = breakStmt()) || (imprime = printStmt()) != null) {
			Stmt novo = new Stmt(se, enquanto, parada, imprime, aux);
                        if (token == ';') {
				nextToken();
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
            
		if (token == 'f') {
			nextToken();
			if (token == '(') {
				nextToken();
				if ((auxiliarExp = expr()) != null) {
					if (token == ')') {
						nextToken();
						if (token == '{') {
							nextToken();
							while ((auxiliarStmt = stmt()) != null) {
                                                                principal.add(auxiliarStmt);
                                                                auxiliarStmt = null;
								if (token == '}') {
									nextToken();
									if (token == 'e') { //aqui entra a parte opcional
										nextToken();
										if (token == '{') {
											nextToken();
											while ((auxiliarStmt = stmt()) != null) {
                                                                                            opcional.add(auxiliarStmt);
                                                                                            auxiliarStmt = null;
												if (token == '}') {
													nextToken();
												}else
                                                                                                    error("IfStmt");
											}
										}else
                                                                                    error("IfStmt");
									}       
								}else
                                                                    error("IfStmt");
							}
                                                        ifstmt = new IfStmt(auxiliarExp, principal, opcional);
                                                        return ifstmt;
						}else
                                                    error("IfStmt");
					}else
                                            error("IfStmt");
				}else
                                    error("IfStmt");
			}else
                            error("IfStmt");
		}
		return null;
	}

	//WhileStmt ::= 'w' '(' Expr ')' '{' { Stmt } '}'
	private WhileStmt whileStmt() {
            Expr auxiliarExp = null;
            ArrayList<Stmt> arrayPrinc = new ArrayList<Stmt>();
            Stmt auxiliarSt = null;
            
		if (token == 'w') {
			nextToken();
			if (token == '(') {
				nextToken();
				if ((auxiliarExp = expr()) != null) {
					if (token == ')') {
						nextToken();
						if (token == '{') {
							nextToken();
							while ((auxiliarSt = stmt()) != null) {
                                                            arrayPrinc.add(auxiliarSt);
                                                            auxiliarSt = null;
							}
                                                        if (token == '}') {
                                                            nextToken();
                                                            WhileStmt enquanto = new WhileStmt(arrayPrinc, auxiliarExp);
                                                            return enquanto;
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

		return null;
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
	private PrintStmt printStmt() {
            ArrayList<Expr> listaExp = new ArrayList<Expr>();
            Expr aux;
            
		if (token == 'p') {
			nextToken();
			if (token == '(') {
				nextToken();
				if ((aux = expr()) != null) {
                                    listaExp.add(aux);
                                    aux = null;
					while (token == ',') {
						nextToken();
						if ((aux = expr()) != null) {
                                                    listaExp.add(aux);
                                                    aux = null;
                                                    nextToken();
						}else
                                                    error("PrintStmt");
					}

					if (token == ')') {
						nextToken();
                                                PrintStmt imprime = new PrintStmt(listaExp);
						return imprime;
					}else
                                            error("PrintStmt");
				}else
                                    error("PrintStmt");
			}else
                            error("PrintStmt");
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
			relop = relOp();
			if (relop != null) {
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
		String aux= null;
		String aux2 = null;
		Term termAux = null;
		Term termAux2 = null;
		ArrayList<String> addop = null;
		ArrayList<Term> termList = null;
		aux = unary();	
	
		termAux = term();
		if (termAux != null) {
			while (true) {
				aux2 = addOp();
				if(aux2 != null){
					if(addop == null)
						addop = new ArrayList<String>();
					else{
						break;
					}
					addop.add(aux);
					termAux2 = term();
					if(termAux2 != null){
						if(termList == null)
							termList = new ArrayList<Term>();
						termList.add(termAux);
					}else{
						error("SimExpr1");
						break;
					}
				}
			}
			return new SimExpr(aux, termAux, addop, termList);
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
				aux2 = mulOp();
				if(aux2 != null){
					if(mulop == null)
						mulop = new ArrayList<String>();
					mulop.add(aux2);
					aux3 = factor();
					if(aux3 != null){
						if(factorList == null)
							factorList = new ArrayList<Factor>();
						factorList.add(aux3);
					}else
						error("Term");
				}else
					break;
			}

			return new Term(aux, mulop, factorList);
		}

		return null;
	}

	// Factor ::= LValue ':' Expr | LValue | '(' Expr ')' | 'r' '(' ')' | 's' '(' ')' | 't' '(' ')'
	private Factor factor() {
		LValue aux = null;
		aux = lValue();
		CompositeExpr aux2 = null;
		if (aux != null) {
			if (token == ':') {
                            nextToken();
			        aux2 = expr();
				if (aux2 != null) {
					return new Factor(aux, aux2, null);
				}
			}
			return new Factor(aux, null, null);
		} else if (token == '(') {
			nextToken();
			aux2 = expr();
			if (aux2 != null) {
				if (token == ')') {
					nextToken();
					return new Factor(null, aux2, null); 
				}
			}

		} else if (token == 'r') {
                        nextToken();
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return new Factor(aux, null, "r()".toString());
				}
			}

		} else if (token == 's') {
                        nextToken();
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return new Factor(aux, null, "s()".toString());
				}
			}

		} else if (token == 't') {
                        nextToken();
			if (token == '(') {
				nextToken();
				if (token == ')') {
					nextToken();
					return new Factor(aux, null, "t()".toString());
				}
			}
		}

		return null;
	}

	// LValue ::= Ident | Ident '[' Expr ']'
	private LValue lValue() {
		String aux = null;
		Expr aux2 = null;
		aux = ident();
		if (aux != null) {
			if (token == '[') {
				nextToken();
				aux2 = expr();
				if (aux2 != null) {
					if (token == ']') {
						
						nextToken();
						return new LValue(aux, aux2);
					}
				}
			} else {
				return new LValue(aux, null);
			}
		}
		return null;
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
	private String relOp() {
		char ret = ' ';
		switch (token) {
			case '=':
			case '#':
			case '<':
			case '>':
				ret = token;
				nextToken();
				return Character.toString(ret);
			default:
				return null;
		}
	}

	//AddOp ::= '+' | '-'
	private String addOp() {
		char ret = ' ';
		switch (token) {
			case '+':
			case '-':
				ret = token;
				nextToken();
				return Character.toString(ret);
			default:
				return null;
		}
	}

	//MulOp ::= '*' | '/' | '%'
	private String mulOp() {
		char ret = ' ';
		switch (token) {
			case '*':
			case '/':
			case '%':
				ret = token;
				nextToken();
				return Character.toString(ret);
			default:
				return null;
		}
	}

	// Unary ::= '+' | '-' | '!'
	private String unary() {
		char ret = ' ';
		switch (token) {
			case '+':
			case '-':
			case '!':
				ret = token;
				nextToken();
				return Character.toString(ret);
			default:
				return null;
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
