package filehandling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.opencsv.CSVWriter;

import context.PreProAiContext;

/**
 * After training a neuronal dataset the values used for normalization need to
 * be stored. When using a trained network the values must be read in to do the
 * denormalization. Otherwise the predicted data is always in the range lower,upper bound.
 * 
 * 
 * @author Wolfgang Kapferer
 *
 */

public class WriteResultTranslatorFile {

	private static Logger log = Logger.getLogger(WriteResultTranslatorFile.class.getName());

	public void writeCSVFile(PreProAiContext preProAiContext) {

		StringBuilder csvFilename = new StringBuilder();

		csvFilename.append(preProAiContext.getDirectory());
		csvFilename.append("/");
		csvFilename.append("ResultTranslator_");
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

			String[] denormalizeWith = new String[1];
			denormalizeWith[0] = "To denormalize use";
			writer.writeNext(denormalizeWith);

			denormalizeWith[0] = "x=(max-min)/(upperbound-lowerbound)*x_norm + min - lowerbound(max-min)/(upperbound-lowerbound)";
			writer.writeNext(denormalizeWith);

			String[] upperLowerBoundText = new String[2];
			upperLowerBoundText[0] = "LowerBound";
			upperLowerBoundText[1] = "UpperBound";

			String[] minimumMaximumText = new String[2];
			minimumMaximumText[0] = "Minimum";
			minimumMaximumText[1] = "Maximum";

			String[] upperLowerBoundValue = new String[2];
			upperLowerBoundValue[0] = String.valueOf(PreProAiContext.getLowerbound());
			upperLowerBoundValue[1] = String.valueOf(PreProAiContext.getUpperbound());

			for (Entry<Integer, double[]> entry : preProAiContext.getMinMaxResultColumn().entrySet()) {
				Integer resultColumn = entry.getKey();
				double[] minMax = entry.getValue();

				String[] minimumMaximumValue = new String[2];
				minimumMaximumValue[0] = String.valueOf(minMax[0]);
				minimumMaximumValue[1] = String.valueOf(minMax[1]);

				String[] resultColumnNumber = new String[1];
				resultColumnNumber[0] = String.valueOf("Result Column " + (resultColumn + 1));

				writer.writeNext(resultColumnNumber);
				writer.writeNext(upperLowerBoundText);
				writer.writeNext(upperLowerBoundValue);
				writer.writeNext(minimumMaximumText);
				writer.writeNext(minimumMaximumValue);
			}

			List<String[]> dataTowrite = concatenateData(preProAiContext);
			for (int i = 0; i < dataTowrite.size(); i++)
				writer.writeNext(dataTowrite.get(i));

		}

		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "IOExcpetion", e);
		}

		log.log(Level.INFO, "The result-transloator file is written.");

	}

	List<String[]> concatenateData(PreProAiContext preProAiContext) {

		List<String[]> data = new ArrayList<>();

		for (Entry<Integer, Map<String, Integer>> entry : preProAiContext.getResultTranslator().entrySet()) {

			Integer resultColumnName = entry.getKey();
			Map<String, Integer> translatedValues = entry.getValue();

			for (Entry<String, Integer> entry1 : translatedValues.entrySet()) {
				String valueSource = entry1.getKey();
				int valueTarget = entry1.getValue();

				String[] line = new String[1];

				StringBuilder tmpString = new StringBuilder();
				tmpString.append(resultColumnName);
				tmpString.append("->");
				tmpString.append(valueSource);
				tmpString.append("->");
				tmpString.append(valueTarget);

				line[0] = tmpString.toString();

				data.add(line);
			}

		}

		return data;
	}

}
