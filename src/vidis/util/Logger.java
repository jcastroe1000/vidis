package vidis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;

/**
 * This class is Deprecated, use log4j logger instead!
 * @author dpsenner
 *
 */
@Deprecated
public class Logger {
    /**
     * defines how many lines are stored into a log file before it is rotated
     */
    public static final int STD_LOG_MAX_LINES = 5000;

    /**
     * std log directory where std err and std out log files are placed
     */
    public static final String STD_LOG_DIRECTORY = "logs" + File.separator
	    + "sim";

    /**
     * file directory to log std err to (relative to STD_LOG_DIRECTORY)
     */
    public static final String STD_ERR_FILE = "std_err.log";

    /**
     * file directory to log std out to (relative to STD_LOG_DIRECTORY)
     */
    public static final String STD_OUT_FILE = "std_out.log";

    /**
     * global error output stream
     */
    private static PrintStream STD_ERR = System.err;
    /**
     * global output stream
     */
    private static PrintStream STD_OUT = System.out;

    /**
     * global input stream
     */
    public static final InputStream STD_IN = System.in;

    static {
	// create log_dir
	File std_log_directory = new File(STD_LOG_DIRECTORY);
	do_logfile_actions(std_log_directory, STD_ERR_FILE);
	do_logfile_actions(std_log_directory, STD_OUT_FILE);
	try {
	    STD_ERR = new PrintStream(new FileOutputStream(new File(
		    std_log_directory, STD_ERR_FILE), true));
	    output(LogLevel.INFO, null, new Date().toString());
	} catch (FileNotFoundException e) {
	    output(LogLevel.ERROR, null, e);
	}
	try {
	    STD_OUT = new PrintStream(new FileOutputStream(new File(
		    std_log_directory, STD_OUT_FILE), true));
	    output(LogLevel.INFO, null, new Date().toString());
	} catch (FileNotFoundException e) {
	    output(LogLevel.ERROR, null, e);
	}
    }

    public static enum LogLevel {
	WARN, ERROR, DEBUG, INFO;
    }

    public static final void output(LogLevel l, Object producer, String msg) {
	String s = l + " [" + producer + "] " + msg;
	PrintStream os = STD_OUT;
	if (l.equals(LogLevel.ERROR))
	    os = STD_ERR;
	os.println(s);
	if (!os.equals(System.out) || !os.equals(System.err)) {
	    if (os.equals(STD_ERR))
		System.err.println(s);
	    else
		System.out.println(s);
	}
    }

    public static final void output(LogLevel l, Object producer, Exception e) {
	output(l, producer, e.getMessage());
    }

    /**
     * execute all actions on a log directory; primarily it creates the directory
     * ;-)
     * 
     * @param logDirectory
     *          the directory to use
     */
    private static void do_logdirectory_actions(File logDirectory) {
	if (!logDirectory.exists()) {
	    try {
		if (logDirectory.mkdirs()) {
		    // TODO all fine
		} else {
		    // TODO problem
		}
	    } catch (SecurityException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * do all actions on a log file; log file is being created and rotated and
     * such
     * 
     * @param logDirectory
     *          the log directory to use (do_logdirectory_actions is called on
     *          this directory)
     * @param logFile
     *          the log file
     */
    private static void do_logfile_actions(File logDirectory, String logFile) {
	do_logdirectory_actions(logDirectory);
	// create std_err log file
	File logFileFile = new File(logDirectory, logFile);
	if (!logFileFile.exists()) {
	    try {
		if (logFileFile.createNewFile()) {
		    // created
		}
	    } catch (IOException e) {
		output(LogLevel.ERROR, null, e);
	    }
	}
	// & output stream
	if (logFileFile.exists()) {
	    try {
		// take a look at how big the file is
		Scanner in = new Scanner(new FileInputStream(logFileFile));
		int lines = 0;
		while (in.hasNextLine()) {
		    lines++;
		    in.nextLine();
		}
		in.close();
		// now check how many lines and iff lines > MAX_LINES rotate
		if (lines > STD_LOG_MAX_LINES) {
		    do_logfile_action_rotate(logFileFile);
		}
	    } catch (FileNotFoundException e) {
		output(LogLevel.ERROR, null, e);
	    }
	}
    }

    /**
     * precondition: file exists postconditions: file renamed to file.{nextIndex};
     * new file created
     * 
     * @param logFile
     *          the log file to rotate
     */
    private static void do_logfile_action_rotate(File logFile) {
	String oldFileName = logFile.getName();
	String newFileNamePrefix = logFile.getName() + ".";
	int nextIndex = 0;
	File newFile = new File(logFile.getParentFile(), oldFileName);
	while (newFile.exists()) {
	    newFile = new File(logFile.getParentFile(), newFileNamePrefix
		    + nextIndex);
	    nextIndex++;
	}
	// move file
	if (logFile.renameTo(newFile)) {
	    try {
		if (!logFile.createNewFile()) {
		    throw new IOException();
		} else {
		    output(LogLevel.INFO, null, "rotate log file '"
			    + oldFileName + "' successful");
		}
	    } catch (IOException e) {
		// undo
		if (newFile.renameTo(logFile)) {
		    output(LogLevel.ERROR, null, e
			    + " but could recover previous state");
		} else {
		    output(LogLevel.ERROR, null, e
			    + " and could not recover previous state!");
		}
	    }
	}
    }
}
