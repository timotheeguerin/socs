#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <bsd/stdlib.h>


int main()
{
	const int MAX_INT = 20;

	int argv[MAX_INT];



	int fd = open("file.bin", O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
	if(fd == -1)
	{
		printf("Error creating the file!");
		exit(EXIT_FAILURE);
	}



	int i;
	for(i = 0; i < MAX_INT; i ++)
	{
		int val = arc4random();
		argv[i] = val;
		printf("Nb: %d\n", val); 
	}

	write(fd, argv, sizeof(argv));

	for(;;)
	{
		int index = 0;

		printf("Get int at index: ");
		scanf("%d", &index);
		if(index == -1)
		{
			break;
		} 



		if(lseek(fd, index*4, SEEK_SET) == -1)
		{
			printf("Seek to start failed\n");
			exit(1);
		}

		int value;

		if(read(fd, &value, 4) != 4)
		{
			printf("Read failed!\n");
			exit(1);
		}

		printf("Value at index %d is %d\n", index, value); 

	}
	
	return 0;

}