package no.haakon.logger.simple;

import no.haakon.logger.Logger;

import java.io.PrintStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SimpleLogger implements Logger {
    public static final String FORMAT = "<%s> [%s] - %s%n";
    public static PrintStream logDest = System.out; // Bare så vi kan endre her for testing og slikt.

    final String name;


    public SimpleLogger(String name) {
        Objects.requireNonNull(name, "Logger name cannot be null!");
        this.name = name;
    }


    protected String currentTime() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    protected String cleanLogString(String logMessage) {
        // Her kan det selvsagt være mer, som å strippe ut ikke-skrivbare elementer mm.
        // Men vi holder det enkelt med vilje her.
        return logMessage == null? ">EMPTY MESSAGE<" : logMessage.replaceAll("\n", "\\n");
    }

    protected void log(String logLevel, String message) {
        logDest.printf(FORMAT, currentTime(), logLevel, cleanLogString(message));
    }

    @Override
    public void debug(String melding) {
        log("DEBUG", melding);
    }

    @Override
    public void info(String melding) {
        log("INFO", melding);
    }

    @Override
    public void warn(String melding) {
        log("WARN", melding);
    }

    @Override
    public void error(String melding) {
        log("ERROR", melding);
    }
}
