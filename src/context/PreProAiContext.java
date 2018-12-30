package context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import infrastructure.ExitProgramm;

/**
 * 
 * The Context class for preparing input-data for a neuronal-network. In
 * addition the program-exit routine is accessible through it.
 * 
 * @author Wolfgang Kapferer
 *
 */
public class PreProAiContext {

	private static ExitProgramm exitProgramm = new ExitProgramm();
	private char decimalpoint;
	// the bounds for normalization
	private static final double LOWERBOUND = 0;
	private static final double UPPERBOUND = 1;
	private static final String TRAINMODE = "TRAIN";
	private static final String APPLYMODE = "APPLY";
	// to handle NULL values from database in the same way, as all other
	// technically or logical null Values
	private static final String NULLVALUEDB = "%123$§§$321%";
	private String parameterFile;
	private String sqlInsertFile;
	private String dBInfoFile;
	private String fileNameWithAbsolutePath;
	private String fileName;
	private String directory;
	private String resultTranslatorFile;
	private boolean headerline;
	// the raw data from the database for learning and prediction
	private List<List<String[]>> data = new ArrayList<>();
	private List<List<String[]>> dataTrain = new ArrayList<>();
	private List<List<String[]>> dataResult = new ArrayList<>();
	private List<List<String[]>> dataResultTrain = new ArrayList<>();
	// for storing the prediction in the database
	private List<String[]> dataPrimaryKey = new ArrayList<>();
	private int[] primaryKeyArray;
	// Maps for analysis of the data and the header information
	private Map<Integer, String[]> header = new TreeMap<>();
	private Map<Integer, String[]> headerResultColumns = new TreeMap<>();
	private Map<Integer, Boolean> whichDataColumnIsNotCategorical = new TreeMap<>();
	private Map<Integer, Boolean> whichResultColumnIsNotCategorical = new TreeMap<>();
	private int[] resultColumns;
	// logical or technical null values
	private String[] nullValues;
	private int[] categoricalColumns;
	private List<Integer> dataColumnsDoDelete = new ArrayList<>();
	private List<Integer> resultColumnsDoDelete = new ArrayList<>();
	private Map<Integer, Map<String, Integer>> resultTranslator = new TreeMap<>();
	private double amountTrainTestData;
	private boolean nullIndicatorColumns = false;
	private Map<Integer, double[]> minMaxResultColumn = new TreeMap<>();
	private Map<Integer, double[]> minMaxNormalizedResultColumn = new TreeMap<>();
	private List<Integer> columnDatatype = new ArrayList<>();
	private String connectionString;
	private String user;
	private String passwd;
	private String schema;
	private String sqlString;
	private String insertSqlString;
	private Connection connection;
	private String trainOrApplyMode;
	private String trainedNetFile;
	private String filnameForTraineNetworkToSave;
	private boolean writeIntoDatabase = false;
	private boolean writePreparedData = false;
	private boolean onlyPrepare = false;

	public Map<Integer, Map<String, Integer>> getResultTranslator() {
		return resultTranslator;
	}

	public void setResultTranslator(Map<Integer, Map<String, Integer>> resultTranslator) {
		this.resultTranslator = resultTranslator;
	}

	public List<Integer> getDataColumnsDoDelete() {
		return dataColumnsDoDelete;
	}

	public void setDataColumnsDoDelete(List<Integer> dataColumnsDoDelete) {
		this.dataColumnsDoDelete = dataColumnsDoDelete;
	}

	public List<Integer> getResultColumnsDoDelete() {
		return resultColumnsDoDelete;
	}

	public void setResultColumnsDoDelete(List<Integer> resultColumnsDoDelete) {
		this.resultColumnsDoDelete = resultColumnsDoDelete;
	}

	public List<List<String[]>> getDataResults() {
		return dataResult;
	}

	public void setDataResult(List<List<String[]>> dataResult) {
		this.dataResult = dataResult;
	}

	public Map<Integer, String[]> getHeaderResultColumns() {
		return headerResultColumns;
	}

	public void setHeaderResultColumns(Map<Integer, String[]> headerResultColumns) {
		this.headerResultColumns = headerResultColumns;
	}

	public static double getLowerbound() {
		return LOWERBOUND;
	}

	public static double getUpperbound() {
		return UPPERBOUND;
	}

	public Map<Integer, Boolean> getWhichResultColumnIsNotCategorical() {
		return whichResultColumnIsNotCategorical;
	}

	public void setWhichResultColumnIsNotCategorical(Map<Integer, Boolean> whichResultColumnIsNotCategorical) {
		this.whichResultColumnIsNotCategorical = whichResultColumnIsNotCategorical;
	}

	public Map<Integer, Boolean> getWhichDataColumnIsNotCategorical() {
		return whichDataColumnIsNotCategorical;
	}

	public void setWhichDataColumnIsNotCategorical(Map<Integer, Boolean> whichDataColumnIsNotCategorical) {
		this.whichDataColumnIsNotCategorical = whichDataColumnIsNotCategorical;
	}

	public Map<Integer, String[]> getHeader() {
		return header;
	}

	public void setHeader(Map<Integer, String[]> header) {
		this.header = header;
	}

	public List<List<String[]>> getData() {
		return data;
	}

	public void setData(List<List<String[]>> data) {
		this.data = data;
	}

	public String getFileNameWithAbsolutPath() {
		return fileNameWithAbsolutePath;
	}

	public void setFileNameWithAbsolutPath(String fileNameWithAbsolutPath) {
		this.fileNameWithAbsolutePath = fileNameWithAbsolutPath;
	}

