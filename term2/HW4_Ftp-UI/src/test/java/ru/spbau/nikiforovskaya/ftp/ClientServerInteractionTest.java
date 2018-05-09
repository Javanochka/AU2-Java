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
        ByteArrayInputStream input = new ByteArrayInputStream(
                "1 src/test/resources/Empty\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("0 lines got", result);
    }
    @Test
    void testListOnlyFile() throws ConnectionProtocolException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "1 src/test/resources/1\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("1 lines got\nsrc/test/resources/1/text false", result);
    }

    @Test
    void testGetOnlyFile() throws ConnectionProtocolException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "2 src/test/resources/1/text\ntext\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Type in filename:\nSuccessfully saved file to text", result);
        String[] res = Files.lines(
                Paths.get("text")).collect(Collectors.toList()).toArray(new String[0]);
        assertEquals(2, res.length);
        assertArrayEquals(new String[] {"meow", "meow"}, res);
        assertTrue(new File("text").delete());
    }

    @Test
    void testListDirectoryWithTwoSubdirectories() throws ConnectionProtocolException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "1 src/test/resources/2\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String[] result = output.toString().trim().split("\n");

        assertEquals("3 lines got", result[0]);
        String[] ans = new String[] {
                "3 lines got",
                "src/test/resources/2/yana false",
                "src/test/resources/2/2.2 true",
                "src/test/resources/2/2.1 true"
        };
        Arrays.sort(ans);
        Arrays.sort(result);
        assertArrayEquals(ans, result);
    }

    @Test
    void testGetEmptyFile() throws ConnectionProtocolException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "2 src/test/resources/2/2.2/empty\nempty\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Type in filename:\nSuccessfully saved file to empty", result);
        String[] res = Files.lines(
                Paths.get("empty")).collect(Collectors.toList()).toArray(new String[0]);
        assertEquals(0, res.length);
        assertArrayEquals(new String[] {}, res);
        assertTrue(new File("empty").delete());
    }

    @Test
    void testGetPictureFile() throws ConnectionProtocolException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "2 src/test/resources/2/2.1/lena_512.bmp\nlena_512.bmp\n0".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        client.runClient();

        String result = output.toString().trim();
        assertEquals("Type in filename:\nSuccessfully saved file to lena_512.bmp", result);
        byte[] b1 = Files.readAllBytes(Paths.get("src/test/resources/2/2.1/lena_512.bmp"));
        byte[] b2 = Files.readAllBytes(Paths.get("lena_512.bmp"));
        assertArrayEquals(b1, b2);
        assertTrue(new File("lena_512.bmp").delete());
    }

    @Test
    void testThrowsConnectionException() {
        ByteArrayInputStream input = new ByteArrayInputStream(
                "3 abaca".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Client client = new Client(hostName, portNumber, input, output);
        assertThrows(ConnectionProtocolException.class, client::runClient);
    }

}