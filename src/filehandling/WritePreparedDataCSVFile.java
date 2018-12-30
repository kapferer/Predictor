package filehandling;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVWriter;

import context.PreProAiContext;

/**
 * This class provides methods to write the prepared data into a CSV file. This
 * is important, if another neuronal-network allication is needed.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class WritePreparedDataCSVFile {

	private static Logger log = Logger.getLogger(WritePreparedDataCSVFile.class.getName());

	/**
	 * This methods writes the prepared data into a CSV file.
	 * 
	 * @param preProAiContext
	 */
	public void writeCSVFile(PreProAiContext preProAiContext) {

		StringBuilder csvFilename = new StringBuilder();

		csvFilename.append(preProAiContext.getDirectory());
		csvFilename.append("/");

		if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getApplymode())) {
			csvFilename.append("PREPARED_");
		} else {
			csvFilename.append("PREPARED_TRAIN_");
		}
		csvFilename.append(preProAiContext.getFileName());
		csvFilename.append(".csv");

		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(csvFilename.toString()), CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER);
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOExcpetion", e);
		}

		if (writer != null) {

			writer.writeNext(concatenateHeaderlines(preProAiContext));

			List<String[]> dataTowrite = null;
			if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getApplymode())) {
				dataTowrite = concatenateData(preProAiContext);
			} else {
				dataTowrite = concatenateDataTrain(preProAiContext);
			}

			if (!dataTowrite.isEmpty()) {
				for (int i = 0; i < dataTowrite.size(); i++)
					writer.writeNext(dataTowrite.get(i));
			}

		}

		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOExcpetion", e);
		}

		if (preProAiContext.getTrainOrApplyMode().equals(PreProAiContext.getApplymode())) {
			log.log(Level.INFO, "The prepared input data is written.", "");
		} else {
			log.log(Level.INFO, "The prepared input data with train columns is written.", "");
		}

	}

	/**
	 * The methods prepares the headerline
	 * 
	 * @param preProAiContext
	 * @return a string array holding the headerline to write
	 */
	private String[] concatenateHeaderlines(PreProAiContext preProAiContext) {

		int numberOfArrayElements = 0;

		for (Entry<Integer, String[]> entry : preProAiContext.getHeader().entrySet()) {
			String[] value = entry.getValue();
			numberOfArrayElements += value.length;
		}

		for (Entry<Integer, String[]> entry : preProAiContext.getHeaderResultColumns().entrySet()) {
			String[] value = entry.getValue();
			numberOfArrayElements += value.length;
		}

		String[] newHeaderline = new String[numberOfArrayElements];

		int i = 0;
		for (Entry<Integer, String[]> entry : preProAiContext.getHeader().entrySet()) {
			String[] value = entry.getValue();
			for (int j = 0; j < value.length; j++) {
				newHeaderline[i] = value[j];
				i++;
			}
		}

		for (Entry<Integer, String[]> entry : preProAiContext.getHeaderResultColumns().entrySet()) {
			String[] value = entry.getValue();
			for (int j = 0; j < value.length; j++) {
				newHeaderline[i] = value[j];
				i++;
			}
		}

		return newHeaderline;
	}

	/**
	 * This data concatenates the data for the input of a neuronal network
	 * 
	 * @param preProAiContext
	 * @return List of string arrays holding the prepared input data
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
	 * This data concatenates the data for the input and train data of a
	 * neuronal-network
	 * 
	 * @param preProAiContext
	 * @return List of string arrays holding the prepared input and result data
	 */
	private List<String[]> concatenateDataTrain(PreProAiContext preProAiContext) {

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

}
