--load the data from S3 and define the schema
raw = LOAD '/user/hadoop/ngrams421.csv' USING PigStorage(',') AS  (word:chararray, year:int, occurences:int);

fltrd = FILTER raw by year >= 1990;

word_group = GROUP fltrd ALL;
results = FOREACH word_group GENERATE COUNT($1);

dump results;




