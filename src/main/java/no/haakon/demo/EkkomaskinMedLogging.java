package no.haakon.demo;

import no.haakon.logger.Logger;
import no.haakon.logger.Loggerfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EkkomaskinMedLogging {
    public static void main(String[] args) {
        Logger log = Loggerfactory.getLoggerFor(EkkomaskinMedLogging.class);

        System.out.println("Ekkoer det du skrev inntil !exit kommer på egen linje, eller EOF blir funnet.");
        try(InputStream input = args.length > 0 ? new FileInputStream(new File(args[0])) : System.in;
            Scanner kb = new Scanner(input)) {
            while(kb.hasNextLine()) {
                String line = kb.nextLine();
                log.info(line);
                if(line.equals("!exit")) {
                    return;
                }
            }

        } catch (IOException ioe) {
            log.error("Noe gikk galt under lesing av input. Feilmelding:  %n%s", ioe); // Vi forventer at %n forsvinner her!
        }
    }

}
