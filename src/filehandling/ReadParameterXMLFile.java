package filehandling;

import context.NeuronalNetworkCogNetContext;
import context.PreProAiContext;
import infrastructure.ExitProgramm;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads the Predictor-parameterfile with the javax XML parser. See
 * the howto at the bottom of the PredictorMain.java in order to get all the
 * details.
 * 
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ReadParameterXMLFile {

	private static Logger log = Logger.getLogger(ReadParameterXMLFile.class.getName());
	private static ExitProgramm exitProgramm = new ExitProgramm();

	/**
	 * Reads the parameter-file and stores all data in the two context classes.
	 * 
	 * @param preProAiContext
	 * @param neuronalNetworkCogNetContext
	 */
	public void readParameterXMLFile(PreProAiContext preProAiContext,
			NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		StringBuilder tmp = new StringBuilder();
		tmp.append("\r\n");
		String logSeperator = "->";
		String nodeTextContent;

		try {

			File fXmlFile = new File(preProAiContext.getParameterFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			String tagName = "filenameSQL";
			NodeList nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			preProAiContext.setFileNameWithAbsolutPath(nList.item(0).getTextContent());

			setFilePathAndName(preProAiContext);

			preProAiContext.setFilnameForTraineNetworkToSave(preProAiContext.getFileNameWithAbsolutPath() + ".encog");

			tagName = "resultcolumns";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmpResultColumns = nodeTextContent.split(",");
				int[] resultColumns = new int[tmpResultColumns.length];
				for (int i = 0; i < tmpResultColumns.length; i++)
					resultColumns[i] = Integer.parseInt(tmpResultColumns[i]) - 1;
				preProAiContext.setResultColumns(resultColumns);
			}

			tagName = "decimalpoint";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			preProAiContext.setDecimalpoint(nodeTextContent.charAt(0));

			tagName = "nullValues";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmpnullValues = nodeTextContent.split(",");
				preProAiContext.setNullValues(tmpnullValues);
			}

			/**
			 * A simple trick to give special null-values for the null values of
			 * the database. The assumption is, that this is never a valid
			 * value, hopefully, to avoid all the further annoying null pointer
			 * exception stuff in java.
			 */
			if (preProAiContext.getNullValues() == null)
				preProAiContext.setNullValues(new String[] { PreProAiContext.getNullvaluedb() });

			tagName = "nullIndicatorColumn";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if ("1".equals(nodeTextContent)) {
				preProAiContext.setNullIndicatorColumns(true);
			} else {
				preProAiContext.setNullIndicatorColumns(false);
			}

			tagName = "categoricalColumns";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmpCategoricalColumnsString = nodeTextContent.split(",");
				int[] tmpCategoricalColumnsInt = new int[tmpCategoricalColumnsString.length];
				for (int i = 0; i < tmpCategoricalColumnsString.length; i++)
					tmpCategoricalColumnsInt[i] = Integer.parseInt(tmpCategoricalColumnsString[i]) - 1;
				preProAiContext.setCategoricalColumns(tmpCategoricalColumnsInt);
			}

			tagName = "neuronsInLayer";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmpNeuronsInLayer = nodeTextContent.split(",");
				int[] neuronsInlayer = new int[tmpNeuronsInLayer.length];
				for (int i = 0; i < tmpNeuronsInLayer.length; i++)
					neuronsInlayer[i] = Integer.parseInt(tmpNeuronsInLayer[i]);
				neuronalNetworkCogNetContext.setLayerTopology(neuronsInlayer);
			}

			tagName = "MaxError";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			neuronalNetworkCogNetContext.setMaxError(Double.valueOf(nodeTextContent));

			tagName = "lineToPredict";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmplinesToPredict = nodeTextContent.split(",");
				int[] linesToPredict = new int[tmplinesToPredict.length];
				for (int i = 0; i < tmplinesToPredict.length; i++)
					linesToPredict[i] = Integer.parseInt(tmplinesToPredict[i]) - 1;
				neuronalNetworkCogNetContext.setLineToPredict(linesToPredict);
			}

			tagName = "ratioTestToTrain";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0)
				neuronalNetworkCogNetContext.setRatioLearnToTest(Double.valueOf(nodeTextContent));

			tagName = "trainOrApplyMode";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (("TRAIN".equalsIgnoreCase(nodeTextContent)) || ("APPLY".equalsIgnoreCase(nodeTextContent))) {
				preProAiContext.setTrainOrApplyMode(nodeTextContent.toUpperCase());
			} else {
				exitProgramm.programmExiter("Please provide train or apply mode.", null);
			}

			tagName = "trainedNetFile";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0)
				preProAiContext.setTrainedNetFile(nodeTextContent);

			tagName = "maxIteration";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0)
				neuronalNetworkCogNetContext.setMaxIteration(Integer.parseInt(nodeTextContent));

			tagName = "RPROPorLMA";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (("RPROP".equalsIgnoreCase(nodeTextContent)) || ("LMA".equalsIgnoreCase(nodeTextContent))) {
				neuronalNetworkCogNetContext.setrPROPorLMA(nodeTextContent.toUpperCase());
			} else {
				exitProgramm.programmExiter("Please provide RPROP or LMA? mode.", null);
			}

			tagName = "primaryKeyColumns";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0) {
				String[] tmplinesToPredict = nodeTextContent.split(",");
				int[] linesToPredict = new int[tmplinesToPredict.length];
				for (int i = 0; i < tmplinesToPredict.length; i++)
					linesToPredict[i] = Integer.parseInt(tmplinesToPredict[i]) - 1;
				preProAiContext.setPrimaryKeyArray(linesToPredict);
			}

			tagName = "writeIntoDatabase";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if ((nodeTextContent.length() > 0) && ("1".equals(nodeTextContent))) {
				preProAiContext.setWriteIntoDatabase(true);
			}

			tagName = "resultTranslatorFile";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0)
				preProAiContext.setResultTranslatorFile(nodeTextContent);

			tagName = "onlyPrepareData";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if ((nodeTextContent.length() > 0) && ("1".equals(nodeTextContent))) {
				preProAiContext.setOnlyPrepare(true);
			}

			tagName = "writePrepareDataIntoCSV";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if ((nodeTextContent.length() > 0) && ("1".equals(nodeTextContent))) {
				preProAiContext.setWritePreparedData(true);
			}

			/**
			 * Either one gives linesToPredict or the RationLearnToTest, but
			 * both is not a good idea
			 */
			if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getTrainmode())
					&& (neuronalNetworkCogNetContext.getLineToPredict() == null)
					&& (neuronalNetworkCogNetContext.getRatioLearnToTest() == -1)) {
				exitProgramm.programmExiter("Please provide lineToPredict or ratioTrainToTest.", null);
			}

			/**
			 * When you are in the training Mode you should provide information
			 * about the resultcolumns
			 */
			if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getTrainmode())
					&& (preProAiContext.getResultColumns() == null)) {
				exitProgramm.programmExiter(
						"Predictor is in train-mode. " + "Please provide result columns to predict.", null);
			}

			tagName = "fileNameInsertSql";
			nList = doc.getElementsByTagName(tagName);
			nodeTextContent = nList.item(0).getTextContent();
			concatLogOutput(tmp, tagName, logSeperator, nodeTextContent);
			if (nodeTextContent.length() > 0)
				preProAiContext.setSqlInsertFile(nodeTextContent);

			log.log(Level.INFO, tmp.toString(), "");

			checkRatioOrLine(neuronalNetworkCogNetContext);

		} catch (Exception e) {
			log.log(Level.SEVERE, "Error readin parameterfile", e.toString());
		}

	}

	/**
	 * The string concatenator for the logger. To log the read parameters.
	 * 
	 * @param tmp
	 * @param tagName
	 * @param logSeperator
	 * @param nodeTextContent
	 * @return Stringbuilder for the output for the logger
	 */
	private StringBuilder concatLogOutput(StringBuilder tmp, String tagName, String logSeperator,
			String nodeTextContent) {

		tmp.append(tagName);
		tmp.append(logSeperator);
		tmp.append(nodeTextContent);
		tmp.append("\r\n");

		return tmp;
	}

	/**
	 * Separates the filename and path from the parameter-file given filenames.
	 * 
	 * @param preProAiContext
	 */
	private void setFilePathAndName(PreProAiContext preProAiContext) {

		Path p = Paths.get(preProAiContext.getFileNameWithAbsolutPath());
		preProAiContext.setFileName(p.getFileName().toString());
		preProAiContext.setDirectory(p.getParent().toString());
	}

	/**
	 * Checks if either lineToPredict or RatioLearnToTest is given.
	 * 
	 * @param neuronalNetworkCogNetContext
	 */
	private void checkRatioOrLine(NeuronalNetworkCogNetContext neuronalNetworkCogNetContext) {

		if (((neuronalNetworkCogNetContext.getLineToPredict().length == 0)
				&& (neuronalNetworkCogNetContext.getRatioLearnToTest() == 0.0))
				|| ((neuronalNetworkCogNetContext.getLineToPredict().length > 0)
						&& (neuronalNetworkCogNetContext.getRatioLearnToTest() > 0.0))) {

			exitProgramm.programmExiter("Either give LinetoPredict or RatioLearnToTest. We stop here.", null);

		}

	}

}
