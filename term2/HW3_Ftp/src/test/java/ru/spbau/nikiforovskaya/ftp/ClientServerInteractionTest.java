package ru.spbau.nikiforovskaya.ftp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClientServerInteractionTest {

    private int portNumber = 15000;
    private String hostName = "localhost";
    private Thread server;

    @BeforeAll
    static void setUpAll() throws IOException {
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

    @BeforeEach
    void setUp() throws InterruptedException {
        server = new Thread(() -> {
            try {
                new Server().runServer(portNumber);
            } catch (FileNotFoundException e) {
                System.err.println("Didn't manage to start server.");
                System.err.println("Error while opening log file");
            }
        });
        server.setDaemon(false);
        server.start();
        Thread.sleep(1000);
    }

    @AfterEach
    void tearDown() {
        while (server.isAlive()) {
            server.interrupt();
        }
    }

    @Test
    void testListEmptyDirectory() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources", "Empty");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("list " + pathToFile.toString() + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("0 lines got", result);
    }
    @Test
    void testListOnlyFile() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources", "1");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("list " + pathToFile.toString() + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String[] result = output.toString().trim().split(System.lineSeparator());
        assertArrayEquals(new String[] {"1 lines got",
               Paths.get(pathToFile.toString(), "text").toString() +" false"}, result);
    }

    @Test
    void testGetOnlyFile() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources", "1", "text");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("get " + pathToFile.toString() + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Successfully saved file to downloaded_text", result);
        String[] res = Files.lines(
                Paths.get("downloaded_text")).collect(Collectors.toList()).toArray(new String[0]);
        assertEquals(2, res.length);
        assertArrayEquals(new String[] {"meow", "meow"}, res);
        assertTrue(new File("downloaded_text").delete());
    }

    @Test
    void testListDirectoryWithTwoSubdirectories() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources", "2");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("list " + pathToFile.toString() + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String[] result = output.toString().trim().split(System.lineSeparator());

        assertEquals("3 lines got", result[0]);
        String[] ans = new String[] {
                "3 lines got",
                Paths.get(pathToFile.toString(), "yana").toString() + " false",
                Paths.get(pathToFile.toString(), "2.2").toString() + " true",
                Paths.get(pathToFile.toString(), "2.1").toString() + " true"
        };
        Arrays.sort(ans);
        Arrays.sort(result);
        assertArrayEquals(ans, result);
    }

    @Test
    void testGetEmptyFile() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources", "2", "2.2", "empty");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("get " + pathToFile.toString() + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Successfully saved file to downloaded_empty", result);
        String[] res = Files.lines(
                Paths.get("downloaded_empty")).collect(Collectors.toList()).toArray(new String[0]);
        assertEquals(0, res.length);
        assertArrayEquals(new String[] {}, res);
        assertTrue(new File("downloaded_empty").delete());
    }

    @Test
    void testGetFolder() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources",
                "2", "2.2");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("get " + pathToFile + System.lineSeparator() + "exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertArrayEquals(new String[] {"You have asked to download a directory.",
                "This command can only download files."}, result.split(System.lineSeparator()));
    }

    @Test
    void testGetPictureFile() throws ConnectionProtocolException, IOException {
        Path pathToFile = Paths.get("src", "test", "resources",
                "2", "2.1", "lena_512.bmp");
        ByteArrayInputStream input = new ByteArrayInputStream(
                ("get " + pathToFile.toString() + System.lineSeparator() +"exit")
                        .getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Successfully saved file to downloaded_lena_512.bmp", result);
        byte[] b1 = Files.readAllBytes(pathToFile);
        byte[] b2 = Files.readAllBytes(Paths.get("downloaded_lena_512.bmp"));
        assertArrayEquals(b1, b2);
        assertTrue(new File("downloaded_lena_512.bmp").delete());
    }

    @Test
    void testThrowsConnectionException() {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "3 abaca".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        assertThrows(ConnectionProtocolException.class, client::runClient);
    }

    @Test
    void testThrowsConnectionExceptionStringType() {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "bu abaca".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        assertThrows(ConnectionProtocolException.class, client::runClient);
    }
}