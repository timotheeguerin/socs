/*
* =====================================================================================
*
*      Filename:  pa2_test.c
*
*   	Description:  Example of testing code of MyMalloc.
*
*      Version:  1.0
*      Created:  18/03/2013 8:30:30 AM
*   	Revision:  none
*      Compiler:  gcc
*
*      Author:  Devarun Bhattacharya
* =====================================================================================
*/
/* Includes */
#include<unistd.h>
#include <stdio.h>
#include <stdlib.h>

// Uncomment the following line -- this should be your header file
#include "mymalloc.h"	

// We assume you have defined the following two definitions
// If so, you should remove these..
// If not, move them to your mymalloc.h file
#define FIRST_FIT                         1
#define BEST_FIT                          2



// Comment out the following 4 lines
//#define my_malloc(X)                      malloc(X)
//#define my_free(X)                        free(X)
//#define my_mallopt(X)                     dummymallopt(X)
//#define my_mallinfo                       dummymallinfo


// No need to modify anything below.. unless you find a bug in the tester!
// Don't modify the tester to circumvent the bug in your code!

void dummymallopt()
{
	puts("No optimization");
}

void dummymallinfo()
{
	puts("No info");
}


int main(int argc, char *argv[])
{
	int i, count = 0;
	void *ptr, *limitafter = NULL, *limitbefore = NULL;
	char *c[32], *ct;

	puts("Hole finding test....");

	// Allocating 32 kbytes of memory..
	for(i=0; i<32;i++)					
		c[i] = (char*)my_malloc(1024);
	// Now deallocating some of the slots ..to free
	for(i=10; i<18;i++)
		my_free(c[i]);

	// Allocate some storage .. this should go into the freed storage
	ct = (char*)my_malloc(5*1024);


	// First test, are you finding the available holes.
	
	if(ct < c[31])						
		puts("\t\t\t\t Passed\n");
	else
		puts("\t\t\t\t Failed\n");

	puts("Program break expansion test...");
	count = 0;
	for(i=1; i<40; i++) {
		limitbefore = sbrk(0);
		ptr = my_malloc(1024*32*i);
		limitafter = sbrk(0);

		if(limitafter > limitbefore)
			count++;

	}
#ifndef my_malloc
	if (count > 0 && count < 40)
		puts("\t\t\t\t Passed");
	else
		puts("\t\t\t\t Failed:");
#else
	puts("\t\t\t\t skipped");
#endif

	puts("Check for first fit algorithm.... ");
	my_mallopt(FIRST_FIT);


	// Allocating 512 kbytes of memory..
	for(i=0; i<32;i++)					
		c[i] = (char*)my_malloc(16*1024);

	// Now deallocating some of the slots ..to free
	my_free(c[31]);
	my_free(c[30]);
	my_free(c[29]);
	my_free(c[28]);
	my_free(c[27]);


	my_free(c[20]);
	my_free(c[19]);


	my_free(c[10]);
	my_free(c[9]);
	my_free(c[8]);


	char *cp2 = my_malloc(16*1024*2);
	if (cp2 == c[8] || cp2 == c[9])
		puts("\t\t\t\t Passed");
	else
		puts("\t\t\t\t Failed");


	my_free(cp2);
	// Now try the other method...

	puts("Check for best first algorithm.... ");
	my_mallopt(BEST_FIT);	
	
	char *cp3 = my_malloc(16*1024*2);
	if (cp3 == c[19])
		puts("\t\t\t\t Passed");
	else
		puts("\t\t\t\t Failed");

	puts("Print some information..");
	my_mallinfo();

	return(0);
}