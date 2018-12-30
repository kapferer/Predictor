## This is Predictor v0.6

An easy to use tool for applying **neuronal-networks on data from databases**. The
tool prepares, trains, predicts and stores the data in the database. This is
done with ENCOG (http://www.heatonresearch.com/encog/) Heaton, Jeff. "Encog:
Library of Interchangeable Machine Learning Models for Java and C#." Journal
of Machine Learning Research 16 (2015): 1243-47. Print.

In oder to use it, you need to download the ENCOG workbench https://github.com/encog/encog-java-core/releases/download/v3.4/encog-workbench-3.4.0-all.jar, the Apache Commons Lang https://commons.apache.org/proper/commons-lang3/download_lang3.cgi, opencvs https://sourceforge.net/projects/opencsv/files/opencsv/ and the Exasol JDBC Driver from https://www.exasol.com/portal/

# Example
In the example folder a link and a datamodel for a credit card default prediction from https://archive.ics.uci.edu/ml/datasets/default+of+credit+card+clients is provided, togheter with an read and store result SQL-File and the corresponding parameter file.

# Important steps

- Edit ConnectToDatabase.java to give information about the right database
driver. In this version an EXASOL database ist used

- Edit the DatabaseInformation.xml file to give user, password, database
connections and schema information.

- Edit the PredictorParameterFile.xml file to give important information on
the input-data and training-parameter.

- Provide a SQL File with the SQL for the input-data.

- Edit InsertIntoDatabase.java for the storage of the results into your
database, if needed.

# Start-arguments for Predictor

In order to start Predictor 2 arguemnts are needed. First the Predictor
Parameter XML-File and second the database-information XML-File. * e.g. java
-jar Predictor.jar PredictorParameterFile.xml DatabaseInformation.xml.

# The XML-Parameterfile for Predictor

A detailed description for the Predictor parameter XML-File.


- The header-tag <b><?xml version="1.0" encoding="UTF-8"
standalone="yes"?></b>.

- It is a predictor parameter input file <b><predictor_input_file></b>.

- The filename of the SQL for the input data to the neuronal-net
<b><filenameSQL>FULL PATH AND FILENAME</filenameSQL></b>.

- These are the result columns for neuronal-net. Use [,] as separator if more
result columns are needed e.g. <b><resultcolumns>10,11</resultcolumns></b>.

- The decimal point is <b><decimalpoint>,</decimalpoint></b>.

- Null values in the data are highlighted with the following String(s). Use
[,] separator if more needed] <b><nullValues>?</nullValues></b>.

- ATTENTION: This is not the null of the database. If you have for example a
column of jobs and you know that for example plumber means no valid job
attribute, then plumber is your NULL-value in this column. If the column is
numeric, the mean will replace the NULL-value. In the case of non-numerical
data the next parameter is important.

- If needed a NULL-Indicator column. An additional column, indicating that
the value is a NULL <b><nullIndicatorColumn>0</nullIndicatorColumn></b>.


- Indicates which columns are categorical ant will therefore be one-hot
encoded e.g. <b><categoricalColumns>2,3</categoricalColumns></b>. Predictor
searches by itself for categorical columns, but if you want to overrule this,
e.g. a column with numerical values indicating some attribute (e.g. 0->yes,
1->no). See <a href="https://de.wikipedia.org/wiki/1-aus-n-Code">this
link</a> for one-hot encoding.


- If you want to store your results in the database, Predictor needs to know
which column(s) are the primary key. Use [,] as separator, if more than one
column is primary key e.g. <b><primaryKeyColumns>1</primaryKeyColumns></b>.
You can then use the array neuronalNetworkCogNetContext.getPrimaryKeys()
[Datatype String[]] in the routine inserPredictionIntoDatabase of the Class
InsertIntoDatabase for storing the data in your database.

- How many Layers and how many perceptons should be used. The layers are
separated by [,] e.g. 2 Layer with first 20 percpetons and 6 in the second
layer <b><neuronsInLayer>20,6</neuronsInLayer></b>.


