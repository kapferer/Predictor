package infrastructure;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

/**
 * 
 * This class holds the predictor method. It uses all the information of a
 * trained network to predict values from normalized input data.
 * 
 * @author Wolfgang Kapferer
 *
 */

public class PredictData {

	private static Logger log = Logger.getLogger(PredictData.class.getName());
	private static DeNormalize deNormalize = new DeNormalize();

	/**
	 * The method fills the encog datastructur for using a already trained
	 * network. The data needs to be normalized.
	 * 
	 * @param neuronalNetworkCogNetContext
	 * @param preProAiContext
	 */
	public void predict(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, PreProAiContext preProAiContext) {

		int numberResultColumns = neuronalNetworkCogNetContext.getNetwork().getOutputCount();

		double[][] prediction = new double[neuronalNetworkCogNetContext.getDataToPredict().length][numberResultColumns];
		double[][] ideal = new double[neuronalNetworkCogNetContext.getDataToPredict().length][numberResultColumns];

		double mittelwertAbstand = 0.0;

		for (int j = 0; j < neuronalNetworkCogNetContext.getDataToPredict().length; j++) {

			BasicMLData trainingSet = new BasicMLData(neuronalNetworkCogNetContext.getDataToPredict()[j]);
			MLData output = neuronalNetworkCogNetContext.getNetwork().compute(trainingSet);

			for (int i = 0; i < output.getData().length; i++) {

				double[] minimumMaximum = preProAiContext.getMinMaxResultColumn().get(i);
				double[] upperLowerBound = preProAiContext.getMinMaxNormalizedResultColumn().get(i);

				double predictionResult = deNormalize.denormalize(output.getData(0), minimumMaximum[0],
						minimumMaximum[1], upperLowerBound[0], upperLowerBound[1]);

				prediction[j][i] = predictionResult;

				double predictionIdeal = 0.0;
				
				if (!preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getApplymode())) {
					predictionIdeal = deNormalize.denormalize(
							neuronalNetworkCogNetContext.getDataToTestOriginal()[j][i], minimumMaximum[0],
							minimumMaximum[1], upperLowerBound[0], upperLowerBound[1]);

					ideal[j][i] = predictionIdeal;
					mittelwertAbstand += Math.abs(predictionIdeal - predictionResult);

				}

				if (preProAiContext.getPrimaryKeyArray() != null) {
					String[] pk = neuronalNetworkCogNetContext.getPrimaryKeys().get(j);
					StringBuilder pkTmp = new StringBuilder();
					for (int k = 0; k < pk.length; k++)
						pkTmp.append(pk[k] + " ");

					String result = String.format("predicted=%.3f", predictionResult).replace(",", ".") + " - "
							+ "ideal=" + predictionIdeal + " for Key " + pkTmp.toString();

					log.log(Level.INFO, result);

				} else {
					String result = String.format("predicted=%.3f", predictionResult).replace(",", ".") + " - "
							+ "ideal=" + predictionIdeal;

					log.log(Level.INFO, result);
				}

			}
		}

		for (Entry<Integer, Map<String, Integer>> entry : preProAiContext.getResultTranslator().entrySet()) {
			Integer key = entry.getKey();
			String tmp = "Translation for ResultColumn " + key + 1;
			log.log(Level.INFO, tmp);
			for (Entry<String, Integer> entry1 : preProAiContext.getResultTranslator().get(key).entrySet()) {
				log.log(Level.INFO, "Original Value " + entry1.getKey() + " -> " + entry1.getValue());

			}

		}

		String tmp = String.format("The mean distance on the Test-Dataset is %.2f",
				mittelwertAbstand / neuronalNetworkCogNetContext.getDataToPredict().length);

		log.log(Level.INFO, tmp);

		neuronalNetworkCogNetContext.setPrediction(prediction);

	}

}
