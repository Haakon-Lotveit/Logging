package no.haakon.logger;

import java.util.function.Consumer;

public class WrappingLogger implements Logger {
    Consumer<String> logDebugMessage, logInfoMessage, logWarnMessage, logErrorMessage;

    public WrappingLogger(Consumer<String> logDebugMessage, Consumer<String> logInfoMessage, Consumer<String> logWarnMessage, Consumer<String> logErrorMessage) {
        this.logDebugMessage = logDebugMessage;
        this.logInfoMessage = logInfoMessage;
        this.logWarnMessage = logWarnMessage;
        this.logErrorMessage = logErrorMessage;
    }

    @Override
    public void debug(String melding) {
        logDebugMessage.accept(melding);
    }

    @Override
    public void info(String melding) {
        logInfoMessage.accept(melding);
    }

    @Override
    public void warn(String melding) {
        logWarnMessage.accept(melding);
    }

    @Override
    public void error(String melding) {
        logErrorMessage.accept(melding);
    }
}
