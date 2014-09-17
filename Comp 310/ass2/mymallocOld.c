	#include "mymalloc.h"

Allocation allocations[500];
Allocation freeSpaces[500];
char *my_malloc_error;
int _policy = FIRST_FIT;
int need_init = 1;

void init()
{
	int i;
	for(i = 0; i < 500; i ++)
	{
		allocations[i].pointer = NULL;
	}

	for(i = 0; i < 500; i ++)
	{
		freeSpaces[i].pointer = NULL;
	}
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

	if(pointer == NULL)
	{
		pointer = sbrk(size);
	}
	if(pointer != (void *) -1)
	{
		int index = getFirstAllocationIndex();
		if(index == -1)
		{
			my_malloc_error = "Error allocation";

			return NULL;
		}
		else
		{
			allocations[index].pointer = pointer;
			allocations[index].size = size;
		}

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
	int index = getAllocationIndex(ptr);
	if(index == -1)
	{
		return;
	}
	addFreeSpace(allocations[index]);
	allocations[index].pointer = NULL;
	allocations[index].size = 0;
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
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(allocations[i].pointer != NULL)
		{
			printf("\t%p - %p ( %d )\n" , allocations[i].pointer, allocations[i].pointer + allocations[i].size,  allocations[i].size);
		}
	}


	printf("Free: \n");
	for(i = 0; i < 500; i ++)
	{
		if(freeSpaces[i].pointer != NULL)
		{
			printf("\t%p - %p ( %d )\n" , freeSpaces[i].pointer, freeSpaces[i].pointer + freeSpaces[i].size,  freeSpaces[i].size);
		}
	}
}

int getFirstAllocationIndex()
{
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(allocations[i].pointer == NULL)
		{
			return i;
		}
	}
	return -1;
}

int getAllocationIndex(void* pointer)
{
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(allocations[i].pointer == pointer)
		{
			return i;
		}
	}
	return -1;
}

int getFirstFreeSpaceIndex()
{
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(freeSpaces[i].pointer == NULL)
		{
			return i;
		}
	}
	return -1;
}

void addFreeSpace(Allocation allocation)
{
	int merged = -1;
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(freeSpaces[i].pointer != NULL)
		{
			void* end = freeSpaces[i].pointer + freeSpaces[i].size;
			void* allocEnd = allocation.pointer + allocation.size;

			//Check if the free memory end match the begining of the new free alloc  FFFFFFNNNNN
			if(end == allocation.pointer)
			{
				//Check if we already merge the freed allocation with a previously free allocation(Before)
				if(merged == -1)
				{
					freeSpaces[i].size += allocation.size;
					merged = i;
				}
				else
				{
					freeSpaces[i].size += freeSpaces[merged].size;
					freeSpaces[merged].pointer  = NULL;
					freeSpaces[merged].size = 0;
					return;
				}
			}
			//Check if the free memory start match the begining of the new free alloc end  NNNNFFFFFF
			if(freeSpaces[i].pointer == allocEnd)
			{
				//Check if we already merge the freed allocation with a previously free allocation(After)
				if(merged == -1)
				{
					freeSpaces[i].size += allocation.size;
					freeSpaces[i].pointer -= allocation.size;
					merged = i;
				}
				else
				{
					freeSpaces[merged].size += freeSpaces[i].size;
					freeSpaces[i].pointer  = NULL;
					freeSpaces[i].size = 0;
					return;
				}
			}
		}
	}
	//If we already merged don't add again
	if(merged != -1)
	{
		return;
	}
	int index  = getFirstFreeSpaceIndex();
	if(index != -1)
	{
		freeSpaces[index].pointer = allocation.pointer;
		freeSpaces[index].size = allocation.size;
	}
}

void* allocFirstFreeSpaceIndex(int size)
{
	int best  = -1;
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(freeSpaces[i].pointer != NULL)
		{
			if(size <= freeSpaces[i].size)
			{
				if(best == -1)
				{
					best = i;
				}
				else
				{
					if(freeSpaces[i].pointer < freeSpaces[best].pointer)
					{
						best  = i;
					}
				}
			}
		}
	}
	if(best == -1)
	{
		return NULL;
	}
	void* pointer = freeSpaces[best].pointer;
	freeSpaces[best].size -= size;
	if(size == freeSpaces[i].size)
	{
		freeSpaces[best].pointer = NULL;
	}
	else
	{
		freeSpaces[best].pointer += size;
	}
	return pointer;
}

void* allocFitFreeSpaceIndex(int size)
{
	int best  = -1;
	int i;
	for(i = 0; i < 500; i ++)
	{
		if(freeSpaces[i].pointer != NULL)
		{
			if(size <= freeSpaces[i].size)
			{
				if(best == -1)
				{
					best = i;
				}
				else
				{
					if(freeSpaces[i].size < freeSpaces[best].size)
					{
						best  = i;
					}
				}
			}
		}
	}
	if(best == -1)
	{
		return NULL;
	}
	void* pointer = freeSpaces[best].pointer;
	freeSpaces[best].size -= size;
	if(size == freeSpaces[i].size)
	{
		freeSpaces[best].pointer = NULL;
	}
	else
	{
		freeSpaces[best].pointer += size;
	}
	return pointer;
}
