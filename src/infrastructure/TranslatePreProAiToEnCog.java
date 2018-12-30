package infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

/**
 * This class translates between the data-structures of the preparation and the
 * neuronal-network data structure
 * 
 * @author Wolfgang Kapferer
 *
 */
public class TranslatePreProAiToEnCog {

	private static Logger log = Logger.getLogger(TranslatePreProAiToEnCog.class.getName());

	/**
	 * THie method does the actual translation between prepared data and the
	 * input data for the neuronal-network
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void translatePreProAIToEncog(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		List<String[]> data = concatenateData(preProAiContext);

		if (preProAiContext.getResultColumns() != null)
			populateNewResultcolumnsForModel(preProAiContext, neuronalNetworkCogNetContext, data);

		if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getTrainmode())) {

			/*
			 * order of function calls is important -> 0.0 means all is for
			 * training
			 */
			if ((neuronalNetworkCogNetContext.getRatioLearnToTest() != 0.0)
					&& (neuronalNetworkCogNetContext.getResultColums() != null))// ->
				populateTestData(preProAiContext, neuronalNetworkCogNetContext, data);
			populateTrainingData(preProAiContext, neuronalNetworkCogNetContext, data);

		}

		if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getApplymode())) {
			populatePredictionData(preProAiContext, neuronalNetworkCogNetContext, data);

		}

	}

	/**
	 * This method concatenates all the data of the prepared input columns and
	 * result columns, which are treated quite different to one List of string
	 * arrays
	 * 
	 * @param preProAiContext
	 * @return A list of String arrays holding the input and result columns for
	 *         the network
	 */
	private List<String[]> concatenateData(PreProAiContext preProAiContext) {

		int numberOfArrayElements = 0;
		int numberOfAllColumns = preProAiContext.getData().size() + preProAiContext.getDataResults().size();
		int numberOfDataColumns = preProAiContext.getData().size();
		int numberOfRows = preProAiContext.getData().get(0).size();
		int counter;
		List<String[]> data = new ArrayList<>();

		for (int i = 0; i < preProAiContext.getData().size(); i++)
			numberOfArrayElements += preProAiContext.getData().get(i).get(0).length;

		for (int i = 0; i < preProAiContext.getDataResults().size(); i++)
			numberOfArrayElements += preProAiContext.getDataResults().get(i).get(0).length;

		for (int j = 0; j < numberOfRows; j++) {
			counter = 0;
			String[] lineOfData = new String[numberOfArrayElements];
			for (int i = 0; i < numberOfAllColumns; i++) {
				if (i < numberOfDataColumns) {
					for (int k = 0; k < preProAiContext.getData().get(i).get(j).length; k++) {
						lineOfData[counter] = preProAiContext.getData().get(i).get(j)[k];

						counter++;
					}
				} else {
					for (int k = 0; k < preProAiContext.getDataResults().get(i - numberOfDataColumns)
							.get(j).length; k++) {
						lineOfData[counter] = preProAiContext.getDataResults().get(i - numberOfDataColumns).get(j)[k];
						counter++;
					}
				}
			}
			data.add(lineOfData);
		}

		return data;
	}

	/**
	 * Populates the result columns for the model for training
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 * @param data
	 */
	private void populateNewResultcolumnsForModel(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, List<String[]> data) {

		int[] resultcolumnsInModel = new int[preProAiContext.getResultColumns().length];

		for (int i = 0; i < preProAiContext.getResultColumns().length; i++) {
			resultcolumnsInModel[i] = data.get(0).length - 1 - i;
		}

		neuronalNetworkCogNetContext.setResultColums(resultcolumnsInModel);

	}

	/**
	 * This method is the inner core of the translation from preparation to the
	 * training data of the neuronal-network. The neuronal-network only
	 * understands numerical values, i.e. doubles.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 * @param data
	 */
	private void populateTrainingData(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, List<String[]> data) {

		int numberOfResultColumns = 0;

		if (neuronalNetworkCogNetContext.getResultColums() != null)
			numberOfResultColumns = neuronalNetworkCogNetContext.getResultColums().length;

		int numberOfColumns = data.get(0).length - numberOfResultColumns;
		int numberOfRows = data.size();
		int runner = 0;
		int resultRunner = 0;
		boolean aResultColumn = false;
		Map<Integer, Integer> resultColumns = new TreeMap<>();

		for (int k = 0; k < numberOfResultColumns; k++)
			resultColumns.put(neuronalNetworkCogNetContext.getResultColums()[k],
					neuronalNetworkCogNetContext.getResultColums()[k]);

		StringBuilder stringTmp = new StringBuilder();
		stringTmp.append("Number of Columns in input layer ");
		stringTmp.append(numberOfColumns);

		log.log(Level.INFO, stringTmp.toString());

		double[][] dataToTrain = new double[numberOfRows][numberOfColumns];
		double[][] dataResults = new double[numberOfRows][numberOfResultColumns];

		// now we populate the data to train
		for (int i = 0; i < data.size(); i++) {

			runner = 0;
			resultRunner = 0;

			for (int j = 0; j < data.get(i).length; j++) {

				aResultColumn = false;

				if (resultColumns.containsKey(j))
					aResultColumn = true;

				if (aResultColumn) {
					dataResults[i][resultRunner] = Double.valueOf(data.get(i)[j]);
					resultRunner++;
				} else {
					dataToTrain[i][runner] = Double.valueOf(data.get(i)[j]);
					runner++;
				}

			}
		}

		neuronalNetworkCogNetContext.setData(dataToTrain);
		neuronalNetworkCogNetContext.setDataTrainResult(dataResults);

	}

	/**
	 * This method is the inner core of the translation from preparation to the
	 * test data for the neuronal-network. The neuronal-network only understands
	 * numerical values, i.e. doubles.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 * @param data
	 */
	private void populateTestData(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, List<String[]> data) {

		int numberOfResultColumns = 0;

		if (neuronalNetworkCogNetContext.getResultColums() != null)
			numberOfResultColumns = neuronalNetworkCogNetContext.getResultColums().length;

		int numberOfColumns = data.get(0).length - numberOfResultColumns;
		int numberOfRows = data.size();
		int runner = 0;
		int resultRunner = 0;
		int pkArraySize = 0;
		boolean aResultColumn = false;
		Map<Integer, Integer> resultColumns = new TreeMap<>();

		for (int k = 0; k < numberOfResultColumns; k++)
			resultColumns.put(neuronalNetworkCogNetContext.getResultColums()[k],
					neuronalNetworkCogNetContext.getResultColums()[k]);

		if (preProAiContext.getPrimaryKeyArray() != null)
			pkArraySize = preProAiContext.getPrimaryKeyArray().length;

		if (neuronalNetworkCogNetContext.getLineToPredict() == null) {
			populateLineToPredictRandomly(neuronalNetworkCogNetContext, numberOfRows);
		}

		double[][] csvDataPrediction = new double[neuronalNetworkCogNetContext
				.getLineToPredict().length][numberOfColumns];
		double[][] csvDataForPrediction = new double[neuronalNetworkCogNetContext
				.getLineToPredict().length][numberOfResultColumns];

		if (neuronalNetworkCogNetContext.getLineToPredict() != null) {

			for (int i = 0; i < neuronalNetworkCogNetContext.getLineToPredict().length; i++) {
				int resultLine = neuronalNetworkCogNetContext.getLineToPredict()[i];

				if (resultLine > data.size() - 1)
					continue;

				runner = 0;
				resultRunner = 0;

				for (int j = 0; j < data.get(resultLine).length; j++) {

					aResultColumn = false;

					if (resultColumns.containsKey(j))
						aResultColumn = true;

					if (!aResultColumn) {
						csvDataPrediction[i][runner] = Double.valueOf(data.get(resultLine)[j]);
						runner++;
					} else {
						if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getTrainmode())) {
							csvDataForPrediction[i][resultRunner] = Double.valueOf(data.get(resultLine)[j]);
							resultRunner++;
						}
					}
				}

				if (pkArraySize > 0)
					neuronalNetworkCogNetContext.getPrimaryKeys()
							.add(preProAiContext.getDataPrimaryKey().get(resultLine));

			}
		}

		List<String[]> dataRemaining = new ArrayList<>();
		List<List<String[]>> dataforTrainOutput = new ArrayList<>();

		ArrayList<Integer> linesToPredict = new ArrayList<>();

		for (int i = 0; i < neuronalNetworkCogNetContext.getLineToPredict().length; i++) {
			linesToPredict.add(neuronalNetworkCogNetContext.getLineToPredict()[i]);
		}

		for (int i = 0; i < data.size(); i++) {
			if (!linesToPredict.contains(i))
				dataRemaining.add(data.get(i));
		}

		/**
		 * if the prepared CSV should be smaller
		for (int i = 0; i < preProAiContext.getData().size(); i++) {
			List<String[]> tmp = new ArrayList<>();
			for (int j = 0; j < preProAiContext.getData().get(i).size(); j++) {

				if (!linesToPredict.contains(j))
					tmp.add(preProAiContext.getData().get(i).get(j));

			}
			dataforTrainOutput.add(tmp);
		}

		preProAiContext.setData(dataforTrainOutput);
		**/
		
		data.clear();
		for (int i = 0; i < dataRemaining.size(); i++)
			data.add(dataRemaining.get(i));

		neuronalNetworkCogNetContext.setDataToPredict(csvDataPrediction);
		neuronalNetworkCogNetContext.setDataToTestOriginal(csvDataForPrediction);

	}

	/**
	 * This method is the inner core of the translation from preparation to the
	 * prediction-data of the neuronal-network. The neuronal-network only
	 * understands numerical values, i.e. doubles.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 * @param data
	 */
	private void populatePredictionData(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, List<String[]> data) {

		int numberOfResultColumns = 0;

		if (neuronalNetworkCogNetContext.getResultColums() != null)
			numberOfResultColumns = neuronalNetworkCogNetContext.getResultColums().length;

		int numberOfColumns = data.get(0).length - numberOfResultColumns;
		int numberOfRows = data.size();
		int runner = 0;
		int resultRunner = 0;
		int pkArraySize = 0;
		boolean aResultColumn = false;
		Map<Integer, Integer> resultColumns = new TreeMap<>();

		for (int k = 0; k < numberOfResultColumns; k++)
			resultColumns.put(neuronalNetworkCogNetContext.getResultColums()[k],
					neuronalNetworkCogNetContext.getResultColums()[k]);

		if (preProAiContext.getPrimaryKeyArray() != null)
			pkArraySize = preProAiContext.getPrimaryKeyArray().length;

		StringBuilder stringTmp = new StringBuilder();
		stringTmp.append("Number of Columns in input layer ");
		stringTmp.append(numberOfColumns);

		log.log(Level.INFO, stringTmp.toString());

		double[][] dataOriginal = new double[numberOfRows][numberOfColumns];
		double[][] dataResults = new double[numberOfRows][numberOfResultColumns];

		// now we populate the data to train
		for (int i = 0; i < data.size(); i++) {

			runner = 0;
			resultRunner = 0;

			for (int j = 0; j < data.get(i).length; j++) {

				aResultColumn = false;

				if (resultColumns.containsKey(j)) {
					aResultColumn = true;
				}

				if (aResultColumn) {
					dataResults[i][resultRunner] = Double.valueOf(data.get(i)[j]);
					resultRunner++;

				}

				else {
					dataOriginal[i][runner] = Double.valueOf(data.get(i)[j]);
					runner++;
				}
			}

			if (pkArraySize > 0)
				neuronalNetworkCogNetContext.getPrimaryKeys().add(preProAiContext.getDataPrimaryKey().get(i));

		}

		neuronalNetworkCogNetContext.setDataToPredict(dataOriginal);
		neuronalNetworkCogNetContext.setDataToTestOriginal(dataResults);

	}

	/**
	 * This method is the inner core of the translation from preparation to the
	 * prediction data of the neuronal-network. The neuronal-network only
	 * understands numerical values, i.e. doubles. The method is called, if
	 * several distinct lines are given for prediction.
	 * 
	 * @param neuronalNetworkCogNetContext
	 * @param numberOfRows
	 */
	private void populateLineToPredictRandomly(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext,
			int numberOfRows) {

		if (neuronalNetworkCogNetContext.getLineToPredict() != null) {
			return;
		} else {
			double ratio = neuronalNetworkCogNetContext.getRatioLearnToTest();
			int numberOfTestRows = (int) Math.floor((numberOfRows / 100.0 * ratio));

			int[] linesToPredict = new int[numberOfTestRows];

			ArrayList<Integer> numbers = new ArrayList<>();

			while (numbers.size() < numberOfTestRows) {
				double randomFloat = Math.random();
				int randomLine = (int) Math.floor(numberOfRows * randomFloat);
				if (!numbers.contains(randomLine)) {
					numbers.add(randomLine);
				}
			}

			for (int i = 0; i < numbers.size(); i++) {
				// linesToPredict[i] = numbers.get(i) + 1;
				linesToPredict[i] = numbers.get(i);
			}

			neuronalNetworkCogNetContext.setLineToPredict(linesToPredict);
		}

	}

}
