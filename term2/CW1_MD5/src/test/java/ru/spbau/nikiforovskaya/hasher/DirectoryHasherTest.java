package ru.spbau.nikiforovskaya.hasher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class for testing directory hasher
 *
 * All the hashes were counted with bash md5sum, manually doing the work.
 */
class DirectoryHasherTest {

    @BeforeEach
    void setUp() throws IOException {
        File f = Paths.get("src", "test", "resources").toFile();
        Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (file.getFileName().endsWith(".keep")) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void testGetDirectoryHashSimpleEmptyFolder() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashSimple(
                Paths.get("src", "test", "resources", "nothing").toString());
        assertEquals("3e47b75000b0924b6c9ba5759a7cf15d", res);
    }

    @Test
    void testGetDirectoryHashForkJoinEmptyFolder() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashForkJoin(
                Paths.get("src", "test", "resources", "nothing").toString());
        assertEquals("3e47b75000b0924b6c9ba5759a7cf15d", res);
    }

    @Test
    void testGetDirectoryHashSimpleRecursiveFolder() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashSimple(
                Paths.get("src", "test", "resources", "recursive").toString());
        assertEquals("50dfc99ad91bea214b5f0579db3bc2aa", res);
    }

    @Test
    void testGetDirectoryHashForkJoinRecursiveFolder() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashForkJoin(
                Paths.get("src", "test", "resources", "recursive").toString());
        assertEquals("50dfc99ad91bea214b5f0579db3bc2aa", res);
    }

    @Test
    void testGetDirectoryHashSimpleOnlyFile() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashSimple(
                Paths.get("src", "test", "resources", "file").toString());
        assertEquals("8689c2de0584ec7dbe5bd1e2503d1f91", res);
    }

    @Test
    void testGetDirectoryHashForkJoinOnlyFile() throws NoSuchAlgorithmException {
        String res = DirectoryHasher.getDirectoryHashForkJoin(
                Paths.get("src", "test", "resources", "file").toString());
        assertEquals("8689c2de0584ec7dbe5bd1e2503d1f91", res);
    }
}