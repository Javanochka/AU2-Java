package ru.spbau.nikiforovskaya.util;

/** Injector exception, which is thrown, when found a cycle in dependencies. */
public class InjectionCycleException extends Exception {

    public InjectionCycleException() {
        super("A cycle in injections found.");
    }
}
