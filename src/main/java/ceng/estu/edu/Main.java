package ceng.estu.edu;

import ceng.estu.edu.utils.Node;
import ceng.estu.edu.utils.InputParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

public class Main {
    @Option(name = "-i", aliases = "--i", usage = "Path of the input file is being processed.", required = true)
    private String path;
    Set<Node> threads = new HashSet<>();
    List<String> lines = new ArrayList<>();

    public static void main(String[] args) {
        final Main instance = new Main();
        try {
            instance.getArgs(args);
            instance.getLines();
            instance.createThreads();
            instance.executeThreads();
        } catch (IOException ex) {
            out.println("An unexpected I/O Exception has been occurred: " + ex);
        }
    }

    private void getArgs(final String[] args) throws IOException {
        final CmdLineParser parser = new CmdLineParser(this);
        if (args.length < 1) {
            parser.printUsage(out);
            System.exit(-1);
        }
        try {
            parser.parseArgument(args);
            if (!Files.exists(Path.of(path))) {
                out.println("Given path is not incorrect.");
                parser.printUsage(out);
                System.exit(-1);
            }
        } catch (CmdLineException ex) {
            out.println("Unable to parse command-line options: " + ex);
        }
    }

    private void getLines() {
        try {
            lines = InputParser.parseLines(path);
        } catch (IOException e) {
            out.println("Unexpected error has been occurred.");
        }
    }

    private void createThreads() {
        for (String line : lines) {
            String[] flow = line.split("->");

            if (flow.length == 1) {
                Node thread = new Node(flow[0]);
                thread = findOrGet(thread);
                threads.add(thread);
                continue;
            }

            String[] prerequisites = flow[0].split(",");
            HashSet<Node> prerequisiteThreads = new HashSet<>();
            for (String prerequisite : prerequisites) {
                Node thread = new Node(prerequisite);
                thread = findOrGet(thread);
                prerequisiteThreads.add(thread);
                threads.add(thread);
            }

            String partialThread = flow[1];
            Node thread = new Node(partialThread);
            thread = findOrGet(thread);
            thread.prerequisites = prerequisiteThreads;
            threads.add(thread);
        }
    }

    private Node findOrGet(Node thread) {
        final String name = thread.name;
        return threads.contains(thread) ? threads.stream().filter(thread1 -> thread1.name.equals(name)).findFirst().get() : thread;
    }

    private void executeThreads() {
        for (Node node : threads) {
            node.perform();
        }
    }
}
