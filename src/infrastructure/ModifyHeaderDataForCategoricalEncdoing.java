package infrastructure;

import java.util.Map;
import java.util.Map.Entry;

import context.PreProAiContext;

/**
 * This class provides a method for modifying the header if a column has stored
 * categorical data, which will then be one-hot encoded.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ModifyHeaderDataForCategoricalEncdoing {

	/**
	 * A method for modifying the header if a column has stored categorical
	 * data, which will be one-hot encoded.
	 * 
	 * @param preProAiContext
	 * @param distinctValue
	 * @param columnNumber
	 * @return A string array holding the updated header entries if a column is
	 *         changed in categorical data (i.e. one-hot encoded)
	 */
	public String[] replaceHeaderEntry(PreProAiContext preProAiContext, Map<String, Integer> distinctValue,
			Integer columnNumber) {

		String[] newHeaderline = new String[distinctValue.size()];

		for (int i = 0; i < newHeaderline.length; i++)
			newHeaderline[i] = preProAiContext.getHeader().get(columnNumber)[0] + " - "
					+ getKeyFromValue(distinctValue, i);

		return newHeaderline;
	}

	/**
	 * The key from a map by providing a value
	 * 
	 * @param theMap
	 * @param value
	 * @return the key for a given value
	 */
	private String getKeyFromValue(Map<String, Integer> theMap, Integer value) {
		for (Entry<String, Integer> o : theMap.entrySet()) {
			if (o.getValue().equals(value)) {
				return o.getKey();
			}
		}
		return null;
	}

}
