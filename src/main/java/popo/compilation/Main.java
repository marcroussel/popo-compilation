package popo.compilation;

import popo.compilation.lexicalAnalysis.LexicalAnalysis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {

        // Lecture du fichier de test C, en mode flux (buffer)
        try (InputStream inputStream = Main.class.getResourceAsStream("/samples/petit_test.c");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // Conversion du buffer du fichier en String
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            String code = builder.toString();
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(code);
            lexicalAnalysis.init();

        } catch (Exception e) {
            System.out.println("ERROR : " + e.toString());
        }
    }
}
