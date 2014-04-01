#include "mythreads.h"

int threadCounter, current_thread, quantum_size, end, active_semaphores;
long long int switch_ctr;
List * runQueue;
mythread_control_block thread_table[THREAD_MAX_SIZE];
Semaphore semaphore_table[SEMAPHORE_MAX_SIZE];
ucontext_t uctx_main;

long getCurrentNanoTime()
{
	struct timespec cur_time;
	clock_gettime(CLOCK_REALTIME, &cur_time);
	return cur_time.tv_nsec;
}
int mythread_init()
{
	int i;
	for(i = 0; i < THREAD_MAX_SIZE; i++)
	{
		thread_table[i].state = NEW;
	}
	runQueue = list_create(NULL);
	threadCounter = 0;
	current_thread = 0;
	quantum_size = 0;
	active_semaphores = 0;
	switch_ctr = 0;
	end = 0;

	if(runQueue == 0)
	{
		return -1;
	}

	return 0;
}

int mythread_create(char *threadname, void (*threadfunc)(), int stacksize)
{
	int pointer = threadCounter;

	if(threadCounter >= THREAD_MAX_SIZE)
	{
		return -1;
	}

	getcontext(&thread_table[pointer].context);

	thread_table[pointer].context.uc_link = &uctx_main;
	thread_table[pointer].context.uc_stack.ss_sp = malloc(stacksize);
	thread_table[pointer].context.uc_stack.ss_size = stacksize;
	thread_table[pointer].context.uc_stack.ss_flags = 0;
	sigemptyset(&thread_table[pointer].context.uc_sigmask);



	thread_table[pointer].thread_name = threadname;
	thread_table[pointer].thread_id = pointer;
	thread_table[pointer].state = RUNNABLE;
	//Time only count when thread is running
	thread_table[pointer].start_time = 0;

	makecontext(&thread_table[pointer].context, threadfunc, 0);


	//Finally add the thread to the running queue
	runQueue = list_append_int(runQueue, pointer);


	threadCounter ++;
	return pointer ;
}

void mythread_exit()
{
	thread_table[current_thread].state = EXIT;
	long end_time = getCurrentNanoTime();
	thread_table[current_thread].running_time += end_time - thread_table[current_thread].start_time;
}

void switch_thread()
{
	switch_ctr++;
	//If the running queue is empty and current thread is exit
	if(list_empty(runQueue) && thread_table[current_thread].state == EXIT)
	{
		//Calculate the running time
		long end_time = getCurrentNanoTime();
		thread_table[current_thread].running_time += end_time - thread_table[current_thread].start_time;
		end = 1;
		setcontext(&uctx_main);
	}
	//If we have a another thread in the running queue we switch
	if(!list_empty(runQueue))
	{
		//Switch thread with the first in the queue
		int old_thread = current_thread;
		current_thread = list_shift_int(runQueue);

		//Update the running time of the old thread
		long end_time = getCurrentNanoTime();
		thread_table[old_thread].running_time += end_time - thread_table[old_thread].start_time;

		//Reset the start time of the current_thread
		thread_table[current_thread].start_time = getCurrentNanoTime();

		//If we still need to run the old thread we put it back in the running quue 
		if(thread_table[old_thread].state == RUNNABLE || thread_table[old_thread].state == RUNNING)
		{
			runQueue = list_append_int(runQueue, old_thread);
		}

		//Update the context
		if(swapcontext(&thread_table[old_thread].context, &thread_table[current_thread].context) == -1)
		{
			return;
		}
	}
	else //No other thread in the queue
	{
		if(swapcontext(&thread_table[current_thread].context, &uctx_main) == -1)
		{
			return;
		}
	}
}
void runthreads()
{

	sigset_t x;
	sigemptyset(&x);
	sigaddset(&x, SIGALRM);
	sigprocmask(SIG_BLOCK, &x, NULL);
	//If we have no thread quit
	if(threadCounter == 0)
	{
		return;
	}

	//Setup the quantum timer
	struct itimerval timer;
	timer.it_interval.tv_sec = 0;
	timer.it_interval.tv_usec = quantum_size;
	timer.it_value.tv_sec = 0;
	timer.it_value.tv_usec = quantum_size;
	setitimer(ITIMER_REAL, &timer, 0);

	sigset(SIGALRM, &switch_thread);

	//Pop the first thread to run
	current_thread = list_shift_int(runQueue);
	//Set its state to RUNNING
	thread_table[current_thread].state = RUNNING;
	//Set the start_running time
	thread_table[current_thread].start_time = getCurrentNanoTime();

	sigprocmask(SIG_UNBLOCK, &x, NULL);

	if(swapcontext(&uctx_main, &thread_table[current_thread].context) == -1)
	{
		return;
	}

	while(!list_empty(runQueue))
	{
		current_thread = list_shift_int(runQueue);
		thread_table[current_thread].state = RUNNING;

		thread_table[current_thread].start_time = getCurrentNanoTime();

		if(swapcontext(&uctx_main, &thread_table[current_thread].context) == -1)
		{
			return;
		}
	}

	sigprocmask(SIG_BLOCK, &x, NULL);

	int i;
	for(i = 0; i < threadCounter; i++)
	{
		thread_table[i].state = EXIT;
	}
}