- When should the training stop regarding the Error
<b><MaxError>1.0E-6</MaxError></b>.


- If you want to predict a certain line from your input-data you can indicate
them here. Use [,] to separate more line numbers. If you want to predict the
first line, alle other lines are used for training then
<b><lineToPredict>1</lineToPredict></b>.

- If you want to use a certain amount of the data to train and to test, then
give here a value in percentage of total data. For example 80% to train and
20% for test <b><ratioTestToTrain>20</ratioTestToTrain></b>.


- If you have already trained a net and you just want to apply it, than set
the parameter to APPLY <b><trainOrApplyMode>TRAIN</trainOrApplyMode></b>.


- The dull path and filename of the file inwhich the trained net will be
stored or will be read in the APPLY mode <b><trainedNetFile>FULL PATH AND
FILENAME OF THE TRAINED NET</trainedNetFile></b>.


- At this max iteration number the network training will stop regardless of
the error <b><maxIteration>1000</maxIteration></b>.

- Training either with Resilient Backpropagation or Levenberg-Marquandt
Optimum search <b><RPROPorLMA>RPROP</RPROPorLMA></b>.

- The Result-Translator File for Predictor with the normalization information
and the mapping onto the input data. THis file will be generatoed in the
training mode of Predictor <b> <resultTranslatorFile>FULL PATH AND FILENAME
OF THE TRANSLATOR FILE</resultTranslatorFile></b>.

- The switch <b><writeIntoDatabase>0</writeIntoDatabase></b> tells Predictor
if you want to write the prediction back into the database. [0 - no write
back, 1- write back into database]. The SQL for the storage has to be edited
in the Class InsertIntoDatabase, in the method inserPredictionIntoDatabase.

- The switch <b><onlyPrepareData>0</onlyPrepareData></b> controls if
Predictor does only data preparation or additional training an or prediction,
[0 - only preparation, 1- preparation with encog part].

- The switch <b><writePrepareDataIntoCSV>0</writePrepareDataIntoCSV></b>
tells Predictor to write the prepared data into a CSV file [0 - no CSV File,
1- CSV File]. The name is "PREPARED_[TRAIN]_-filenameSQL-.csv".

- The switch <b><fileNameInsertSqlFULL PATH AND FILENAME</fileNameInsertSql></b>
tells Predictor the file , in which the result SQL is stored. Such a file needs to
have information about how many keys and results you want to store per prediction.
E.G.
INSERT INTO DATEN.RESULT (ID, PREDICTION) VALUES(?,?,?);
KEY,1,2
PREDICTION,1 

- <b></predictor_input_file></b> indicates the end of a Predictor
parameter-file

# The Database XML-File for Predictor

In this file basic database-information is stored.

- The header-tag <b><?xml version="1.0" encoding="UTF-8"
standalone="yes"?></b>

- It is a predictor database XML-File <b><database_predictor_input_file></b>

- The connection URL e.g.
<b><connectionURL>192.168.202.129:8563</connectionURL></b>

- The user name <b><user>USER</user></b>

- The password <b><passwd>nice_to_know</passwd></b>

- The default Schema <b><schema>DATEN</schema></b>

- <b></database_predictor_input_file></b> indicates the end of a database
XML-File


#  Tips and Hints

- If you want to have another activation function then Sigmoid (standard in
Predictor) just edit the Class SetUpNeuronalNet.class. Information about
ENCOG can be found <a href="http://www.heatonresearch.com/encog/">here</a>.

- Typical a good point to start is a net whith one layer and a number of
percepton in the in the order of 2/3 of the input data vector.

- Start with Resilient-Backpropagation Training (RPROP) and see how the error
evolves.

- If the error does not get smaller, alter the number of neurons (go to less
perceptons)

- If the error stays at high values, think about your input-data. Is there
structure in it? Maybe a visual inspection (e.g. pair plots)



@author Wolfgang Kapferer 2018


