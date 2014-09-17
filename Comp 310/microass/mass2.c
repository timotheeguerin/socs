#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main()
{
	const int MAX_ARGS = 10;
	char cmdstr[100];
	for(;;)
	{
		printf("$ ");
		fgets(cmdstr, 100, stdin);
		if(strcmp(cmdstr, "exit\n") == 0)

		{
			break;
		}
		pid_t pid;

		pid = fork();

		if (pid == -1)
		{
			printf("Error forking");
			return 1;
		}
		else if (pid == 0) //child
		{
			
			char* argv[MAX_ARGS];
			char * pch;
			int counter = 1;
			argv[0] = strtok (cmdstr," \n");

			while (pch != NULL)
			{
				if(counter >= MAX_ARGS)
				{
					break;
				}
				argv[counter] = strtok (NULL, " \n");
				counter ++;
			}

			if(execvp(argv[0], argv) == -1)
			{
				exit(EXIT_FAILURE);
			}
			exit(EXIT_SUCCESS);
		}
		else
		{
			int status;
			wait(&status);
			if(status == 256)
			{
				printf("Command not found\n");
			}
		}
	}
	return 0;
}