package infrastructure;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

/**
 * 
 * This class provides a method to use a trained neuronal network for
 * prediction. If ideal test data is given it lists some information about the
 * quality of the prediction.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ShowTrainedResult {

	private static DeNormalize deNormalize = new DeNormalize();
	private static Logger log = Logger.getLogger(ShowTrainedResult.class.getName());

	/**
	 * This method is given to use a trained neuronal network for prediction. If
	 * ideal test data is given it lists some information about the quality of
	 * the prediction.
	 * 
	 * @param neuronalNetworkCogNetContext
	 * @param preProAiContext
	 */
	public void result(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext, PreProAiContext preProAiContext) {

		// test the neural network
		log.log(Level.INFO, "Neural Network Results:");

		double mittelwert = 0;
		double counter = 0;

		for (int j = 0; j < neuronalNetworkCogNetContext.getTrainingSet().size(); j++) {

			MLDataPair pair = neuronalNetworkCogNetContext.getTrainingSet().get(j);

			final MLData output = neuronalNetworkCogNetContext.getNetwork().compute(pair.getInput());

			for (int i = 0; i < output.getData().length; i++) {

				double[] minimumMaximum = preProAiContext.getMinMaxResultColumn().get(i);

				double[] upperLowerBound = preProAiContext.getMinMaxResultColumn().get(i);

				double learnedResult = deNormalize.denormalize(output.getData(0), minimumMaximum[0], minimumMaximum[1],
						upperLowerBound[0], upperLowerBound[1]);

				double idealResult = deNormalize.denormalize(pair.getIdeal().getData(0), minimumMaximum[0],
						minimumMaximum[1], upperLowerBound[0], upperLowerBound[1]);

				if (idealResult == 0)
					idealResult += NeuronalNetworkCogNetContext.getEpsilon();
				if (learnedResult == 0)
					learnedResult += NeuronalNetworkCogNetContext.getEpsilon();

				String tmp = String.format("actual=%.3f", learnedResult) + String.format(",ideal=%.3f", idealResult)
						+ String.format(",difference=%.3f", Math.abs(idealResult - learnedResult));

				log.log(Level.INFO, tmp);

				mittelwert += Math.abs(idealResult - learnedResult);
				counter++;

			}
		}

		if (counter > 0) {
			String tmp = String.format("The mean distance on the Training-Dataset is %.2f", mittelwert / counter);
			log.log(Level.INFO, tmp);
		}
	}

}
