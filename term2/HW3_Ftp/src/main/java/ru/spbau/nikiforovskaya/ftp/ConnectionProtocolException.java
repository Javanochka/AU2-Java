package ru.spbau.nikiforovskaya.ftp;

/** Exception, which is thrown when something happens not due to protocol. */
public class ConnectionProtocolException extends Exception {
    /** Creates a connection protocol exception */
    public ConnectionProtocolException() {
        super("The query does not match the protocol");
    }
}
