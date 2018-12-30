package infrastructure;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.pattern.ElmanPattern;

import context.NeuronalNetworkCogNetContext;

/**
 * This class provides a method to set up neuronal network topology and data for
 * ENCOG.
 * 
 * 
 * @author Wolfgang Kapferer
 *
 */
public class SetUpNeuronalNet {

	/**
	 * This methods sets up neuronal network topology for ENCOG and provides
	 * normalized data to use it.
	 * 
	 * @param neuronalNetworkCogNetContext
	 */
	public void setUpNetwork(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		neuronalNetworkCogNetContext.setNetwork(new BasicNetwork());

		neuronalNetworkCogNetContext.getNetwork()
				.addLayer(new BasicLayer(null, true, neuronalNetworkCogNetContext.getData()[0].length));

		for (int i = 0; i < neuronalNetworkCogNetContext.getLayerTopology().length; i++) {
			neuronalNetworkCogNetContext.getNetwork().addLayer(
					new BasicLayer(new ActivationSigmoid(), true, neuronalNetworkCogNetContext.getLayerTopology()[i]));
		}

		neuronalNetworkCogNetContext.getNetwork().addLayer(
				new BasicLayer(new ActivationSigmoid(), true, neuronalNetworkCogNetContext.getResultColums().length));

		neuronalNetworkCogNetContext.getNetwork().getStructure().finalizeStructure();
		neuronalNetworkCogNetContext.getNetwork().reset(42);

		neuronalNetworkCogNetContext.setTrainingSet(new BasicMLDataSet(neuronalNetworkCogNetContext.getData(),
				neuronalNetworkCogNetContext.getDataTrainResult()));

	}

	/**
	 * This methods sets up a Elman (recurrent) neuronal network topology for ENCOG and provides
	 * normalized data to use it.
	 * 
	 * @param neuronalNetworkCogNetContext
	 */
	public void setUpElmanNetwork(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		ElmanPattern pattern = new ElmanPattern();

		pattern.setActivationFunction(new ActivationSigmoid());

		pattern.setInputNeurons(neuronalNetworkCogNetContext.getData()[0].length);

		for (int i = 0; i < neuronalNetworkCogNetContext.getLayerTopology().length; i++) {
			pattern.addHiddenLayer(neuronalNetworkCogNetContext.getLayerTopology()[i]);
		}

		pattern.setOutputNeurons(neuronalNetworkCogNetContext.getResultColums().length);

		neuronalNetworkCogNetContext.setNetwork((BasicNetwork) pattern.generate());

		//neuronalNetworkCogNetContext.getNetwork().getStructure().finalizeStructure();
		neuronalNetworkCogNetContext.getNetwork().reset(42);

		neuronalNetworkCogNetContext.setTrainingSet(new BasicMLDataSet(neuronalNetworkCogNetContext.getData(),
				neuronalNetworkCogNetContext.getDataTrainResult()));

	}

}
