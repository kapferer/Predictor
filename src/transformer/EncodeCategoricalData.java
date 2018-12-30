package transformer;

import java.util.Map.Entry;

import context.PreProAiContext;

import filehandling.AnalyzeResultSetFile;

/**
 * This class provides methods for the actual encoding, either it is one-hot
 * encoding or categorical to numerical encoding.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class EncodeCategoricalData {

	private static OneHotEncoding oneHotEncoding = new OneHotEncoding();
	private static CategoricalToNumberEncdoing categoricalToNumberEncdoing = new CategoricalToNumberEncdoing();
	private static AnalyzeResultSetFile analyzeCSVFile = new AnalyzeResultSetFile();

	/**
	 * This methods checks if a columns is categorical or not and does either
	 * one-hot or numerical encoding. If it is a input layer it does one-hot, if
	 * a result column is given it does a numerical encoding.
	 * 
	 * @param preProAiContext
	 */
	public void encodeCategoricalData(PreProAiContext preProAiContext) {

		for (Entry<Integer, Boolean> entry : preProAiContext.getWhichDataColumnIsNotCategorical().entrySet()) {
			Boolean value = entry.getValue();
			Integer key = entry.getKey();

			if (!value) {

				// TODO if needed a special date encoding could be implemented
				// System.out.println(analyzeCSVFile.isDateColumn(preProAiContext.getCsvData().get(key),
				// preProAiContext.getNullValues(), "YYYY-MM-DD"));

				// --DATUM in Monat und Tag splitten array einfach add @
				// position, dann wie eine categorical behandeln
				oneHotEncoding.makeOneHotEncoding(preProAiContext.getData().get(key), preProAiContext, key);

			}
		}

		for (Entry<Integer, Boolean> entry : preProAiContext.getWhichResultColumnIsNotCategorical().entrySet()) {
			Boolean value = entry.getValue();
			Integer key = entry.getKey();

			if (!value) {

				// TODO if needed a special date encoding could be implemented
				// System.out.println(analyzeCSVFile.isDateColumn(preProAiContext.getCsvDataResults().get(key),
				// preProAiContext.getNullValues(), "YYYY-MM-DD"));

				// --DATUM in Monat und Tag splitten array einfach add @
				// position, dann wie eine categorical behandeln

				categoricalToNumberEncdoing.categoricalToNumberEncoding(preProAiContext.getDataResults().get(key),
						preProAiContext, key);

			}
		}

	}

}
