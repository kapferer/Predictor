package infrastructure;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

import filehandling.AnalyzeResultSetFile;
import filehandling.WritePreparedDataCSVFile;
import transformer.EncodeCategoricalData;
import transformer.HandleNullValues;
import transformer.TransformNumericalData;

/**
 * In this class all the data preparation for a neuronal network is done. The
 * columns will be checked for categorical data, the corresponding encoding and
 * translation into the ENCOG format is done.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class PrepareData {

	private static AnalyzeResultSetFile analizeResultSet = new AnalyzeResultSetFile();
	private static EncodeCategoricalData encodeCategoricalData = new EncodeCategoricalData();
	private static TransformNumericalData transformNumericalData = new TransformNumericalData();
	private static RemoveUselessColumns removeUselessColumns = new RemoveUselessColumns();
	private static TranslatePreProAiToEnCog translatePreProAiToEnCog = new TranslatePreProAiToEnCog();
	private static HandleNullValues handleNullValues = new HandleNullValues();
	private static WritePreparedDataCSVFile writePreparedDataCSVFile = new WritePreparedDataCSVFile();

	/**
	 * In this method the 8 steps for preparing the data for ENCOG are done.
	 * 
	 * Step 1: Check, which input-data columns are numerical or not. This is
	 * important because char arrays can in principle hold numbers. If in the
	 * parameterfile the categorical columns are provided, numbers will be
	 * treated like categorical numbers. Categorical columns are one-hot
	 * encoded.
	 * 
	 * Step 2: Check, which input-result-data columns are numerical or not. This
	 * is important because char arrays can in principle hold numbers. Result
	 * columns will be translated in the interval 0,1
	 * 
	 * 
	 * Step 3: NULL values will be either filled wich mean value (if numerical)
	 * or can be highlighted with a null Indicator column. This has to be set in
	 * the nullIndicatorColumn parameter in the parameter file (0 -> no null
	 * indicator-column; 1 -> null indicator-column).
	 * 
	 * Step 4: Encode Categorical Data with one-hot encoding, if a result column
	 * is categorical it will be numerical encoded.
	 * 
	 * Step 5: Transform input-data into the interval LOWERBOUND,UPPERBOUND (in
	 * the class PreProAiContext)
	 * 
	 * Step 6: Transform result data into the interval LOWERBOUND,UPPERBOUND (in
	 * the class PreProAiContext)
	 * 
	 * Step 7: If a columns has no distinct values it will be removed, what will
	 * we learn from it ;-)
	 * 
	 * Step 8: The prepared data will be translated into the datastructure for
	 * ENCOG
	 * 
	 * Step 2.9:If needed a CSV with the prepared data can be written
	 * 
	 * In the method the steps are counted with 2.1,2.2,.... because it is the
	 * second step in the main routine
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void preapareForNeuronalNet(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		/**
		 * Step 2.1: Check, which input-data columns are numerical or not. This
		 * is important because char arrays can in principle hold numbers. If in
		 * the parameterfile the categorical columns are provided, numbers will
		 * be treated like categorical numbers. Categorical columns are one-hot
		 * encoded.
		 */
		preProAiContext.setWhichDataColumnIsNotCategorical(analizeResultSet.whichColumnIsNotCategorical(
				preProAiContext.getData(), preProAiContext.getNullValues(), preProAiContext.getCategoricalColumns(),
				preProAiContext.getResultColumns(), preProAiContext.getDecimalpoint()));

		/**
		 * Step 2.2: Check, which input-result-data columns are numerical or
		 * not. This is important because char arrays can in principle hold
		 * numbers. Result columns will be translated in the interval 0,1
		 */
		preProAiContext.setWhichResultColumnIsNotCategorical(
				analizeResultSet.whichColumnIsNotCategorical(preProAiContext.getDataResults(),
						preProAiContext.getNullValues(), null, null, preProAiContext.getDecimalpoint()));

		/**
		 * Step 2.3: NULL values will be either filled wich mean value (if
		 * numerical) or can be highlighted with a null Indicator column. This
		 * has to be set in the nullIndicatorColumn parameter in the parameter
		 * file (0 -> no null indicator-column; 1 -> null indicator-column).
		 * 
		 */
		if (preProAiContext.getNullValues() != null)
			handleNullValues.handleNullValue(preProAiContext);

		/**
		 * Step 2.4: Encode Categorical Data with one-hot encoding, if a result
		 * column is categorical it will be numerical encoded.
		 */
		encodeCategoricalData.encodeCategoricalData(preProAiContext);

		/**
		 * Step 2.5: Transform input-data into the interval
		 * LOWERBOUND,UPPERBOUND (in the class PreProAiContext)
		 * after normalization
		 */
		transformNumericalData.transformNumericalData(preProAiContext);

		/**
		 * Step 2.6: Transform result data into the interval
		 * LOWERBOUND,UPPERBOUND (in the class PreProAiContext)
		 */
		transformNumericalData.transformResultData(preProAiContext);

		/**
		 * Step 2.7: If a columns has no distinct values it will be removed,
		 * what will we learn from it ;-)
		 */
		removeUselessColumns.removeColumns(preProAiContext);

		/**
		 * Step 2.8: The prepared data will be translated into the datastructure
		 * for ENCOG
		 */
		translatePreProAiToEnCog.translatePreProAIToEncog(preProAiContext, neuronalNetworkCogNetContext);

		/**
		 * Step 2.9:If needed a CSV with the prepared data can be written
		 */
		if (preProAiContext.isWritePreparedData())
			writePreparedDataCSVFile.writeCSVFile(preProAiContext);

	}

}
