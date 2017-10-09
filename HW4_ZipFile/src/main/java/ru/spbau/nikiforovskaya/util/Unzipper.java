package ru.spbau.nikiforovskaya.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Unzipper class can do several thinks with zip files.
 * You can extract some files from zips, search for zips, etc.
 */
public class Unzipper {

    /**
     * Returns all zip files in the path you pass to the function.
     * @param path path where to search for zip files
     * @return list of zip files found
     * @throws IOException
     */
    public static ArrayList<File> getAllZipFiles(String path) throws IOException {
        ArrayList<File> result = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(path))) {
            for (Path file : stream.collect(Collectors.toList())) {
                if (file.getFileName().toString().endsWith(".zip")) {
                    result.add(file.toFile());
                }
            }
        }
        return result;
    }

    /**
     * Returns all zip entries-files matching the given regular expression in the given ZipFile.
     * @param source a zip file where to search for
     * @param regex a regular expression to be matched
     * @return list of ZipFiles, containing needed files.
     */
    public static ArrayList<ZipEntry> getZipEntriesMatchingRegex(ZipFile source, String regex) {
        ArrayList<ZipEntry> result = new ArrayList<>();
        for (Enumeration<? extends ZipEntry> fileEnum = source.entries();
             fileEnum.hasMoreElements();) {
            ZipEntry file = fileEnum.nextElement();
            if (!file.isDirectory() && file.getName().matches(regex)) {
                result.add(file);
            }
        }
        return result;
    }

    /**
     * Extracts the given file from zip file to the given path.
     * A path is needed to be to the directory or non existing. (Directories will be created.)
     * Extracts saving all the directory tree.
     * @param file a zip entry to extract from zip file
     * @param zipFile a zip file to extract from
     * @param dirPath a path to extract to
     * @throws IOException
     */
    public static void extractZipEntry(ZipEntry file, File zipFile, String dirPath)
            throws IOException {
        File outputFile = Paths.get(dirPath,
                zipFile.getName().substring(0, zipFile.getName().length() - 4),
                file.getName()).toFile();
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        FileSystem fileSystem = FileSystems.newFileSystem(zipFile.toPath(), null);
        Files.copy(fileSystem.getPath(file.getName()), outputFile.toPath());
        fileSystem.close();
    }

    /**
     * Finds all files matching given regular expression from all zip files found by the given path
     * and extracts them to the given path, saving all the directory tree.
     * @param source path where search for zip files
     * @param regex a regular expression to match files in zip files
     * @param destination a path where to extract files to.
     * @throws IOException
     */
    public static void extractFilesByRegex(String source, String regex, String destination)
            throws IOException {
        ArrayList<File> allZipFiles = getAllZipFiles(source);
        for (File file : allZipFiles) {
            ArrayList<ZipEntry> entries = getZipEntriesMatchingRegex(new ZipFile(file), regex);
            for (ZipEntry entry : entries) {
                extractZipEntry(entry, file, destination);
            }
        }
    }
}
