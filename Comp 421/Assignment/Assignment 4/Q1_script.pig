--load the data from S3 and define the schema
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);

-- filter per percent
fltrd = FILTER raw by percent >= 60;

gen = foreach fltrd generate CONCAT(firstname, CONCAT(' ', lastname));


results = DISTINCT gen;

STORE results INTO 's3n://comp421-h4/q1_results';



