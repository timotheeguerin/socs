#ifndef MY_THREAD_H
#define MY_THREAD_H

#include <stdio.h>
#include <stdlib.h>
#include <ucontext.h>
#include <signal.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <sys/timeb.h>
#include <slack/std.h>
#include <slack/list.h>

#define THREAD_MAX_SIZE 100
#define SEMAPHORE_MAX_SIZE 100

typedef enum ThreadState {
	NEW,
	RUNNING,
	RUNNABLE,
	BLOCKED,
	EXIT
} ThreadState;

typedef struct _mythread_control_block
{
	ucontext_t context;
	char *thread_name;
	int thread_id;
	ThreadState state;
	long start_time,  running_time;
	void * stack;
} mythread_control_block;

typedef struct Semaphore_t {
	List * queue;
	int value;
	int initial;
} Semaphore;



int mythread_init();
int mythread_create(char *threadname, void (*threadfunc)(), int stacksize);
void mythread_exit();
void runthreads();
void set_quantum_size(int quantum);
int create_semaphore(int value);
void semaphore_wait(int semaphore);
void semaphore_signal(int semaphore);
void destroy_semaphore(int semaphore);
void mythread_state();
void switch_thread();


extern ucontext_t uctx_main;
extern int threadCounter, current_thread, quantum_size, end, active_semaphores;
extern long long int switch_ctr;
extern List * runQueue;
extern mythread_control_block thread_table[THREAD_MAX_SIZE];
extern Semaphore semaphore_table[SEMAPHORE_MAX_SIZE];

#endif