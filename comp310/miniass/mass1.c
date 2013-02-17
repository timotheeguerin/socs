#include <stdio.h>
#include <stdlib.h>
#include <alloca.h>
#include <fcntl.h>
#include <sys/resource.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>

void printHeap();
void memoryMappingSegment();
void printStack();
void printText();
void printData();
void printBss();



int main(int argc, char *argv[])
{

	printStack();
	printHeap();
	memoryMappingSegment();

	printText();
	printData();
	printBss();
	return 0;
}



void printHeap()
{
	printf("Heap segment: \n");

	int i =0;
	void *ptr;
	void *tmpPtr = malloc(1);
	printf("\tstart:\t%p\n", ptr);

	while(tmpPtr)
	{
		ptr = tmpPtr;
		i++;
		tmpPtr = realloc(ptr, i*1024);

	}
	printf("\tend:\t%p\n", ptr);


}

void memoryMappingSegment()
{
	int fd = open("mass1.c", O_RDONLY);
	struct stat filestat;
	fstat(fd, &filestat);
	void *ptr;
	ptr = mmap(NULL, filestat.st_size, PROT_READ, MAP_PRIVATE, fd, 0);

	printf("Memory mapping:\n");
	printf("\tstart:\t%p\n", ptr);

	//Iterate to find the end
	void *newPtr;
	for(;;)
	{
		ptr = mmap(NULL, filestat.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
		if(ptr == (void *)-1)
		{
			printf("\tend:\t%p\n", newPtr);
			break;
		}
		else
		{
			newPtr = ptr;
		}
	}

}

void printStack()
{
	printf("Stack: \n");
	printf("\tstart:\t%p\n", alloca(1));

	void *ptr;
	ptr = alloca(RLIMIT_STACK);
	printf("\tend:\t%p\n", ptr);
}


void printText()
{
	extern char etext;
	printf("Text segment: \n");
	printf("\tstart:\t0x08048000\n");
	printf("\tend:\t%p\n", &etext);
}

void printData()
{
	extern char etext, edata;
	printf("Data segment: \n");
	printf("\tstart:\t%p\n", &etext);
	printf("\tend:\t%p\n", &edata);
}

void printBss()
{
	extern char edata, end;
	printf("BSS segment: \n");
	printf("\tstart:\t%p\n", &edata);
	printf("\tend:\t%p\n", &end);
}