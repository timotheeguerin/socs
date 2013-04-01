#ifndef MYMALLOC_H
#define MYMALLOC_H

#include<unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define FIRST_FIT 1
#define BEST_FIT  2
#define ALLOC_DATA_SIZE (sizeof(void**) + 2*sizeof(int*))


typedef struct _allocation {
	void** prev ;
	int* size;
	int* flag;
	void* pointer;
} Allocation;

void* my_malloc(int size);
void my_free(void *ptr);
void my_mallopt(int policy);
void my_mallinfo();

//Helper functions
void* allocFirstFreeSpaceIndex(int size);
void* allocFitFreeSpaceIndex(int size);
void* getPrev(void* pointer);
Allocation getAllocation(void *pointer);

void init();

extern char *my_malloc_error;
extern int _policy;
#endif
