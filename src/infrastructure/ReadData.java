package infrastructure;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

import filehandling.ReadDatabaseXMLFile;
import filehandling.ReadParameterXMLFile;
import filehandling.ReadSQLFile;

/**
 * 
 * ReadData reads the Predictor start arguments, the database and parameter
 * XML-files and the SQL file for the input-data from the database. In addition
 * it connects to the database and executes the SQL in order to obtain the
 * input-data.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ReadData {

	private static ProcessStartArguments processStartArguments = new ProcessStartArguments();
	private static ConnectToDatabase connectToDatabase = new ConnectToDatabase();
	private static ReadParameterXMLFile readParameterXMLFile = new ReadParameterXMLFile();
	private static ReadDatabaseXMLFile readDatabaseXMLFile = new ReadDatabaseXMLFile();
	private static ReadSQLFile readSQLFile = new ReadSQLFile();
	private static ExecuteSQL executeSQL = new ExecuteSQL();

	/**
	 * This methods reads in all the necessary input Files for starting a
	 * neuronal-network training or applying mode. The input-files are
	 * 
	 * the parameter XML File, the database XML File and the the input-data SQL
	 * File.
	 * 
	 * A connection to the database will be established and the SQL files will
	 * be executed.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 * @param args
	 */
	public void readData(PreProAiContext preProAiContext, NeuronalNetworkCogNetContext neuronalNetworkCogNetContext,
			String[] args) {

		/**
		 * Step 1.1: Check if both XML Files are given, the one for the database
		 * properties and the one for the program parameters
		 */
		processStartArguments.processArguments(args, preProAiContext);

		/**
		 * Step 1.2: Read the parameter-file
		 */
		readParameterXMLFile.readParameterXMLFile(preProAiContext, neuronalNetworkCogNetContext);

		/**
		 * Step 1.3: Read the database connection information file
		 */
		readDatabaseXMLFile.readDatabaseXMLFile(preProAiContext);

		/**
		 * Step 1.4: Connection to the database
		 */
		connectToDatabase.connectToExasol(preProAiContext);

		/**
		 * Step 1.5: The SQL for the input data, which provides the input-data
		 * for the neuronal-network
		 */
		readSQLFile.readSQLSelectFile(preProAiContext);

		/**
		 * Step 1.6: Execute the input-data SQL from the SQL file
		 */
		executeSQL.executeSelectSQl(preProAiContext);

		/**
		 * Step 1.7: Execute the input-data SQL from the SQL file
		 */
		if (preProAiContext.getSqlInsertFile() != null)
			readSQLFile.readInsertSQLFile(preProAiContext);

	}

}
