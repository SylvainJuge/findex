package findex;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Searches files under a directory for a given term.
 */
public class FileIndex {

    private FileIndex() {
    }

    /**
     * Recursively searches all regular files under {@code directory} and
     * returns paths of files whose content contains {@code searchTerm}.
     *
     * @param directory  root directory to search
     * @param searchTerm term to look for inside file contents
     * @return list of matching file paths, sorted for deterministic output
     * @throws IOException if an I/O error occurs while walking the directory
     */
    public static List<Path> search(Path directory, String searchTerm) throws IOException {
        List<Path> matches = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(directory)) {
            walk.filter(Files::isRegularFile)
                .sorted()
                .forEach(file -> {
                    try {
                        if (fileContains(file, searchTerm)) {
                            matches.add(file);
                        }
                    } catch (IOException e) {
                        // skip files that cannot be read (e.g. binary files or permission errors)
                        System.err.println("Warning: skipping '" + file + "': " + e.getMessage());
                    }
                });
        }
        return matches;
    }

    /**
     * Returns {@code true} if any line in {@code file} contains {@code searchTerm}.
     */
    static boolean fileContains(Path file, String searchTerm) throws IOException {
        try {
            return Files.lines(file).anyMatch(line -> line.contains(searchTerm));
        } catch (MalformedInputException e) {
            // binary file – skip
            return false;
        }
    }
}
