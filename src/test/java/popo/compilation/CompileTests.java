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
    void compile(Path path) {
        IO.println(String.format("La méthode compile doit être complété par vos soins :) (fichier: %s)", path));
    }

    Stream<Path> getTestFiles() throws IOException, URISyntaxException {
        Stream<Path> res = Files.find(
                Path.of(getClass().getResource("/c-files").toURI()),
                Integer.MAX_VALUE,
                (path, bfa) -> path.toString().endsWith(".c") && bfa.isRegularFile()
        );

        return res;
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
