#include "mymalloc.h"

Allocation allocations[500];
Allocation freeSpaces[500];
char *my_malloc_error;
int _policy = FIRST_FIT;
int need_init = 1;
void* first;

void init()
{
	first = sbrk(0);
}
void* my_malloc(int size)
{
	if(need_init)
	{
		need_init = 0;
		init();
	}
	void* pointer = NULL;
	if(_policy == FIRST_FIT)
	{
		pointer = allocFirstFreeSpaceIndex(size);
	}
	else if(_policy == BEST_FIT)
	{
		pointer = allocFitFreeSpaceIndex(size);
	}
	int free = 0;
	if(pointer == sbrk(0))
	{
		sbrk(sizeof(void**));
		sbrk(sizeof(int));
		sbrk(sizeof(int));
		sbrk(size);
	}
	else
	{
		free = 1;
	}


	if(pointer != (void *) -1)
	{
		void** prev = pointer;
		pointer += sizeof(void**);
		int* pSize = pointer;
		pointer += sizeof(int*);
		int* pFlag = pointer;
		pointer += sizeof(int*);

		//Update the free space if necssary
		if(free)
		{
			if(*pSize != size)
			{
				void* next = pointer + size;
				Allocation newAlloc = getAllocation(next);
				*newAlloc.size = *pSize - size - ALLOC_DATA_SIZE ;
				*newAlloc.prev = *prev;
				*newAlloc.flag = 1;

				printf("p: %p, s: %d\n", *newAlloc.prev, *newAlloc.size);

				next = pointer + *pSize;
				Allocation nextAlloc = getAllocation(next);
				*nextAlloc.prev = newAlloc.prev;
			}

		}

		//Setup values
		*prev = getPrev(prev);
		*pSize = size;
		*pFlag = 0;

		return pointer;
	}
	else
	{
		my_malloc_error = "Error allocation";
		return NULL;
	}

}


void my_free(void *ptr)
{
	if(ptr == NULL)
	{
		return;
	}
	void* cur = ptr - ALLOC_DATA_SIZE;  
	void** prev = cur;
	cur += sizeof(void**);
	int* pSize = cur;
	cur += sizeof(int*);
	int* pFlag = cur;
	cur += sizeof(int*);
	*pFlag = 1;

	int merged = 0;
	//Check the previous space if free
	Allocation prevAlloc = getAllocation(*prev);
	if(*prevAlloc.flag == 1)
	{
		*prevAlloc.size += *pSize + ALLOC_DATA_SIZE;
		merged = 1;
	}

	void* c_next = cur + *pSize;
	Allocation nextAlloc = getAllocation(cur + *pSize);

	if(*nextAlloc.flag == 1)
	{	
		void** next = c_next + *nextAlloc.size;
		if(merged)
		{
			*prevAlloc.size += *nextAlloc.size + ALLOC_DATA_SIZE;
			*next = prevAlloc.prev;
		}
		else
		{
			*pSize += *nextAlloc.size +ALLOC_DATA_SIZE;
			*next = prevAlloc.prev;
		}
	}
	else
	{
		if(merged)
		{
			*nextAlloc.prev = prevAlloc.prev;
		}
	}
}

void my_mallopt(int policy)
{
	_policy = policy;
}

void my_mallinfo()
{
	printf("------------------------------------\n");
	printf("Information: \n");

	printf("--------------------------------------\n");
	printf("Allocated: \n");
	void* cur = first;
	printf("f: %p , e: %p\n", first, sbrk(0));

	for(;;)
	{

		if(cur == sbrk(0))
		{
			break;
		}
		void** prev = cur;
		cur += sizeof(void**);
		int* pSize = cur;
		cur += sizeof(int*);
		int* pFlag = cur;
		cur += sizeof(int*);
		if(*pFlag == 0)
		{
			printf("\t%p - %p ( %d )\n" , cur, cur + *pSize,  *pSize);
		}
		cur += *pSize;

	}
	cur = first;
	printf("Free: \n");
	for(;;)
	{

		if(cur == sbrk(0))
		{
			break;
		}
		void** prev = cur;
		cur += sizeof(void**);
		int* pSize = cur;
		cur += sizeof(int*);
		int* pFlag = cur;
		cur += sizeof(int*);
		if(*pFlag == 1)
		{
			printf("\t%p - %p ( %d )\n" , cur, cur + *pSize,  *pSize);
		}
		cur += *pSize;

	}
}


void* allocFirstFreeSpaceIndex(int size)
{
	void* cur = first;
	for(;;)
	{
		if(cur == sbrk(0))
		{
			return cur;
		}
		void** prev = cur;
		cur += sizeof(void**);
		int* pSize = cur;
		cur += sizeof(int*);
		int* pFlag = cur;
		cur += sizeof(int*);
		if(*pFlag == 1 && *pSize >= size)
		{
			return prev;
		}
		cur += *pSize;
	}
}

void* allocFitFreeSpaceIndex(int size)
{
	void* best  = sbrk(0);
	int bestSize = -1;

	void* cur = first;
	for(;;)
	{
		if(cur == sbrk(0))
		{
			break;
		}
		Allocation alloc = getAllocation(cur);
	
		if(*alloc.flag == 1 && *alloc.size >= size)
		{
			printf("\t best :  p:%p f: %d s: %d, b: %d\n" , alloc.prev, *alloc.flag, *alloc.size, bestSize);
			if(bestSize == -1)
			{
				best = alloc.prev;
				bestSize = *alloc.size;
			}

			if(*alloc.size < bestSize)
			{
				best = alloc.prev;
				bestSize = *alloc.size;
			}

		}
		cur += ALLOC_DATA_SIZE + *alloc.size;
	}
	return best;
}

void* getPrev(void* pointer)
{
	void* cur = first;

	for(;;)
	{

		if(cur == sbrk(0) || cur == pointer)
		{
			break;
		}
		void** prev = cur;
		cur += sizeof(void**);
		int* pSize = cur;
		cur += sizeof(int*);
		int* pFlag = cur;
		cur += sizeof(int*);

		if(cur + *pSize == pointer) 
		{
			return prev;
		}
		cur += *pSize;
	}
	return first;
}

Allocation getAllocation(void *pointer)
{
	void* cur = pointer;
	Allocation allocation;
	allocation.prev = cur;
	cur += sizeof(void**);
	allocation.size = cur;
	cur += sizeof(int*);
	allocation.flag = cur;
	cur += sizeof(int*);
	allocation.pointer = cur;
	return allocation;
}
/*
int main()
{

	my_malloc(1024);
	void* ptr1 = my_malloc(1024);
	void* ptr2 = my_malloc(1024);
	void* ptr3 = my_malloc(1024);
	my_malloc(1024);
	my_mallinfo();
	my_free(ptr1);
	my_free(ptr3);
	my_mallinfo();
	my_free(ptr2);
	my_mallinfo();

	void* ptr4 = my_malloc(1024);
	my_mallinfo();
	return 0;
}*/