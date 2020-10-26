package com.company;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;


import java.io.FileReader;
import java.util.Scanner;

public class Main {

    private static final String input_file = "test.c";
    private static String textProgram;

    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }


    public static void main(String[] args) throws IOException {

            textProgram = readUsingFiles(input_file);

            Analyzer analyzer = new Analyzer();
            analyzer.Analysis(textProgram);


        for (Lex lex:analyzer.Lexemes) {
            switch(lex.id){
                case 1:
                    System.out.println("<" + lex.val + ">"  + " RESERVED");
                    break;
                case 2:
                    System.out.println("<" + lex.val + ">"  + " SEPARATORS");
                    break;
                case 3:
                    System.out.println("<" + lex.val + ">"  + " NUMBERS");
                    break;
                case 4:
                    System.out.println("<" + lex.val + ">"  + " IDENTIFIER");
                    break;
                case 6:
                    System.out.println("<" + lex.val + ">"  + " OPERATOR");
                    break;
                case 7:
                    System.out.println("<" + lex.val + ">"  + " STRING_CONSTANT");
                    break;
                case 8:
                    System.out.println("<" + lex.val + ">"  + " CHAR_CONSTANT");
                    break;
                case 9:
                    System.out.println("<" + lex.val + ">"  + " UNEXPECTED_STRING");
                    break;
                case 10:
                    System.out.println("<" + lex.val + ">"  + " HEXADECIMAL");
                    break;
                case 11:
                    System.out.println("<" + lex.val + ">"  + " PROCESSOR_DIRECTIVES");
                    break;

            }

        }

    }
}
