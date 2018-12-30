package infrastructure;

import java.util.logging.Level;
import java.util.logging.Logger;

import context.PreProAiContext;

public class RemoveUselessColumns {

	private static Logger log = Logger.getLogger(RemoveUselessColumns.class.getName());

	/**
	 * If input data columns are not distinct, they will be quite useless for
	 * training. In the classe the routine removeColumns will exactly remove
	 * these columns.
	 * 
	 * @param preProAiContext
	 */
	public void removeColumns(PreProAiContext preProAiContext) {

		for (int i = 0; i < preProAiContext.getDataColumnsDoDelete().size(); i++) {
			int index = preProAiContext.getDataColumnsDoDelete().get(i);
			preProAiContext.getHeader().remove(index);
			preProAiContext.getData().remove(index);
			preProAiContext.getWhichDataColumnIsNotCategorical().remove(index);

			for (int j = 0; j < preProAiContext.getDataColumnsDoDelete().size(); j++) {
				if (preProAiContext.getDataColumnsDoDelete().get(j) >= index)
					preProAiContext.getDataColumnsDoDelete().set(j, preProAiContext.getDataColumnsDoDelete().get(j));

			}

			showLog("Data Section", index + 1);

		}

		if (!preProAiContext.getResultColumnsDoDelete().isEmpty()) {

			int newNumberOfResultColumns = preProAiContext.getResultColumns().length
					- preProAiContext.getResultColumnsDoDelete().size();

			if (newNumberOfResultColumns == 0) {
				preProAiContext.getExitProgramm().programmExiter(
						"All result columns were removed, because not distinct value was present. We stop here.", null);

			}

			int[] resultColumns = new int[newNumberOfResultColumns];

			for (int i = 0; i < preProAiContext.getResultColumnsDoDelete().size(); i++) {
				int index = preProAiContext.getResultColumnsDoDelete().get(i);
				preProAiContext.getHeaderResultColumns().remove(index);
				preProAiContext.getDataResults().remove(index);
				preProAiContext.getWhichResultColumnIsNotCategorical().remove(index);

				for (int j = 0; j < preProAiContext.getResultColumnsDoDelete().size(); j++) {
					if (preProAiContext.getResultColumnsDoDelete().get(j) >= index)
						preProAiContext.getResultColumnsDoDelete().set(j,
								preProAiContext.getResultColumnsDoDelete().get(j));

				}

				showLog("Result Section", index + 1);
				resultColumns[i] = index;
			}

			preProAiContext.setResultColumns(resultColumns);
		}

	}

	/**
	 * Shows the log of the removeColumns step.
	 * 
	 * @param dataSection
	 * @param columnumber
	 */
	private void showLog(String dataSection, int columnumber) {
		StringBuilder tmpString = new StringBuilder();
		tmpString.append("Column ");
		tmpString.append(columnumber);
		tmpString.append(" of the ");
		tmpString.append(dataSection);
		tmpString.append(" was removed because no distinct values present");
		log.log(Level.INFO, tmpString.toString(), "");
	}

}
