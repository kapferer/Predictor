package transformer;

import java.util.Map.Entry;

import context.PreProAiContext;

/**
 * The purpose of this class is the transformation of numerical data for input
 * and training data
 * 
 * @author Wolfgang Kapferer
 *
 */
public class TransformNumericalData {

	private static Normalize normalize = new Normalize();

	/**
	 * Checks for the complete input data array, if there are numerical columns
	 * and does the appropriate transformation. If a numerical column does not
	 * have distinct values it will be marked for further deletion.
	 * 
	 * @param preProAiContext
	 */
	public void transformNumericalData(PreProAiContext preProAiContext) {

		for (Entry<Integer, Boolean> entry : preProAiContext.getWhichDataColumnIsNotCategorical().entrySet()) {
			Boolean value = entry.getValue();
			Integer key = entry.getKey();

			if (value) {

				double[] minimumMaximum = normalize.extractMinimumMaximum(preProAiContext.getData().get(key),
						preProAiContext.getNullValues());

				double[] meanStandardDevation = normalize.extractMeanStandardDeviation(
						preProAiContext.getData().get(key), preProAiContext.getNullValues());

				if (minimumMaximum[0] == minimumMaximum[1]) {
					preProAiContext.getDataColumnsDoDelete().add(key);
				} else {

					preProAiContext.getData().set(key, normalize.normalizeNumericalColumn(
							preProAiContext.getData().get(key), meanStandardDevation, preProAiContext.getNullValues()));
					
					preProAiContext.getData().set(key,
							normalize.scaleNumericalColumn(preProAiContext.getData().get(key), minimumMaximum,
									PreProAiContext.getLowerbound(), PreProAiContext.getUpperbound(),
									preProAiContext.getNullValues()));
					

				}
			}

		}
	}

	/**
	 * /** Checks for the complete result data array, if there are numerical
	 * columns and does the appropriate transformation. If a numerical column
	 * does not have distinct values it will be merked for further deletion.
	 * 
	 * @param preProAiContext
	 */
	public void transformResultData(PreProAiContext preProAiContext) {

		int counter = 0;
		for (int i = 0; i < preProAiContext.getDataResults().size(); i++) {

			if (preProAiContext.getResultColumnsDoDelete().contains(i))
				continue;

			double[] minimumMaximum = normalize.extractMinimumMaximum(preProAiContext.getDataResults().get(i),
					preProAiContext.getNullValues());

			preProAiContext.getMinMaxResultColumn().put(counter, minimumMaximum);

			if (minimumMaximum[0] == minimumMaximum[1]) {
				preProAiContext.getResultColumnsDoDelete().add(i);
			} else {
				preProAiContext.getDataResults().set(i,
						normalize.scaleNumericalColumn(preProAiContext.getDataResults().get(i), minimumMaximum,
								PreProAiContext.getLowerbound(), PreProAiContext.getUpperbound(),
								preProAiContext.getNullValues()));

				double[] minimumMaximumNormalizedColumn = normalize.extractMinimumMaximum(
						preProAiContext.getDataResults().get(i), preProAiContext.getNullValues());

				preProAiContext.getMinMaxNormalizedResultColumn().put(counter, minimumMaximumNormalizedColumn);

			}
			counter++;
		}
	}

}
