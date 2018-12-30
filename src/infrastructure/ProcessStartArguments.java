package infrastructure;

import context.PreProAiContext;

/**
 * Processes the two necessary start arguments. The database- and parameter-
 * XML-file.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ProcessStartArguments {

	/**
	 * This reads the the start parameters and puts the values into the context
	 * class
	 * 
	 * @param startarguments
	 * @param preProAiContext
	 */
	public void processArguments(String[] startarguments, PreProAiContext preProAiContext) {

		if (startarguments.length != 2) {
			preProAiContext.getExitProgramm().programmExiter("Not enough start arguments given!", null);
		} else {
			preProAiContext.setParameterFile(startarguments[0]);
			preProAiContext.setdBInfoFile(startarguments[1]);
		}

	}
}
