package infrastructure;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.neural.networks.training.lma.LevenbergMarquardtTraining;
import org.encog.neural.networks.training.propagation.resilient.RPROPType;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import context.NeuronalNetworkCogNetContext;

/**
 * This class provides the basic network training mode. Two methods are given,
 * one for Resilient Backpropagation and one for the Levenberg Marquardt method.
 * It depends on your problem, which method will find a optimum and how fast it
 * converges.
 * 
 * @author kapf
 *
 */
public class TrainNetwork {

	static private Logger log = Logger.getLogger(TrainNetwork.class.getName());

	/**
	 * The working horse of backprogation training the resilient method. See
	 * details here https://en.wikipedia.org/wiki/Rprop or
	 * https://de.wikipedia.org/wiki/Resilient_Propagation
	 * 
	 * @param neuronalNetworkCogNetContext
	 */
	public void trainNetworkResilientPropagation(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		int epoch = 1;

		ResilientPropagation train = new ResilientPropagation(neuronalNetworkCogNetContext.getNetwork(),
				neuronalNetworkCogNetContext.getTrainingSet());
		
		train.setRPROPType(RPROPType.iRPROPp);

		do {
			train.iteration();
			if (epoch % 100 == 0) {
				StringBuilder logString = new StringBuilder();
				logString.append("Epoch #" + epoch + " Error:" + train.getError());
				String logText = logString.toString();
				log.log(Level.INFO, logText);
			}
			epoch++;
		} while ((train.getError() > neuronalNetworkCogNetContext.getMaxError())
				&& (epoch < neuronalNetworkCogNetContext.getMaxIteration()));
		train.finishTraining();

	}

	/**
	 * 
	 * The Levenberg-Marquardt-Algorithmus for searching the optimum in the
	 * traning mode.. See details here
	 * https://de.wikipedia.org/wiki/Levenberg-Marquardt-Algorithmus
	 * 
	 * 
	 * @param neuronalNetworkCogNetContext
	 */
	public void trainNetworkLevenbergMarquardt(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		int epoch = 1;

		LevenbergMarquardtTraining train = new LevenbergMarquardtTraining(neuronalNetworkCogNetContext.getNetwork(),
				neuronalNetworkCogNetContext.getTrainingSet());

		do {
			train.iteration();
			if (epoch % 10 == 0) {
				StringBuilder logString = new StringBuilder();
				logString.append("Epoch #" + epoch + " Error:" + train.getError());
				String logText = logString.toString();
				log.log(Level.INFO, logText);
			}
			epoch++;
		} while ((train.getError() > neuronalNetworkCogNetContext.getMaxError())
				&& (epoch < neuronalNetworkCogNetContext.getMaxIteration()));
		train.finishTraining();

	}
}
