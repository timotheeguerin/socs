loginTest: login.o cipher.o linked.o
	gcc -Wall -o loginTest login.o cipher.o linked.o

login.o: login.c linked.h cipher.h
	gcc -Wall -c login.c

cipher.o: cipher.c cipher.h
	gcc -Wall -c cipher.c

linked.o: linked.h linked.h cipher.h
	gcc -Wall -c linked.c

clean: 
	rm -f loginTest login.o linked.o cipher.o

