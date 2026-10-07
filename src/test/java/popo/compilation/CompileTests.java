package popo.compilation;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class CompileTests {
    /**
     * Compile le fichier passé en paramètre et échoue si le résultat n'est pas conforme au code machine attendu
     * @param path le chemin du fichier à compiler
     * @throws RuntimeException en cas de résultat incorrect à la compilation
     */
    void compile(Path path) throws RuntimeException {
        IO.println(String.format("La méthode compile doit être complété par vos soins :) (fichier: %s)", path));

        // récupérer le fichier compilé par vos soins d'une manière ou d'une autre
        // comparer avec le fichier valide (dans /results) et faire planter si c'est pas bon
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
