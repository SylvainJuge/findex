package findex;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Entry point for the findex CLI application.
 *
 * <p>Usage:
 * <pre>
 *   findex &lt;directory&gt; &lt;search-term&gt;
 * </pre>
 *
 * <p>Recursively searches files under {@code directory} and prints paths of
 * files whose content contains {@code search-term}.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: findex <directory> <search-term>");
            System.exit(1);
        }

        Path directory = Path.of(args[0]);
        String searchTerm = args[1];

        if (!directory.toFile().isDirectory()) {
            System.err.println("Error: '" + directory + "' is not a directory");
            System.exit(1);
        }

        try {
            List<Path> matches = FileIndex.search(directory, searchTerm);
            if (matches.isEmpty()) {
                System.out.println("No files found containing: " + searchTerm);
            } else {
                matches.forEach(System.out::println);
            }
        } catch (IOException e) {
            System.err.println("Error searching files: " + e.getMessage());
            System.exit(1);
        }
    }
}
