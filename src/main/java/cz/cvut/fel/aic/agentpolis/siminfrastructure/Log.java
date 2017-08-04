/* 
 * AgentSCAI
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure;


import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger,
 *
 * @author david_000
 */
public class Log {

    /**
     * Java logger.
     */
    private static Logger logger;
    
    private static FileHandler fileHandler;


    /**
     * Inits logger.
     *
     * @param name        Java log name
     * @param logLevel    Java log level for logging to file.
     * @param logFilePath path to log file
     */
    public static void init(final String name, final Level logLevel, final String logFilePath) {
        logger = Logger.getLogger(name);
        logger.setLevel(logLevel);

        // do not send log messages to other logs
        logger.setUseParentHandlers(false);

        LogFormater logFormater = new LogFormater();

        // console log settings
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(logFormater);
        logger.addHandler(consoleHandler);

        try {
            // file log settings
            fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setLevel(logLevel);
            fileHandler.setFormatter(logFormater);
            logger.addHandler(fileHandler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(){
        if(fileHandler != null){
            fileHandler.close();
        }
    }

    /**
     * Log message with no params.
     *
     * @param caller  Caller of the method.
     * @param level   Java log level.
     * @param message Log message.
     */
    public static void log(Object caller, Level level, String message) {
        message = caller.getClass().getName() + ": " + message;
        logger.log(level, message);
    }

    /**
     * Log message.
     *
     * @param caller  Caller of the method.
     * @param level   Java log level.
     * @param message Log message.
     * @param params  Message parameters.
     */
    public static void log(Object caller, Level level, String message, Object... params) {
        message = caller.getClass().getName() + ": " + message;
        logger.log(level, message, params);
    }

    public static void info(Object caller, String msg) {
        log(caller, Level.INFO, msg);
    }

    public static void warn(Object caller, String msg) {
        log(caller, Level.WARNING, msg);
    }

    public static void debug(Object caller, String msg) {
        log(caller, Level.FINE, msg);
    }

    public static void error(Object caller, String msg) {
        log(caller, Level.SEVERE, msg);
    }
}
