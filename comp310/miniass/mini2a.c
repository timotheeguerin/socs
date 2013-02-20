#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <bsd/stdlib.h>
#include <sys/time.h>

int buffer[100000];
int bIndex = 0;
int pCount = 0;
int cCount = 0;
int n = 10;

static void *producer(void *arg)
{
	int item;
	int i;
	for(i = 0; i < n; i++)
	{
		item = arc4random();
		buffer[bIndex] = item;
		//printf("Producer: %d\n", item);
		bIndex++;
		pCount++;
	}
	return NULL;
}

static void *consumer(void *arg)
{
	int item;
	while(1)
	{
		while(bIndex == 0);
		item = buffer[bIndex-1];
		//printf("Consumer: %d\n", item);
		bIndex--;
		cCount++;
	}
	return NULL;
}

int main()
{
	pthread_t tProducer[10];
	pthread_t tConsumer;
	void *pRes[10];
	void *cRes;
	struct timeval t;
	
	gettimeofday(&t, NULL);
	double t1 = t.tv_sec+(t.tv_usec/1000000.0);
	
	int i;
	for(i = 0; i < 10; i++)
	{
		if(pthread_create(&tProducer[i], NULL, producer, ""))
		{
			printf("Producer thread create error\n");
			return 1;
		}
	}
	if(pthread_create(&tConsumer, NULL, consumer, ""))
	{
		printf("Consumer thread create error\n");
		return 1;
	}
	for(i = 0; i < 10; i++)
	{
		if(pthread_join(tProducer[i], &pRes[i]))
		{
			printf("Producer thread join error\n");
			return 1;
		}
	}
	/*if(pthread_join(tConsumer, &cRes))
	{
		printf("Consumer thread join error\n");
		return 1;
	}*/
	
	gettimeofday(&t, NULL);
	double t2 = t.tv_sec+(t.tv_usec/1000000.0);
	
	printf("Time elapsed in seconds: %.6lf\n", t2-t1);
	printf("Produced: %d\nConsumed: %d\n", pCount, cCount);
	return 0;
}
