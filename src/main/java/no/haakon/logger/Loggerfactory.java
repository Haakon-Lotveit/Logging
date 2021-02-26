package no.haakon.logger;

import no.haakon.logger.implementations.SimpleLogger;
import no.haakon.logger.implementations.WrappingLogger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class Loggerfactory {

    private static final Function<String, Logger> chosenLoggerFactory = selectLoggerFactory();

    private static Function<String, Logger> selectLoggerFactory() {

        try {
            Class<?> loggerContextClass = Class.forName("ch.qos.logback.classic.LoggerContext");
            final Object loggerContext = loggerContextClass.getDeclaredConstructor().newInstance();
            Class<?> contextInitializerClass = Class.forName("ch.qos.logback.classic.util.ContextInitializer");
            Object contextInitializer = contextInitializerClass.getDeclaredConstructors()[0].newInstance(loggerContext);

            // Henter in konfigurasjonen som en URL, fordi logback er bare slik:
            File configurationFile = new File("loggers/logback.xml"); // Vi har bare lagt den her for å ikke kødde til ting.
            URL configurationUrl = configurationFile.toURI().toURL();

            contextInitializerClass.getMethod("configureByResource", URL.class).invoke(contextInitializer, configurationUrl);

            final Method getLoggerMethod = loggerContextClass.getMethod("getLogger", String.class);

            System.out.println("Using Logback for logging");
            return (name) -> {
                try {
                    Object wrappedLogger = getLoggerMethod.invoke(loggerContext, name);
                    Class<?> wrappedLoggerClass = wrappedLogger.getClass();
                    Method debug = wrappedLoggerClass.getMethod("debug", String.class);
                    Method info = wrappedLoggerClass.getMethod("info", String.class);
                    Method warn = wrappedLoggerClass.getMethod("warn", String.class);
                    Method error = wrappedLoggerClass.getMethod("error", String.class);

                    return new WrappingLogger(
                            invokeLogMessage(wrappedLogger, debug),
                            invokeLogMessage(wrappedLogger, info),
                            invokeLogMessage(wrappedLogger, warn),
                            invokeLogMessage(wrappedLogger, error));
                } catch (Exception ignored) {
                    throw new RuntimeException("Kunne ikke kalle logbacks getLogger!");
                }
            };
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | MalformedURLException ignored) {
            // Okay, we don't have this logger on our classpath, let's try another.
            // Or at least not in a form we recognize...
        }

        // If none of the factories match, we just use simplelogger.
        System.out.println("Bruker SimpleLogger!");
        return SimpleLogger::new;
    }

    public static Logger getLoggerFor(Class<?> loggingClass) {
        return getLoggerFor(loggingClass.getName());
    }

    public static Logger getLoggerFor(String name) {
        return chosenLoggerFactory.apply(name);
    }

    // Helper method to make some stuff seem cleaner than it actually is.
    private static Consumer<String> invokeLogMessage(Object logger, Method method) {
        return (message) -> {
            try {
                method.invoke(logger, message);
            } catch (Exception e) {
                // We ignore this. May God forgive us, for code review should NOT.
            }
        };
    }

}
