#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <bsd/stdlib.h>
#include <sys/time.h>


int queue[1000000];
int queueStart = 0;
int queueEnd = 0;
int n = 50;
int thSync = 1;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

static void *producer(void *arg);
static void *consumer(void *arg);

int main(int argc, char *argv[])
{
	//Enable no sync mode
	if(argc > 1)
	{
		if(!strcmp(argv[1], "-nosync"))
		{
			thSync = 0;
		} else if(!strcmp(argv[1], "-sync"))
		{
			thSync = 1;
		}
	}

	//Custom size
	if(argc > 2)
	{
		n = atoi(argv[2]);
	}


	pthread_t producerTh[10];
	pthread_t consumerTh;
	void *pRes[10];
	void *cRes;
	struct timeval t;

	gettimeofday(&t, NULL);
	double tstart = t.tv_sec+(t.tv_usec/1000000.0);

	int i;

	//Create the producer threads
	for(i = 0; i < 10; i++)
	{
		if(pthread_create(&producerTh[i], NULL, producer, ""))
		{
			printf("Error when creating producer thread %d\n", i);
			return 1;
		}
	}
	//Create the cosumer thread
	if(pthread_create(&consumerTh, NULL, consumer, ""))
	{
		printf("Error when creating costumer thread\n");
		return 1;
	}
	//Join the producer
	for(i = 0; i < 10; i++)
	{
		if(pthread_join(producerTh[i], &pRes[i]))
		{
			printf("Error when joining producer thread  %d\n", i);
			return 1;
		}
	}

	gettimeofday(&t, NULL);
	double tend = t.tv_sec+(t.tv_usec/1000000.0);

	printf("Time: : %.6lf\n", tend-tstart);
	printf("Produced: %d\nConsumed: %d\n", queueEnd, queueStart);
	return 0;
}


static void *producer(void *arg)
{
	int i;
	for(i = 0; i < n; i++)
	{
		int item = arc4random();
		if(thSync){
			pthread_mutex_lock(&mutex);
		}

		queue[queueEnd] = item;
		queueEnd++;
		if(thSync){
			pthread_cond_signal(&cond);
			pthread_mutex_unlock(&mutex);
		}
	}
	return NULL;
}

static void *consumer(void *arg)
{
	int item;
	for(;;)
	{
		if(thSync){
			pthread_mutex_lock(&mutex);
			if(queueEnd == queueStart)
			{ 
				pthread_cond_wait(&cond, &mutex);
			}
		}
		else
		{
			while(queueEnd == queueStart);
		}

		item = queue[queueStart];

		queueStart++;
		if(thSync){
			pthread_mutex_unlock(&mutex);
		}
	}
	return NULL;
}