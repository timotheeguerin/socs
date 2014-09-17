gcc -o mini1.o mini1.c
setarch ‘arch’ -R mini1.o

./mini1.o -stack > tempstack
echo "Stack:"
cat tempstack | head -n 1
cat tempstack | tail -n 3 | head -n 1
rm tempstack
./mini1.o -mem
./mini1.o
