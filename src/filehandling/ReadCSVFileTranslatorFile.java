package filehandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.opencsv.CSVReader;

import context.PreProAiContext;

/**
 * This class reads in a result-translator file. In it all information about minima,
 * maxima and lower, upper bounds of result columns for already trained data are
 * stored. In the predicition (APPLY) - mode these numbers will be read in and applied to
 * the predicted data, in order to get denormalized values.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ReadCSVFileTranslatorFile {

	private static Logger log = Logger.getLogger(ReadCSVFileTranslatorFile.class.getName());

	public void readTranslatorFile(PreProAiContext preProAiContext) {

		List<String[]> readLines = null;
		CSVReader reader = null;

		try {
			reader = new CSVReader(new FileReader(preProAiContext.getResultTranslatorFile()));
		} catch (FileNotFoundException e1) {
			log.log(Level.SEVERE, "File Not Found", e1);
		}

		try {
			readLines = reader.readAll();
		} catch (IOException e) {
			log.log(Level.SEVERE, "IO Problem at translatort file", e);
		}

		try {
			reader.close();
		} catch (IOException e) {
			log.log(Level.SEVERE, "IO Problem at translatort file closing", e);
		}

		int resultColumn = 0;

		if (readLines != null) {
			for (int i = 0; i < readLines.size(); i++) {
				for (int j = 0; j < readLines.get(i).length; j++) {

					String tmp = readLines.get(i)[j];

					if (tmp.equals("LowerBound")) {
						double[] lowerUpperBound = new double[2];
						lowerUpperBound[0] = Double.valueOf(readLines.get(i + 1)[0]);
						lowerUpperBound[1] = Double.valueOf(readLines.get(i + 1)[1]);

						preProAiContext.getMinMaxNormalizedResultColumn().put(resultColumn, lowerUpperBound);
					}

					if (tmp.equals("Minimum")) {
						double[] minimumMaximum = new double[2];
						minimumMaximum[0] = Double.valueOf(readLines.get(i + 1)[0]);
						minimumMaximum[1] = Double.valueOf(readLines.get(i + 1)[1]);

						preProAiContext.getMinMaxResultColumn().put(resultColumn, minimumMaximum);

						resultColumn++;
					}
				}
			}
		}else{
			log.log(Level.SEVERE, "No information in the given result translator file present");
			preProAiContext.getExitProgramm();
		}

	}

}
