--load the data from S3 and define the schema
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);

parl_group = GROUP raw BY parl;

parl_count = FOREACH parl_group GENERATE ($0) as parl, COUNT($1) as parl_count;

party_group = GROUP raw BY (parl, party);

party_count = FOREACH party_group GENERATE FLATTEN($0) as (parl, party),  COUNT($1) as party_count;

joined = JOIN party_count BY parl, parl_count BY parl;

results = FOREACH joined GENERATE parl_count::parl as parl, party_count::party as party, party_count::party_count as party_count, parl_count::parl_count as parl_count;

store results into '/user/hadoop/q4output.csv' using PigStorage('\t','-schema');


