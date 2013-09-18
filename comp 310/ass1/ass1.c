/*
 * =============================================================================
 *
 *      Filename:  my-test1.c
 *
 *   	Description:  Simple tester for MyThread - Test 1.
 *
 *      Version:   1.1
 *      Created:   03/08/2012 11:09:44 AM
 *      Compiler:  gcc
 *
 *      Author:    Xing Shi Cai
 *      Revisions: Devarun Bhattacharya   
 *                 M. Maheswaran
 * =============================================================================
 */
/* Includes */
#include <stdio.h>      /* Input/Output */
#include <stdlib.h>     /* General Utilities */
#include <math.h>

#include "mythreads.h"

/* prototype for thread routine */
void handler ( );

/* global vars */

/* semaphores are declared global so they can be accessed 
   in main() and in thread routine,
   here, the semaphore is used as a mutex */

int counter_mutex;

/* shared variables */
int counter; 
double result = 0.0;

int main()
{
    int thread_num = 10;
    int j;
    char* thread_names[] = {
        "thread 0",
        "thread 1",
        "thread 2",
        "thread 3",
        "thread 4",
        "thread 5",
        "thread 6",
        "thread 7",
        "thread 8",
        "thread 9"
    };

    /* Initialize MyThreads library. */
    mythread_init();

    /* 250 ms */
    set_quantum_size(250);

    counter_mutex = create_semaphore(1);

    for(j=0; j<thread_num; j++)
    {
        mythread_create(thread_names[j], (void *) &handler, 6004);
    }

    /* Print threads informations before run */
    mythread_state();

    /* When this function returns, all threads should have exited. */
    runthreads();
    printf("Run ended\n");

    destroy_semaphore(counter_mutex);

    /* Print threads informations after run */
    mythread_state();

    printf("The counter is %d\n", counter);
    printf("The result is %f\n", result);

    if (counter == 50 &&
	(result - 151402.656521) < 0.000001)
      printf(">>> Thread library PASSED the Test 1\n");

    exit(0);
}


void handler ()
{
    int i;
    for(i=0; i < 5; i++)
    {
        /* If you remove this protection, you should be able to see different
         * out of every time you run this program.  With this protection, you
         * should always be able to see result to be 151402.656521 */
        semaphore_wait(counter_mutex);       /* down semaphore */

        /* START CRITICAL REGION */
        int j;
        for (j = 0; j < 1000; j++) {
            result = result + sin(counter) * tan(counter);
        }
        counter++;
        /* END CRITICAL REGION */    

        semaphore_signal(counter_mutex);       /* up semaphore */
    }
    mythread_exit(); /* exit thread */
}
