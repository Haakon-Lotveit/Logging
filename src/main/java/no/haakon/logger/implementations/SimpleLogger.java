package no.haakon.logger.implementations;

import no.haakon.logger.Logger;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SimpleLogger implements Logger {
    private static final String FORMAT = "<%s> [%s] - %s%n";

    final String name;


    public SimpleLogger(String name) {
        Objects.requireNonNull(name, "Logger name cannot be null!");
        this.name = name;
    }


    private String currentTime() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    private String cleanLogString(String logMessage) {
        // Her kan det selvsagt være mer, som å strippe ut ikke-skrivbare elementer mm.
        // Men vi holder det enkelt med vilje her.
        return logMessage == null? ">EMPTY MESSAGE<" : logMessage.replaceAll("\n", "\\n");
    }

    private void log(String logLevel, String message) {
        System.out.printf(FORMAT, currentTime(), logLevel, cleanLogString(message));
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
