#Comp 421 Assignment 4

Setup(because she not able to give the right instructions)
This worked for me at least
* Download the data2.csv from the discussion board
* Upload it to the root of your instance as `data2.csv`.
* `pig`
* `fs -copyFromLocal data2.csv /user/hadoop/data2.csv`
* change the raw command to 
```pig
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);
```
* Have fun with running year long query!


To copy to server either use `scp`:
```bash
scp -i your_key data2.csv hadoop@server-blabla.com
```
Or upload it to one one your s3 instance using amazon console managment(S3>Your bucket>Upload)and use `wget` Dont forget to set permission or it might not work
```bash
scp wget http://urlgiven-by-amazon
```
