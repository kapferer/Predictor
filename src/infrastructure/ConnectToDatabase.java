package infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import context.PreProAiContext;

/**
 * The Connector to the database. Change for your database accordingly.
 * 
 * @author kapf
 *
 */
public class ConnectToDatabase {

	private static Logger log = Logger.getLogger(ConnectToDatabase.class.getName());
	private static ExitProgramm exitProgramm = new ExitProgramm();

	/**
	 * Connects to the database
	 * 
	 * @param preProAiContext
	 */
	public void connectToExasol(PreProAiContext preProAiContext) {

		Connection con = null;

		try {
			Class.forName("com.exasol.jdbc.EXADriver");
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "JDBC Class not found", e);
		}

		try {
			con = DriverManager.getConnection(
					"jdbc:exa:" + preProAiContext.getConnectionString() + ";schema=" + preProAiContext.getSchema(),
					preProAiContext.getUser(), preProAiContext.getPasswd());

			log.log(Level.INFO, "Connected to Database");
		} catch (SQLException e) {
			exitProgramm.programmExiter("Connection to database failed.", e);
		}

		log.log(Level.INFO, "Successfully connected to the database.");
		
		preProAiContext.setConnection(con);

	}

	/**
	 * Closes the connection to the database.
	 * 
	 * @param preProAiContext
	 */
	public void disconnectFromDatabase(PreProAiContext preProAiContext) {

		try {
			preProAiContext.getConnection().close();
			log.log(Level.INFO, "Conncetion to database closed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at closing database-connection.");
		}

	}

}
