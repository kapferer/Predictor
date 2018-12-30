package infrastructure;

import java.io.File;

import static org.encog.persist.EncogDirectoryPersistence.saveObject;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;

import filehandling.ReadCSVFileTranslatorFile;
import filehandling.WriteResultTranslatorFile;

/**
 * 
 * This class provides the methods for saving and loading the trained
 * neuronal-network data in ENCOGs EG native file format. See
 * https://s3.amazonaws.com/heatonresearch-books/free/encog-3_3-devguide.pdf for
 * detailed information.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class SaveLoadTrainedNNnet {

	private static WriteResultTranslatorFile writeResultTranslatorFile = new WriteResultTranslatorFile();
	private static ReadCSVFileTranslatorFile readCSVFileTranslatorFile = new ReadCSVFileTranslatorFile();

	/**
	 * Saves the trained neuronal-network data in ENCOGs EG native file format
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void saveTrainedNNetFromFile(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		saveObject(new File(preProAiContext.getFilnameForTraineNetworkToSave()),
				neuronalNetworkCogNetContext.getNetwork());

		writeResultTranslatorFile.writeCSVFile(preProAiContext);

	}

	/**
	 * Loads the trained neuronal-network data in ENCOGs EG native file format
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void loadTrainedNNetFromFile(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		neuronalNetworkCogNetContext.setNetwork(
				(BasicNetwork) EncogDirectoryPersistence.loadObject(new File(preProAiContext.getTrainedNetFile())));

		readCSVFileTranslatorFile.readTranslatorFile(preProAiContext);

	}

}
