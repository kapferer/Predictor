package infrastructure;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

/**
 * THis class needs special care, if one needs to store the result in a
 * database. It provides therefore a insert method, which has to be adapted to
 * the given problem accordingly.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class InsertIntoDatabase {

	private static Logger log = Logger.getLogger(InsertIntoDatabase.class.getName());

	/**
	 * The method to insert the prediction into the database. One needs to alter
	 * the SQL - Insert statement and the primary keys. Keep in mind that it
	 * depends strongly on your data-model for storing the results.
	 * 
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void inserPredictionIntoDatabase(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		PreparedStatement preparedStatement = null;

		String sqlString = preProAiContext.getInsertSqlString();
		
		try {
			preparedStatement = preProAiContext.getConnection().prepareStatement(sqlString);

		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at prepraredStatement", e);
		}

		if (preparedStatement == null) {
			preProAiContext.getExitProgramm().programmExiter("Problem preparing the insert statement for the database",
					null);
		}

		for (int i = 0; i < neuronalNetworkCogNetContext.getPrediction().length; i++) {

			int counter = 0;
			try {
				for (int j = 0; j < preProAiContext.getPrimaryKeyArray().length; j++)
					preparedStatement.setString(++counter, neuronalNetworkCogNetContext.getPrimaryKeys().get(i)[j]);
				for (int j = 0; j < preProAiContext.getResultColumns().length; j++)
					preparedStatement.setDouble(++counter, neuronalNetworkCogNetContext.getPrediction()[i][j]);
				preparedStatement.addBatch();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Problem at prepraredStatement", e);
			}

		}

		try {
			preparedStatement.executeBatch();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at executing", e);
		}

		try {
			preProAiContext.getConnection().commit();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at executing", e);
		}

		try {
			preparedStatement.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at closing prepraredStatement", e);
		}

	}

}
