package Lexer;

import java.util.*;
import AST.*;

public class Lexer {

	//public Lexer(char[] input, CompilerError error) {
	public Lexer(char[] input) {
		this.input = input;
		// add an end-of-file label to make it easy to do the lexer
		input[input.length - 1] = '\0';
		// number of the current line
		lineNumber = 1;
		tokenPos = 0;
		//this.error = error;
	}

	// contains the keywords
	static private Hashtable<String, Symbol> keywordsTable;

	// this code will be executed only once for each program execution
	static {
		keywordsTable = new Hashtable<String, Symbol>();
		keywordsTable.put("var", Symbol.VAR);
		keywordsTable.put("begin", Symbol.BEGIN);
		keywordsTable.put("end", Symbol.END);
		keywordsTable.put("if", Symbol.IF);
		keywordsTable.put("then", Symbol.THEN);
		keywordsTable.put("else", Symbol.ELSE);
		keywordsTable.put("endif", Symbol.ENDIF);
		keywordsTable.put("read", Symbol.READ);
		keywordsTable.put("write", Symbol.WRITE);
		keywordsTable.put("integer", Symbol.INTEGER);
		keywordsTable.put("boolean", Symbol.BOOLEAN);
		keywordsTable.put("char", Symbol.CHAR);
		keywordsTable.put("true", Symbol.TRUE);
		keywordsTable.put("false", Symbol.FALSE);
		keywordsTable.put("and", Symbol.AND);
		keywordsTable.put("or", Symbol.OR);
		keywordsTable.put("not", Symbol.NOT);

		keywordsTable.put("readInteger", Symbol.READINTEGER);
		keywordsTable.put("readDouble", Symbol.READDOUBLE);
		keywordsTable.put("readChar", Symbol.READCHAR);
		keywordsTable.put("print", Symbol.PRINT);
		keywordsTable.put("break", Symbol.BREAK);
		keywordsTable.put("while", Symbol.WHILE);
		keywordsTable.put("void", Symbol.VOID);
		keywordsTable.put("main", Symbol.MAIN);
		keywordsTable.put("int", Symbol.INTEGER);
		keywordsTable.put("double", Symbol.DOUBLE);
		keywordsTable.put("char", Symbol.CHAR);

	}

