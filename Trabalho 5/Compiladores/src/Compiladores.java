/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
import AST.Program;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author ricke
 */
public class Compiladores {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        ArrayList<String> nomeArquivos = new ArrayList<String>();
        String dir = System.getProperty("user.dir");
        String dirTestes = System.getProperty("user.dir");
        dir = dir.concat("/src/Testes");
        dirTestes = dir.concat("/GenC");
        Boolean criaArq;

        System.out.println(dir);

        // pega todos os arquivos e adiciona um arrayList
        // de forma recursiva
        Files.walk(Paths.get(dir)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                nomeArquivos.add(filePath.getFileName().toString());
            }
        });
        Collections.sort(nomeArquivos);

        for (String nome : nomeArquivos) {
            criaArq = false;
            System.out.println(nome);
            String entrada = new String();
            String linha = null;
            try {
                FileReader arq = new FileReader(dir + "/" + nome);
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

            ArrayList<Program> program = null;
            try {
                program = compiler.compile(input, nome);
                System.out.println("OK!!!!");
                criaArq = true;
            } catch (RuntimeException e) {
                criaArq = false;
            }

            if (criaArq == true) {
                File file = new File(dirTestes + "/" + nome.replace(".txt", ".c"));
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                StringBuffer aux;
                Integer i = 0;

                if (program != null) {
                        writer.append("#include <stdio.h>\n");
                        writer.append("#include <stdlib.h>\n\n");
                        for (Program s : program) {
                            if (i != 0) {
                                writer.append("\n");
                            }
                            aux = s.genC(0);
                            writer.append(aux);
                            i++;
                        }

                    writer.close();
                }
            }
            System.out.println("==========================================================================");

        }
    }

}
