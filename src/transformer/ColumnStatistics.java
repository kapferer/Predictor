package transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * A class providing some basic column statistic methods
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ColumnStatistics {

	/**
	 * It returns the mean value of a given column
	 * 
	 * @param column
	 * @param nullvalues
	 * @return the mean value
	 */
	public double getMeanFromNumericalColumn(List<String[]> column, String[] nullvalues) {

		double mean = 0.0;
		double epsilon = 1.0E-100;
		int counter = 0;
		boolean isANullValue;

		for (int i = 0; i < column.size(); i++) {

			isANullValue = false;

			for (int j = 0; j < nullvalues.length; j++) {
				if (nullvalues[j].equals(column.get(i)[0]))
					isANullValue = true;
			}

			if (isANullValue)
				continue;

			counter++;
			mean += Double.parseDouble(column.get(i)[0].replaceAll(",", "."));
		}

		return mean / (counter + epsilon);
	}

	/**
	 * The method replaces null values in column by a given numerical value
	 * 
	 * @param column
	 * @param nullvalues
	 * @param mean
	 * @return a column with all the replaced null values
	 */
	public List<String[]> replaceNullByValue(List<String[]> column, String[] nullvalues, double mean) {

		List<String[]> columnReplaced = new ArrayList<>();
		boolean replace = false;

		for (int i = 0; i < column.size(); i++) {

			replace = false;

			for (int j = 0; j < nullvalues.length; j++) {
				if (nullvalues[j].equals(column.get(i)[0])) {
					String[] tmp = new String[1];
					tmp[0] = String.valueOf(mean);
					columnReplaced.add(tmp);
					replace = true;
					break;
				}
			}

			if (!replace)
				columnReplaced.add(column.get(i));

		}

		return columnReplaced;

	}

	/**
	 * In some cases one needs an additional column indicating that a value in a
	 * given column is null
	 * 
	 * @param column
	 * @param nullvalues
	 * @return the null indicator column
	 */
	public List<String[]> createNullIndicatorColumn(List<String[]> column, String[] nullvalues) {

		List<String[]> columnNullIndicator = new ArrayList<>();
		boolean replace = false;

		for (int i = 0; i < column.size(); i++) {

			String[] tmp = new String[1];

			replace = false;

			for (int j = 0; j < nullvalues.length; j++) {
				if (nullvalues[j].equals(column.get(i)[0])) {
					tmp[0] = "1";
					columnNullIndicator.add(tmp);
					replace = true;
					break;
				}
			}

			if (!replace) {
				tmp[0] = "0";
				columnNullIndicator.add(tmp);
			}

		}

		return columnNullIndicator;

	}

	/**
	 * Checks if a column hosts a given null value
	 * 
	 * @param column
	 * @param nullvalues
	 * @return true or false
	 */
	public boolean checkIfNullValueIsPresent(List<String[]> column, String[] nullvalues) {

		boolean containNullValues = false;

		for (int i = 0; i < column.size(); i++) {

			for (int j = 0; j < nullvalues.length; j++) {

				if (nullvalues[j].equals(column.get(i)[0])) {
					containNullValues = true;
					break;
				}
			}

			if (containNullValues)
				break;

		}

		return containNullValues;
	}

}
