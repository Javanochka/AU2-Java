package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing Unzipper.
 * Uses resources.
 */
class UnzipperTest {

    File direction;

    @BeforeEach
    void setUp() {
        direction = Paths.get("src", "test", "resources", "res").toFile();
        direction.mkdir();
    }

    @AfterEach
    void tearDown() throws IOException {
        File f = Paths.get("src", "test", "resources", "res").toFile();
        Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException
            {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw e;
                }
            }
        });
    }

    @Test
    void testGetAllZipFiles() throws IOException {
        ArrayList<File> res = Unzipper.getAllZipFiles(
                Paths.get("src", "test", "resources").toString());
        assertEquals(2, res.size());
        Collections.sort(res);
        String[] names = {
                res.get(0).toString(),
                res.get(1).toString(),
        };
        assertArrayEquals(new String[] {
                Paths.get("src", "test", "resources", "fA", "fB.zip").toString(),
                Paths.get("src", "test", "resources", "fA", "fB", "fA.zip").toString()
        }, names);
    }

    @Test
    void testGetZipEntriesMatchingRegex() throws IOException {
        ArrayList<File> allZips = Unzipper.getAllZipFiles(
                Paths.get("src", "test", "resources").toString());
        Collections.sort(allZips);
        ArrayList<ZipEntry> res =
        Unzipper.getZipEntriesMatchingRegex(new ZipFile(allZips.get(1)), ".*\\.txt");
        Collections.sort(res, Comparator.comparing(ZipEntry::toString));
        assertEquals(3, res.size());
        assertArrayEquals(new String[] {
                "d1.txt",
                "d2.txt",
                Paths.get("fB", "d3.txt").toString()
        }, new String[] {
                res.get(0).toString(),
                res.get(1).toString(),
                res.get(2).toString()
        });
    }

    @Test
    void testExtractZipEntry() throws IOException {
        ArrayList<File> allZips = Unzipper.getAllZipFiles(
                Paths.get("src", "test", "resources").toString());
        Collections.sort(allZips);
        ArrayList<ZipEntry> allEntries =
                Unzipper.getZipEntriesMatchingRegex(new ZipFile(allZips.get(1)), ".*\\.txt");
        Collections.sort(allEntries, Comparator.comparing(ZipEntry::toString));
        Unzipper.extractZipEntry(allEntries.get(0), allZips.get(1),
                Paths.get("src", "test", "resources", "res").toString());
        File res = Paths.get(direction.toString(), "fA", "d1.txt").toFile();
        assertTrue(res.exists());
        File check = Paths.get("src", "test", "resources", "fA", "d1.txt").toFile();
        assertArrayEquals(
                Files.readAllLines(check.toPath()).toArray(),
                Files.readAllLines(res.toPath()).toArray());
    }

    @Test
    void testExtractFilesByRegex() throws IOException {
        Unzipper.extractFilesByRegex(Paths.get("src", "test", "resources").toString(),
                ".*\\.txt",
                direction.getPath());
        ArrayList<File> result = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(direction.getPath()))) {
            for (Path file : stream.collect(Collectors.toList())) {
                if (!file.toFile().isDirectory()) {
                    result.add(file.toFile());
                }
            }
        }
        Collections.sort(result, Comparator.comparing(File::toString));
        assertEquals(4, result.size());
        assertArrayEquals(new String[] {
                Paths.get(direction.getPath(), "fA", "d1.txt").toString(),
                Paths.get(direction.getPath(), "fA", "d2.txt").toString(),
                Paths.get(direction.getPath(), "fA", "fB", "d3.txt").toString(),
                Paths.get(direction.getPath(), "fB", "fB", "d3.txt").toString()

        }, new String[] {
                result.get(0).toString(),
                result.get(1).toString(),
                result.get(2).toString(),
                result.get(3).toString()
        });
    }

}