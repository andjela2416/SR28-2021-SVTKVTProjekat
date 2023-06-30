package rs.ac.uns.ftn.svtvezbe07.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jExample {
    private static final Logger logger = LogManager.getLogger(Log4jExample.class);

    public static void main(String[] args) {
        // Logovanje poruka različitog nivoa
        logger.debug("Ovo je debug poruka.");
        logger.info("Ovo je info poruka.");
        logger.warn("Ovo je upozorenje.");
        logger.error("Ovo je greška.");
    }
}
