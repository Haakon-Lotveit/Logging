package no.haakon.logger;

@SuppressWarnings("unused")
public interface Logger {
    default void debug(String format, Object... args) {
        debug(String.format(format, args));
    }

    void debug(String melding);

    default void info(String format, Object... args) {
        info(String.format(format, args));
    }

    void info(String melding);

    default void warn(String format, Object... args) {
        warn(String.format(format, args));
    }

    void warn(String melding);

    default void error(String format, Object... args) {
        error(String.format(format, args));
    }
    void error(String melding);

}
