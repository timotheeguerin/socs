--load the data from S3 and define the schema
raw = LOAD 's3n://googleNgramsSample/ngrams421.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);

