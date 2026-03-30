package findex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileIndexTest {

    @TempDir
    Path tempDir;

    @Test
    void searchFindsMatchingFile() throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "hello world\n");
        Files.writeString(tempDir.resolve("b.txt"), "nothing here\n");

        List<Path> results = FileIndex.search(tempDir, "hello");

        assertEquals(1, results.size());
        assertEquals("a.txt", results.get(0).getFileName().toString());
    }

    @Test
    void searchReturnsAllMatchingFiles() throws IOException {
        Files.writeString(tempDir.resolve("x.txt"), "foo bar\n");
        Files.writeString(tempDir.resolve("y.txt"), "foo baz\n");
        Files.writeString(tempDir.resolve("z.txt"), "no match\n");

        List<Path> results = FileIndex.search(tempDir, "foo");

        assertEquals(2, results.size());
    }

    @Test
    void searchReturnsEmptyListWhenNoMatch() throws IOException {
        Files.writeString(tempDir.resolve("only.txt"), "some content\n");

        List<Path> results = FileIndex.search(tempDir, "xyz");

        assertTrue(results.isEmpty());
    }

    @Test
    void searchIsRecursive() throws IOException {
        Path sub = Files.createDirectory(tempDir.resolve("sub"));
        Files.writeString(sub.resolve("nested.txt"), "find me\n");

        List<Path> results = FileIndex.search(tempDir, "find me");

        assertEquals(1, results.size());
        assertEquals("nested.txt", results.get(0).getFileName().toString());
    }

    @Test
    void fileContainsReturnsTrueForMatchingLine() throws IOException {
        Path file = Files.writeString(tempDir.resolve("test.txt"), "line one\nline two\n");

        assertTrue(FileIndex.fileContains(file, "line two"));
    }

    @Test
    void fileContainsReturnsFalseForNoMatch() throws IOException {
        Path file = Files.writeString(tempDir.resolve("test.txt"), "hello world\n");

        assertFalse(FileIndex.fileContains(file, "goodbye"));
    }
}
