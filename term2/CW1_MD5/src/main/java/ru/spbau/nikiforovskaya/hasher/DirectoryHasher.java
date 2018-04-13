package ru.spbau.nikiforovskaya.hasher;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Class, helping to find the md5 hash of the directory given.
 * Note, that if it couldn't read any file, it ignores it.
 */
public class DirectoryHasher {

    private static String getStringByMessageDigest(MessageDigest md) {
        return (new BigInteger(1, md.digest())).toString(16);
    }

    private static String getSimpleFileHash(File file, MessageDigest md) {
        try (InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    md.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

        } catch (IOException e) {
            return "";
        }
        return getStringByMessageDigest(md);
    }

    /**
     * Simple directory hasher.
     * Usually, should take quite longer to execute.
     *
     * @param path a directory which to hash with md5
     * @return the string, representing the hex number -- hash of the directory.
     * @throws NoSuchAlgorithmException, if did't find the algorithm md5.
     */
    public static String getDirectoryHashSimple(String path) throws NoSuchAlgorithmException {
        MessageDigest ans = MessageDigest.getInstance("MD5");
        ans.reset();
        Path current = Paths.get(path);
        if (!current.toFile().isDirectory()) {
            return getSimpleFileHash(current.toFile(), ans);
        }
        File[] list = current.toFile().listFiles();
        ans.update(current.toFile().getName().getBytes());
        if (list == null) {
            return getStringByMessageDigest(ans);
        }
        for (File file : list) {
            ans.update(getDirectoryHashSimple(file.getAbsolutePath()).getBytes());
        }
        return getStringByMessageDigest(ans);
    }

    /**
     * Fork join directory hasher.
     * Should be faster, than the simple one.
     *
     * @param path a directory which to hash with md5
     * @return the string, representing the hex number -- hash of the directory.
     * @throws NoSuchAlgorithmException, if did't find the algorithm md5.
     */
    public static String getDirectoryHashForkJoin(String path)
            throws NoSuchAlgorithmException {
        return new ForkJoinPool().invoke(new DirectoryHasherTask(path));
    }

    private static class DirectoryHasherTask extends RecursiveTask<String> {

        String path;

        private DirectoryHasherTask(String path) {
            this.path = path;
        }

        @Override
        protected String compute() {
            MessageDigest ans;
            try {
                ans = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                return "";
            }
            ans.reset();
            Path current = Paths.get(path);
            if (!current.toFile().isDirectory()) {
                return getSimpleFileHash(current.toFile(), ans);
            }
            File[] list = current.toFile().listFiles();
            ans.update(current.toFile().getName().getBytes());
            if (list == null) {
                return getStringByMessageDigest(ans);
            }
            ArrayList<DirectoryHasherTask> subTasks = new ArrayList<>();
            for (File file : list) {
                DirectoryHasherTask subTask = new DirectoryHasherTask(file.getAbsolutePath());
                subTask.fork();
                subTasks.add(subTask);
            }
            for (DirectoryHasherTask task : subTasks) {
                ans.update(task.join().getBytes());
            }
            return getStringByMessageDigest(ans);
        }
    }
}
