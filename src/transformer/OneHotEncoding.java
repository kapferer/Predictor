package transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import context.PreProAiContext;

import infrastructure.ExtractDistinctValuesMap;
import infrastructure.ModifyHeaderDataForCategoricalEncdoing;

/**
 * The class for ONE-HOT encoding, providing the core method and the caller for
 * a column
 * 
 * @author Wolfgang Kapferer
 *
 */
public class OneHotEncoding {

	private static ExtractDistinctValuesMap extractDistinctValuesMap = new ExtractDistinctValuesMap();
	private static ModifyHeaderDataForCategoricalEncdoing modifyHeaderDataForCategoricalOneHotEncdoing = new ModifyHeaderDataForCategoricalEncdoing();

	/**
	 * This methods applies the ONE-HOT encoding on a given column, given by the
	 * number of the column
	 * 
	 * @param data
	 * @param preProAiContext
	 * @param columnNumber
	 */
	public void makeOneHotEncoding(List<String[]> data, PreProAiContext preProAiContext, int columnNumber) {

		Map<String, Integer> distinctValue = extractDistinctValuesMap.extractDistinctValues(data);

		if (distinctValue.size() == 1) {
			preProAiContext.getDataColumnsDoDelete().add(columnNumber);
		} else {
			preProAiContext.getHeader().replace(columnNumber, modifyHeaderDataForCategoricalOneHotEncdoing
					.replaceHeaderEntry(preProAiContext, distinctValue, columnNumber));
			preProAiContext.getData().set(columnNumber, hotOneEncodeColumn(data, distinctValue));
		}

	}

	/**
	 * The actual ONE-HOT encoding core function.
	 * 
	 * @param data
	 * @param distinctValue
	 * @return the ONE-HOT encoded column, i.e. a List of String arrays holding
	 *         the ones an zeros
	 */
	private List<String[]> hotOneEncodeColumn(List<String[]> data, Map<String, Integer> distinctValue) {

		List<String[]> encodedColumn = new ArrayList<>();

		for (int i = 0; i < data.size(); i++) {

			String[] encodedInformation = new String[distinctValue.size()];
			for (int j = 0; j < encodedInformation.length; j++)
				encodedInformation[j] = "0";

			encodedInformation[distinctValue.get(data.get(i)[0])] = "1";
			encodedColumn.add(encodedInformation);
		}

		return encodedColumn;
	}

}