	public ExitProgramm getExitProgramm() {
		return exitProgramm;
	}

	public int[] getResultColumns() {
		return resultColumns;
	}

	public void setResultColumns(int[] resultColums) {
		this.resultColumns = resultColums;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public double getAmountTrainTestData() {
		return amountTrainTestData;
	}

	public void setAmountTrainTestData(double amountTrainTestData) {
		this.amountTrainTestData = amountTrainTestData;
	}

	public List<List<String[]>> getdataTrain() {
		return dataTrain;
	}

	public void setDataTrain(List<List<String[]>> dataTrain) {
		this.dataTrain = dataTrain;
	}

	public List<List<String[]>> getDataResultTrain() {
		return dataResultTrain;
	}

	public void setDataResultTrain(List<List<String[]>> dataResultTrain) {
		this.dataResultTrain = dataResultTrain;
	}

	public String getParameterFile() {
		return parameterFile;
	}

	public void setParameterFile(String parameterFile) {
		this.parameterFile = parameterFile;
	}

	public boolean isHeaderline() {
		return headerline;
	}

	public void setHeaderline(boolean headerline) {
		this.headerline = headerline;
	}

	public String[] getNullValues() {
		return nullValues;
	}

	public void setNullValues(String[] nullValues) {
		this.nullValues = nullValues;
	}

	public char getDecimalpoint() {
		return decimalpoint;
	}

	public void setDecimalpoint(char decimalpoint) {
		this.decimalpoint = decimalpoint;
	}

	public boolean isNullIndicatorColumns() {
		return nullIndicatorColumns;
	}

	public void setNullIndicatorColumns(boolean nullIndicatorColumns) {
		this.nullIndicatorColumns = nullIndicatorColumns;
	}

	public int[] getCategoricalColumns() {
		return categoricalColumns;
	}

	public void setCategoricalColumns(int[] categoricalColumns) {
		this.categoricalColumns = categoricalColumns;
	}

	public Map<Integer, double[]> getMinMaxResultColumn() {
		return minMaxResultColumn;
	}

	public void setMinMaxResultColumn(Map<Integer, double[]> minMaxResultColumn) {
		this.minMaxResultColumn = minMaxResultColumn;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getdBInfoFile() {
		return dBInfoFile;
	}

	public void setdBInfoFile(String dBInfoFile) {
		this.dBInfoFile = dBInfoFile;
	}

	public String getSqlString() {
		return sqlString;
	}

	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public List<Integer> getColumnDatatype() {
		return columnDatatype;
	}

	public void setColumnDatatype(List<Integer> columnDatatype) {
		this.columnDatatype = columnDatatype;
	}

	public Map<Integer, double[]> getMinMaxNormalizedResultColumn() {
		return minMaxNormalizedResultColumn;
	}

	public void setMinMaxNormalizedResultColumn(Map<Integer, double[]> minMaxNormalizedResultColumn) {
		this.minMaxNormalizedResultColumn = minMaxNormalizedResultColumn;
	}

	public String getTrainOrApplyMode() {
		return trainOrApplyMode;
	}

	public void setTrainOrApplyMode(String trainOrApplyMode) {
		this.trainOrApplyMode = trainOrApplyMode;
	}

	public String getTrainedNetFile() {
		return trainedNetFile;
	}

	public void setTrainedNetFile(String trainedNetFile) {
		this.trainedNetFile = trainedNetFile;
	}

	public static String getTrainmode() {
		return TRAINMODE;
	}

	public static String getApplymode() {
		return APPLYMODE;
	}

	public String getFilnameForTraineNetworkToSave() {
		return filnameForTraineNetworkToSave;
	}

	public void setFilnameForTraineNetworkToSave(String filnameForTraineNetworkToSave) {
		this.filnameForTraineNetworkToSave = filnameForTraineNetworkToSave;
	}

	public List<String[]> getDataPrimaryKey() {
		return dataPrimaryKey;
	}

	public void setDataPrimaryKey(List<String[]> dataPrimaryKey) {
		this.dataPrimaryKey = dataPrimaryKey;
	}

	public int[] getPrimaryKeyArray() {
		return primaryKeyArray;
	}

	public void setPrimaryKeyArray(int[] primaryKeyArray) {
		this.primaryKeyArray = primaryKeyArray;
	}

	public boolean isWriteIntoDatabase() {
		return writeIntoDatabase;
	}

	public void setWriteIntoDatabase(boolean writeIntoDatabase) {
		this.writeIntoDatabase = writeIntoDatabase;
	}

	public String getResultTranslatorFile() {
		return resultTranslatorFile;
	}

	public void setResultTranslatorFile(String resultTranslatorFile) {
		this.resultTranslatorFile = resultTranslatorFile;
	}

	public static String getNullvaluedb() {
		return NULLVALUEDB;
	}

	public boolean isWritePreparedData() {
		return writePreparedData;
	}

	public void setWritePreparedData(boolean writePreparedData) {
		this.writePreparedData = writePreparedData;
	}

	public boolean isOnlyPrepare() {
		return onlyPrepare;
	}

	public void setOnlyPrepare(boolean onlyPrepare) {
		this.onlyPrepare = onlyPrepare;
	}

	public String getSqlInsertFile() {
		return sqlInsertFile;
	}

	public void setSqlInsertFile(String sqlInsertFile) {
		this.sqlInsertFile = sqlInsertFile;
	}

	public String getInsertSqlString() {
		return insertSqlString;
	}

	public void setInsertSqlString(String insertSqlString) {
		this.insertSqlString = insertSqlString;
	}

}
