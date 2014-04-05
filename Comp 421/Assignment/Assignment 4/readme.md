#Comp 421 Assignment 4


##Setup(because she not able to give the right instructions)
This worked for me at least

###Active user key error
If you have an error when creating the cluster saying no active key found for user, you need to cerate a key for the user(AWS console> Username > security > active key > create one)

###Connection timeout when trying to connect to ssh
You might need to open ssh port on the server. To do that go to EC2 > instance click on the cluster > inbound > add port 22 alowed from anywhere)



###Load the file
Multiple methods to upload the file to your server.
####METHOD 1
* Download the data2.csv from the discussion board
* Upload it to the root of your instance as `data2.csv`.
To copy to server either use `scp`:
```bash
scp -i your_key data2.csv hadoop@server-blabla.com
```
Or upload it to one one your s3 instance using amazon console managment(S3>Your bucket>Upload)and use `wget` Dont forget to set permission or it might not work
```bash
wget http://urlgiven-by-amazon
```
####METHOD 2
Or you can just use my links which is a lot easier 
```bash
wget https://s3.amazonaws.com/comp421-h4/data2.csv
wget https://s3.amazonaws.com/comp421-h4/ngrams421.csv
```



* `pig`
* `fs -copyFromLocal data2.csv /user/hadoop/data2.csv`
* change the raw command to 
```pig
raw = LOAD '/user/hadoop/data2.csv' USING PigStorage(',') AS  (date, type:chararray, parl:int, prov:chararray, riding:chararray, lastname:chararray, firstname:chararray,gender:chararray, occupation:chararray, party:chararray,votes:int, percent:double, elected:int);
```
* Have fun with running year long query!

