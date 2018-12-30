package infrastructure;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.Encog;

/**
 * A class providing an System.exit(). In a message for a logger can be given.
 * 
 * @author Wolfgang Kapferer
 *
 */

public class ExitProgramm {

	private static Logger log = Logger.getLogger(ExitProgramm.class.getName());

	/**
	 * The exit programm routine.
	 * 
	 * @param message
	 * @param e
	 *            an exception
	 */
	public void programmExiter(String message, Exception e) {

		StringBuilder stringForLogger = new StringBuilder();
		stringForLogger.append("Exiting Predictor - ");
		stringForLogger.append(message);

		log.log(Level.SEVERE, stringForLogger.toString(), e);

		Encog.getInstance().shutdown();

		// it is above 10, therefore the typical JAVA RCs are not changed
		System.exit(11);

	}

}
