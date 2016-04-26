/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import AST.Program;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author ricke
 */
public class Compiladores {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		String dir = System.getProperty("user.dir");
		dir.concat("/");
		System.out.println("current dir = " + dir);
		Scanner ler = new Scanner(System.in);
		String nome = ler.nextLine();
		System.out.println(nome);
		String entrada = new String();
		String linha = null;
		System.out.println(dir+"/"+nome);
		try {
			FileReader arq = new FileReader(dir+"/"+nome);
			BufferedReader lerArq = new BufferedReader(arq);

			linha = lerArq.readLine();
			while (linha != null) {
				entrada = entrada.concat(linha).concat("\n");
				linha = lerArq.readLine();
				
			}
			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
				e.getMessage());
		}
		

		entrada = entrada.concat(" ");

		char[] input = entrada.toCharArray();
		Compiler compiler = new Compiler();

		Program program = compiler.compile(input, nome);
		program.genC();
	}

}
