passweb: passweb.o
	gcc -Wall -o passweb passweb.o
	cp *.c ../../backup

passweb.o: passweb.c cipher.c menu.c
	gcc -Wall -c passweb.c

../../backup/*.c: *.c 

clean:
	rm -f passweb passweb.o

