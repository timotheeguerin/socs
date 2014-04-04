--load the data from S3 and define the schema
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);

--some data entries use the middle name as well, so this way we will catch all of them
fltrd = FILTER raw by type == 'Gen' and elected == 1;

parl_group = GROUP fltrd BY parl;

parl_count = FOREACH parl_group GENERATE ($0) as parl, COUNT($1) as count;
parl_count_before = FOREACH parl_count GENERATE ($0+1) as parl, $1 as count;
parl_join = JOIN parl_count BY parl, parl_count_before BY parl;

parl_diff = FOREACH parl_join GENERATE parl_count::parl as parl, parl_count::count, parl_count::count - parl_count_before::count;

results = ORDER parl_diff BY parl;

dump results;

