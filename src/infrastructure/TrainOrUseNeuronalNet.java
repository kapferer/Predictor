package infrastructure;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

/**
 * This class provides the method for training or using a neuronal-network.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class TrainOrUseNeuronalNet {

	private static SetUpNeuronalNet setUpNeuronalNet = new SetUpNeuronalNet();
	private static TrainNetwork trainNetwork = new TrainNetwork();
	private static ShowTrainedResult showTrainedResult = new ShowTrainedResult();
	private static PredictData dataToPredict = new PredictData();
	private static SaveLoadTrainedNNnet saveLoadTrainedNNnet = new SaveLoadTrainedNNnet();

	/**
	 * This method sets up a network topology, fills ENCOG with the normalized
	 * training data and trains the network. Afterwards it shows the results and
	 * applies the trained network on yet unknown data. Finally it saves the
	 * trained network parameters into ENCOGs dative format.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void trainAndTestNeuronalNet(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		/**
		 * Step 3.1: Set-up the neuronal-net
		 */
		setUpNeuronalNet.setUpNetwork(neuronalNetworkCogNetContext);
		
		/**
		 * Step 3.2: Train the network either a Resilient-Backpropagation or a
		 * Levenberg-Marquardt Method for the minimum.
		 */
		if (neuronalNetworkCogNetContext.getrPROPorLMA().equals(NeuronalNetworkCogNetContext.getTrainmodeRprop()))
			trainNetwork.trainNetworkResilientPropagation(neuronalNetworkCogNetContext);

		if (neuronalNetworkCogNetContext.getrPROPorLMA().equals(NeuronalNetworkCogNetContext.getTrainmodeLma()))
			trainNetwork.trainNetworkLevenbergMarquardt(neuronalNetworkCogNetContext);

		/**
		 * Step 3.3: Show the trained result on the console
		 */
		showTrainedResult.result(neuronalNetworkCogNetContext, preProAiContext);

		/**
		 * Step 3.4: Predict the data which is yet unknown
		 */
		dataToPredict.predict(neuronalNetworkCogNetContext, preProAiContext);

		/**
		 * Step 3.5: Save the trained net into ENCOGs native file format
		 */
		saveLoadTrainedNNnet.saveTrainedNNetFromFile(preProAiContext, neuronalNetworkCogNetContext);

	}

	/**
	 * This methods loads a trained network and applies it on the given
	 * normalized data
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void predictWithNeuronalNet(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		/**
		 * Step 3.1: Load the Trained Network
		 */
		saveLoadTrainedNNnet.loadTrainedNNetFromFile(preProAiContext, neuronalNetworkCogNetContext);

		/**
		 * Step 3.2: Predict the data which is yet unknown
		 */
		dataToPredict.predict(neuronalNetworkCogNetContext, preProAiContext);

	}

}
