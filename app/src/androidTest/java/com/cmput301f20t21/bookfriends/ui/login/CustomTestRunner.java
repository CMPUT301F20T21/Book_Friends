package com.cmput301f20t21.bookfriends.ui.login;

import android.app.Application;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;

import com.cmput301f20t21.bookfriends.BuildConfig;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class CustomTestRunner extends Runner implements Filterable, Sortable {

    private static final String TAG = "CustomTestRunner";

    private final Runner delegate;

    public CustomTestRunner(Class<?> klass) throws InitializationError {
        delegate = loadRunner(klass);
        BuildConfig.IS_TESTING.set(true);
    }

    private static String getRunnerClassName() {
        String runnerClassName = System.getProperty("android.junit.runner", null);
        if (runnerClassName == null) {
            if (!System.getProperty("java.runtime.name").toLowerCase().contains("android")
                    && hasClass("org.robolectric.RobolectricTestRunner")) {
                return "org.robolectric.RobolectricTestRunner";
            } else {
                return "androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner";
            }
        }
        return runnerClassName;
    }

    private static boolean hasClass(String className) {
        try {
            return Class.forName(className) != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Runner loadRunner(Class<?> testClass) throws InitializationError {
        String runnerClassName = getRunnerClassName();
        return loadRunner(testClass, runnerClassName);
    }

    @SuppressWarnings("unchecked")
    private static Runner loadRunner(Class<?> testClass, String runnerClassName)
            throws InitializationError {

        Class<? extends Runner> runnerClass = null;
        try {
            runnerClass = (Class<? extends Runner>) Class.forName(runnerClassName);
        } catch (ClassNotFoundException e) {
            throwInitializationError(
                    String.format(
                            "Delegate runner %s for AndroidJUnit4 could not be found.\n", runnerClassName),
                    e);
        }

        Constructor<? extends Runner> constructor = null;
        try {
            constructor = runnerClass.getConstructor(Class.class);
        } catch (NoSuchMethodException e) {
            throwInitializationError(
                    String.format(
                            "Delegate runner %s for AndroidJUnit4 requires a public constructor that takes a"
                                    + " Class<?>.\n",
                            runnerClassName),
                    e);
        }

        try {
            return constructor.newInstance(testClass);
        } catch (IllegalAccessException e) {
            throwInitializationError(
                    String.format("Illegal constructor access for test runner %s\n", runnerClassName), e);
        } catch (InstantiationException e) {
            throwInitializationError(
                    String.format("Failed to instantiate test runner %s\n", runnerClassName), e);
        } catch (InvocationTargetException e) {
            String details = getInitializationErrorDetails(e, testClass);
            throwInitializationError(
                    String.format("Failed to instantiate test runner %s\n%s\n", runnerClass, details), e);
        }
        throw new IllegalStateException("Should never reach here");
    }

    private static void throwInitializationError(String details, Throwable cause)
            throws InitializationError {
        throw new InitializationError(new RuntimeException(details, cause));
    }

    private static String getInitializationErrorDetails(Throwable throwable, Class<?> testClass) {
        StringBuilder innerCause = new StringBuilder();
        final Throwable cause = throwable.getCause();

        if (cause == null) {
            return "";
        }

        final Class<? extends Throwable> causeClass = cause.getClass();
        if (causeClass == InitializationError.class) {
            final InitializationError initializationError = (InitializationError) cause;
            final List<Throwable> testClassProblemList = initializationError.getCauses();
            innerCause.append(
                    String.format(
                            "Test class %s is malformed. (%s problems):\n",
                            testClass, testClassProblemList.size()));
            for (Throwable testClassProblem : testClassProblemList) {
                innerCause.append(testClassProblem).append("\n");
            }
        }
        return innerCause.toString();
    }

    @Override
    public Description getDescription() {
        return delegate.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        delegate.run(runNotifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        ((Filterable) delegate).filter(filter);
    }

    @Override
    public void sort(Sorter sorter) {
        ((Sortable) delegate).sort(sorter);
    }
}