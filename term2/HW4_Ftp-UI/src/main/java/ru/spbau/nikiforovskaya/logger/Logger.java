package ru.spbau.nikiforovskaya.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/** Class for logging server. */
public class Logger {

    private PrintWriter output;

    /**
     * Constructor for logger
     * @param name -- unique name for you server, if there are running several ones.
     * @throws FileNotFoundException if didn't manage to open/create file for logging.
     */
    public Logger(String name) throws FileNotFoundException {
        output = new PrintWriter(name + "server.log");
    }

    /**
     * Writes down log info with certain tag.
     * @param what -- what to write to log.
     */
    public void log(String what) {
        output.println("Log info: " + what);
        output.flush();
    }

    /**
     * Writes down error message with tag.
     * @param what -- an error message occurred.
     */
    public void error(String what) {
        output.println("ERROR: "  + what);
        output.flush();
    }
}
