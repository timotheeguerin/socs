#ifndef MYMALLOC_H
#define MYMALLOC_H

#include<unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define FIRST_FIT                         1
#define BEST_FIT                          2


typedef struct _allocation {
	void* pointer ;
	int size;
} Allocation;

void* my_malloc(int size);
void my_free(void *ptr);
void my_mallopt(int policy);
void my_mallinfo();

//Helper functions
int getFirstAllocationIndex();
int getAllocationIndex(void* pointer);
int getFirstFreeSpaceIndex();
void addFreeSpace(Allocation allocation);
void* allocFirstFreeSpaceIndex(int size);
void* allocFitFreeSpaceIndex(int size);

void init();
extern char *my_malloc_error;

extern Allocation allocations[500];
extern Allocation freeSpaces[500];

extern char *my_malloc_error;
extern int _policy;
#endif