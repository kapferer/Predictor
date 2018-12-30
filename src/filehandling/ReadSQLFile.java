package filehandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import context.PreProAiContext;

/**
 * This class reads the SQL file for the declaration of the input-data to either
 * train or predict. It is just a simple text-file with the SQL in it.
 * 
 * @author kapf
 *
 */
public class ReadSQLFile {

	private static Logger log = Logger.getLogger(ReadSQLFile.class.getName());

	public void readSQLSelectFile(PreProAiContext preProAiContext) {

		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(preProAiContext.getFileNameWithAbsolutPath()));
		} catch (FileNotFoundException e2) {
			log.log(Level.SEVERE, "File not found", e2);
		}

		try {

			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "reading SQL File problem", e1);
			}

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				try {
					line = br.readLine();
				} catch (IOException e) {
					log.log(Level.SEVERE, "reading SQL File problem", e);
				}
			}

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Problem closing SQLFile", e);
			}
		}

		preProAiContext.setSqlString(sb.toString());

		log.log(Level.INFO, "The SQL file is successfully read in.");

	}

	/**
	 * Reads a insertSQL file for the result storage in the database
	 * 
	 * @param preProAiContext
	 */
	public void readInsertSQLFile(PreProAiContext preProAiContext) {

		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(preProAiContext.getSqlInsertFile()));
		} catch (FileNotFoundException e2) {
			log.log(Level.SEVERE, "File not found", e2);
		}

		try {

			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "reading SQL File problem", e1);
			}

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				try {
					line = br.readLine();
				} catch (IOException e) {
					log.log(Level.SEVERE, "reading SQL File problem", e);
				}
			}

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Problem closing SQLFile", e);
			}
		}

		preProAiContext.setInsertSqlString(sb.toString());
		
		log.log(Level.INFO, "The SQL file is successfully read in.");
	}

}
