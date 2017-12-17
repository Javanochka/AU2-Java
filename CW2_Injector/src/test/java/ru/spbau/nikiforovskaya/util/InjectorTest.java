package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.spbau.nikiforovskaya.examples.*;

class InjectorTest {


    @Test
    void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.ClassWithoutDependencies",
                new Class[] {ClassWithoutDependencies.class});
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.ClassWithOneClassDependency",
                new Class[] {ClassWithoutDependencies.class, ClassWithOneClassDependency.class}
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.ClassWithOneInterfaceDependency",
                new Class[] {InterfaceImpl.class, ClassWithOneInterfaceDependency.class,
                        Interface.class}
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    void injectorClassWithOneClassDependencyNotImplemented()
            throws Exception {

        assertThrows(ImplementationNotFoundException.class, () -> Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.ClassWithOneClassDependency",
                new Class[] {ClassWithOneClassDependency.class}
        ));
    }

    @Test
    void injectorClassWithOneInterfaceDependencyManyImplementations()
            throws Exception {

        assertThrows(AmbiguousImplementationException.class, () -> Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.ClassWithOneInterfaceDependency",
                new Class[] {InterfaceImpl.class, ClassWithOneInterfaceDependency.class,
                        Interface.class, InterfaceImplAnother.class}
        ));
    }

    @Test
    void injectorCyclicDependenceException() throws Exception {

        assertThrows(InjectionCycleException.class, () -> Injector.initialize(
                "ru.spbau.nikiforovskaya.examples.CyclicA",
                new Class[] {CyclicA.class, CyclicB.class}));
    }
}