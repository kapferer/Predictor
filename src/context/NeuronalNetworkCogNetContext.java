package context;

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;

import infrastructure.ExitProgramm;

/**
 * 
 * The Context-Class for the neuronal network with all parameters for training
 * and using a neuronal network with ECNOG, with all getters and setters. In addition 
 * the program-exit routine is accessible through it.  
 *  
 * 
 * @author Wolfgang Kapferer
 *
 */
public class NeuronalNetworkCogNetContext {

	private static ExitProgramm exitProgramm = new ExitProgramm();
	//The 2D arrays holding all the normalized data for prediction and training
	private double[][] data;
	private double[][] dataTrainResult;
	private double[][] dataToPredict;
	private double[][] dataToTestOriginal;
	private double[][] prediction;
	//the list of PKs for storing back the results into the database
	private List<String[]> primaryKeys = new ArrayList<>();
	//holds the number of the result Columns
	private int[] resultColums;
	private String parameterFile;
	private String fileNameWithAbsolutePath;
	private String fileNameTranslatorFileWithAbsolutePath;
	private String fileName;
	private String directory;
	private boolean headerline;
	//Important to checking the data
	private char decimalpoint = '.';
	private char seperator = ',';
	private int[] layerTopology;
	private BasicNetwork network;
	private MLDataSet trainingSet;
	//the maximum error at which the training will stopp
	private double maxError;
	//the maximum number of iterations in the backpropagation at which the training will stop
	private int maxIteration;
	//classical limiter
	private static final double EPSILON = 1.0E-10;
	//the array holding the linenumbers of the result set for prediction
	private int[] lineToPredict;
	private double ratioTestToTrain = -1;
	private String rPROPorLMA;
	//the trainingmode RPROP or LMA (i.e. Resilient Backpropagation or Levenberg-Marquandt optimum search
	private static final String TRAINMODE_RPROP = "RPROP";
	private static final String TRAINMODE_LMA = "LMA";

	//now the typical getters and setters
	public double[][] getData() {
		return data;
	}

	public void setData(double[][] csvData) {
		this.data = csvData;
	}

	public double[][] getDataToPredict() {
		return dataToPredict;
	}

	public void setDataToPredict(double[][] csvDataTrain) {
		this.dataToPredict = csvDataTrain;
	}

	public String getParameterFile() {
		return parameterFile;
	}

	public void setParameterFile(String parameterFile) {
		this.parameterFile = parameterFile;
	}

	public String getFileNameWithAbsolutePath() {
		return fileNameWithAbsolutePath;
	}

	public void setFileNameWithAbsolutePath(String fileNameWithAbsolutePath) {
		this.fileNameWithAbsolutePath = fileNameWithAbsolutePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean isHeaderline() {
		return headerline;
	}

	public void setHeaderline(boolean headerline) {
		this.headerline = headerline;
	}

	public char getDecimalpoint() {
		return decimalpoint;
	}

	public void setDecimalpoint(char decimalpoint) {
		this.decimalpoint = decimalpoint;
	}

	public char getSeperator() {
		return seperator;
	}

	public void setSeperator(char seperator) {
		this.seperator = seperator;
	}

	public int[] getResultColums() {
		return resultColums;
	}

	public void setResultColums(int[] resultColums) {
		this.resultColums = resultColums;
	}

	public ExitProgramm getExitProgramm() {
		return exitProgramm;
	}

	public void setExitProgramm(ExitProgramm exitProgramm) {
		this.exitProgramm = exitProgramm;
	}

	public int[] getLayerTopology() {
		return layerTopology;
	}

	public void setLayerTopology(int[] layerTopology) {
		this.layerTopology = layerTopology;
	}

	public double[][] getDataTrainResult() {
		return dataTrainResult;
	}

	public void setDataTrainResult(double[][] csvTrain) {
		this.dataTrainResult = csvTrain;
	}

	public BasicNetwork getNetwork() {
		return network;
	}

	public void setNetwork(BasicNetwork network) {
		this.network = network;
	}

	public MLDataSet getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(MLDataSet trainingSet) {
		this.trainingSet = trainingSet;
	}

	public String getFileNameTranslatorFileWithAbsolutePath() {
		return fileNameTranslatorFileWithAbsolutePath;
	}

	public void setFileNameTranslatorFileWithAbsolutePath(String fileNameTranslatorFileWithAbsolutePath) {
		this.fileNameTranslatorFileWithAbsolutePath = fileNameTranslatorFileWithAbsolutePath;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public int[] getLineToPredict() {
		return lineToPredict;
	}

	public void setLineToPredict(int[] lineToPredict) {
		this.lineToPredict = lineToPredict;
	}

	public static double getEpsilon() {
		return EPSILON;
	}

	public double[][] getPrediction() {
		return prediction;
	}

	public void setPrediction(double[][] prediction) {
		this.prediction = prediction;
	}

	public double[][] getDataToTestOriginal() {
		return dataToTestOriginal;
	}

	public void setDataToTestOriginal(double[][] csvDataToTestOriginal) {
		this.dataToTestOriginal = csvDataToTestOriginal;
	}

	public double getRatioLearnToTest() {
		return ratioTestToTrain;
	}

	public void setRatioLearnToTest(double ratioLearnToTest) {
		this.ratioTestToTrain = ratioLearnToTest;
	}

	public int getMaxIteration() {
		return maxIteration;
	}

	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	public String getrPROPorLMA() {
		return rPROPorLMA;
	}

	public void setrPROPorLMA(String rPROPorLMA) {
		this.rPROPorLMA = rPROPorLMA;
	}

	public static String getTrainmodeRprop() {
		return TRAINMODE_RPROP;
	}

	public static String getTrainmodeLma() {
		return TRAINMODE_LMA;
	}

	public List<String[]> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<String[]> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

}
