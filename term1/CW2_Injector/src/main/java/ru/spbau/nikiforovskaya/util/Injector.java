package ru.spbau.nikiforovskaya.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Class which privides functions to find dependencies and to create example objects,
 * using them.
 */
public class Injector {

    private static boolean[] was;
    private static Class[] allClasses;
    private static Object[] instances;

    /**
     * Returns an example instance of given Class, using only classes and interfaces,
     * declared in allClasses.
     * @param rootClassName a full name of class, of which to create an object.
     * @param allClasses all classes and interfaces,
     *                   which can be used in order to create the object.
     * @return an example instance of given Class
     * @throws ImplementationNotFoundException if couldn't find some class needed in given scope
     * @throws AmbiguousImplementationException if has found several implementations
     *                   of the given class in scope.
     * @throws InjectionCycleException if found a cycle of dependencies
     * @throws InvocationTargetException if didn't manage to construct an object
     * @throws NoSuchMethodException if couldn't find a constructor in needed classes
     * @throws InstantiationException if didn't manage to construct an object
     * @throws IllegalAccessException if a constructor found wasn't accessable.
     */
    public static Object initialize(String rootClassName, Class[] allClasses)
            throws ImplementationNotFoundException, AmbiguousImplementationException,
            InjectionCycleException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        int rootIndex = -1;
        for (int i = 0; i < allClasses.length; i++) {
            if (rootClassName.equals(allClasses[i].getCanonicalName())) {
                rootIndex = i;
                break;
            }
        }
        if (rootIndex == -1) {
            throw new ImplementationNotFoundException();
        }
        was = new boolean[allClasses.length];
        Injector.allClasses = allClasses;
        instances = new Object[allClasses.length];
        return dfsInClassesInjection(rootIndex);
    }

    @SuppressWarnings("unchecked")
    private static Object dfsInClassesInjection(int current)
            throws AmbiguousImplementationException, ImplementationNotFoundException,
            InjectionCycleException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        was[current] = true;
        if (allClasses[current].isInterface()) {
            int[] realizations = getAllRealizations(allClasses[current]);
            if (realizations.length > 1) {
                throw new AmbiguousImplementationException();
            }
            if (realizations.length == 0) {
                throw new ImplementationNotFoundException();
            }
            if (instances[realizations[0]] == null) {
                if (was[realizations[0]]) {
                    throw new InjectionCycleException();
                }
                instances[realizations[0]] = dfsInClassesInjection(realizations[0]);
            }
            return instances[realizations[0]];
        }

        Constructor constructor = allClasses[current].getDeclaredConstructors()[0];
        Class[] parametres = constructor.getParameterTypes();
        Object[] args = new Object[parametres.length];
        for (int i = 0; i < parametres.length; i++) {
            int index = getIndex(parametres[i]);
            if (index == -1) {
                throw new ImplementationNotFoundException();
            }
            if (instances[index] == null) {
                if (was[index]) {
                    throw new InjectionCycleException();
                }
                instances[index] = dfsInClassesInjection(index);
            }
            args[i] = instances[index];
        }
        instances[current] = constructor.newInstance(args);
        return instances[current];
    }

    private static int getIndex(Class clazz) {
        for (int j = 0; j < allClasses.length; j++) {
            if (allClasses[j].equals(clazz)) {
                return j;
            }
        }
        return -1;
    }

    private static int[] getAllRealizations(Class interfaze) {
        ArrayList<Integer> answer = new ArrayList<>();
        for (int i = 0; i < allClasses.length; i++) {
            if (!allClasses[i].isInterface()) {
                Class[] interfaces = allClasses[i].getInterfaces();
                for (Class anInterface : interfaces) {
                    if (anInterface.getName().equals(interfaze.getName())) {
                        answer.add(i);
                    }
                }
            }
        }
        int[] result = new int[answer.size()];
        for (int i = 0; i < answer.size(); i++) {
            result[i] = answer.get(i);
        }
        return result;
    }
}
