package popo.compilation;

import popo.compilation.codeGenerator.CodeGenerator;
import popo.compilation.debug.DebugLogger;
import popo.compilation.token.ValidTokens;


public class Main {

    public static CodeGenerator codeGenerator;

    public static void main(String[] args) {

        // Initialisation de la machine virtuelle
        System.out.println(".start");

        // Exception levée si aucun fichier n'a été renseigné
        if (args.length == 0) {
            System.err.println("Erreur : aucun fichier à compiler n'a été fourni.");
            System.err.println("Usage : ./run.sh <fichier_source.c>");
            System.exit(1);
        }

        // Importation et début de lecture du fichier à compiler
        DebugLogger.log("[DEBUG] Compiling file: " + args[0]);
        init(args[0]);

        // Génération du code pour la machine msm, jusqu'à trouver la fin du fichier
        while (codeGenerator.semanticAnalysis.syntaxAnalysis.lexicalAnalysis.currentToken.type != ValidTokens.EOS) {
            codeGenerator.genCode();
        }

        // On arrête le programme
        System.out.println(".halt");
    }
    
    public static void init(String filePath) {
        codeGenerator = new CodeGenerator(filePath);
        codeGenerator.semanticAnalysis.syntaxAnalysis.lexicalAnalysis.next();
    }
}
