package popo.compilation.debug;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Écrit les logs de débogage dans un fichier texte, afin de ne pas les mélanger
 * avec la sortie standard qui sert à générer les instructions de la machine virtuelle.
 */
public class DebugLogger {

    public static final String LOG_FILE = "debug.log";

    private static PrintWriter writer;

    private static PrintWriter getWriter() {
        if (writer == null) {
            try {
                // Le fichier est écrasé à chaque exécution
                writer = new PrintWriter(new FileWriter(LOG_FILE, false), true);
            } catch (IOException e) {
                System.err.println("Impossible d'ouvrir le fichier de log : " + e.getMessage());
                return null;
            }
        }
        return writer;
    }

    public static void log(String message) {
        PrintWriter w = getWriter();
        if (w != null) {
            w.println(message);
        }
    }

    public static void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
