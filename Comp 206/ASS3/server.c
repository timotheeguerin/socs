#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
/* Execute the command using this shell program.  */
#define SHELL "/bin/sh"
void producer();
void cosumer();

int main()
{
	//Remove the file if its here that tell if the producer has ended to read value.txt
	system("rm terminated.txt");
    	FILE* file_ptr = NULL;

    	while(!file_ptr)
    	{
        	file_ptr= fopen("data.txt", "r");
   	}

    	fclose(file_ptr);

	int status = 0, status2 = 0;
	pid_t pid;

	pid = fork();

    	if (pid == 0)
    	{

		pid_t pid2;
		pid2 = fork();
		printf("pid1: %d\n", pid);
		if (pid2 == 0)
        {
			producer();
			exit (EXIT_FAILURE);
        }
        else if (pid2 < 0)  /* The fork failed.  Report failure.  */
            status2 = -1;
        else
        {
			printf("%d\n", pid2);
			cosumer();
			if(waitpid(pid2, &status2, 0) != pid2)
				exit (EXIT_FAILURE);
			else
				exit (EXIT_SUCCESS);
		}



     	}
    	else if (pid < 0)  /* The fork failed.  Report failure.  */
        	status = -1;
    	else  /* This is the parent process.  Wait for the child to complete.  */
    	{

		if (waitpid(pid, &status, 0) != pid )  status = -1;

    	}
	return status;
}
void producer(int *status)
{

        FILE* file_ptr = NULL;
        file_ptr= fopen("data.txt", "r");

	if(!file_ptr)
		return;

	int numberLine = 0;
	char line[50];
	while(fgets(line,50,file_ptr))
	{
		numberLine ++;
	}

	//Go back to the beginning of the file
	fseek(file_ptr, 0, SEEK_SET);

	//create an array containing all the value of data.txt
	int * values = malloc(numberLine*sizeof(int));
	int i = 0;

	while(!feof(file_ptr))
	{

		fscanf(file_ptr, "%d", &values[i]);
		i ++;
	}
    	fclose(file_ptr);

	for(i = 0; i < numberLine; i ++)
	{

		int idle = 1;
		file_ptr = NULL;
	    	while(idle)
	    	{
			file_ptr= fopen("values.txt", "r");
			if(file_ptr == NULL)
				idle = 0;
			else
				fclose(file_ptr);
	   	}

		//Create the file
		file_ptr= fopen("values.txt", "w");

		if(!file_ptr)
			return;

		int j;
		char buf[10];
		for(j = values[i]; j >=0; j--)
		{
			fprintf(file_ptr, " %d", j);
		}
		fclose(file_ptr);
	}

	FILE* termfile_ptr = fopen("terminated.txt", "w");
	if(termfile_ptr)
		fclose(termfile_ptr);



}

void cosumer()
{
	int i = 1;
	while(1)
	{

		FILE* file_ptr = NULL;
		FILE* termfile_ptr = NULL;
		int idle = 1;

		while(idle)
		{
			termfile_ptr = fopen("terminated.txt", "r");
			file_ptr= fopen("values.txt", "r");
			if(file_ptr)
				break;
			if(termfile_ptr)
			{
				fclose(termfile_ptr);
				exit(EXIT_SUCCESS);
			}
		}
		printf("Prcessing VALUES.TXT file number %d with values", i);
		int value =0, odd = 0;

		while(!feof(file_ptr))
		{
			fscanf(file_ptr, "%d", &value);
			printf(" %d", value);
			if(value %2 == 1)
				odd++;

		}
		fclose(file_ptr);
		printf("\nIt has %d odd number\n", odd);
		i ++;

		system("rm values.txt");
	}

}
