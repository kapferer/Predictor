package transformer;

import java.util.Map.Entry;

import context.PreProAiContext;

/**
 * This class provides the core method for handling null values
 * 
 * @author Wolfgang Kapferer
 *
 */
public class HandleNullValues {

	private static ColumnStatistics columnStatistics = new ColumnStatistics();
	private static Normalize normalize = new Normalize();

	/**
	 * The basic idea of this method is the handling of null values in columns.
	 * Basically it applies the minimum value if a null indicator column is
	 * applied or it exchanges the mean for the null value. This is done for
	 * input and result traning columns.
	 * 
	 * @param preProAiContext
	 */
	public void handleNullValue(PreProAiContext preProAiContext) {

		for (Entry<Integer, Boolean> entry : preProAiContext.getWhichDataColumnIsNotCategorical().entrySet()) {
			Boolean value = entry.getValue();
			Integer key = entry.getKey();

			if (value) {

				if (!columnStatistics.checkIfNullValueIsPresent(preProAiContext.getData().get(key),
						preProAiContext.getNullValues()))
					continue;

				if (preProAiContext.isNullIndicatorColumns()) {

					double[] extractMinimumMaximum = normalize.extractMinimumMaximum(preProAiContext.getData().get(key),
							preProAiContext.getNullValues());

					preProAiContext.getData().add(columnStatistics.createNullIndicatorColumn(
							preProAiContext.getData().get(key), preProAiContext.getNullValues()));

					preProAiContext.getData().set(key,
							columnStatistics.replaceNullByValue(preProAiContext.getData().get(key),
									preProAiContext.getNullValues(), extractMinimumMaximum[0]));

					String[] newHeaderEntry = new String[1];
					int entryNumber = preProAiContext.getHeader().size();

					newHeaderEntry[0] = preProAiContext.getHeader().get(key)[0] + " - nullIndicatorColumn";
					preProAiContext.getHeader().put(entryNumber, newHeaderEntry);

				} else {

					double mean = columnStatistics.getMeanFromNumericalColumn(preProAiContext.getData().get(key),
							preProAiContext.getNullValues());

					preProAiContext.getData().set(key, columnStatistics.replaceNullByValue(
							preProAiContext.getData().get(key), preProAiContext.getNullValues(), mean));
				}

			}
		}

		for (Entry<Integer, Boolean> entry : preProAiContext.getWhichResultColumnIsNotCategorical().entrySet()) {
			Boolean value = entry.getValue();
			Integer key = entry.getKey();

			if (value) {

				if (!columnStatistics.checkIfNullValueIsPresent(preProAiContext.getDataResults().get(key),
						preProAiContext.getNullValues()))
					continue;

				if (preProAiContext.isNullIndicatorColumns()) {

					double[] extractMinimumMaximum = normalize.extractMinimumMaximum(
							preProAiContext.getDataResults().get(key), preProAiContext.getNullValues());

					preProAiContext.getDataResults().add(columnStatistics.createNullIndicatorColumn(
							preProAiContext.getDataResults().get(key), preProAiContext.getNullValues()));

					preProAiContext.getDataResults().set(key,
							columnStatistics.replaceNullByValue(preProAiContext.getDataResults().get(key),
									preProAiContext.getNullValues(), extractMinimumMaximum[0]));

					String[] newHeaderEntry = new String[1];
					int entryNumber = preProAiContext.getHeaderResultColumns().size();

					newHeaderEntry[0] = preProAiContext.getHeaderResultColumns().get(key)[0] + " - nullIndicatorColumn";
					preProAiContext.getHeaderResultColumns().put(entryNumber, newHeaderEntry);
				} else {

					double mean = columnStatistics.getMeanFromNumericalColumn(preProAiContext.getDataResults().get(key),
							preProAiContext.getNullValues());

					preProAiContext.getDataResults().set(key, columnStatistics.replaceNullByValue(
							preProAiContext.getDataResults().get(key), preProAiContext.getNullValues(), mean));

				}

			}
		}

	}

}
