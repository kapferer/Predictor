package transformer;

import java.util.ArrayList;
import java.util.List;

import infrastructure.ExitProgramm;

/**
 * This class provides all methods for normalization, i.e. minimum maximum
 * extraction and normalization to lower upper baound values.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class Normalize {

	private static ExitProgramm exitProgramm = new ExitProgramm();

	/**
	 * The linear scaling.
	 * 
	 * @param toNormalize
	 * @param minimum
	 * @param maximum
	 * @param lowerBound
	 * @param upperBound
	 * @return the normalized value
	 */
	double scaling(double toNormalize, double minimum, double maximum, double lowerBound, double upperBound) {

		double normalized;
		
		
		if (maximum == minimum) {
			normalized = 1;
		} else {
			normalized = ((upperBound - lowerBound) / (maximum - minimum)) * (toNormalize)
					- ((upperBound - lowerBound) / (maximum - minimum)) * minimum + lowerBound;
		}

		return normalized;

	}

	/**
	 * The normalization.
	 * 
	 * @param toNormalize
	 * @param minimum
	 * @param maximum
	 * @param lowerBound
	 * @param upperBound
	 * @return the normalized value
	 */
	double normalize(double toNormalize, double mean, double standardDeviation) {

		double normalized = 0;

		if (standardDeviation == 0.0) {
			exitProgramm.programmExiter("Normaliaztion not possible;Standard Devation is zero", null);
		} else {
			normalized = (toNormalize - mean) / standardDeviation;
		}

		return normalized;

	}

	/**
	 * This method extracts the minimum and maximum values for a given numerical
	 * column.
	 * 
	 * @param numericalColumn
	 * @param nullValues
	 * @return an array holding first the minimum and then the maximum
	 */
	double[] extractMinimumMaximum(List<String[]> numericalColumn, String[] nullValues) {

		double[] mininumMaximum = new double[2];
		double value;
		boolean nullValueFound;

		mininumMaximum[0] = Double.MAX_VALUE;
		mininumMaximum[1] = Double.MIN_VALUE;

		for (int i = 0; i < numericalColumn.size(); i++) {

			nullValueFound = false;

			if (nullValues != null) {
				for (int j = 0; j < nullValues.length; j++) {
					if (nullValues[j].equals((numericalColumn.get(i)[0]))) {
						nullValueFound = true;
						break;
					}
				}
			}

			if (!nullValueFound) {
				value = Double.parseDouble(numericalColumn.get(i)[0].replaceAll(",", "."));

				if (value < mininumMaximum[0]) {
					mininumMaximum[0] = value;
				}

				if (value > mininumMaximum[1]) {
					mininumMaximum[1] = value;
				}
			}

		}

		return mininumMaximum;

	}

	/**
	 * The linear scaler for a complete List of array strings
	 * 
	 * @param numericalColumn
	 * @param minimumMaximum
	 * @param lowerBound
	 * @param upperBound
	 * @param nullValues
	 * @return list of array strings in a normalized way
	 */
	List<String[]> scaleNumericalColumn(List<String[]> numericalColumn, double[] minimumMaximum, double lowerBound,
			double upperBound, String[] nullValues) {

		List<String[]> normalizedColumn = new ArrayList<>();
		double value;
		boolean nullValueFound;

		for (int i = 0; i < numericalColumn.size(); i++) {

			nullValueFound = false;

			if (nullValues != null) {

				for (int j = 0; j < nullValues.length; j++) {
					if (nullValues[j].equals((numericalColumn.get(i)[0]))) {
						nullValueFound = true;
						break;
					}
				}
			}

			if (!nullValueFound) {
				value = Double.parseDouble(numericalColumn.get(i)[0].replaceAll(",", "."));
				String[] tmp = new String[1];
				tmp[0] = String.valueOf(scaling(value, minimumMaximum[0], minimumMaximum[1], lowerBound, upperBound));
				normalizedColumn.add(tmp);
			} else {
				normalizedColumn.add(numericalColumn.get(i));
			}
		}

		return normalizedColumn;

	}

	/**
	 * The normalization for a complete List of array strings
	 * 
	 * @param numericalColumn
	 * @param minimumMaximum
	 * @param lowerBound
	 * @param upperBound
	 * @param nullValues
	 * @return list of array strings in a normalized way
	 */
	List<String[]> normalizeNumericalColumn(List<String[]> numericalColumn, double[] meanStandardDevation,
			String[] nullValues) {

		List<String[]> normalizedColumn = new ArrayList<>();
		double value;
		boolean nullValueFound;

		for (int i = 0; i < numericalColumn.size(); i++) {

			nullValueFound = false;

			if (nullValues != null) {

				for (int j = 0; j < nullValues.length; j++) {
					if (nullValues[j].equals((numericalColumn.get(i)[0]))) {
						nullValueFound = true;
						break;
					}
				}
			}

			if (!nullValueFound) {
				value = Double.parseDouble(numericalColumn.get(i)[0].replaceAll(",", "."));
				String[] tmp = new String[1];
				tmp[0] = String.valueOf(normalize(value, meanStandardDevation[0], meanStandardDevation[1]));
				normalizedColumn.add(tmp);
			} else {
				normalizedColumn.add(numericalColumn.get(i));
			}
		}

		return normalizedColumn;

	}

	/**
	 * This method extracts the minimum and maximum values for a given numerical
	 * column.
	 * 
	 * @param numericalColumn
	 * @param nullValues
	 * @return an array holding first the mean and second the stanrad deviation
	 */
	double[] extractMeanStandardDeviation(List<String[]> numericalColumn, String[] nullValues) {

		double[] meanStandardDevation = new double[2];
		double value = 0;
		int counter = 0;
		boolean nullValueFound;

		meanStandardDevation[0] = 0.0;
		meanStandardDevation[1] = 0.0;

		for (int i = 0; i < numericalColumn.size(); i++) {

			nullValueFound = false;

			if (nullValues != null) {
				for (int j = 0; j < nullValues.length; j++) {
					if (nullValues[j].equals((numericalColumn.get(i)[0]))) {
						nullValueFound = true;
						break;
					}
				}
			}

			if (!nullValueFound) {
				value += Double.parseDouble(numericalColumn.get(i)[0].replaceAll(",", "."));
				counter++;

			}

		}

		if (counter != 0)
			meanStandardDevation[0] = value / counter;

		counter = 0;
		value = 0;
		for (int i = 0; i < numericalColumn.size(); i++) {

			nullValueFound = false;

			if (nullValues != null) {
				for (int j = 0; j < nullValues.length; j++) {
					if (nullValues[j].equals((numericalColumn.get(i)[0]))) {
						nullValueFound = true;
						break;
					}
				}
			}

			if (!nullValueFound) {
				value += Math.pow(
						Double.parseDouble(numericalColumn.get(i)[0].replaceAll(",", ".")) - meanStandardDevation[0],
						2);
				counter++;

			}

		}

		if (counter != 1)
			meanStandardDevation[1] = Math.sqrt(1.0 / (counter - 1.0) * value);

		return meanStandardDevation;

	}

}
