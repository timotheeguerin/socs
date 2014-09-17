#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main()
{
	int fd[2];

	if(pipe(fd) == -1)
	{
		printf("Pipe failed!\n");
		exit(1);
	}

	pid_t childPid;
	childPid = fork();

	if(childPid == -1)
	{
		printf("Fork failed!\n");
		exit(1);
	} 
	else if (childPid == 0)
	{
		wait(NULL);
		if(close(fd[1]) == -1)
		{
			printf("Close child write failed!\n");
			exit(1);
		}

		if(dup2(fd[0], fileno(stdin)) == -1)
		{
			printf("Child dup2 failed!\n");
			exit(1);
		}

		execlp("wc", "wc", "-l", NULL);

		if(close(fd[0]) == -1)
		{
			printf("Close child read failed!\n");
			exit(1);
		}
		exit(0);
	}
	else
	{		
		if(close(fd[0]) == -1)
		{
			printf("Close parent read failed!\n");
			exit(1);
		}

		if(dup2(fd[1], fileno(stdout)) == -1)
		{
			printf("Parent dup2 failed!\n");
			exit(1);
		}

		execlp("ls", "ls", NULL);

		if(close(fd[1]) == -1)
		{
			printf("Close parent write failed!\n");
			exit(1);
		}
		exit(0);
	}

return 0;
}