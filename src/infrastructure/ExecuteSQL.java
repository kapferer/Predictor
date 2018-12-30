package infrastructure;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import context.PreProAiContext;

/**
 * Executes the SQL for the input-data against the database connections and
 * stores the result set in a List of String arrays. This is important for the
 * further analysis of the data in the different columns.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ExecuteSQL {

	private static Logger log = Logger.getLogger(ExecuteSQL.class.getName());

	public void executeSelectSQl(PreProAiContext preProAiContext) {

		List<String[]> data = new ArrayList<>();
		List<String[]> pk = new ArrayList<>();

		ResultSet rs = null;
		int columnCount = 0;
		int dataColumCount = 0;
		int primaryKeyColumnCount = 0;

		Map<Integer, Integer> whichPKColumns = new HashMap<>();
		if (preProAiContext.getPrimaryKeyArray() != null) {
			for (int i = 0; i < preProAiContext.getPrimaryKeyArray().length; i++)
				whichPKColumns.put(preProAiContext.getPrimaryKeyArray()[i], preProAiContext.getPrimaryKeyArray()[i]);
		}

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = preProAiContext.getConnection().prepareStatement(preProAiContext.getSqlString());
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at prepraredStatement", e);
		}

		try {
			rs = preparedStatement.executeQuery();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at prepraredStatement", e);
		}

		try {
			columnCount = rs.getMetaData().getColumnCount();

			if (preProAiContext.getResultColumns() != null)
				stopIfResultColumnsAreBeyondTheSiezOfTheProblem(preProAiContext, columnCount);

			if (preProAiContext.getPrimaryKeyArray() != null) {
				dataColumCount = rs.getMetaData().getColumnCount() - preProAiContext.getPrimaryKeyArray().length;
				primaryKeyColumnCount = preProAiContext.getPrimaryKeyArray().length;
			} else {
				dataColumCount = rs.getMetaData().getColumnCount();
				primaryKeyColumnCount = 0;
			}
		} catch (SQLException e1) {
			log.log(Level.SEVERE, "Problem at meta data extraction", e1);
		}

		try {
			while (rs.next()) {

				String[] dataToUse = new String[dataColumCount];
				String[] primaryKey = new String[primaryKeyColumnCount];

				int counterPK = 0;
				int counterDataToUser = 0;

				for (int i = 0; i < columnCount; i++) {

					if (whichPKColumns.containsKey(i)) {
						primaryKey[counterPK] = rs.getString(i + 1);
						counterPK++;
					} else {
						dataToUse[counterDataToUser] = rs.getString(i + 1);
						dataToUse[counterDataToUser] = alterInputIfDBNull(preProAiContext,
								dataToUse[counterDataToUser]);
						counterDataToUser++;
					}
				}
				data.add(dataToUse);
				pk.add(primaryKey);

			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem iteration on result set", e);
		}

		try {
			rs.close();
		} catch (SQLException e1) {
			log.log(Level.SEVERE, "Problem at closing result set", e1);
		}

		try {
			preparedStatement.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Problem at closing prepared statment", e);
		}

		int counter = 0;
		int[] dataType = new int[columnCount];
		String[] headerline = new String[columnCount];

		for (int i = 0; i < columnCount; i++) {
			try {
				headerline[i] = rs.getMetaData().getColumnLabel(i + 1);
				dataType[counter] = rs.getMetaData().getColumnType(i + 1);
				counter++;
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Problem at meta data extraction part 2", e);
			}
		}

		String[] alteredHeaderlines = alterHeaderlinesArrayBecauseOfPK(headerline, whichPKColumns);

		if (preProAiContext.getPrimaryKeyArray() != null) {
			if (preProAiContext.getCategoricalColumns() != null)
				alterCategoricalColums(preProAiContext);
			if (preProAiContext.getResultColumns() != null)
				alterResultColumns(preProAiContext);
		}

		extractDatatypeForColumns(preProAiContext, dataType);

		preProAiContext.setData(extractDataColumns(preProAiContext.getResultColumns(), data));
		preProAiContext.setDataResult(extractResultColumns(preProAiContext.getResultColumns(), data));

		preProAiContext.setHeader(extractHeaderInformation(preProAiContext.getResultColumns(), alteredHeaderlines));
		preProAiContext.setHeaderResultColumns(
				extractResultHeaderInformation(preProAiContext.getResultColumns(), alteredHeaderlines));

		preProAiContext.setDataPrimaryKey(pk);

	}

	/**
	 * Check it the given resultColumns are within the borders of the input-data
	 * 
	 * @param preProAiContext
	 * @param columnCount
	 */
	private void stopIfResultColumnsAreBeyondTheSiezOfTheProblem(PreProAiContext preProAiContext, int columnCount) {

		for (int i = 0; i < preProAiContext.getResultColumns().length; i++) {
			if (preProAiContext.getResultColumns()[i] > columnCount - 1)
				preProAiContext.getExitProgramm()
						.programmExiter("The result columns you have giben in the parameter-file are not "
								+ "within the borders of the input data.", null);
		}

	}

	/**
	 * if Primary key columns are given in the parameter set, the categorical
	 * columns have to be altered accordingly.
	 * 
	 * @param preProAiContext
	 */
	private void alterCategoricalColums(PreProAiContext preProAiContext) {

		int counter = 0;

		for (int i = 0; i < preProAiContext.getCategoricalColumns().length; i++) {
			counter = 0;
			for (int j = 0; j < preProAiContext.getPrimaryKeyArray().length; j++) {

				int categoricalColumn = preProAiContext.getCategoricalColumns()[i];
				int pkColumn = preProAiContext.getPrimaryKeyArray()[j];

				if (pkColumn < categoricalColumn) {
					counter++;
				}

			}
			preProAiContext.getCategoricalColumns()[i] -= counter;
		}

	}

	/**
	 * if Primary key columns are given in the parameter set, the result columns
	 * have to be altered accordingly.
	 * 
	 * @param preProAiContext
	 */
	private void alterResultColumns(PreProAiContext preProAiContext) {
		int counter = 0;

		for (int i = 0; i < preProAiContext.getResultColumns().length; i++) {
			counter = 0;
			for (int j = 0; j < preProAiContext.getPrimaryKeyArray().length; j++) {

				int categoricalColumn = preProAiContext.getResultColumns()[i];
				int pkColumn = preProAiContext.getPrimaryKeyArray()[j];

				if (pkColumn < categoricalColumn) {
					counter++;
				}

			}
			preProAiContext.getResultColumns()[i] -= counter;
		}

	}

	/**
	 * If one needs it, here the meta-data from the database are stored.
	 * Particularly the datat-ypes for the different columns.
	 * 
	 * @param preProAiContext
	 * @param dataType
	 */
	public void extractDatatypeForColumns(PreProAiContext preProAiContext, int[] dataType) {

		for (int i = 0; i < dataType.length; i++)
			preProAiContext.getColumnDatatype().add(dataType[i]);

	}

	/**
	 * The input-data for neuronal-net are extracted from the result set.
	 * 
	 * @param resultColumns
	 * @param data
	 * @return A 2-D List of String arrays storing the input data for the
	 *         neuronal net.
	 */
	public List<List<String[]>> extractDataColumns(int[] resultColumns, List<String[]> data) {

		List<List<String[]>> transformedData = new ArrayList<>();
		boolean add;

		for (int j = 0; j < data.get(0).length; j++) {
			add = true;

			if (resultColumns != null) {
				for (int k = 0; k < resultColumns.length; k++) {
					if (j == resultColumns[k]) {
						add = false;
						break;
					}
				}
			}
			if (add) {
				List<String[]> column = new ArrayList<>();
				for (int i = 0; i < data.size(); i++) {
					String[] tmp = new String[1];
					tmp[0] = data.get(i)[j];
					column.add(tmp);
				}
				transformedData.add(column);
			}
		}

		return transformedData;

	}

	/**
	 * 
	 * The result-data for neuronal-net are extracted from the result set. This
	 * is needed for the training mode.
	 * 
	 * @param resultColumns
	 * @param data
	 * @return A 2-D List of String arrays storing the result data for the
	 *         training of the neuronal net.
	 */
	public List<List<String[]>> extractResultColumns(int[] resultColumns, List<String[]> data) {

		List<List<String[]>> transformedData = new ArrayList<>();
		boolean add;

		for (int j = 0; j < data.get(0).length; j++) {
			add = false;

			if (resultColumns != null) {
				for (int k = 0; k < resultColumns.length; k++) {
					if (j == resultColumns[k]) {
						add = true;
						break;
					}
				}
			}
			if (add) {
				List<String[]> column = new ArrayList<>();
				for (int i = 0; i < data.size(); i++) {
					String[] tmp = new String[1];
					tmp[0] = data.get(i)[j];
					column.add(tmp);
				}
				transformedData.add(column);
			}
		}

		return transformedData;
	}

	/**
	 * This method gives a map with the header information. This is basically
	 * the number of the columns.
	 * 
	 * @param resultColumns
	 * @param headerline
	 * @return Map with header information of the input-data(if a file export is
	 *         needed)
	 */
	public Map<Integer, String[]> extractHeaderInformation(int[] resultColumns, String[] headerline) {

		Map<Integer, String[]> headerData = new TreeMap<>();
		boolean add = false;
		int counter = 0;

		for (int i = 0; i < headerline.length; i++) {
			add = true;

			if (resultColumns != null) {
				for (int k = 0; k < resultColumns.length; k++) {
					if (i == resultColumns[k]) {
						add = false;
						break;
					}
				}
			}
			if (add) {
				String[] tmp = new String[1];
				tmp[0] = headerline[i];
				headerData.put(counter, tmp);
				counter++;
			}
		}

		return headerData;
	}

	/**
	 * This method gives a map with the header information. This is basically
	 * the number of the columns.
	 * 
	 * @param resultColumns
	 * @param headerline
	 * @return Map with header information of the result-data (if a file export
	 *         is needed)
	 */
	public Map<Integer, String[]> extractResultHeaderInformation(int[] resultColumns, String[] headerline) {

		Map<Integer, String[]> headerData = new TreeMap<>();
		boolean add;
		int counter = 0;

		for (int i = 0; i < headerline.length; i++) {
			add = false;

			if (resultColumns != null) {
				for (int k = 0; k < resultColumns.length; k++) {
					if (i == resultColumns[k]) {
						add = true;
						break;
					}
				}
			}
			if (add) {
				String[] tmp = new String[1];
				tmp[0] = headerline[i];
				headerData.put(counter, tmp);
				counter++;
			}
		}

		return headerData;
	}

	/**
	 * Here we write the first given null value, from the parameterfile at the
	 * position of all null values.
	 * 
	 * @param preProAiContext
	 * @param toAlter
	 * @return the first null value from the preProAiContext
	 */
	private String alterInputIfDBNull(PreProAiContext preProAiContext, String toAlter) {

		if (toAlter == null) {
			return preProAiContext.getNullValues()[0];
		} else {
			return toAlter;
		}
	}

	/**
	 * If a PKcolumn is given this function extracts the header-information from
	 * this headerline.
	 * 
	 * @param headerline
	 * @param whichPKColumns
	 * @return the altered headerline
	 */
	private String[] alterHeaderlinesArrayBecauseOfPK(String[] headerline, Map<Integer, Integer> whichPKColumns) {

		String[] alteredHaederline = new String[headerline.length - whichPKColumns.size()];

		int counter = 0;
		for (int i = 0; i < headerline.length; i++) {
			if (!whichPKColumns.containsKey(i)) {
				alteredHaederline[counter] = headerline[i];
				counter++;
			}
		}

		return alteredHaederline;
	}

}
