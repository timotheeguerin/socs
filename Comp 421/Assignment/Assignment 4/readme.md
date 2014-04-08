#Comp 421 Assignment 4


##Setup(because she isn't able to give the right instructions)
This worked for me at least

###Active user key error
If you have an error when creating the cluster saying no active key found for user, you need to cerate a key for the user(AWS console> Username > security > active key > create one)

###Connection timeout when trying to connect to ssh
You might need to open ssh port on the server. To do that go to AWS console > EC2> Security group(in the left bar)> choose instance(master or slave dont know which one) > inbound> edit > add rule > ssh (set from everywhwere)



###Load the file

Download the file in your local 
```bash
wget https://s3.amazonaws.com/comp421-h4/data2.csv
wget https://s3.amazonaws.com/comp421-h4/ngrams421.csv
```
Then
* `pig`
* `fs -copyFromLocal data2.csv /user/hadoop/data2.csv`
* change the raw command to 
```pig
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);
```
* Have fun with running year long query!