void set_quantum_size(int q)
{
	quantum_size = q;
}

int create_semaphore(int value)
{
	if(active_semaphores >= SEMAPHORE_MAX_SIZE)
	{
		return -1;
	}

	int pointer = active_semaphores;

	semaphore_table[pointer].value = value;
	semaphore_table[pointer].initial = value;
	semaphore_table[pointer].queue = list_create(NULL);

	active_semaphores++;
	return pointer;
}

void semaphore_wait(int semaphore)
{
	sigset_t set;
	sigemptyset(&set);
	sigaddset(&set,SIGALRM);
	sigprocmask(SIG_BLOCK,&set,NULL);

	long long int old_switch_ctr = switch_ctr; 

	semaphore_table[semaphore].value --;

	if(semaphore_table[semaphore].value < 0)
	{
		thread_table[current_thread].state = BLOCKED;
		list_append_int(semaphore_table[semaphore].queue, current_thread);
	}
	sigprocmask(SIG_UNBLOCK,&set,NULL);

	while(old_switch_ctr==switch_ctr);
}

void semaphore_signal(int semaphore)
{

	//Init signal
	sigset_t x;
	sigemptyset(&x);
	sigaddset(&x, SIGALRM);

	//Block signal
	sigprocmask(SIG_BLOCK, &x, NULL);

	semaphore_table[semaphore].value++;

	if(semaphore_table[semaphore].value <= 0)
	{
		int thread;
		thread = list_shift_int(semaphore_table[semaphore].queue);
		thread_table[thread].state = RUNNABLE;

		//Put back the thread in the running queue
		runQueue = list_append_int(runQueue, thread);
	} 

	sigprocmask(SIG_UNBLOCK, &x, NULL);
	switch_thread();
}

void destroy_semaphore(int semaphore)
{
	if(!list_empty(semaphore_table[semaphore].queue))
	{
		return;
	}
	if(semaphore_table[semaphore].initial != semaphore_table[semaphore].value)
	{
		return;
	}
}

void mythread_state()
{
	printf("Thread state: \n");
	printf("%-20s%-10s %s\n", "Name","State", "Time");

	int i;
	for(i = 0; i < threadCounter; i++)
	{
		char * state;
		switch(thread_table[i].state)
		{
		case NEW:
			state = "NEW";
			break;
		case RUNNING:
			state = "RUNNING";
			break;
		case RUNNABLE:
			state = "RUNNABLE";
			break;
		case BLOCKED:
			state = "BLOCKED";
			break;
		case EXIT:
			state = "EXIT";
			break;
		default:
			state = "UNDEFINED";
			break;
		}
		printf("%-20s%-10s %lu\n", thread_table[i].thread_name, state, thread_table[i].running_time);
	}

	printf("\n");
}