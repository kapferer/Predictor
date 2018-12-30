package infrastructure;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This classe provides a method to extract distinct values from a list of
 * string arrays
 * 
 * @author Wolfgang Kapferer
 *
 */
public class ExtractDistinctValuesMap {

	/**
	 * a method to extract distinct values from a list of string arrays
	 * 
	 * @param data
	 * @return A Map providing the distinct values
	 */
	public Map<String, Integer> extractDistinctValues(List<String[]> data) {

		Map<String, Integer> distinctValues = new TreeMap<>();

		for (int i = 0; i < data.size(); i++) {
			distinctValues.put(data.get(i)[0], 0);
		}

		int i = 0;
		for (Map.Entry<String, Integer> entry : distinctValues.entrySet()) {
			String key = entry.getKey();
			distinctValues.replace(key, i);
			i++;
		}

		return distinctValues;
	}

}
