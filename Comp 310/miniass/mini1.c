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
	int choice = 0;
	if(argc > 1)
	{

		if(!strcmp(argv[1], "-stack"))
		{
			choice = 1;
		}
		else if(!strcmp(argv[1], "-mem"))
		{
			choice = 2;
		}
	}
	if(choice == 1)
	{
		printStack();
	}
	else if (choice == 2)
	{
		memoryMappingSegment();
	}
	else
	{
		printHeap();
		printText();
		printData();
		printBss();
	}
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
	int fd = open("mini1.c", O_RDONLY);
	struct stat filestat;
	if(fstat(fd, &filestat) == -1)
	{
		printf("Error Memory mapping segment readibg file\n");
		return;
	}
	
	void *ptr, *tmpPtr;

	ptr = mmap(NULL, filestat.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
	printf("Memory mapping:\n");
	printf("\tstart:\t%p\n", ptr);

	//Iterate to find the end
	for(;;)
	{
		tmpPtr = mmap(NULL, filestat.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
		if(tmpPtr == (void *)-1)
		{
			printf("\tend:\t%p\n", ptr);
			break;
		}
		else
		{
			ptr = tmpPtr;
		}

	}

}

void printStack()
{
	void *ptr = alloca(1024);
	printf("\tstart:\t%p\n", ptr);
	for(;;)
	{
		//Run until seg fault
		ptr = alloca(1024);
		printf("\tend:\t%p\n", ptr);
	}
}


void printText()
{
	extern char etext;
	printf("Text segment: \n");
	printf("\tstart:\t0x8048000\n");
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