package kam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Parser {
    public static void parseFile(Path file) {
        try (Stream<String> stream = Files.lines(file)) {
            stream.forEach(Parser::parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseLine(String line) {
        if (line.startsWith("[")) {
            return;
        }
        int index = line.indexOf('=');
        String path = line.substring(0, index);
        String data = line.substring(index);
    }
}
