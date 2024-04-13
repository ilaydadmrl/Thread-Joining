package ceng.estu.edu.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InputParser {
    public static List<String> parseLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(lines::add);
        }
        return lines;
    }
}
