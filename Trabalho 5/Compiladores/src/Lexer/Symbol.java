package Lexer;

public enum Symbol {
	STRING("string"),

	RETURN("return"),
	EOF("eof"),
	IDENT("Ident"),
	NUMBER("Number"),
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	LT("<"),
	LE("<="),
	GT(">"),
	GE(">="),
	NEQ("!="),
	EQ("=="),
	ASSIGN("="),
	LEFTPAR("("),
	RIGHTPAR(")"),
	SEMICOLON(";"),
	VAR("var"),
	BEGIN("begin"),
	END("end"),
	IF("if"),
	THEN("then"),
	ELSE("else"),
	ENDIF("endif"),
	COMMA(","),
	READ("read"),
	WRITE("write"),
	COLON(":"),
	DEFINITION(":="),
	BOOLEAN("boolean"),
	CHARACTER("character"),
	TRUE("true"),
	FALSE("false"),
	OR("||"),
	PIPE("|"),
	AND("&&"),
	REMAINDER("%"),
	NOT("!"),
	READINTEGER("readInteger"),
	READDOUBLE("readDouble"),
	READCHAR("readChar"),
	PRINT("print"),
	BREAK("break"),
	WHILE("while"),
	UNDERSCORE("_"),
	LEFTSQUARE("["),
	RIGHTSQUARE("]"),
	CHAR("char"),
	DOUBLE("double"),
	INTEGER("int"),
	LEFTBRACKET("{"),
	RIGHTBRACKET("}"),
	VOID("void"),
	MAIN("main"),
	QUOTE("'");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;

}
