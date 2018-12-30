package transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import context.PreProAiContext;

import infrastructure.ExtractDistinctValuesMap;

/**
 * The purpose of the class is the encoding of categorical data for the network.
 * It is a number encoding. That means that each distinct value in a categorical
 * column a natural number will bes assigned.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class CategoricalToNumberEncdoing {

	private static Logger log = Logger.getLogger(CategoricalToNumberEncdoing.class.getName());
	private static ExtractDistinctValuesMap extractDistinctValuesMap = new ExtractDistinctValuesMap();

	/**
	 * This method does the preparation of the categorical to number encoding
	 * 
	 * @param data
	 * @param preProAiContext
	 * @param columnNumber
	 */
	void categoricalToNumberEncoding(List<String[]> data, PreProAiContext preProAiContext, int columnNumber) {

		Map<String, Integer> distinctValue = extractDistinctValuesMap.extractDistinctValues(data);

		preProAiContext.getResultTranslator().put(columnNumber, distinctValue);

		if (distinctValue.size() == 1) {
			preProAiContext.getResultColumnsDoDelete().add(columnNumber);

		} else {

			preProAiContext.getDataResults().set(columnNumber, categoricalToNumberEncoding(data, distinctValue));
		}

	}

	/**
	 * The core of the categorical to number encoding
	 * 
	 * @param data
	 * @param distinctValue
	 * @return
	 */
	private List<String[]> categoricalToNumberEncoding(List<String[]> data, Map<String, Integer> distinctValue) {

		List<String[]> encodedColumn = new ArrayList<>();

		for (int i = 0; i < data.size(); i++) {

			String[] encodedInformation = new String[1];
			for (int j = 0; j < encodedInformation.length; j++)
				encodedInformation[j] = String.valueOf(distinctValue.get(data.get(i)[0]));

			encodedColumn.add(encodedInformation);
		}

		return encodedColumn;
	}

}
