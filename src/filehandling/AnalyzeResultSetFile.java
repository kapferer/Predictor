package filehandling;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AnalyzeResultSetFile Class checks all columns of the result set, if the
 * data is numeric or not. After the analysis the result is stored in an Map
 * with the column number and a boolean telling us, if the column is categorical
 * or not. If a null occurs in the column as given in the parameter-file the
 * value is ommited for the anaysis.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class AnalyzeResultSetFile {

	private static Logger log = Logger.getLogger(AnalyzeResultSetFile.class.getName());

	/**
	 * Analyzes which columns are numeric of categorical. Takes into account
	 * information from the parameter categoricalColumns in the parameter-file.
	 * 
	 * @param data
	 * @param nullValues
	 * @param categoricalColumns
	 * @param resultcolumns
	 * @param decimalPoint
	 * @return A Map telling which column is numerical or categorical
	 */
	public Map<Integer, Boolean> whichColumnIsNotCategorical(List<List<String[]>> data, String[] nullValues,
			int[] categoricalColumns, int[] resultcolumns, char decimalPoint) {

		Map<Integer, Boolean> categoricalColumn = new TreeMap<>();

		boolean isDigit = true;
		for (int j = 0; j < data.size(); j++) {
			for (int i = 0; i < data.get(j).size(); i++) {

				String dataPiece = data.get(j).get(i)[0];

				if (checkIfNull(nullValues, dataPiece))
					continue;

				isDigit = isItANumber(dataPiece, decimalPoint);

				if (!isDigit) {
					break;
				}
			}

			if (isDigit) {
				categoricalColumn.put(j, true);
			} else {
				categoricalColumn.put(j, false);
			}

		}

		if (categoricalColumn.size() > 0)
			categoricalColumn = alterCategoricalColumnAnalsysisByInput(categoricalColumn, categoricalColumns,
					resultcolumns);

		StringBuilder stringToLog = new StringBuilder();
		stringToLog.append("\n");

		for (Entry<Integer, Boolean> entry : categoricalColumn.entrySet()) {

			stringToLog.append("Column #");
			stringToLog.append(entry.getKey() + 1);
			stringToLog.append(" is numeric ");
			stringToLog.append(entry.getValue());
			stringToLog.append("\n");
		}

		if (categoricalColumn.size() > 0)
			log.log(Level.INFO, stringToLog.toString(), "");

		return categoricalColumn;
	}

	/**
	 * This function tells us, if the actual value is a NULL, as defined in the
	 * parameter-file (or from the database).
	 * 
	 * @param nullValues
	 * @param dataPiece
	 * @return boolean null/notNull
	 */
	private boolean checkIfNull(String[] nullValues, String dataPiece) {

		boolean nullValue = false;

		if (dataPiece == null)
			return true;

		if (nullValues == null)
			return nullValue;

		for (int i = 0; i < nullValues.length; i++) {
			if (dataPiece.equals(nullValues[i])) {
				return true;
			}

		}

		return nullValue;
	}

	/**
	 * Tests if the given value is number. In principal one could ask the
	 * database, but many people store numbers in char Columns for many reason.
	 * 
	 * @param toTest
	 * @param decimalPoint
	 * @return boolean numer/noNumber
	 */
	boolean isItANumber(String toTest, char decimalPoint) {

		boolean isDigit = false;
		int decimalPointCounter = 0;
		char c;

		for (int l = 0; l < toTest.length(); l++) {

			c = toTest.charAt(l);

			if (c == decimalPoint)
				decimalPointCounter++;

			if ((c == decimalPoint) && (decimalPointCounter < 2) || ((l == 0) && ((c == '-') || (c == '+'))))
				continue;

			isDigit = c >= '0' && c <= '9';

			if (!isDigit) {
				break;
			}
		}

		return isDigit;

	}

	/**
	 * 
	 * @param categoricalData
	 * @param categoricalColumns
	 * @param resultcolumns
	 * @return
	 */

	public Map<Integer, Boolean> alterCategoricalColumnAnalsysisByInput(Map<Integer, Boolean> categoricalData,
			int[] categoricalColumns, int[] resultcolumns) {

		if (categoricalColumns == null)
			return categoricalData;

		if (resultcolumns != null) {

			for (int j = 0; j < resultcolumns.length; j++) {
				for (int i = 0; i < categoricalColumns.length; i++) {
					if (categoricalColumns[i] > resultcolumns[j])
						categoricalColumns[i] = --categoricalColumns[i];
				}
			}
		}

		for (int i = 0; i < categoricalColumns.length; i++)
			categoricalData.replace(categoricalColumns[i], false);

		return categoricalData;

	}

	public boolean isDateColumn(List<String[]> column, String[] nullValues, String dateFormat) {

		boolean isDateColumn = true;

		for (int i = 0; i < column.size(); i++) {

			if (checkIfNull(nullValues, column.get(i)[0]))
				continue;

			if (!isValidDate(column.get(i)[0])) {
				isDateColumn = false;
				break;
			}

		}

		return isDateColumn;

	}

	private static Set<String> dates = new HashSet<String>();
	static {
		for (int year = 1900; year < 2050; year++) {
			for (int month = 1; month <= 12; month++) {
				for (int day = 1; day <= daysInMonth(year, month); day++) {
					StringBuilder date = new StringBuilder();
					date.append(String.format("%04d", year));
					date.append("-");
					date.append(String.format("%02d", month));
					date.append("-");
					date.append(String.format("%02d", day));
					dates.add(date.toString());
				}
			}
		}
	}

	private static int daysInMonth(int year, int month) {
		int daysInMonth;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysInMonth = 31;
			break;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				daysInMonth = 29;
			} else {
				daysInMonth = 28;
			}
			break;
		default:
			// returns 30 even for nonexistent months
			daysInMonth = 30;
		}
		return daysInMonth;
	}

	public static boolean isValidDate(String dateString) {
		return dates.contains(dateString);
	}

}
