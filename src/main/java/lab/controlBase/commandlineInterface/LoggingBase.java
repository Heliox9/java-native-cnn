package lab.controlBase.commandlineInterface;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapping class for all logging
 * provides a logger and some commonly used functions
 */
public abstract class LoggingBase {
    public static Logger log = LoggerFactory.getLogger("Wizard");
    public static String hLine = "-------------------------------------------------------------------";
    public static String solidHLine = "===================================================================";

    public static void printHLine() {
        log.trace(hLine);
    }

    public static void printSolidHLine() {
        log.info(solidHLine);
    }


}
