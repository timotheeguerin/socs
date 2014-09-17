#include "mymalloc.h"

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
		Allocation alloc = getAllocation(pointer);
		pointer += ALLOC_DATA_SIZE;

		//Update the free space if necssary
		if(free)
		{
			if(*alloc.size != size)
			{
				void* next = pointer + size;
				Allocation newAlloc = getAllocation(next);
				*newAlloc.size = *alloc.size - size - ALLOC_DATA_SIZE ;
				*newAlloc.prev = *alloc.prev;
				*newAlloc.flag = 1;

				printf("p: %p, s: %d\n", *newAlloc.prev, *newAlloc.size);

				next = pointer + *alloc.size;
				Allocation nextAlloc = getAllocation(next);
				*nextAlloc.prev = newAlloc.prev;
			}

		}

		//Setup values
		*alloc.prev = getPrev(alloc.prev);
		*alloc.size = size;
		*alloc.flag = 0;
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

	Allocation alloc = getAllocation(ptr - ALLOC_DATA_SIZE);

	*alloc.flag = 1;

	int merged = 0;
	//Check the previous space if free
	Allocation prevAlloc = getAllocation(*alloc.prev);
	if(*prevAlloc.flag == 1)
	{
		*prevAlloc.size += *alloc.size + ALLOC_DATA_SIZE;
		merged = 1;
	}

	void* c_next = ptr + *alloc.size;
	Allocation nextAlloc = getAllocation(ptr + *alloc.size);

	//Check if the next space is also free and merge
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
			*alloc.size += *nextAlloc.size + ALLOC_DATA_SIZE;
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


	//Check if we have 128kb free
	Allocation lastAlloc = getAllocation(getPrev(sbrk(0)));
	if(*lastAlloc.flag == 1 && *lastAlloc.size >= 128*1024*1024)
	{
		//remove the last free space
		brk(lastAlloc.prev);
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
		Allocation alloc = getAllocation(cur);

		if(*alloc.flag == 0)
		{
			printf("\t%p - %p ( %d )\n" , cur, cur + *alloc.size,  *alloc.size);
		}
		cur += ALLOC_DATA_SIZE + *alloc.size;

	}
	cur = first;
	printf("Free: \n");
	for(;;)
	{

		if(cur == sbrk(0))
		{
			break;
		}
		Allocation alloc = getAllocation(cur);
		if(*alloc.flag == 1)
		{
			printf("\t%p - %p ( %d )\n" , cur, cur + *alloc.size,  *alloc.size);
		}
		cur += ALLOC_DATA_SIZE + *alloc.size;

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

//Return the pointer for the previous data
void* getPrev(void* pointer)
{
	void* cur = first;

	for(;;)
	{
		if(cur == sbrk(0) || cur == pointer)
		{
			break;
		}
		Allocation alloc = getAllocation(cur);
		cur += ALLOC_DATA_SIZE;
		if(cur + *alloc.size == pointer) 
		{
			return alloc.prev;
		}
		cur += *alloc.size;
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