	public void nextToken() {
		char ch;

		while ((ch = input[tokenPos]) == ' ' || ch == '\r'
			|| ch == '\t' || ch == '\n') {
			// count the number of lines
			if (ch == '\n') {
				lineNumber++;
			}
			System.out.print(input[tokenPos]);
			tokenPos++;
		}
		if (ch == '\0') {
			token = Symbol.EOF;
		} else if (input[tokenPos] == '/' && input[tokenPos + 1] == '/') {
			// comment found
			while (input[tokenPos] != '\0' && input[tokenPos] != '\n') {
				System.out.print(input[tokenPos]);
				tokenPos++;
			}

			nextToken();
		} else if (input[tokenPos] == '/' && input[tokenPos + 1] == '*') {
			// comment found
			while (input[tokenPos] != '\0' && (input[tokenPos] != '*' || input[tokenPos+1] != '/')){
				System.out.print(input[tokenPos]);
				tokenPos++;
			}
			System.out.print(input[tokenPos]);
			tokenPos++;
			System.out.print(input[tokenPos]);
			tokenPos++;
			nextToken();
		} else if (Character.isLetter(ch)) {
			// get an identifier or keyword
			StringBuffer ident = new StringBuffer();
			while (Character.isLetter(input[tokenPos])) {
				System.out.print(input[tokenPos]);
				ident.append(input[tokenPos]);
				tokenPos++;
			}
			stringValue = ident.toString();
			// if identStr is in the list of keywords, it is a keyword !
			Symbol value = keywordsTable.get(stringValue);
			if (value == null) {
				token = Symbol.IDENT;
			} else {
				token = value;
			}
			//if (Character.isDigit(input[tokenPos])) {
			//	System.out.println("Word followed by a number");
			//}
			//System.out.println(value);
		} else if (Character.isDigit(ch)) {
			// get a number
			StringBuffer number = new StringBuffer();
			while (Character.isDigit(input[tokenPos])) {
				System.out.print(input[tokenPos]);
				number.append(input[tokenPos]);
				tokenPos++;
			}
			token = Symbol.NUMBER;
			try {
				numberValue = Integer.valueOf(number.toString()).intValue();
			} catch (NumberFormatException e) {
				//System.out.println("Number out of limits");
			}
			stringValue = number.toString();
			if (numberValue >= MaxValueInteger) {
				//System.out.println("Number out of limits");
			}

		} else {
			System.out.print(input[tokenPos]);
			tokenPos++;

			switch (ch) {
				case '+':
					token = Symbol.PLUS;
					break;
				case '-':
					token = Symbol.MINUS;
					break;
				case '*':
					token = Symbol.MULT;
					break;
				case '/':
					token = Symbol.DIV;
					break;
				case '%':
					token = Symbol.REMAINDER;
					break;
				case '<':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.LE;
					} else if (input[tokenPos] == '>') {
						tokenPos++;
						token = Symbol.NEQ;
					} else {
						token = Symbol.LT;
					}
					break;
				case '>':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.GE;
					} else {
						token = Symbol.GT;
					}
					break;
				case '=':
					if (input[tokenPos] == '=') {
						tokenPos++;
						token = Symbol.EQ;
					} else {
						token = Symbol.ASSIGN;
					}
					break;
				case '_':
					token = Symbol.UNDERSCORE;
					break;
				case '(':
					token = Symbol.LEFTPAR;
					break;
				case ')':
					token = Symbol.RIGHTPAR;
					break;
				case '[':
					token = Symbol.LEFTSQUARE;
					break;
				case ']':
					token = Symbol.RIGHTSQUARE;
					break;
				case '{':
					token = Symbol.LEFTBRACKET;
					break;
				case '}':
					token = Symbol.RIGHTBRACKET;
					break;
				case ',':
					token = Symbol.COMMA;
					break;
				case ';':
					token = Symbol.SEMICOLON;
					break;
				case ':':
					token = Symbol.COLON;
					break;
				case '\'':
					token = Symbol.CHARACTER;
					charValue = input[tokenPos];
					tokenPos++;
					if (input[tokenPos] != '\'') {
						System.out.println("Illegal literal character" + input[tokenPos - 1]);
					}
					tokenPos++;
					break;
				default:
					System.out.println("Invalid Character: '" + ch + "'");
				//error.signal("Invalid Character: '" + ch + "'");
				}
		}
		lastTokenPos = tokenPos - 1;
	}

	// return the line number of the last token got with getToken()
	public int getLineNumber() {
		return lineNumber;
	}

	public String getCurrentLine() {
		int i = lastTokenPos;
		if (i == 0) {
			i = 1;
		} else if (i >= input.length) {
			i = input.length;
		}

		StringBuffer line = new StringBuffer();
		// go to the beginning of the line
		while (i >= 1 && input[i] != '\n') {
			i--;
		}
		if (input[i] == '\n') {
			i++;
		}
		// go to the end of the line putting it in variable line
		while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
			line.append(input[i]);
			i++;
		}
		return line.toString();
	}

	public String getStringValue() {
		return stringValue;
	}

	public int getNumberValue() {
		return numberValue;
	}

	public char getCharValue() {
		return charValue;
	}
	// current token
	public Symbol token;
	private String stringValue;
	private int numberValue;
	private char charValue;

	public int tokenPos;
	//  input[lastTokenPos] is the last character of the last token
	private int lastTokenPos;
	// program given as input - source code
	private char[] input;

	// number of current line. Starts with 1
	private int lineNumber;

	//private CompilerError error;
	private static final int MaxValueInteger = 32768;
}
