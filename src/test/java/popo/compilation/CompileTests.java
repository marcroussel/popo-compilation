package popo.compilation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompileTests {

    private static final Path ROOT = Path.of("").toAbsolutePath();
    private static final Path MSM_DIR = ROOT.resolve("msm");

    /**
     * Démarre un processus, en exécutant la commande cmd passée en paramètres, dans un répertoire de la machine spécifié
     * @param cmd la commande exécutée par le processus
     * @param workDir le répertoire dans lequel la commande s'exécutera
     * @return la sortie standard de la commande
     * @throws IOException en cas d'erreur de communication entre le sous-processus et le programme appelant
     * @throws InterruptedException en cas d'interruption inattendue du processus
     * @throws RuntimeException en cas d'erreur d'exécution du processus
     */
    private static String run(List<String> cmd, Path workDir) throws IOException, InterruptedException, RuntimeException {
        Process p = new ProcessBuilder(cmd)
                .directory(workDir.toFile())
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (p.waitFor() != 0) throw new RuntimeException("Échec de : " + String.join(" ", cmd));
        return out.replace("\r\n", "\n");
    }

    /**
     * Compile le fichier passé en paramètre et échoue si le résultat n'est pas conforme au code machine attendu
     * @param path le chemin du fichier à compiler
     * @throws RuntimeException en cas de résultat incorrect à la compilation
     */
    void compile(Path path) throws RuntimeException {
        try {
            String name = path.getFileName().toString().replaceFirst("\\.c$", "");
            Path res = path.getParent().getParent();                    // .../src/test/resources

            // 1. Compilation avec Main dans une JVM séparée
            String asm = run(List.of(
                    Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                    "-cp", System.getProperty("java.class.path"),
                    "popo.compilation.Main", path.toString()), ROOT);

            Path expectedAsm = res.resolve("results").resolve(name + ".txt");
            assertEquals(Files.readString(expectedAsm).strip(), asm.strip(), "Code machine différent pour " + name);

            // 2. Exécution sur msm (si un .log attendu existe)
            Path expectedLog = res.resolve("outputs").resolve(name + ".log");
            if (Files.exists(expectedLog)) {
                Path tmp = Files.createTempFile(name, ".txt");
                Files.writeString(tmp, asm);
                String msmOut = run(List.of(MSM_DIR.resolve("msm.exe").toString(), tmp.toString()), MSM_DIR);
                assertEquals(Files.readString(expectedLog).strip(), msmOut.strip(), "Sortie msm différente pour " + name);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtient un stream des fichiers dont la compilation doit être testée
     * @return le stream
     * @throws IOException en cas d'erreur lors du parcours des fichiers
     * @throws URISyntaxException si le dossier n'est pas trouvé ou est sur un chemin avec un caractère invalide
     */
    Stream<Path> getTestFiles() throws IOException, URISyntaxException {
        return Files.find(
                Path.of(getClass().getResource("/c-files").toURI()),
                Integer.MAX_VALUE,
                (path, bfa) -> path.toString().endsWith(".c") && bfa.isRegularFile()
        );
    }

    // TODO provisoire (debug) : à supprimer
    /** Nom du fichier de test à déboguer (sans l'extension .c) */
    private static final String DEBUG_FILE = "22_espaces_commentaires";

    /** Compile uniquement le fichier DEBUG_FILE. Modifier la constante ci-dessus pour changer de fichier. */
    @Test
    void compileOne() throws IOException, URISyntaxException {
        Path file = Path.of(getClass().getResource("/c-files").toURI()).resolve(DEBUG_FILE + ".c");
        compile(file);
    }

    @TestFactory
    ArrayList<DynamicTest> generateTests() throws IOException {
        ArrayList<DynamicTest> out = new ArrayList<>();

        try {
            getTestFiles().forEach((path ->
                out.add(
                    DynamicTest.dynamicTest(
                        path.toString(),    // nom du test
                        () -> compile(path) // ce que doit exécuter le test
                    )
                )
            ));
        } catch (URISyntaxException e) {
            System.err.println("Erreur lors de la génération des tests: " + e.getMessage());
        }

        return out;
    }
}
